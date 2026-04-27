package com.fx.gateway.domain;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public final class CurrencyBridge {

	private static final int SCALE = 8;

	private static final MathContext MC = MathContext.DECIMAL64;

	private CurrencyBridge() {
	}

	public static Optional<BigDecimal> convert(CurrencyCode from, CurrencyCode to, BigDecimal amount,
			List<ExchangeRateSnapshot> latestDistinctPairs) {
		if (from.value().equals(to.value())) {
			return Optional.of(amount.setScale(SCALE, RoundingMode.HALF_UP));
		}
		List<DirectedEdge> edges = buildEdges(latestDistinctPairs);
		if (edges.isEmpty()) {
			return Optional.empty();
		}
		Deque<AmountAtCurrency> dq = new ArrayDeque<>();
		Set<String> visited = new HashSet<>();
		dq.addLast(new AmountAtCurrency(from.value(), amount));
		visited.add(from.value());
		while (!dq.isEmpty()) {
			AmountAtCurrency cur = dq.removeFirst();
			if (cur.code().equals(to.value())) {
				return Optional.of(cur.amount().setScale(SCALE, RoundingMode.HALF_UP));
			}
			for (DirectedEdge edge : edges) {
				if (edge.from().equals(cur.code()) && visited.add(edge.to())) {
					dq.addLast(new AmountAtCurrency(edge.to(), cur.amount().multiply(edge.factor(), MC)));
				}
			}
		}
		return Optional.empty();
	}

	private static List<DirectedEdge> buildEdges(List<ExchangeRateSnapshot> snapshots) {
		List<DirectedEdge> edges = new ArrayList<>();
		for (ExchangeRateSnapshot s : snapshots) {
			String p = s.pair();
			int slash = p.indexOf('/');
			if (slash <= 0 || slash == p.length() - 1) {
				continue;
			}
			String base = p.substring(0, slash).toUpperCase();
			String quote = p.substring(slash + 1).toUpperCase();
			BigDecimal r = s.rate();
			if (r.compareTo(BigDecimal.ZERO) <= 0) {
				continue;
			}
			edges.add(new DirectedEdge(base, quote, r));
			edges.add(new DirectedEdge(quote, base, BigDecimal.ONE.divide(r, MC)));
		}
		return edges;
	}

	private record DirectedEdge(String from, String to, BigDecimal factor) {
	}

	private record AmountAtCurrency(String code, BigDecimal amount) {
	}
}

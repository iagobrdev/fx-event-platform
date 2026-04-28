package com.fx.exchangerate.application.port.out;

import java.util.List;

public interface AvailableFxPairsPort {

	List<String> awesomeHyphenPairs();

	void refreshIfStale();
}

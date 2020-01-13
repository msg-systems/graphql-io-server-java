package com.thinkenterprise.graphqlio.server.samples.sample1.server.domain;

import java.util.Collection;
import java.util.HashMap;

import org.springframework.stereotype.Component;

@Component
public class RouteRepository {

	HashMap<String, Route> repositoryMap = new HashMap<String, Route>();

	public RouteRepository() {
		Route a = new Route("LH2113", "MUC", "BRE");
		repositoryMap.put("LH2113", a);

		Route b = new Route("BA7611", "HAM", "BCN");
		repositoryMap.put("BA7611", b);

		Route c = new Route("UA1000", "FRA", "CGN");
		repositoryMap.put("UA1000", c);
	}

	public Collection<Route> findAll() {
		return repositoryMap.values();
	}

	public Route getByFlightNumber(String flightNumber) {
		return repositoryMap.get(flightNumber);
	}

	public Route save(Route route) {
		repositoryMap.put(route.getFlightNumber(), route);
		return route;
	}

}

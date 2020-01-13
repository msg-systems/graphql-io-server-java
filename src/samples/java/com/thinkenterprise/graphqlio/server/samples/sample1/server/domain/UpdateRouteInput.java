package com.thinkenterprise.graphqlio.server.samples.sample1.server.domain;

public class UpdateRouteInput {

	private String flightNumber = null;
	private String departure = null;
	private String destination = null;

	UpdateRouteInput() {
	}

	UpdateRouteInput(String flightNumber, String departure, String destination) {
		this.flightNumber = flightNumber;
		this.departure = departure;
		this.destination = destination;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public String getDeparture() {
		return departure;
	}

	public void setDeparture(String departure) {
		this.departure = departure;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

}

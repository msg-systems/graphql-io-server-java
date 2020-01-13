package com.thinkenterprise.graphqlio.server.samples;

public class Route {

	private String flightNumber;
	private String departure;
	private String destination;

	public Route(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public Route(String flightNumber, String departure, String destination) {
		this.flightNumber = flightNumber;
		this.destination = destination;
		this.departure = departure;
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

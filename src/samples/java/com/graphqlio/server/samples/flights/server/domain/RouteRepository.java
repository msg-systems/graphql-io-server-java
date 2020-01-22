/*******************************************************************************
 * *
 * **  Design and Development by msg Applied Technology Research
 * **  Copyright (c) 2019-2020 msg systems ag (http://www.msg-systems.com/)
 * **  All Rights Reserved.
 * ** 
 * **  Permission is hereby granted, free of charge, to any person obtaining
 * **  a copy of this software and associated documentation files (the
 * **  "Software"), to deal in the Software without restriction, including
 * **  without limitation the rights to use, copy, modify, merge, publish,
 * **  distribute, sublicense, and/or sell copies of the Software, and to
 * **  permit persons to whom the Software is furnished to do so, subject to
 * **  the following conditions:
 * **
 * **  The above copyright notice and this permission notice shall be included
 * **  in all copies or substantial portions of the Software.
 * **
 * **  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * **  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * **  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * **  IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * **  CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * **  TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * **  SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * *
 ******************************************************************************/
package com.graphqlio.server.samples.flights.server.domain;

import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

import org.springframework.stereotype.Component;

/**
 * A simple repository where the route instances are hold.
 * 
 * @author Michael Schäfer
 * @author Torsten Kühnert
 */

@Component
public class RouteRepository {

	HashMap<String, Flight> repositoryMap = new HashMap<String, Flight>();

	public RouteRepository() {
		Flight a = new Flight("LH2113", "MUC", "BRE");
		a.setId(new Random().nextLong());
		repositoryMap.put("LH2113", a);

		Flight b = new Flight("BA7611", "HAM", "BCN");
		b.setId(new Random().nextLong());
		repositoryMap.put("BA7611", b);

		Flight c = new Flight("UA1000", "FRA", "CGN");
		c.setId(new Random().nextLong());
		repositoryMap.put("UA1000", c);
	}

	public Collection<Flight> findAll() {
		return repositoryMap.values();
	}

	public Flight getByFlightNumber(String flightNumber) {
		return repositoryMap.get(flightNumber);
	}

	public Flight save(Flight flight) {
		if (flight.getId() == null) {
			flight.setId(new Random().nextLong());
		}
		repositoryMap.put(flight.getFlightNumber(), flight);
		return flight;
	}

}

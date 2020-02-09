/*
**  Design and Development by msg Applied Technology Research
**  Copyright (c) 2019-2020 msg systems ag (http://www.msg-systems.com/)
**  All Rights Reserved.
** 
**  Permission is hereby granted, free of charge, to any person obtaining
**  a copy of this software and associated documentation files (the
**  "Software"), to deal in the Software without restriction, including
**  without limitation the rights to use, copy, modify, merge, publish,
**  distribute, sublicense, and/or sell copies of the Software, and to
**  permit persons to whom the Software is furnished to do so, subject to
**  the following conditions:
**
**  The above copyright notice and this permission notice shall be included
**  in all copies or substantial portions of the Software.
**
**  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
**  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
**  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
**  IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
**  CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
**  TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
**  SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.graphqlio.server.messages;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.socket.AbstractWebSocketMessage;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import com.graphqlio.server.helpers.FlightTest;
import com.graphqlio.server.helpers.RootMutationResolverTest;
import com.graphqlio.server.helpers.RootQueryResolverTest;
import com.graphqlio.server.server.GsServer;
import com.graphqlio.wsf.converter.WsfAbstractConverter;

/**
 * test class for testing queries, mutations, subscriptions and messages with
 * subprotocols
 *
 * @author Michael Schäfer
 * @author Torsten Kühnert
 */

@Tag("annotations")
@Tag("junit5")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
class MessagesTests {

	private final Logger logger = LoggerFactory.getLogger(MessagesTests.class);

	@LocalServerPort
	private int port;

	@Autowired
	private GsServer graphqlioServer;

	@Autowired
	private RootQueryResolverTest routeResolver;

	@Autowired
	private RootMutationResolverTest routeMutationResolver;
	
	@BeforeAll
	private void startServers() {
		this.graphqlioServer.registerGraphQLResolver(routeResolver);
		this.graphqlioServer.registerGraphQLResolver(routeMutationResolver);
		this.graphqlioServer.start();
	}

	@AfterAll
	private void stopServers() {
		this.graphqlioServer.stop();
	}

	@BeforeEach
	private void initRoutes() {
		this.routeResolver.init();
	}

	private final String simpleQuery = "[1,0,\"GRAPHQL-REQUEST\", {\"query\":\"query { routes { flightNumber departure destination } }\"} ]";

	@Test
	void whenTextMessageIsSendThenAnswerIsReturned() {
		try {
			MessagesTestsHandler webSocketHandler = new MessagesTestsHandler();

			WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
			headers.setSecWebSocketProtocol(Arrays.asList(WsfAbstractConverter.SUB_PROTOCOL_TEXT));

			URI uri = URI.create("ws://127.0.0.1:" + port + "/api/data/graph");

			AbstractWebSocketMessage message = new TextMessage(simpleQuery);

			WebSocketClient webSocketClient = new StandardWebSocketClient();
			WebSocketSession webSocketSession = webSocketClient.doHandshake(webSocketHandler, headers, uri).get();
			webSocketSession.sendMessage(message);

			while (webSocketHandler.count < 1) {
				Thread.sleep(100);
			}

			logger.info("webSocketHandler = " + webSocketHandler);

			Assert.assertTrue(webSocketHandler.text_count == 1);
			Assert.assertTrue(webSocketHandler.cbor_count == 0);
			Assert.assertTrue(webSocketHandler.msgpack_count == 0);
			Assert.assertTrue(webSocketHandler.default_count == 0);

			// [1,1,"GRAPHQL-RESPONSE",{"data":{"routes":[{"flightNumber":"LH2122","departure":"MUC","destination":"BRE"},{"flightNumber":"LH2084","departure":"CGN","destination":"BER"}]}}]

			String flight_a = "{\"flightNumber\":\"LH2084\",\"departure\":\"CGN\",\"destination\":\"BER\"}";
			String flight_b = "{\"flightNumber\":\"LH2122\",\"departure\":\"MUC\",\"destination\":\"BRE\"}";
			Assert.assertTrue(webSocketHandler.routes.contains(new FlightTest(flight_a)));
			Assert.assertTrue(webSocketHandler.routes.contains(new FlightTest(flight_b)));

			webSocketSession.close();

		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	private final String mutationQuery = "[1,0,\"GRAPHQL-REQUEST\", {\"query\":\"mutation { updateRoute( flightNumber: \\\"LH2084\\\" input: { flightNumber: \\\"LH2084\\\" departure: \\\"HAM\\\" destination: \\\"MUC\\\" disabled: false } ) { flightNumber departure destination } }\"} ]";

	@Test
	void whenBinaryMessageIsSendThenAnswerIsReturned() {
		try {
			MessagesTestsHandler webSocketHandler = new MessagesTestsHandler();

			WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
			headers.setSecWebSocketProtocol(Arrays.asList(WsfAbstractConverter.SUB_PROTOCOL_CBOR));

			URI uri = URI.create("ws://127.0.0.1:" + port + "/api/data/graph");

			AbstractWebSocketMessage message = new BinaryMessage(WsfAbstractConverter.toCbor(mutationQuery));

			WebSocketClient webSocketClient = new StandardWebSocketClient();
			WebSocketSession webSocketSession = webSocketClient.doHandshake(webSocketHandler, headers, uri).get();
			webSocketSession.sendMessage(message);

			while (webSocketHandler.count < 1) {
				Thread.sleep(100);
			}

			logger.info("webSocketHandler = " + webSocketHandler);

			Assert.assertTrue(webSocketHandler.text_count == 0);
			Assert.assertTrue(webSocketHandler.cbor_count == 1);
			Assert.assertTrue(webSocketHandler.msgpack_count == 0);
			Assert.assertTrue(webSocketHandler.default_count == 0);

			// [1,1,"GRAPHQL-RESPONSE",{"data":{"updateRoute":{"flightNumber":"LH2084","departure":"HAM","destination":"MUC"}}}]

			String flight_a = "{\"flightNumber\":\"LH2084\",\"departure\":\"HAM\",\"destination\":\"MUC\"}";
			Assert.assertTrue(webSocketHandler.routes.contains(new FlightTest(flight_a)));

			webSocketSession.close();

		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

}

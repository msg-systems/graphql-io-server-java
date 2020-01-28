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
package com.graphqlio.server.samples.counter.client;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.AbstractWebSocketMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Client application for the counter increase subscription sample. This client
 * subscribes on the counter value. When the counter is increased this client is
 * notificated by graphql-io-server.
 * 
 * @author Michael Schäfer
 * @author Torsten Kühnert
 */

public class CounterClientSubscription {

	private final Logger logger = LoggerFactory.getLogger(CounterClientSubscription.class);

	private final String Query = "[1,0,\"GRAPHQL-REQUEST\",{\"query\":\"query { _Subscription { subscribe } counter { value } }\"}]";
	private final String Query2 = "[3,1,\"GRAPHQL-REQUEST\",{\"query\":\"query { _Subscription { subscribe } counter { value value } }\"}]";

	public static void main(String[] args) {
		new CounterClientSubscription().runQuery();
	}

	public void runQuery() {
		try {
			final WebSocketClient webSocketClient = new StandardWebSocketClient();
			final WebSocketHandler webSocketHandler = new CounterClientSubscriptionHandler();
			final WebSocketHttpHeaders webSocketHttpHeaders = new WebSocketHttpHeaders();
			final URI uri = URI.create("ws://127.0.0.1:8080/api/data/graph");

			final WebSocketSession webSocketSession = webSocketClient
					.doHandshake(webSocketHandler, webSocketHttpHeaders, uri).get();

			AbstractWebSocketMessage message = new TextMessage(Query);
			webSocketSession.sendMessage(message);
			// second subscriber:
			message = new TextMessage(Query2);
			webSocketSession.sendMessage(message);

			logger.info("Subscription::waiting 60 seconds...");
			Thread.sleep(60000);
			webSocketSession.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class CounterClientSubscriptionHandler extends TextWebSocketHandler {

		@Override
		protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
			logger.info("Subscription::message received: " + message.getPayload());
		}

		@Override
		public void afterConnectionEstablished(WebSocketSession session) throws Exception {
			logger.info("Subscription::connection established: " + session.getId());
		}

	}

}
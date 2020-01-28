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
package com.graphqlio.server.samples.helloworld.client;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Client application for the hello world sample.
 * 
 * @author Michael Schäfer
 * @author Torsten Kühnert
 */

public class SampleHelloWorldJavaClient {

	private static final String helloWorldQuery = "[1,0,\"GRAPHQL-REQUEST\",{\"query\":\"query { hello }\"}]";

	public static void main(String[] args) {
		try {
			SampleHelloWorldHandler webSocketHandler = new SampleHelloWorldHandler();
			WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
			WebSocketClient webSocketClient = new StandardWebSocketClient();
			URI uri = URI.create("ws://127.0.0.1:8080/api/data/graph");
			WebSocketSession webSocketSession = webSocketClient.doHandshake(webSocketHandler, headers, uri).get();

			webSocketSession.sendMessage(new TextMessage(helloWorldQuery));

			Thread.sleep(200);

			webSocketSession.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static class SampleHelloWorldHandler extends TextWebSocketHandler {

		private final Logger logger = LoggerFactory.getLogger(SampleHelloWorldHandler.class);

		@Override
		protected void handleTextMessage(WebSocketSession session, TextMessage message) {
			logger.info("message received : id = " + session.getId());
			logger.info("                 : message = " + message.getPayload());
		}

		@Override
		public void afterConnectionEstablished(WebSocketSession session) {
			logger.info("connection est.  : id = " + session.getId());
		}

	}

}
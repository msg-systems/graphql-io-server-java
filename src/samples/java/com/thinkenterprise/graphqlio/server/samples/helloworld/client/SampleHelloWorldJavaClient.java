package com.thinkenterprise.graphqlio.server.samples.helloworld.client;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import com.thinkenterprise.graphqlio.server.handler.GsWebSocketHandler;

public class SampleHelloWorldJavaClient {

	private final Logger logger = LoggerFactory.getLogger(SampleHelloWorldJavaClient.class);

	private static final String helloWorldQuery = "[1,0,\"GRAPHQL-REQUEST\",query { hello } ]";

	private static final List<String> subscriptionIds = new ArrayList<String>();

	public static void main(String[] args) {
		try {
			GraphQLIOSampleHandler webSocketHandler = new GraphQLIOSampleHandler(subscriptionIds);

			WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
			headers.setSecWebSocketProtocol(Arrays.asList(GsWebSocketHandler.SUB_PROTOCOL_TEXT));

			WebSocketClient webSocketClient = new StandardWebSocketClient();
			WebSocketSession webSocketSession = webSocketClient
					.doHandshake(webSocketHandler, headers, URI.create("ws://127.0.0.1:8080/api/data/graph")).get();

			// send 1st query to GraphQLIO server:
			webSocketSession.sendMessage(new TextMessage(helloWorldQuery));
			// a little wait for getting and handling answer(s)
			Thread.sleep(200);

			webSocketSession.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
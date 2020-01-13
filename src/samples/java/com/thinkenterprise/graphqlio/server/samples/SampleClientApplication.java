package com.thinkenterprise.graphqlio.server.samples;

import java.net.URI;

import org.springframework.web.socket.AbstractWebSocketMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

public class SampleClientApplication {

	private final String Query = "[1,0,\"GRAPHQL-REQUEST\",query { allRoutes { flightNumber departure destination } } ]";

	public static void main(String[] args) {
		new SampleClientApplication().runQuery();
	}

	public void runQuery() {
		try {
			final WebSocketClient webSocketClient = new StandardWebSocketClient();
			final WebSocketHandler webSocketHandler = new SampleClientWebSocketHandler();
			final WebSocketHttpHeaders webSocketHttpHeaders = new WebSocketHttpHeaders();
			final URI uri = URI.create("ws://127.0.0.1:8080/api/data/graph");

			final WebSocketSession webSocketSession = webSocketClient
					.doHandshake(webSocketHandler, webSocketHttpHeaders, uri).get();

			final AbstractWebSocketMessage message = new TextMessage(Query);
			webSocketSession.sendMessage(message);

			Thread.sleep(3000);
			webSocketSession.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
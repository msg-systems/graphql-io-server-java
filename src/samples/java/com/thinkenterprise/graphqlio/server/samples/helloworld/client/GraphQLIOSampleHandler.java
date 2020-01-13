package com.thinkenterprise.graphqlio.server.samples.helloworld.client;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class GraphQLIOSampleHandler extends TextWebSocketHandler {

	private final Logger logger = LoggerFactory.getLogger(GraphQLIOSampleHandler.class);

	private static List<String> subscriptionIds = new ArrayList<String>();

	public GraphQLIOSampleHandler(List<String> subscriptionIds) {
		this.subscriptionIds = subscriptionIds;
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		logger.info("message received : id = " + session.getId());
		logger.info("                 : message = " + message.getPayload());

		if (this.isResponse(message.getPayload())) {
			if (this.isSubscription(message.getPayload())) {
				this.handleSubscriptions(message.getPayload());
			} else {
				// handle other messages
			}

		} else if (this.isNotification(message.getPayload())) {
			// handle notificvation
		}
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		logger.info("connection est.  : id = " + session.getId());
	}

	private boolean isResponse(String payload) {
		return payload.indexOf("GRAPHQL-RESPONSE") >= 0;
	}

	private boolean isNotification(String payload) {
		return payload.indexOf("GRAPHQL-NOTIFIER") >= 0;
	}

	private boolean isSubscription(String payload) {
		return payload.indexOf("_Subscription") >= 0 && payload.indexOf("\"subscribe") > 0;
	}

	private void handleSubscriptions(String payload) {
		int pos = payload.indexOf("\"subscribe");
		if (pos > 0) {
			payload = payload.substring(pos - 1, payload.indexOf("}", pos) + 1);
			JSONObject json = new JSONObject(payload);
			String subscriptionId = json.getString("subscribe");
			logger.info("                 : subscriptionId = " + subscriptionId);
			this.subscriptionIds.add(subscriptionId);
		}
	}

}

/**
 * *****************************************************************************
 *
 * <p>Design and Development by msg Applied Technology Research Copyright (c) 2019-2020 msg systems
 * ag (http://www.msg-systems.com/) All Rights Reserved.
 *
 * <p>Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * <p>The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * <p>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * <p>****************************************************************************
 */
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
 * Client application for the counter increase sample. The counter is increased every second for 50
 * seconds.
 *
 * @author Michael Schäfer
 * @author Torsten Kühnert
 */
public class CounterClientIncrease {

  private final String Query =
      "[1,0,\"GRAPHQL-REQUEST\",{\"query\":\"{ counter { increase { value } } }\"}]";

  public static void main(String[] args) {
    new CounterClientIncrease().runQuery();
  }

  public void runQuery() {
    try {
      final WebSocketClient webSocketClient = new StandardWebSocketClient();
      final WebSocketHandler webSocketHandler = new CounterClientIncreaseHandler();
      final WebSocketHttpHeaders webSocketHttpHeaders = new WebSocketHttpHeaders();
      final URI uri = URI.create("ws://127.0.0.1:8080/api/data/graph");

      final WebSocketSession webSocketSession =
          webSocketClient.doHandshake(webSocketHandler, webSocketHttpHeaders, uri).get();

      final AbstractWebSocketMessage message = new TextMessage(Query);

      // sending this increase-message 50 times with 1 sec waiting
      for (int i = 0; i < 50; i++) {
        webSocketSession.sendMessage(message);

        Thread.sleep(1000);
      }

      webSocketSession.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private class CounterClientIncreaseHandler extends TextWebSocketHandler {

    private final Logger logger = LoggerFactory.getLogger(CounterClientIncreaseHandler.class);

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)
        throws Exception {
      logger.info("Increase::message received: " + message.getPayload());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
      logger.info("Increase::connection established: " + session.getId());
    }
  }
}

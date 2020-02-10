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
package com.graphqlio.server.samples.types.client;

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
 * Client application for the flights sample. 1st query of all flights 2nd mutation of UA flight 3rd
 * query of all flights
 *
 * @author Michael Schäfer
 * @author Torsten Kühnert
 */
public class TypesClientApplication {

  private final String QueryDate = "[1,0,\"GRAPHQL-REQUEST\",{\"query\":\"query { getDate }\"} ]";
  private final String QueryJson = "[2,1,\"GRAPHQL-REQUEST\",{\"query\":\"query { getJson }\"} ]";
  private final String QueryUuid = "[3,2,\"GRAPHQL-REQUEST\",{\"query\":\"query { getUuid }\"} ]";
  private final String QueryVoid = "[4,3,\"GRAPHQL-REQUEST\",{\"query\":\"query { getVoid }\"} ]";

  private final String MutationDate =
      "[5,4,\"GRAPHQL-REQUEST\",{\"query\":\"mutation { updateDate ( date: \\\"2020-02-20 20:02:20\\\",                  input: \\\"1999-09-19 19:09:10\\\" )                  } \"} ]";
  private final String MutationJson =
      "[6,5,\"GRAPHQL-REQUEST\",{\"query\":\"mutation { updateJson ( json: \\\"{ x: 3, y: 4, z:5 }\\\",                  input: \\\"{ x: 9, y: 8, z: 7 }\\\" )                 } \"} ]";
  private final String MutationUuid =
      "[7,6,\"GRAPHQL-REQUEST\",{\"query\":\"mutation { updateUuid ( uuid: \\\"00000005-e566-4dc5-0000-001be8a4e0e1\\\", input: \\\"00000005-e566-4dc5-0000-001be8a4e0e2\\\" ) } \"} ]";
  private final String MutationVoid =
      "[8,7,\"GRAPHQL-REQUEST\",{\"query\":\"mutation { updateVoid ( vOID: null,                                         input: null  )                                        } \"} ]";

  public static void main(String[] args) {
    new TypesClientApplication().runQuery();
  }

  public void runQuery() {
    try {
      final WebSocketClient webSocketClient = new StandardWebSocketClient();
      final WebSocketHandler webSocketHandler = new TypesClientWebSocketHandler();
      final WebSocketHttpHeaders webSocketHttpHeaders = new WebSocketHttpHeaders();
      final URI uri = URI.create("ws://127.0.0.1:8080/api/data/graph");

      final WebSocketSession webSocketSession =
          webSocketClient.doHandshake(webSocketHandler, webSocketHttpHeaders, uri).get();

      AbstractWebSocketMessage message = new TextMessage(QueryDate);
      webSocketSession.sendMessage(message);
      Thread.sleep(1000);

      message = new TextMessage(QueryJson);
      webSocketSession.sendMessage(message);
      Thread.sleep(1000);

      message = new TextMessage(QueryUuid);
      webSocketSession.sendMessage(message);
      Thread.sleep(1000);

      message = new TextMessage(QueryVoid);
      webSocketSession.sendMessage(message);
      Thread.sleep(1000);

      message = new TextMessage(MutationDate);
      webSocketSession.sendMessage(message);
      Thread.sleep(1000);

      message = new TextMessage(MutationJson);
      webSocketSession.sendMessage(message);
      Thread.sleep(1000);

      message = new TextMessage(MutationUuid);
      webSocketSession.sendMessage(message);
      Thread.sleep(1000);

      message = new TextMessage(MutationVoid);
      webSocketSession.sendMessage(message);
      Thread.sleep(1000);

      webSocketSession.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private class TypesClientWebSocketHandler extends TextWebSocketHandler {

    private final Logger logger = LoggerFactory.getLogger(TypesClientWebSocketHandler.class);

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)
        throws Exception {
      logger.info("message received: " + message.getPayload());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
      logger.info("conn.established: " + session.getId());
    }
  }
}

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
package com.graphqlio.server.subscription;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import com.graphqlio.server.helpers.FlightTest;
import com.graphqlio.wsf.converter.WsfAbstractConverter;

/**
 * websockethandler Class for testing subscriptions
 *
 * @author Michael Schäfer
 * @author Torsten Kühnert
 */
public class SubscriptionTestsHandler extends AbstractWebSocketHandler {

  private final Logger logger = LoggerFactory.getLogger(SubscriptionTestsHandler.class);

  public int text_count = 0;
  public int cbor_count = 0;
  public int msgpack_count = 0;
  public int default_count = 0;

  public int count = 0;

  public int notifier_count = 0;

  public List<FlightTest> routes = new ArrayList<FlightTest>();
  public List<String> subscriptionIds = new ArrayList<String>();

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    logger.info("handleTextMessage");
    this.handlePayload(session, message.getPayload());
  }

  @Override
  protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message)
      throws Exception {
    logger.info("handleBinaryMessage");
    this.handlePayload(session, message.getPayload());
  }

  protected void handlePayload(WebSocketSession session, Object payload) throws Exception {
    logger.info("session.getAcceptedProtocol = " + session.getAcceptedProtocol());
    logger.info("payload = " + payload);

    if (WsfAbstractConverter.SUB_PROTOCOL_TEXT.equalsIgnoreCase(session.getAcceptedProtocol())) {
      this.text_count++;
      this.count++;
      String msg = (String) payload;
      if (isNotifier(msg)) {
        this.notifier_count++;
      } else {
        this.addFlights(msg);
        this.handleSubecriptionIds(msg);
      }

    } else if (WsfAbstractConverter.SUB_PROTOCOL_CBOR.equalsIgnoreCase(
        session.getAcceptedProtocol())) {
      this.cbor_count++;
      this.count++;
      String msg = WsfAbstractConverter.fromCbor((ByteBuffer) payload);
      if (isNotifier(msg)) {
        this.notifier_count++;
      } else {
        this.addFlights(msg);
        this.handleSubecriptionIds(msg);
      }

    } else if (WsfAbstractConverter.SUB_PROTOCOL_MSGPACK.equalsIgnoreCase(
        session.getAcceptedProtocol())) {
      this.msgpack_count++;
      this.count++;
      String msg = WsfAbstractConverter.fromMsgPack((ByteBuffer) payload);
      if (isNotifier(msg)) {
        this.notifier_count++;
      } else {
        this.addFlights(msg);
        this.handleSubecriptionIds(msg);
      }

    } else {
      this.default_count++;
      this.count++;
      String msg = (String) payload;
      if (isNotifier(msg)) {
        this.notifier_count++;
      } else {
        this.addFlights(msg);
        this.handleSubecriptionIds(msg);
      }
    }
  }

  private boolean isNotifier(String msg) {
    return msg.indexOf("GRAPHQL-NOTIFY") > 0;
  }

  // [1,1,"GRAPHQL-RESPONSE",{"data":{"_Subscription":{"subscribe":"2250bf90-f6a4-4a4d-9587-4e538bb2d4ab"},"routes":[{"flightNumber":"LH2122","departure":"MUC","destination":"BRE"},{"flightNumber":"LH2084","departure":"CGN","destination":"BER"}]}}]

  protected void handleSubecriptionIds(String payload) throws Exception {
    int pos_gql = payload.indexOf("GRAPHQL-RESPONSE");
    int pos_sub = payload.indexOf("_Subscription");
    int pos = payload.indexOf("\"subscribe");

    if (pos_gql > 0 && pos_sub > 0 && pos > 0) {
      payload = payload.substring(pos - 1, payload.indexOf("}", pos) + 1);
      logger.info("handleSubecriptionIds payload = " + payload);
      JSONObject json = new JSONObject(payload);
      String subscriptionId = json.getString("subscribe");
      logger.info("handleSubecriptionIds subscriptionId = " + subscriptionId);
      this.subscriptionIds.add(subscriptionId);
    }
  }

  // [1,1,"GRAPHQL-RESPONSE",{"data":{"routes":[{"flightNumber":"LH2122","departure":"MUC","destination":"BRE"},{"flightNumber":"LH2084","departure":"CGN","destination":"BER"}]}}]
  // [1,1,"GRAPHQL-RESPONSE",{"data":{"updateRoute":{"flightNumber":"LH2084","departure":"HAM","destination":"MUC"}}}]

  private void addFlights(String msg) throws JSONException {
    int pos = msg.indexOf("{\"data\":");
    if (pos > 0) {
      String jsonStr = msg.substring(pos);
      JSONObject jsonObj = new JSONObject(jsonStr);
      JSONObject dataObj = jsonObj.getJSONObject("data");

      if (dataObj.has("routes")) {
        JSONArray routesArr = dataObj.getJSONArray("routes");

        for (int i = 0; i < routesArr.length(); i++) {
          JSONObject flightObj = routesArr.getJSONObject(i);

          this.routes.add(new FlightTest(flightObj.toString()));
        }

      } else if (dataObj.has("updateRoute")) {
        JSONObject flightObj = dataObj.getJSONObject("updateRoute");

        this.routes.add(new FlightTest(flightObj.toString()));
      }
    }
  }
}

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

import java.net.URI;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.socket.AbstractWebSocketMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import com.graphqlio.server.helpers.FlightTest;
import com.graphqlio.server.helpers.RootMutationResolverTest;
import com.graphqlio.server.helpers.RootQueryResolverTest;
import com.graphqlio.server.server.GsServer;

/**
 * Class for testing subscriptions
 *
 * @author Michael Schäfer
 * @author Torsten Kühnert
 */
@Tag("annotations")
@Tag("junit5")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
class SubscriptionTests {

  @LocalServerPort private int port;

  @Autowired private GsServer graphqlioServer;

  @Autowired private RootQueryResolverTest routeResolver;

  @Autowired private RootMutationResolverTest routeMutationResolver;

  @BeforeAll
  private void startServers() {
    this.graphqlioServer.registerGraphQLResolver(routeResolver);
    this.graphqlioServer.registerGraphQLResolver(routeMutationResolver);
    this.graphqlioServer.start();
  }

  @AfterAll
  private void stopServers() {
    this.graphqlioServer.stop();
    this.graphqlioServer.deregisterGraphQLResolver(routeResolver);
    this.graphqlioServer.deregisterGraphQLResolver(routeMutationResolver);
  }

  @BeforeEach
  private void initRoutes() {
    this.routeResolver.init();
  }

  private final String subscriptionQuery1 =
      "[1,0,\"GRAPHQL-REQUEST\",{\"query\":\"query { _Subscription { subscribe } routes { flightNumber departure destination disabled signature bookingDate } }\"} ]";
  private final String mutationQuery2a =
      "[1,0,\"GRAPHQL-REQUEST\",{\"query\" : \"mutation { updateRoute(flightNumber: \\\"LH2084\\\" input: { flightNumber: \\\"LH2084\\\" departure: \\\"HAM\\\" destination: \\\"ROM\\\" disabled: true signature: null } ) { flightNumber departure destination disabled signature bookingDate } }\" } ]";
  private final String mutationQuery2b =
      "[1,0,\"GRAPHQL-REQUEST\",{\"query\": \"mutation { updateRoute(flightNumber: \\\"LH2122\\\" input: { flightNumber: \\\"LH2122\\\" departure: \\\"FRA\\\" destination: \\\"BCN\\\" disabled: true signature: null } ) { flightNumber departure destination disabled signature bookingDate } }\" } ]";
  private final String unsubscribeQuery3 =
      "[1,0,\"GRAPHQL-REQUEST\",{\"query\":\"mutation { _Subscription { unsubscribe( sid: \\\"%s\\\" ) } }\"} ]";
  private final String mutationQuery4 =
      "[1,0,\"GRAPHQL-REQUEST\",{\"query\":\"mutation { updateRoute(flightNumber: \\\"LH2084\\\" input: { flightNumber: \\\"LH2084\\\" departure: \\\"ROM\\\" destination: \\\"HAM\\\" disabled: false } ) { flightNumber departure destination disabled signature bookingDate } }\"} ]";

  private final String flight_1a =
      "{\"flightNumber\":\"LH2084\",\"departure\":\"CGN\",\"destination\":\"BER\"}";
  private final String flight_1b =
      "{\"flightNumber\":\"LH2122\",\"departure\":\"MUC\",\"destination\":\"BRE\"}";
  private final String flight_2a =
      "{\"flightNumber\":\"LH2084\",\"departure\":\"HAM\",\"destination\":\"ROM\"}";
  private final String flight_2b =
      "{\"flightNumber\":\"LH2122\",\"departure\":\"FRA\",\"destination\":\"BCN\"}";
  private final String flight_3 =
      "{\"flightNumber\":\"LH2084\",\"departure\":\"HAM\",\"destination\":\"ROM\"}";
  private final String flight_4 =
      "{\"flightNumber\":\"LH2084\",\"departure\":\"ROM\",\"destination\":\"HAM\"}";

  @Test
  void whenSubscriptionIsUsedThenNotifictionsAreSend() {
    try {
      SubscriptionTestsHandler webSocketHandler = new SubscriptionTestsHandler();

      WebSocketHttpHeaders headers = new WebSocketHttpHeaders();

      URI uri = URI.create("ws://127.0.0.1:" + port + "/api/data/graph");

      WebSocketClient webSocketClient = new StandardWebSocketClient();
      WebSocketSession webSocketSession =
          webSocketClient.doHandshake(webSocketHandler, headers, uri).get();

      ////////////////////////////
      // 1st: subscriptionQuery1
      ////////////////////////////

      AbstractWebSocketMessage textMessage = new TextMessage(subscriptionQuery1);
      webSocketSession.sendMessage(textMessage);

      while (webSocketHandler.count < 1) {
        Thread.sleep(100);
      }

      Assert.assertTrue(webSocketHandler.text_count == 0);
      Assert.assertTrue(webSocketHandler.cbor_count == 0);
      Assert.assertTrue(webSocketHandler.msgpack_count == 0);
      Assert.assertTrue(webSocketHandler.default_count == 1);

      Assert.assertTrue(webSocketHandler.subscriptionIds.size() == 1);
      Assert.assertTrue(webSocketHandler.notifier_count == 0);

      Assert.assertTrue(webSocketHandler.routes.contains(new FlightTest(flight_1a)));
      Assert.assertTrue(webSocketHandler.routes.contains(new FlightTest(flight_1b)));
      Assert.assertTrue(routeResolver.allRoutes.values().contains(new FlightTest(flight_1a)));
      Assert.assertTrue(routeResolver.allRoutes.values().contains(new FlightTest(flight_1b)));
      Assert.assertTrue(!routeResolver.allRoutes.values().contains(new FlightTest(flight_2a)));
      Assert.assertTrue(!routeResolver.allRoutes.values().contains(new FlightTest(flight_2b)));

      ////////////////////////////
      // 2nd: mutationQuery2a & mutationQuery2b
      ////////////////////////////

      textMessage = new TextMessage(mutationQuery2a);
      webSocketSession.sendMessage(textMessage);
      textMessage = new TextMessage(mutationQuery2b);
      webSocketSession.sendMessage(textMessage);

      while (webSocketHandler.count < 5) {
        Thread.sleep(100);
      }

      Assert.assertTrue(webSocketHandler.text_count == 0);
      Assert.assertTrue(webSocketHandler.cbor_count == 0);
      Assert.assertTrue(webSocketHandler.msgpack_count == 0);
      Assert.assertTrue(webSocketHandler.default_count == 5);

      Assert.assertTrue(webSocketHandler.subscriptionIds.size() == 1);
      Assert.assertTrue(webSocketHandler.notifier_count == 2);

      Assert.assertTrue(webSocketHandler.routes.contains(new FlightTest(flight_2a)));
      Assert.assertTrue(webSocketHandler.routes.contains(new FlightTest(flight_2b)));
      Assert.assertTrue(routeResolver.allRoutes.values().contains(new FlightTest(flight_2a)));
      Assert.assertTrue(routeResolver.allRoutes.values().contains(new FlightTest(flight_2b)));
      Assert.assertTrue(!routeResolver.allRoutes.values().contains(new FlightTest(flight_1a)));
      Assert.assertTrue(!routeResolver.allRoutes.values().contains(new FlightTest(flight_1b)));

      ////////////////////////////
      // 3rd: unsubscribeQuery3
      ////////////////////////////

      textMessage =
          new TextMessage(unsubscribeQuery3.replace("%s", webSocketHandler.subscriptionIds.get(0)));
      webSocketSession.sendMessage(textMessage);

      while (webSocketHandler.count < 6) {
        Thread.sleep(100);
      }

      Assert.assertTrue(webSocketHandler.text_count == 0);
      Assert.assertTrue(webSocketHandler.cbor_count == 0);
      Assert.assertTrue(webSocketHandler.msgpack_count == 0);
      Assert.assertTrue(webSocketHandler.default_count == 6);

      Assert.assertTrue(webSocketHandler.subscriptionIds.size() == 1);
      Assert.assertTrue(webSocketHandler.notifier_count == 2);

      Assert.assertTrue(webSocketHandler.routes.contains(new FlightTest(flight_3)));
      Assert.assertTrue(routeResolver.allRoutes.values().contains(new FlightTest(flight_3)));
      Assert.assertTrue(!routeResolver.allRoutes.values().contains(new FlightTest(flight_4)));

      ////////////////////////////
      // 4th: mutationQuery4
      ////////////////////////////

      textMessage = new TextMessage(mutationQuery4);
      webSocketSession.sendMessage(textMessage);

      while (webSocketHandler.count < 7) {
        Thread.sleep(100);
      }

      Assert.assertTrue(webSocketHandler.text_count == 0);
      Assert.assertTrue(webSocketHandler.cbor_count == 0);
      Assert.assertTrue(webSocketHandler.msgpack_count == 0);
      Assert.assertTrue(webSocketHandler.default_count == 7);

      Assert.assertTrue(webSocketHandler.subscriptionIds.size() == 1);
      Assert.assertTrue(webSocketHandler.notifier_count == 2);

      Assert.assertTrue(webSocketHandler.routes.contains(new FlightTest(flight_4)));
      Assert.assertTrue(routeResolver.allRoutes.values().contains(new FlightTest(flight_4)));
      Assert.assertTrue(!routeResolver.allRoutes.values().contains(new FlightTest(flight_3)));

      webSocketSession.close();

    } catch (Exception e) {
      // e.printStackTrace();
    }
  }
}

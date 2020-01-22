# About
graphql-io-server-java is the **Java** implementation of the **JavaScript** graphql-io-server reference implementation. 
The documentation of the reference implementation can be found at  (https://github.com/rse/graphql-io-server). 

# Installation
## Maven 

```
mvn clean install
```

## Gradle 

FIXME

# Usage 

## Maven 
```
<dependency>
	<groupId>com.graphqlio</groupId>
	<artifactId>graphql-io-server-java</artifactId>
	<version>0.0.9</version>
</dependency>

```

## Gradle 

```
dependencies {
  compile 'com.graphqlio:graphql-io-server-java:0.0.9'
}
```


# Samples 

## Hello World Sample

[Link to the Hello World Sample ...](https://github.com/Thinkenterprise/graphql-io-server-java/tree/master/src/samples/java/com/thinkenterprise/graphqlio/server/samples/helloworld)

graphql schema:

```
schema {
	query: Query
}
type Query {
	hello: String!
}
```

graphql resolver class:

``` java
@Component
public class SampleHelloWorldResolver implements GraphQLQueryResolver {
	public String hello() {
		return "Hello World";
	}
}
```

spring boot application with graphql-io-server:

```java
@SpringBootApplication
@EnableGraphQLIOServer
public class SampleHelloWorldApplication implements ApplicationRunner {

        // GraphQL Server
	private GsServer graphqlioServer;
	
	SampleHelloWorldApplication(GsServer graphqlioServer) {
		this.graphqlioServer = graphqlioServer;
	}

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(SampleHelloWorldApplication.class);
		
		Properties properties = new Properties();
		properties.put("graphqlio.server.schemaLocationPattern", "**/*.helloworld.graphql");
		properties.put("graphqlio.server.endpoint", "/api/data/graph");
		properties.put("graphqlio.toolssubscribe.useEmbeddedRedis", "true");
			
		application.setDefaultProperties(properties);
		application.run(args);
	}
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		this.graphqlioServer.start();
	}
	
	@PreDestroy
	public void destroy() throws Exception {
		this.graphqlioServer.stop();
	}
}
```

Client requesting "hello":

``` java
	String helloWorldQuery = "[1,0,\"GRAPHQL-REQUEST\",query { hello } ]";

	SampleHelloWorldHandler webSocketHandler = new SampleHelloWorldHandler();
	WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
	WebSocketClient webSocketClient = new StandardWebSocketClient();
	URI uri = URI.create("ws://127.0.0.1:8080/api/data/graph");
	WebSocketSession webSocketSession = webSocketClient.doHandshake(webSocketHandler, headers, uri).get();

	webSocketSession.sendMessage(new TextMessage(helloWorldQuery));

	Thread.sleep(200);

	webSocketSession.close();
```

Handler receiving response:

``` java
	private static class SampleHelloWorldHandler extends TextWebSocketHandler {

		@Override
		protected void handleTextMessage(WebSocketSession session, TextMessage message) {
			System.out.println("message received : id = " + session.getId());
			System.out.println("                 : message = " + message.getPayload());
		}

		@Override
		public void afterConnectionEstablished(WebSocketSession session) {
			System.out.println("connection est.  : id = " + session.getId());
		}
	}
```


## Counter Sample (increase, subscription)

[Link to the Hello Counter Sample ...](https://github.com/Thinkenterprise/graphql-io-server-java/tree/master/src/samples/java/com/thinkenterprise/graphqlio/server/samples/counter)

Graphql schema:

``` 
schema {
	query: Query
}
type Query {
	counter: Counter
}
type Counter {
	value: Int
	increase: Counter
}
```

Graphql domain classes:

``` java
public class Counter {

	private int value = 0;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public void inc() {
		this.value++;
	}
}

```

Graphql resolver classes:

``` java
@Component
public class RootQueryResolver implements GraphQLQueryResolver {

	private CounterRepository repo;

	public RootQueryResolver(CounterRepository repo) {
		this.repo = repo;
	}

	public Counter counter(DataFetchingEnvironment env) {
		Counter counter = repo.getCounter();

		GtsContext context = env.getContext();
		GtsScope scope = context.getScope();
		scope.addRecord(GtsRecord.builder().op(GtsOperationType.READ).arity(GtsArityType.ALL)
				.dstType(Counter.class.getName()).dstIds(new String[] { "0" }).dstAttrs(new String[] { "*" }).build());

		return counter;
	}
}

@Component
public class CounterQueryResolver implements GraphQLResolver<Counter> {

	private CounterRepository repo;

	public CounterQueryResolver(CounterRepository repo) {
		this.repo = repo;
	}

	public Counter increase(Counter counter, DataFetchingEnvironment env) {
		Counter counter = repo.getCounter();

		counter.inc();

		GtsContext context = env.getContext();
		GtsScope scope = context.getScope();
		scope.addRecord(GtsRecord.builder().op(GtsOperationType.UPDATE).arity(GtsArityType.ALL)
				.dstType(Counter.class.getName()).dstIds(new String[] { "0" }).dstAttrs(new String[] { "*" }).build());

		return counter;
	}
}
```

Spring boot application with graphql-io-server:

``` java
@SpringBootApplication
@EnableGraphQLIOServer
public class CounterServerApplication implements ApplicationRunner {

	private GsServer graphqlioServer;

	CounterServerApplication(GsServer graphqlioServer) {
		this.graphqlioServer = graphqlioServer;
	}

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(CounterServerApplication.class);

		Properties properties = new Properties();
		properties.put("graphqlio.server.schemaLocationPattern", "**/*.counter.graphql");
		properties.put("graphqlio.server.endpoint", "/api/data/graph");
		properties.put("graphqlio.toolssubscribe.useEmbeddedRedis", "true");
		
		application.setDefaultProperties(properties);
		application.run(args);
	}

	

	@Override
	public void run(ApplicationArguments args) throws Exception {
		this.graphqlioServer.start();
	}

	@PreDestroy
	public void destroy() throws Exception {
		this.graphqlioServer.stop();
	}
}
```

Client subscribing to counter value, handler for responses and notifications:

``` java
	final String Query = "[1,0,\"GRAPHQL-REQUEST\",query { _Subscription { subscribe } counter { value } } ]";

	final WebSocketClient webSocketClient = new StandardWebSocketClient();
	final WebSocketHandler webSocketHandler = new CounterClientSubscriptionHandler();
	final WebSocketHttpHeaders webSocketHttpHeaders = new WebSocketHttpHeaders();
	final URI uri = URI.create("ws://127.0.0.1:8080/api/data/graph");

	final WebSocketSession webSocketSession = webSocketClient
			.doHandshake(webSocketHandler, webSocketHttpHeaders, uri).get();

	final AbstractWebSocketMessage message = new TextMessage(Query);
			webSocketSession.sendMessage(message);

	System.out.println("Subscription::waiting 60 seconds...");
	Thread.sleep(60000);
	webSocketSession.close();


	class CounterClientSubscriptionHandler extends TextWebSocketHandler {

		@Override
		protected void handleTextMessage(WebSocketSession session, TextMessage message) {
			System.out.println("Subscription::message received: " + message.getPayload());
		}

		@Override
		public void afterConnectionEstablished(WebSocketSession session) {
			System.out.println("Subscription::connection established: " + session.getId());
		}
	}
```

client increasing counter value every second, handler for responses:

``` java
	final String Query = "[1,0,\"GRAPHQL-REQUEST\",query { counter { increase { value } } } ]";

	final WebSocketClient webSocketClient = new StandardWebSocketClient();
	final WebSocketHandler webSocketHandler = new CounterClientIncreaseHandler();
	final WebSocketHttpHeaders webSocketHttpHeaders = new WebSocketHttpHeaders();
	final URI uri = URI.create("ws://127.0.0.1:8080/api/data/graph");

	final WebSocketSession webSocketSession = webSocketClient
			.doHandshake(webSocketHandler, webSocketHttpHeaders, uri).get();

	final AbstractWebSocketMessage message = new TextMessage(Query);

	// sending this increase-message 50 times with 1 sec waiting
	for (int i = 0; i < 50; i++) {
		webSocketSession.sendMessage(message);
		Thread.sleep(1000);
	}
	webSocketSession.close();


	class CounterClientIncreaseHandler extends TextWebSocketHandler {

		@Override
		protected void handleTextMessage(WebSocketSession session, TextMessage message) {
			System.out.println("Increase::message received: " + message.getPayload());
		}

		@Override
		public void afterConnectionEstablished(WebSocketSession session) {
			System.out.println("Increase::connection established: " + session.getId());
		}
	}
```


# License 
Design and Development by msg Applied Technology Research
Copyright (c) 2019-2020 msg systems ag (http://www.msg-systems.com/)
All Rights Reserved.
 
Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:
 
The above copyright notice and this permission notice shall be included
in all copies or substantial portions of the Software.
 
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

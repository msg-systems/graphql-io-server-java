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

## Counter Sample (increase, subscription)

[Link to the Counter Sample ...](https://github.com/Thinkenterprise/graphql-io-server-java/tree/master/src/samples/java/com/thinkenterprise/graphqlio/server/samples/counter)


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


Graphql resolver classes:

``` java
@Component
public class RootQueryResolver implements GraphQLQueryResolver {

	public Counter counter(DataFetchingEnvironment env) {
		Counter counter = counterRepository.getCounter();

		GtsContext context = env.getContext();
		GtsScope scope = context.getScope();
		scope.addRecord(
			GtsRecord.builder()
				.op(GtsOperationType.READ)
				.arity(GtsArityType.ALL)
				.dstType(Counter.class.getName())
				.dstIds(new String[] { "0" })
				.dstAttrs(new String[] { "*" })
				.build()
			);

		return counter;
	}
}

@Component
public class CounterQueryResolver implements GraphQLResolver<Counter> {

	public Counter increase(Counter counter, DataFetchingEnvironment env) {
		Counter counter = counterRepository.getCounter();

		counter.inc();

		GtsContext context = env.getContext();
		GtsScope scope = context.getScope();
		scope.addRecord(
			GtsRecord.builder()
				.op(GtsOperationType.UPDATE)
				.arity(GtsArityType.ALL)
				.dstType(Counter.class.getName())
				.dstIds(new String[] { "0" })
				.dstAttrs(new String[] { "*" })
				.build()
			);

		return counter;
	}
}
```


Spring boot application with graphql-io-server:

``` java
@SpringBootApplication
@EnableGraphQLIOServer
public class CounterServerApplication implements ApplicationRunner {

	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.put("graphqlio.server.schemaLocationPattern", "**/*.counter.graphql");
		properties.put("graphqlio.server.endpoint", "/api/data/graph");
		properties.put("graphqlio.toolssubscribe.useEmbeddedRedis", "true");

		SpringApplication application = new SpringApplication(CounterServerApplication.class);
		application.setDefaultProperties(properties);
		application.run(args);
	}

	@Autowired
	private GsServer graphqlioServer;

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


Client subscribing to counter.value:

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
```


client increasing counter.value every second:

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

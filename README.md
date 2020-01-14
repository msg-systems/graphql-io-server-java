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
	<groupId>com.thinkenterprise</groupId>
	<artifactId>graphql-io-server-java</artifactId>
	<version>0.0.9</version>
</dependency>

```

## Gradle 

```
dependencies {
  compile 'com.thinkenterprise:graphql-io-server-java:0.0.9'
}
```


# Samples 

## Hello World Sample

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

```
@Component
public class SampleHelloWorldResolver implements GraphQLQueryResolver {
	public String hello() {
		return "Hello World";
	}
}
```

spring boot application with graphql-io-server:

```
@SpringBootApplication
@EnableGraphQLIOGsLibraryModule
@EnableGraphQLIOGtsLibraryModule
@EnableGraphQLIOGttLibraryModule
@EnableGraphQLIOWsfLibraryModule
public class SampleHelloWorldApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(SampleHelloWorldApplication.class);
		
		Properties properties = new Properties();
		properties.put("server.port", "8080");
		properties.put("graphqlio.server.schemaLocationPattern", "**/*.helloworld.graphql");
		properties.put("graphqlio.server.endpoint", "/api/data/graph");
		properties.put("graphqlio.toolssubscribe.useEmbeddedRedis", "true");
		properties.put("spring.redis.host", "localhost");
		properties.put("spring.redis.port", "26379");
		
		application.setDefaultProperties(properties);
		application.run(args);
	}
	
	private GsServer graphqlioServer;
	
	SampleHelloWorldApplication(GsServer graphqlioServer) {
		this.graphqlioServer = graphqlioServer;
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

client requesting "hello":

```
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

handler receiving response:

```
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

```
FIXME
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
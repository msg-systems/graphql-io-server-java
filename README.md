# GraphQL-IO-Server-Java
GraphQL Network Communication Framework (Server) for **Java**

## About
This is a GraphQL network communication framework for Java based on the JavaScript 
[Graphql-IO-Server](https://github.com/rse/graphql-io-server) reference implementation. For more detailed information
, we recommend you visit the website of [GraphQL-IO Java](http://java.graphql-io.com/). If you're looking for a
 suitable client implementation, you can use the [GraphQL-IO-Client](https://github.com/rse/graphql-io-client
 ) written in JavaScript.

**Important note:** This is a newly founded project that is in active development, so we strongly recommend to use this
 software for testing purposes only until it is declared _stable_. 

## Dependencies
GraphQL-IO Server Java is a GraphQL that primarily depends on the following projects:

- [WebSocket-Framed Java](https://github.com/msg-systems/websocket-framed-java)
- [GraphQL-Tools-Types Java](https://github.com/msg-systems/graphql-tools-types-java)
- [GraphQL-Tools-Subscribe Java](https://github.com/msg-systems/graphql-tools-subscribe-java)

## Installation
Our releases are hosted on on [Maven central](). To integrate GraphQL-IO Server Java into your application just add the
 following dependency to your project and set the appropriate version (checkout available versions on Maven
  central):

### Maven

```
<dependency>
	<groupId>com.graphqlio</groupId>
	<artifactId>graphql-io-server-java</artifactId>
	<version>${version}</version>
</dependency>
```

### Gradle 
```
dependencies {
  compile 'com.graphqlio:graphql-io-server-java:${version}'
}
```

## Usage 
To get a better understanding on how to use this Framework we defined a minimal example.

### Minimal example: Counter 
In this example we show how a client can receive real-time updates on a server-side counter object via a GraphQL-IO
 query, without consuming a separate Subscription, as we know from common GraphQL.

Following steps are necessary to archive this:
1. Setup a GraphQL scheme containing a `Counter` object having a field _value_ we're interested in and a corresponding
 field on the `Query` object to resolve an instance of this type: 

    ```
    schema {
        query: Query
        mutation: Mutation
    }
    type Query {
        counter: Counter!
    }
    type Mutation {
        increase: Counter!
    }
    type Counter {
        value: Int!
    }
    ```

2. Implement a `GraphQLQueryResolver` containing a suitable resolver method for the field _counter_ defined in the
 `Query` object:
     ```java
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
    ```
3. Define a GraphQL-IO query, subscribing to changes for the field _value_ using the GraphQL-IO extension `_Subscription
 { subscribe }`. To better understand the magic that's happening here, take a look at our [documentation]().

    ```
    query { _Subscription { subscribe } counter { value }}
    ```

4. Implement a WebSocket client sending the query in text format defined by WebSocket-Framed or just use the
 JavaScript [GraphQL-IO-Client](https://github.com/rse/graphql-io-client
 ) implementation. In this example we used a Websocket client provided by Spring Boot. Of course you can also use any
  other Websocket client that you like the best.

    ```java
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

5. Create an application class instantiating the GraphQL-IO Server and setting a few properties, e.g. to define the
 location of the schema file.  In this example we created a small Spring Boot application:

    ```java
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

For more information, e.g. on how to modify the counter (via Mutations) take a look at our 
[example code](https://github.com/msg-systems/graphql-io-server-java/samples/counter).

## Roadmap
_tbd_ 

## Documentation
The documentation can be found [here](http://java.graphql-io.com/).

## Contributing
We welcome anyone to contribute to this project. Detailed information about contribution can be found 
[here](CONTRIBUTING.md).

## Code of conduct
Participation to this project is governed by the [Code of Conduct](code-of-conduct.md).

## Additional Resources

- [GraphQL-IO Java Website](http://java.graphql-io.com/)
- [GraphQL-IO JS Website](http://graphql-io.com/)

## License 
This project is Open Source software released under the [MIT License](LICENSE).

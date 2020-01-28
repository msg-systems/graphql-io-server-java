# Review Open Issues 
1. Short Github Project Description (oriented Ralfs description A Link to or copy pased?)
- Sample Hello World raus 

```
-> done! (tku)
```

- Sample Counter - Counter raus, Beiden Resolver Repository raus (ggf. repo -> ...), Client Sample etwas anpassen 

```
-> done! (tku)
```

2. Should be a pure Java Library - Web Socket Implementation based Jetty - Michael 
3. Autoconfiguration should be moved to the starter project - Edgar 

4. GSWebSocketHandler should be revised to much handler functions and cloud be improved  - Torsten (Pair Reviewing am Dienstag)

   - @Override **handleMessage**(WebSocketSession session, WebSocketMessage<?> message)
     - hätte nicht unbedingt definiert werden müssen
     - handelt aber session.getAcceptedProtocol()
     - ruft deswegen auf:
       - (a) @Override **handleTextMessage**(session, (TextMessage) message);
       - (b) **handleCborMessage**(session, (BinaryMessage) message);
       - (c) **handleMsgPackMessage**(session, (BinaryMessage) message);
   - alle 3 (a,b,c) rufen letztlich auf:
     - **handleStringMessage**(session, input);
       - diese Methode macht die komplette Execute-Bearbeitung
       - (d) und sendet den Reponse zum Client: **sendAnswerBackToClient**(session, answerFrame);
       - (e) und außerdem Notify, wenn nötig: **sendNotifierMessageToClients**(sids4cid, requestMessage);
   - (d) **sendAnswerBackToClient** unterscheidet nach session.getAcceptedProtocol()
   - (e) **sendNotifierMessageToClients** ruft auch (d) auf: **sendAnswerBackToClient**
   - zusätzlich gibt noch einige Hilfsfunktionen:
     - private String **createData**(Set<String> set)
       - ruft auf: private String **surroundWithQuotes**(String value)
     - private String **getSubscriptionScopeId**( String requestMessage )
       - ruft auf: private UUID **isValidUUID**(String uuidString)
     - public **static** String getFromCbor(BinaryMessage message)
     - public **static** String getFromMsgPack(BinaryMessage message)
     - public **static** BinaryMessage createFromStringCbor(String message)
     - public **static** BinaryMessage createFromStringMsgPack(String message)

5. GsGraphQLStandardSchemaCreator vs. GsGraphQLSchemaCreator what is the difference, what do we need? - initScalarTypes Was ist das - Torsten 

   - interface **GsGraphQLSchemaCreator**
     - create()
     - getGraphQLSchema()
   - abstract class **GsGraphQLAbstractSchemaCreator** implements **GsGraphQLSchemaCreator**
     - impementiert: getGraphQLSchema()
     - initScalarTypes()
   - class **GsGraphQLSimpleSchemaCreator** extends **GsGraphQLAbstractSchemaCreator**
     - ruft initScalarTypes() auf
     - ***von uns genutzt*** in GsAutoConfiguration
   - class **_GsGraphQLStandardSchemaCreator_** extends **GsGraphQLAbstractSchemaCreator**
     - ***unbenutzt***
   - new GttUuidType() in **initScalarTypes** kann ersetzt werden durch:
     - @Autowired
     - GttUuidType gttUuidType;

6. GsGraphQLEngine Why do we have no interface there but for GsGraphQLExecution - Torsten 

```
-> done! (tku)
```

7. GsGraphQLStandardSchemaCreator Why explizit @Scope(singelton) bitte raus? - Torsten 

```
-> done! (tku)
```

8. In many classes the Calss Documentation is missing - Torsten 

```
-> done! (tku)
```

9. Motivation of GraphQLIOLibraryGsConfiguration & GraphQLIOLibraryGsConfiguration - Edgar (Autoconfiguration)
10. Production Configuration Properties are missing - Edgar 
11. Some Configuration Properties are unclear. Why do we need graphql.servlet.mapping for example - Edgar (Autoconfiguration)
12. GraphQL Schema places an file naming should be changed - Michael 

13. Naming Method Convention whenThenElse for all Tests - Torsten 

```
-> done! (tku)
```

14. Property Generator for our Properties is missing - Edgar (Autoconfiguration)

15. Samples werden nicht im Eclipse angezeigt, da die Sample Folder nicht automatisch in den Classpath ... - Torsten Nachdenken 

```
-> look in wsf: pom.xml
```

16. Maven Ich will Samples nicht ausliefern ... ? - Torsten Nachdenken 

```
-> look in wsf: pom.xml
```

17. Maven Ich will Test nicht ausliefern ... ? - Torsten Nachdenken 

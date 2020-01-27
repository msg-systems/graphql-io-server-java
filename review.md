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

14. Property Generator for our Properties is missing - Edgar (Autoconfiguration)

15. Samples werden nicht im Eclipse angezeigt, da die Sample Folder nicht automatisch in den Classpath ... - Torsten Nachdenken 
16. Maven Ich will Samples nicht ausliefern ... ? - Torsten Nachdenken 
17. Maven Ich will Test nicht ausliefern ... ? - Torsten Nachdenken 

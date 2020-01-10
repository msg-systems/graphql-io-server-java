# Review Changes 

1. Change the Name in ```graphql-io-server-java``` 
2. Change the POM File Parent to BOM 
3. Maven POM Rename the Project GroupID ArtifactID
3. Maven POM Delete some unimportant dependencies (7 Dependencies) :-)
4. Maven POM Add Spring Repository URLs 
5. Maven POM Delete GraphQL Toools & Kotlin replace with GraphQL Tools 5.6 / 2.2 



# Review Open Issues 
1. Short Github Project Description (oriented Ralfs description A Link to or copy pased?)
2. Change Package Structure to Base Package graphqlio.server - Delete gs 
```
-> done! (tku)
```
3. Should be a pure Java Library - Web Socket Implementation based Jetty 
4. Autoconfiguration should be moved to the starter project 
5. GSWebSocketHandler should be revised to much handler functions and cloud be improved (Pair Reviewing)
6. GsGraphQLStandardSchemaCreator vs. GsGraphQLStandardSchemaCreator what is the difference, what do we need?
7. GsGraphQLEngine Why do we have no interface there but for GsGraphQLStandardSchemaCreator 
8. GsGraphQLStandardSchemaCreator Why explizit singelton?
9. In many classes the Calss Documentation is missing 
10. GsExecutionStrategy Why ist htis class in a seperate Package or the implementation not there? 
11. Motivation of GraphQLIOLibraryGsConfiguration & GraphQLIOLibraryGsConfiguration
12. Right samples are missing
13. Production Configuration Properties are missing 
14. Some Configuration Properties are unclear. Why do we need graphql.servlet.mapping for example 
15. GraphQL Schema places an file naming should be changed 
16. Naming Method Convention whenThenElse for all Tests 
17. GraphQlIoSubscriptionTests Problem with Do you want the application "redis-server-2.8.19.app" to accept incoming network connections?
18. GraphQlIoSubscriptionTestsHandler I don't understand this class. Why do we implement a new Handler here. 
19. The same here GraphQlIoSubprotocolsTestsHandler
20. Property Generator for our Properties is missing 

# Discussion 
1. Should the Actuator Implementation a seperate Project 
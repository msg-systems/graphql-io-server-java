package com.graphqlio.server;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.graphqlio.gts.evaluation.GtsEvaluation;
import com.graphqlio.gts.keyvaluestore.GtsKeyValueStore;
import com.graphqlio.server.converter.WsfNotifyConverter;
import com.graphqlio.server.converter.WsfRequestConverter;
import com.graphqlio.server.converter.WsfResponseConverter;
import com.graphqlio.server.execution.GsExecutionStrategy;
import com.graphqlio.server.graphql.GsGraphQLExecution;
import com.graphqlio.server.graphql.GsGraphQLService;
import com.graphqlio.server.graphql.schema.GsGraphQLSchemaCreator;
import com.graphqlio.server.graphql.schema.GsGraphQLSimpleSchemaCreator;
import com.graphqlio.server.handler.GsWebSocketHandler;
import com.graphqlio.server.server.GsServer;


/**
 * Class to automatically configure the beans for the GraphQL IO Server library based on conditions 
 * and to configure processing WebSocket requests
 *
 * @author Michael Schäfer
 * @author Dr. Edgar Müller
 */


@SpringBootApplication
@Configuration
@ComponentScan(basePackageClasses = ApplicationTest.class, excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.graphqlio.*.samples.*"))
public class ApplicationTest implements WebSocketConfigurer {
	
	
	@Autowired
	private GsWebSocketHandler handler;
	
	@Bean
	@ConditionalOnMissingBean
	public GsGraphQLSchemaCreator gsGraphQLSchemaCreator() {
		return new GsGraphQLSimpleSchemaCreator("**/*.server.graphql");
	}
	
	@Bean
	@ConditionalOnMissingBean
	public GsGraphQLService gsGraphQLService(GsGraphQLSchemaCreator gsSchemaCreator) {
		return new GsGraphQLService(gsSchemaCreator);
	}
	
	
	@Bean
	public ObjectMapper createObjectMapper() {
		ObjectMapper mapper = new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS).registerModule(new Jdk8Module());
		
		InjectableValues.Std injectableValues = new InjectableValues.Std();
		injectableValues.addValue(ObjectMapper.class, mapper);
		mapper.setInjectableValues(injectableValues);
		
		return mapper;
	}
	
	@Bean
	@ConditionalOnMissingBean
	public GsExecutionStrategy gsGraphQLExecution(ObjectMapper mapper) {
		return new GsGraphQLExecution(mapper);
	}
	
	
	@Bean
	public WsfRequestConverter requestConverter() {
		return new WsfRequestConverter();
	}
	
	@Bean
	public WsfResponseConverter responseConverter() {
		return new WsfResponseConverter();
	}
	
	@Bean
	public WsfNotifyConverter notifyerConverter() {
		return new WsfNotifyConverter();
	}
	
	@Bean 
	public GtsKeyValueStore gtsKeyValueStore() {
		return new GtsKeyValueStore();
	}
	
	@Bean GtsEvaluation gtsEvaluation(GtsKeyValueStore gtsKeyValueStore) {
		return new GtsEvaluation(gtsKeyValueStore);
	}
	
	@Bean
	@ConditionalOnMissingBean
	public GsWebSocketHandler gsWebSocketHandler( GsExecutionStrategy gsExecutionStategy
											, GtsEvaluation gtsEvaluation
											, GsGraphQLSchemaCreator gsSchemaCreator
											, WsfRequestConverter requestConverter
											, WsfResponseConverter responseConverter
											, WsfNotifyConverter notifyConverter
											) {
		return new GsWebSocketHandler	( gsExecutionStategy
										, gtsEvaluation
										, gsSchemaCreator
										, requestConverter
										, responseConverter
										, notifyConverter
				);
	
	}
	
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {	 
		registry.addHandler(this.handler, "/api/data/graph");   
	}
	
	@Bean
	@ConditionalOnMissingBean
	public GsServer gsServer (GsGraphQLService gsGraphQLService) {
		return new GsServer(gsGraphQLService);
	}
}



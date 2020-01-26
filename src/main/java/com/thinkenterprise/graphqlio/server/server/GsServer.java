/*******************************************************************************
 * *
 * **  Design and Development by msg Applied Technology Research
 * **  Copyright (c) 2019-2020 msg systems ag (http://www.msg-systems.com/)
 * **  All Rights Reserved.
 * ** 
 * **  Permission is hereby granted, free of charge, to any person obtaining
 * **  a copy of this software and associated documentation files (the
 * **  "Software"), to deal in the Software without restriction, including
 * **  without limitation the rights to use, copy, modify, merge, publish,
 * **  distribute, sublicense, and/or sell copies of the Software, and to
 * **  permit persons to whom the Software is furnished to do so, subject to
 * **  the following conditions:
 * **
 * **  The above copyright notice and this permission notice shall be included
 * **  in all copies or substantial portions of the Software.
 * **
 * **  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * **  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * **  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * **  IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * **  CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * **  TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * **  SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * *
 ******************************************************************************/
package com.thinkenterprise.graphqlio.server.server;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.thinkenterprise.graphqlio.server.execution.GsExecutionStrategy;
import com.thinkenterprise.graphqlio.server.graphql.GsGraphQLEngine;
import com.thinkenterprise.graphqlio.server.graphql.GsGraphQLExecution;
import com.thinkenterprise.graphqlio.server.graphql.GsGraphQLService;
import com.thinkenterprise.graphqlio.server.handler.GsWebSocketHandler;
import com.thinkenterprise.gts.actuator.GtsCounter;
import com.thinkenterprise.gts.evaluation.GtsEvaluation;
import com.thinkenterprise.gts.keyvaluestore.GtsKeyValueStore;
import com.thinkenterprise.gts.resolver.GtsResolverRegistry;


/**
 * Main Server (Service) responsible to start GraphQL service
 * Application needs inject service and run start command 
 *
 * @author Michael Schäfer
 * @author Dr. Edgar Müller
 */


public class GsServer implements WebSocketConfigurer {

	private GsGraphQLService gsGraphQLService = null;
	
	private String webSocketEndPoint;
	
	private GsWebSocketHandler gsWebSocketHandler = null;
	
	private GsExecutionStrategy gsExecutionStrategy = null;
	
	private GtsEvaluation gtsEvaluation = null;


	
	
	//// ToDo: replace by pure java implementation
	@Autowired
	private GtsKeyValueStore gtsKeyValueStore;
		
	@Autowired
	private GtsCounter gsGtsCounter;
	
//// define beans	
	
	@Bean
	@ConditionalOnMissingBean
	public GsWebSocketHandler webSocketHandler	() {
		
		if ( gsWebSocketHandler == null) {
			gsWebSocketHandler = new GsWebSocketHandler	(); 
		}
		return gsWebSocketHandler;		
	}
		
	
	
	/// constructor
	public GsServer(String schemaLocationPattern, String webSocketEndPoint) {
		this.webSocketEndPoint = webSocketEndPoint;
		this.gsGraphQLService = new GsGraphQLService(schemaLocationPattern);
		this.gsExecutionStrategy = new GsGraphQLExecution(createObjectMapper());
		this.gtsEvaluation = new GtsEvaluation();
	}

	
	@Override
    public  void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {	 
    	registry.addHandler(webSocketHandler(), webSocketEndPoint);   
	}
	
	public void registerGraphQLResolver( GraphQLResolver resolver) {
		GtsResolverRegistry.registerGraphQLResolver(resolver);
	}
	
	
	public boolean start() throws IOException {
		/// keys associated to a client connection are deleted if connection closes
		/// however there may be keys left from last session if application terminated unexpectedly
		
		this.gtsEvaluation.setGtsKeyValueStore(gtsKeyValueStore);
		this.gtsKeyValueStore.start();
		
		if (gsGraphQLService.start() ) {
			webSocketHandler().setGtsKeyValueStore(gtsKeyValueStore);
			webSocketHandler().setGtsCounter(gsGtsCounter);
			webSocketHandler().setSchemaCreator(gsGraphQLService.getSchemaCreator());
			webSocketHandler().setExecutionStrategy(gsExecutionStrategy);
			webSocketHandler().setGtsEvaluation(gtsEvaluation);
			return true;			
		}
		else
			return false;
	}
	
	public void stop() {
		gtsKeyValueStore.stop();
		gsGraphQLService.stop();
	}
	
	
	private ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS).registerModule(new Jdk8Module());
        
        InjectableValues.Std injectableValues = new InjectableValues.Std();
        injectableValues.addValue(ObjectMapper.class, mapper);
        mapper.setInjectableValues(injectableValues);

        return mapper;
    }
			
	
}

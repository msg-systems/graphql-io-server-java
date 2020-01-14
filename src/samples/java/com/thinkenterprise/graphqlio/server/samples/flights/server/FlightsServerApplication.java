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
package com.thinkenterprise.graphqlio.server.samples.flights.server;

import java.util.Properties;

import javax.annotation.PreDestroy;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.thinkenterprise.graphqlio.server.EnableGraphQLIOGsLibraryModule;
import com.thinkenterprise.graphqlio.server.server.GsServer;
import com.thinkenterprise.gts.EnableGraphQLIOGtsLibraryModule;
import com.thinkenterprise.gtt.EnableGraphQLIOGttLibraryModule;
import com.thinkenterprise.wsf.EnableGraphQLIOWsfLibraryModule;

/**
 * Server application for the flights sample.
 * This spring boot application set the properties and starts it.
 * The graphql-io-server is started by the ApplicationRunner implementation.
 * On end of spring boot application the server should be stopped.
 * 
 * @author Michael Schäfer
 * @author Torsten Kühnert
 */

@SpringBootApplication
@EnableGraphQLIOWsfLibraryModule
@EnableGraphQLIOGttLibraryModule
@EnableGraphQLIOGtsLibraryModule
@EnableGraphQLIOGsLibraryModule
public class FlightsServerApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(FlightsServerApplication.class);

		Properties properties = new Properties();
		properties.put("server.port", "8080");
		properties.put("graphqlio.server.schemaLocationPattern", "**/*.flights.graphql");
		properties.put("graphqlio.server.endpoint", "/api/data/graph");
		properties.put("graphqlio.toolssubscribe.useEmbeddedRedis", "true");
		properties.put("spring.redis.host", "localhost");
		properties.put("spring.redis.port", "26379");

		application.setDefaultProperties(properties);
		application.run(args);
	}

	private GsServer graphqlioServer;

	FlightsServerApplication(GsServer graphqlioServer) {
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

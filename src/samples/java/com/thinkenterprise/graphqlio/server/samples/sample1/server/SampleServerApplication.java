package com.thinkenterprise.graphqlio.server.samples.sample1.server;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.thinkenterprise.graphqlio.server.EnableGraphQLIOGsLibraryModule;
import com.thinkenterprise.graphqlio.server.server.GsServer;
import com.thinkenterprise.gts.EnableGraphQLIOGtsLibraryModule;
import com.thinkenterprise.gtt.EnableGraphQLIOGttLibraryModule;
import com.thinkenterprise.wsf.EnableGraphQLIOWsfLibraryModule;

@SpringBootApplication
@EnableGraphQLIOWsfLibraryModule
@EnableGraphQLIOGttLibraryModule
@EnableGraphQLIOGtsLibraryModule
@EnableGraphQLIOGsLibraryModule
public class SampleServerApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(SampleServerApplication.class);

		Properties properties = new Properties();
		properties.put("server.port", "8080");
		properties.put("graphqlio.server.schemaLocationPattern", "**/*.sample1.graphql");
		properties.put("graphqlio.server.endpoint", "/api/data/graph");
		properties.put("graphqlio.toolssubscribe.useEmbeddedRedis", "true");
		properties.put("spring.redis.host", "localhost");
		properties.put("spring.redis.port", "26379");

		application.setDefaultProperties(properties);
		application.run(args);
	}

	@Autowired
	private GsServer graphqlioServer;

	@PostConstruct
	public void construct() throws IOException {
		System.out.println("construct sample");
		this.graphqlioServer.start();
	}

	@PreDestroy
	public void destroy() throws Exception {
		System.out.println("destroy sample");
		this.graphqlioServer.stop();
	}

}

package com.thinkenterprise.graphqlio.server.samples.helloworld.server;

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
@EnableGraphQLIOGsLibraryModule
@EnableGraphQLIOGtsLibraryModule
@EnableGraphQLIOGttLibraryModule
@EnableGraphQLIOWsfLibraryModule
public class SampleHelloWorldApplication {

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

	@Autowired
	private GsServer graphqlioServer;

	@PostConstruct
	public void init() throws IOException {
		System.out.println("init helloworld");
		this.graphqlioServer.start();
	}

	@PreDestroy
	public void destroy() throws Exception {
		System.out.println("destroy helloworld");
		this.graphqlioServer.stop();
	}

}

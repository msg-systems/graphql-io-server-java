package com.thinkenterprise.graphqlio.server.samples.helloworld.server;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.thinkenterprise.graphqlio.server.EnableGraphQLIOGsLibraryModule;
import com.thinkenterprise.graphqlio.server.server.GsServer;
import com.thinkenterprise.gts.EnableGraphQLIOGtsLibraryModule;
import com.thinkenterprise.gtt.EnableGraphQLIOGttLibraryModule;
import com.thinkenterprise.wsf.EnableGraphQLIOWsfLibraryModule;

@Profile("helloworld")
@SpringBootApplication
@Configuration
@EnableGraphQLIOGsLibraryModule
@EnableGraphQLIOGtsLibraryModule
@EnableGraphQLIOGttLibraryModule
@EnableGraphQLIOWsfLibraryModule

public class SampleHelloWorldApplication implements ApplicationRunner, DisposableBean {

	private GsServer graphqlioServer;

	SampleHelloWorldApplication(GsServer graphqlioServer) {
		this.graphqlioServer = graphqlioServer;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		this.graphqlioServer.start();

	}

	@Override
	public void destroy() throws Exception {
		this.graphqlioServer.stop();
	}

	public static void main(String[] args) {
		SpringApplication.run(SampleHelloWorldApplication.class, args);
	}

}

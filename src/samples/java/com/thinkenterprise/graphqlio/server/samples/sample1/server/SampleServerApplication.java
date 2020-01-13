package com.thinkenterprise.graphqlio.server.samples.sample1.server;

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

@Profile("sample1")
@SpringBootApplication
@Configuration
@EnableGraphQLIOWsfLibraryModule
@EnableGraphQLIOGttLibraryModule
@EnableGraphQLIOGtsLibraryModule
@EnableGraphQLIOGsLibraryModule
public class SampleServerApplication implements ApplicationRunner, DisposableBean {

	private GsServer graphqlioServer;

	SampleServerApplication(GsServer graphqlioServer) {
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
		SpringApplication.run(SampleServerApplication.class, args);
	}

}

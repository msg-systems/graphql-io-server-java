package com.thinkenterprise.graphqlio.server.samples.java.helloworld.service;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import com.thinkenterprise.graphqlio.server.server.GsServer;

@Service
public class SampleHelloWorldService implements ApplicationRunner {
	
	
	private GsServer graphqlioServer;
	
	SampleHelloWorldService(GsServer graphqlioServer){
		this.graphqlioServer = graphqlioServer;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		this.graphqlioServer.start();
		
	}
}

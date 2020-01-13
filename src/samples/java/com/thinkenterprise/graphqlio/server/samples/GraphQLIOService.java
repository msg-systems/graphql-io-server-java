package com.thinkenterprise.graphqlio.server.samples;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.thinkenterprise.graphqlio.server.server.GsServer;

@Profile("query")
@Service
public class GraphQLIOService implements ApplicationRunner, DisposableBean {

	private GsServer graphqlioServer;

	GraphQLIOService(GsServer graphqlioServer) {
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

}

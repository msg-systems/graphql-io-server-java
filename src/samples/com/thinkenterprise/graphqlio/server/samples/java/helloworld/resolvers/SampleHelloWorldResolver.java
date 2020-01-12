package com.thinkenterprise.graphqlio.server.samples.java.helloworld.resolvers;

import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;

@Component
public class SampleHelloWorldResolver implements GraphQLQueryResolver {
	
	
	public String hello() {
		return "Hello World";
	}
}


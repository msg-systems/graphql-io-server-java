package com.thinkenterprise.graphqlio.server.samples.java.helloworld;

import java.net.URL;
import java.net.URLClassLoader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

import com.thinkenterprise.graphqlio.server.EnableGraphQLIOGsLibraryModule;
import com.thinkenterprise.gts.EnableGraphQLIOGtsLibraryModule;
import com.thinkenterprise.gtt.EnableGraphQLIOGttLibraryModule;
import com.thinkenterprise.wsf.EnableGraphQLIOWsfLibraryModule;

@SpringBootApplication
@Configuration
@EnableGraphQLIOGsLibraryModule
@EnableGraphQLIOGtsLibraryModule
@EnableGraphQLIOGttLibraryModule
@EnableGraphQLIOWsfLibraryModule

public class SampleHelloWorldApplication {

	public static void main(String[] args) {
		SpringApplication.run(SampleHelloWorldApplication.class, args);
	}

}

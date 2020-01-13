package com.thinkenterprise.graphqlio.server.samples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.thinkenterprise.graphqlio.server.EnableGraphQLIOGsLibraryModule;
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
		SpringApplication.run(SampleServerApplication.class, args);
	}

}

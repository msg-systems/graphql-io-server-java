package com.thinkenterprise.graphqlio.server;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

import com.thinkenterprise.gts.EnableGraphQLIOGtsLibraryModule;
import com.thinkenterprise.gtt.EnableGraphQLIOGttLibraryModule;
import com.thinkenterprise.wsf.EnableGraphQLIOWsfLibraryModule;

@SpringBootApplication
@Configuration
@EnableGraphQLIOGtsLibraryModule
@EnableGraphQLIOGttLibraryModule
@EnableGraphQLIOWsfLibraryModule
public class TestApplication {

}

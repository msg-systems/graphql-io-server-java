package com.thinkenterprise.graphqlio.server;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import com.thinkenterprise.gts.EnableGraphQLIOGtsLibraryModule;
import com.thinkenterprise.gtt.EnableGraphQLIOGttLibraryModule;
import com.thinkenterprise.wsf.EnableGraphQLIOWsfLibraryModule;

@SpringBootApplication
@Configuration
@ComponentScan(basePackageClasses = TestApplication.class, excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.thinkenterprise.*.samples.*"))
@EnableGraphQLIOGtsLibraryModule
@EnableGraphQLIOGttLibraryModule
@EnableGraphQLIOWsfLibraryModule
public class TestApplication {

}

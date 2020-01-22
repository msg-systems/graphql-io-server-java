package com.graphqlio.server;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import com.graphqlio.gts.EnableGraphQLIOGtsLibraryModule;
import com.graphqlio.gtt.EnableGraphQLIOGttLibraryModule;
import com.graphqlio.wsf.EnableGraphQLIOWsfLibraryModule;

@SpringBootApplication
@Configuration
@ComponentScan(basePackageClasses = TestApplication.class, excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.graphqlio.*.samples.*"))
@EnableGraphQLIOGtsLibraryModule
@EnableGraphQLIOGttLibraryModule
@EnableGraphQLIOWsfLibraryModule
public class TestApplication {

}

package com.thinkenterprise.graphqlio.server.samples.sample1.server.resolver;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thinkenterprise.graphqlio.server.samples.sample1.server.domain.Route;
import com.thinkenterprise.graphqlio.server.samples.sample1.server.domain.RouteRepository;
import com.thinkenterprise.gts.context.GtsContext;
import com.thinkenterprise.gts.tracking.GtsRecord;
import com.thinkenterprise.gts.tracking.GtsRecord.GtsArityType;
import com.thinkenterprise.gts.tracking.GtsRecord.GtsOperationType;
import com.thinkenterprise.gts.tracking.GtsScope;

import graphql.schema.DataFetchingEnvironment;

@Component
public class QueryResolver implements GraphQLQueryResolver {

	private RouteRepository routeRepository;

	public QueryResolver(RouteRepository routeRepository) {
		this.routeRepository = routeRepository;
	}

	public List<Route> allRoutes(DataFetchingEnvironment env) {

		Iterable<Route> allRoutes = routeRepository.findAll();

		List<Route> allRoutesList = new ArrayList<>();
		allRoutes.forEach(allRoutesList::add);

		List<String> dstIds = new ArrayList<>();
		if (!allRoutesList.isEmpty()) {
			allRoutesList.forEach(route -> dstIds.add(route.getFlightNumber()));
		} else
			dstIds.add("*");

		GtsContext context = env.getContext();
		GtsScope scope = context.getScope();
		scope.addRecord(
				GtsRecord.builder().op(GtsOperationType.READ).arity(GtsArityType.ALL).dstType(Route.class.getName())
						.dstIds(dstIds.toArray(new String[dstIds.size()])).dstAttrs(new String[] { "*" }).build());

		return allRoutesList;
	}
}

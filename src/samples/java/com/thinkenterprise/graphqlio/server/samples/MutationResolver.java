package com.thinkenterprise.graphqlio.server.samples;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.thinkenterprise.gts.context.GtsContext;
import com.thinkenterprise.gts.tracking.GtsRecord;
import com.thinkenterprise.gts.tracking.GtsRecord.GtsArityType;
import com.thinkenterprise.gts.tracking.GtsRecord.GtsOperationType;
import com.thinkenterprise.gts.tracking.GtsScope;

import graphql.schema.DataFetchingEnvironment;

@Component
public class MutationResolver implements GraphQLMutationResolver {

	private RouteRepository routeRepository;

	public MutationResolver(RouteRepository routeRepository) {
		this.routeRepository = routeRepository;
	}

	@Transactional
	public Route updateRoute(String flightNumber, UpdateRouteInput input, DataFetchingEnvironment env) {
		Route route = routeRepository.getByFlightNumber(flightNumber);

		route.setFlightNumber(input.getFlightNumber());
		route.setDeparture(input.getDeparture());
		route.setDestination(input.getDestination());

		Route modifiedRoute = null;
		modifiedRoute = routeRepository.save(route);

		GtsContext context = env.getContext();
		GtsScope scope = context.getScope();
		scope.addRecord(GtsRecord.builder().op(GtsOperationType.UPDATE).arity(GtsArityType.ONE)
				.dstType(Route.class.getName()).dstIds(new String[] { modifiedRoute.getFlightNumber().toString() })
				.dstAttrs(new String[] { "*" }).build());

		return modifiedRoute;
	}

}

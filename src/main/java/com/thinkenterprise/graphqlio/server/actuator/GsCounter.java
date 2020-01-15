package com.thinkenterprise.graphqlio.server.actuator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.thinkenterprise.gts.actuator.GtsCounter;
import com.thinkenterprise.gts.actuator.GtsCounterNames;
import com.thinkenterprise.gts.actuator.GtsCounterNotification;

/**
 * class GsCounter
 *
 * @author Michael Schäfer
 * @author Dr. Edgar Müller
 */

@Component
public class GsCounter implements GtsCounter {
	
	private List<GtsCounterNotification> counterNotificationList = new ArrayList<>();;
	private GtsCounterNames[] counters = 
		{ GtsCounterNames.CONNECTIONS, GtsCounterNames.SCOPES, GtsCounterNames.RECORDS};
	

	@Override
	public GtsCounterNames[] getCounters() {
		return counters;
	}
	
	@Override
	public void registerCounterNotification( GtsCounterNotification counterNotification) {
		counterNotificationList.add(counterNotification);
	}
	
	@Override
	public void modifyCounter(GtsCounterNames name, long byNumber) {
		counterNotificationList.forEach(counterNotification -> counterNotification.onModifiedCounter(name, byNumber));		
	}

}

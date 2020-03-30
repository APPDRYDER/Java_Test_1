/* Copyright (c) AppDynamics, Inc., and its affiliates
 * 2016
 * All Rights Reserved
 * THIS IS UNPUBLISHED PROPRIETARY CODE OF APPDYNAMICS, INC.
 * The copyright notice above does not evidence any actual or intended publication of such source code
 *
 * Maintainer: David Ryder, David.Ryder@AppDynamics.com
 *
 * Example AppDynamics Java SDK interceptor to track a method's execution time and report the value
 * in a field of the Transaction snapshot data
 */

import com.appdynamics.apm.appagent.api.AgentDelegate;
import com.appdynamics.apm.appagent.api.DataScope;
import com.appdynamics.instrumentation.sdk.template.AGenericInterceptor;
import com.appdynamics.instrumentation.sdk.Rule;
import com.appdynamics.instrumentation.sdk.SDKClassMatchType;
import com.appdynamics.instrumentation.sdk.SDKStringMatchType;

import java.util.*;

public class TestApp1Interceptor1 extends AGenericInterceptor {
	private long startTime, endTime;

	public TestApp1Interceptor1() {
		super();
	} // TestApp1Interceptor1

	@Override
	public List<Rule> initializeRules() {
		List<Rule> rules = new ArrayList<Rule>();
		rules.add(new Rule.Builder("TestApp1").classMatchType(SDKClassMatchType.MATCHES_CLASS).methodMatchString("sum")
				.methodStringMatchType(SDKStringMatchType.EQUALS).build());

		return rules;
	} // initializeRules

	public Object onMethodBegin(Object invokedObject, String className, String methodName, Object[] paramValues) {
		try {
			getLogger().info("TestApp1Interceptor onMethodBegin");
			startTime = System.currentTimeMillis();
			Set<DataScope> allScopes = new HashSet<DataScope>();
			allScopes.add(DataScope.ANALYTICS);
			allScopes.add(DataScope.SNAPSHOTS);
			AgentDelegate.getMetricAndEventPublisher().addSnapshotData("TESTAPP1_START_TIME", startTime, allScopes);
		} catch (Exception e) {
			getLogger().error("TestApp1Interceptor onMethodBegin", e);
		}

		return null;
	} // onMethodBegin

	public void onMethodEnd(Object state, Object invokedObject, String className, String methodName,
			Object[] paramValues, Throwable thrownException, Object returnValue) {
		try {
			getLogger().info("TestApp1Interceptor onMethodEnd");
			endTime = System.currentTimeMillis();
			Set<DataScope> allScopes = new HashSet<DataScope>();
			allScopes.add(DataScope.ANALYTICS);
			allScopes.add(DataScope.SNAPSHOTS);

			long durationTime = endTime - startTime;
			AgentDelegate.getMetricAndEventPublisher().addSnapshotData("TESTAPP1_END_TIME", endTime, allScopes);
			AgentDelegate.getMetricAndEventPublisher().addSnapshotData("TESTAPP1_DURATION_TIME", durationTime, allScopes);
		} catch (Exception e) {
			getLogger().error("TestApp1Interceptor onMethodEnd", e);
		}
	} // onMethodEnd
} // class TestApp1Interceptor1

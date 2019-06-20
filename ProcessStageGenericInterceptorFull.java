/*
 * Copyright (c) AppDynamics, Inc., and its affiliates
 * 2016
 * All Rights Reserved
 * THIS IS UNPUBLISHED PROPRIETARY CODE OF APPDYNAMICS, INC.
 * The copyright notice above does not evidence any actual or intended publication of such source code
 */

import com.appdynamics.apm.appagent.api.AgentDelegate;
import com.appdynamics.apm.appagent.api.DataScope;
import com.appdynamics.instrumentation.sdk.template.AGenericInterceptor;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.IReflector;
import com.appdynamics.instrumentation.sdk.toolbox.reflection.ReflectorException;
import com.singularity.ee.agent.appagent.entrypoint.bciengine.AMethodInterceptor;
import com.appdynamics.instrumentation.sdk.Rule;
import com.appdynamics.instrumentation.sdk.SDKClassMatchType;
import com.appdynamics.instrumentation.sdk.SDKStringMatchType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

//public class Test extends AMethodInterceptor{

public class ProcessStageGenericInterceptorFull extends AGenericInterceptor {
	private int counter;
	private HashMap<UUID,TaskDataStore> taskDataStore;
    //processStage
    private IReflector getResourceId = null;
    private IReflector getSubStatus = null;
    private IReflector getTaskUpdate = null;	
	
	private class TaskDataStore {
		private UUID id;
		private String subTask;
		private int retries;
		private int completed;
		private long startTime;
		private long endTime;
		private long durationMs;
		
		public TaskDataStore(UUID id, String subTask) {
			this.id = id;
			this.subTask = subTask;
			this.retries = 0;
			this.startTime = System.currentTimeMillis();
			this.completed = 0;
		}
		
		public void retry() {
			this.retries++;
		}
		
		public void completed() {
			this.endTime = System.currentTimeMillis();
			this.durationMs = endTime - startTime;
			this.completed = 1;
		}
		
		public int getRetries() {
			return this.retries;
		}
		
		public int getCompleted() {
			return this.completed;
		}
		
		public String getSubTask() {
			return this.subTask;
		}
		
		public void setSubTask(String subTask) {
			this.subTask = subTask;
		}
	} // DataStore
	
    public ProcessStageGenericInterceptorFull() {
        super();
        counter = 0;
        taskDataStore = new HashMap<UUID,TaskDataStore>();

        getResourceId = getNewReflectionBuilder()
                .invokeInstanceMethod("getResourceId", true).build();

        getSubStatus = getNewReflectionBuilder()
                .invokeInstanceMethod("getSubStatus", true).build();

        getTaskUpdate = getNewReflectionBuilder()
                .invokeInstanceMethod("getTaskUpdate", true).build();
        //().info("sdk: ProcessStageGenericInterceptor: start");
    }
    
    @Override
    public List<Rule> initializeRules() {
    	List<Rule> rules = new ArrayList<Rule>();
    	rules.add(new Rule.Builder("XXXexecutionService.SddcProvisioningServiceImpl")
				.classMatchType(SDKClassMatchType.MATCHES_CLASS)
				.methodMatchString("processStage")
				.methodStringMatchType(SDKStringMatchType.EQUALS)
				.build());
        return rules;
    } // initializeRules

    @Override
    public Object onMethodBegin(Object invokedObject, String className, String methodName, Object[] paramValues) {
    	TaskDataStore tds = null;
    	counter++;

    	Object task = paramValues[0];
       	java.util.Set<DataScope> dataScope = new HashSet<DataScope>();
    	dataScope.add(DataScope.SNAPSHOTS);
        dataScope.add(DataScope.ANALYTICS);
        
        getLogger().info("sdk: onMethodBegin ");
        
    	try {
    		UUID resourceId = (UUID)getResourceId.execute(invokedObject.getClass().getClassLoader(), task);
            String subStatus = getSubStatus.execute(invokedObject.getClass().getClassLoader(), task).toString();
			
			tds = taskDataStore.get(resourceId);
			if (tds == null) {
				tds = new TaskDataStore(resourceId, subStatus);
				taskDataStore.put(resourceId, tds);
			} else {
				tds = taskDataStore.get(resourceId);
				tds.setSubTask(subStatus);
				tds.retry();
			}
	    	AgentDelegate.getMetricAndEventPublisher().addSnapshotData("B_RETRIES", Integer.toString(tds.getRetries()), dataScope);
	    	AgentDelegate.getMetricAndEventPublisher().addSnapshotData("B_RID", resourceId.toString(), dataScope);
	    	AgentDelegate.getMetricAndEventPublisher().addSnapshotData("B_COMPLETED", tds.getCompleted(), dataScope);
	    	AgentDelegate.getMetricAndEventPublisher().addSnapshotData("B_SUBSTATUS", subStatus, dataScope);
	    	AgentDelegate.getMetricAndEventPublisher().addSnapshotData("B_HMSIZE", taskDataStore.size(), dataScope);
		} catch (ReflectorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			getLogger().error("sdk: onMethodBegin", e);
		}

    	getLogger().info("sdk: onMethodBegin ]" );
    	
    
    	//counter++;
    	//String ts = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
    	//getLogger().info("TEST2: start" + ruleClass + "." + ruleMethod + " " + ts);
        //Map<String, String> keyVsValue = new HashMap<String, String>();
        //keyVsValue.put("vmc-ts", ts);
        //keyVsValue.put("vmc-counter", Integer.toString( counter ));
        //AgentDelegate.getMetricAndEventPublisher().publishEvent("aw snap", "INFO", "AGENT_STATUS", keyVsValue);
    	//AgentDelegate.getMetricAndEventPublisher().addSnapshotData("X_RETRIES", counter, dataScope);
    	//AgentDelegate.getMetricAndEventPublisher().addSnapshotData("X_COMPLETED", counter, dataScope);
    	//AgentDelegate.getMetricAndEventPublisher().publishInfoEvent("sdk: DataStore " + Integer.toString( counter ), null );
    	 	
    
        return null;
    }
	
    @Override
	public void onMethodEnd(Object state, Object invokedObject, String className, String methodName,
            Object[] paramValues, Throwable thrownException, Object returnValue) {
    	
       	java.util.Set<DataScope> dataScope  = new HashSet<DataScope>();
    	dataScope.add(DataScope.SNAPSHOTS);
        dataScope.add(DataScope.ANALYTICS);
        String subStatus = "subStatusX";
        UUID resourceId = null;
        TaskDataStore tds = null;
        String status = "NA";
        
    	getLogger().info("sdk: onMethodEnd " + returnValue.toString() );
		try {
			Object taskUpdate = getTaskUpdate.execute(invokedObject.getClass().getClassLoader(), returnValue);
			resourceId = (UUID)getResourceId.execute(invokedObject.getClass().getClassLoader(), taskUpdate);
	        subStatus = (String)getSubStatus.execute(invokedObject.getClass().getClassLoader(), taskUpdate).toString();
	        tds = taskDataStore.get(resourceId);
	        if (tds == null) {
	        	status = "OUT_SYNC";
			} else {
				getLogger().info("sdk: onMethodEnd " + tds.getSubTask() + " " + subStatus);
				if (tds.getSubTask().equals(subStatus)) {
					status = "CONTINUING";
				} else {
					status = "COMPLETED";
					tds.completed();
					// remove from hashmap
				}
			}
		} catch (ReflectorException e) {
			// TODO Auto-generated catch block
			getLogger().info("sdk: onMethodEnd EXP");
			getLogger().error("sdk: onMethodEnd", e);
		}

    	AgentDelegate.getMetricAndEventPublisher().addSnapshotData("E_RID", resourceId, dataScope);
    	AgentDelegate.getMetricAndEventPublisher().addSnapshotData("E_SUBSTATUS", subStatus, dataScope);
		AgentDelegate.getMetricAndEventPublisher().addSnapshotData("E_STATUS", status, dataScope);
		getLogger().info("sdk: onMethodEnd ]" );
    }
}

import java.util.Random;

import java.util.HashSet;
import java.util.Set;

//AppDynamics Instrumentation
import com.appdynamics.apm.appagent.api.AgentDelegate;
import com.appdynamics.apm.appagent.api.ITransactionDemarcator;
import com.appdynamics.apm.appagent.api.IMetricAndEventReporter;
import com.appdynamics.apm.appagent.api.DataScope;

public class TestApp1
{
	private int total;
	private Random r1;

	public TestApp1() {
		r1 = new Random();
	}

	public void sum(int value) {
		total += value;
	}

	public int getTotal() {
		return total;
	}

	public int getRandom() {
		return r1.nextInt(100);
	}

	public void waitMs(int waitMs) {
		try {
			Thread.sleep(waitMs);
		} catch (Exception e ) {
			System.out.println( e );
		}
	}

	public void worker1(int value, int waitMs, String nodeName) {
		String txUUID = ""; // AppDynamics uniqueIdentifierForTransaction
		ITransactionDemarcator tx = AgentDelegate.getTransactionDemarcator();
		IMetricAndEventReporter reporter = AgentDelegate.getMetricAndEventPublisher();
		Set<DataScope> allScopes = new HashSet<DataScope>();
		allScopes.add(DataScope.ANALYTICS);
		allScopes.add(DataScope.SNAPSHOTS);

		// Current BT: null if not in a BT
		txUUID = tx.getUniqueIdentifierForTransaction();
		System.out.println(String.format("BT: %s", txUUID));

		long startTime = System.currentTimeMillis();
		sum(value);
		int total = getTotal();
		int random = getRandom();
		//System.out.println( "Total " + total + " Random " + random);
		waitMs(waitMs);
		long durationTime = System.currentTimeMillis();

		// Add duration to snapshot data
		reporter.addSnapshotData("TEST_DURATION_1", durationTime, allScopes);

		// Add custom getMetricAndEventPublisher
		AgentDelegate.getMetricAndEventPublisher().reportMetric(String.format("Custom Metrics|JAVA_TEST1|WORKER_TASK|%s|Duration", nodeName),
		 																												durationTime, "OBSERVATION", "CURRENT", "INDIVIDUAL");
	}

	public void test1(int count, int waitMs, String nodeName) {
    for (int i =0; i < count; i++) {
			worker1(i, waitMs, nodeName);
    }
	}

	public static void main(String[] args) {
		int iterations, waitMs;
		String nodeName;
		if (args.length >= 1) {
			iterations =  Integer.parseInt(args[0]);
		} else {
			iterations = 10;
		}

		if (args.length >= 2) {
			waitMs =  Integer.parseInt(args[1]);
		} else {
			waitMs = 5000;
		}

		if (args.length >= 3) {
			nodeName =  args[2];
		} else {
			nodeName = "NODE-1";
		}
		System.out.println("Starting " + iterations + " " + waitMs);

		TestApp1 ta1 = new TestApp1();

		ta1.test1( iterations, waitMs, nodeName );
		System.out.println( "Total " + ta1.getTotal());

		System.out.println("Complete");
	}
}

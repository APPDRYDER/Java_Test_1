import java.util.Random;

import java.util.HashSet;
import java.util.Set;

public class TestApp1
{
	private int total;
	private Random r1;

	public TestApp1() {
		r1 = new Random();
	}

	public void sum(int value) {
		total += value;
		try {
			Thread.sleep(1000);
		} catch (Exception e ) {
			System.out.println( e );
		}
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
		long startTime = System.currentTimeMillis();
		sum(value);
		int total = getTotal();
		int random = getRandom();
		//System.out.println( "Total " + total + " Random " + random);
		waitMs(waitMs);
		long durationTime = System.currentTimeMillis();
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
			iterations = 123456;
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

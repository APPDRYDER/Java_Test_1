import java.util.Random;

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

	public void worker1(int value, int waitMs) {
		sum(value);
		int total = getTotal();
		int random = getRandom();
		//System.out.println( "Total " + total + " Random " + random);
		waitMs(waitMs);
	}

	public void test1(int count, int waitMs) {
    for (int i =0; i < count; i++) {
			worker1(i, waitMs);
    }
	}

	public static void main(String[] args) {
		int iterations, waitMs;
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
		System.out.println("Starting " + iterations + " " + waitMs);

		TestApp1 ta1 = new TestApp1();

		ta1.test1( iterations, waitMs );
		System.out.println( "Total " + ta1.getTotal());

		System.out.println("Complete");
	}
}

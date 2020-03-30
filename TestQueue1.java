import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Random;
import java.util.UUID;

public class TestQueue1
{
	private Random r1 = new Random();

	public class Task {
		private long effort;
		private long waitMs;
		private double errorRate;
		private long total;
		private UUID uuid;

		public Task() {
				this(1000, 0.20);
		}

		public Task(long waitMs, double errorRate) {
			this.effort = 1 + r1.nextInt(10);
			this.waitMs = waitMs;
			this.errorRate = errorRate;
			this.total = 0;
			this.uuid = UUID.randomUUID();
		} // Task

		public String toString() {
			return String.format("Task(%.8s, %d, %d)", this.uuid, this.effort, this.total);
		}

		public Boolean incomplete() {
			return this.effort > 0;
		}

		public void waitMs() {
			try {
				//Thread.sleep(this.waitMs);
				if (Math.random() < errorRate) {
						Thread.sleep(this.waitMs * 3);
				}
			} catch (InterruptedException e ) {
				System.out.println( e );
			}
		} // waitMs

		public void process() {
			if (incomplete()) {
				this.effort--;
				this.total += r1.nextInt(100);
				waitMs();
			}
		} // process

		public void work(String nodeName) {
			long startTime = System.currentTimeMillis();
			long durationTime = System.currentTimeMillis() - startTime;
		} // work
	} // Task

	public class Producer implements Runnable {
		private String name;
		private Queue<Task> q;
		private int queueSizeMax;
		private int queueFullMax, queueFullCount;

		public Producer(String name, Queue<Task> q, int queueSizeMax) {
			this.name = name;
			this.q = q;
			this.queueSizeMax = queueSizeMax;
			this.queueFullMax = 1;
		} // Producer

		public String getName() {
			return this.name;
		}

		public void pauseWhenFull() {
			try {
				int pauseSec = r1.nextInt(60); // pause for upto nn seconds
				System.out.println(String.format("%s: Pausing for: %d seconds", this.name, pauseSec));
				Thread.sleep(pauseSec * 1000);
			} catch (InterruptedException e ) {
				System.out.println( e );
			}
		} // pauseWhenFull

		@Override
		public void run() {
				if (this.q.size() < this.queueSizeMax) {
					Task t = new Task();
					System.out.println(String.format("%s: Adding %s Queue Size: %d", this.name, t, this.q.size()));
					this.q.add(t);
				} else {
					System.out.println(String.format("%s: Queue full %d", this.name, this.q.size()));
					pauseWhenFull();
				}
		} //run
	} // Producer

	public class Consumer implements Runnable {
		private String name;
		private Queue<Task> q;
		long completed;

		public Consumer(String name, Queue<Task> q) {
			this.name = name;
			this.q = q;
			this.completed = 0;
		}

		public String getName() {
			return this.name;
		}

		public void consume() {
			Task t = this.q.poll();
			if (t != null) {
				if (t.incomplete()) {
					System.out.println(String.format("%s: Working %s, Queue Size: %d", this.name, t, this.q.size()));
					t.process();
					q.add( t );
				} else {
					this.completed++;
					System.out.println(String.format("%s: Completed %s Completed: %d", this.name, t, this.completed));
				}
			} else {
				System.out.println(String.format("%s: Queue Empty, Completed: %d, Queue Size: %d", this.name, this.completed, this.q.size()));
			 });
			}
		} // consume

		public void showQueue() {
				this.q.forEach(task -> { System.out.println(task); } );
		}


		public void consume2() {
			int qs = this.q.size();
			try {
				Task t = this.q.remove(); // Throws NoSuchElementException if the Queue is empty
				if (t.incomplete()) {
					System.out.println(String.format("%s: Working %s, Queue Size: %d", this.name, t, this.q.size()));
					t.process();
					this.q.add( t );
				} else {
					this.completed++;
					System.out.println(String.format("%s: Completed %s Completed: %d", this.name, t, this.completed));
				}
			} catch (Exception e) {
				System.out.println(String.format("%s: Queue Empty, Completed: %d, Queue Size: (%d, %d)", this.name, this.completed, this.q.size(), qs));
				System.out.println("E " + e);
			}
		} // consume

		@Override
		public void run() {
				consume();
		} //run
	} // Worker

	// TestQueue1
	public TestQueue1() {
	}

	public void startQueues(long durationSeconds, long waitMs, double errorRate, long consumerThreads, String nodeName) {
		Queue<Task> queue1 = new LinkedList<>();

		ScheduledExecutorService ses = Executors.newScheduledThreadPool(4);

		for (int threadN=1; threadN<=consumerThreads; threadN++) {
			Consumer c = new Consumer(nodeName+Integer.toString(threadN), queue1);
			//ses.scheduleAtFixedRate(c, r1.nextInt(10), 1, TimeUnit.SECONDS);
			ses.scheduleWithFixedDelay(c, r1.nextInt(10), 1, TimeUnit.SECONDS);
		}

		Producer p = new Producer("Producer1", queue1, 10);
		ses.scheduleWithFixedDelay(p, 1, 2, TimeUnit.SECONDS); // new task after the previous task has finished

		// Wait for tasks to complete
		System.out.println( "Waiting" );
		try {
			ses.awaitTermination(durationSeconds, TimeUnit.SECONDS);
			ses.shutdownNow();
		} catch (Exception e ) {
			System.out.println( e );
		}
		System.out.println( "Complete" );
	}

	// Main
	public static void main(String[] args) {
		long durationSeconds, waitMs, nodes;
		double errorRate;
		String nodeName;

		if (args.length >= 1) {
			durationSeconds =  Integer.parseInt(args[0]);
		} else {
			durationSeconds = 60;
		}

		if (args.length >= 2) {
			waitMs =  Integer.parseInt(args[1]);
		} else {
			waitMs = 1000;
		}

		if (args.length >= 3) {
			errorRate = Double.parseDouble(args[2]);
		} else {
			errorRate = 0; // .1;
		}

		if (args.length >= 4) {
				nodes =  Integer.parseInt(args[3]);
		} else {
				nodes = 3;
		}

		if (args.length >= 5) {
				nodeName =  args[4];
		} else {
				nodeName = "NODE-";
		}

		TestQueue1 ta1 = new TestQueue1();
		ta1.startQueues(durationSeconds, waitMs, errorRate, nodes, nodeName);
	} // main
} // TestQueue1

<<<<<<< HEAD
=======
import java.util.Date;
import java.util.TimeZone;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
>>>>>>> 8e99d2f0690a3e497a67c05053d43b2def142949
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.Queue;
<<<<<<< HEAD
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
=======
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Random;
import java.util.UUID;

import java.util.logging.*;
//import java.io.IOException;
//import org.apache.log4j.Logger;

// Sample application using 1 x producer, and 3 x consumer threads across a queue
//
// Maintainer: David Ryder, david.ryder@appdynamics.com

public class TestQueue1
{
	private static final Logger log1 = Logger.getLogger( TestQueue1.class.getName() );
	private Random r1 = new Random();

  public static final void log(String s) {
		//log1.info(s);
		System.out.println(s);
	}
	public class Task {
		private int effort;
		private int waitMs;
		private int errorRate;
		private int total;
		private UUID uuid;

		public Task() {
				this(1000, 20);
		}

		public Task(int waitMs, int errorRate) {
>>>>>>> 8e99d2f0690a3e497a67c05053d43b2def142949
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
<<<<<<< HEAD
				//Thread.sleep(this.waitMs);
				if (Math.random() < errorRate) {
						Thread.sleep(this.waitMs * 3);
				}
			} catch (InterruptedException e ) {
				System.out.println( e );
=======
				if (r1.nextInt(100) < errorRate) {
						Thread.sleep(this.waitMs * r1.nextInt(10));
				}
			} catch (InterruptedException e ) {
				log(String.format("Exception: %s",e));
>>>>>>> 8e99d2f0690a3e497a67c05053d43b2def142949
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
<<<<<<< HEAD
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
=======
				int pauseSec = r1.nextInt(20)+10; // pause for upto nn seconds
				log(String.format("%s: Pausing for: %d seconds", this.name, pauseSec));
				Thread.sleep(pauseSec * 1000);
			} catch (InterruptedException e ) {
				log(String.format("Exception: %s",e));
			}
		} // pauseWhenFull

		public void produce() {
			if (this.q.size() < this.queueSizeMax) {
				Task t = new Task();
				log(String.format("%s: Adding %s Queue Size: %d", this.name, t, this.q.size()));
				this.q.add(t);
			} else {
				log(String.format("%s: Queue full %d", this.name, this.q.size()));
				pauseWhenFull();
			}
		} // produce

		@Override
		public void run() {
				produce();
>>>>>>> 8e99d2f0690a3e497a67c05053d43b2def142949
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
<<<<<<< HEAD
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
=======
				log(String.format("%s: Working %s, Queue Size: %d", this.name, t, this.q.size()));
				t.process();
				if (t.incomplete()) {
					q.add( t );
				} else {
					this.completed++;
					log(String.format("%s: Completed %s Completed: %d", this.name, t, this.completed));
				}
			} else {
				log(String.format("%s: Queue Empty, Completed: %d, Queue Size: (%d)", this.name, this.completed, this.q.size()));
>>>>>>> 8e99d2f0690a3e497a67c05053d43b2def142949
			}
		} // consume

		public void showQueue() {
				this.q.forEach(task -> { System.out.println(task); } );
		}

<<<<<<< HEAD

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

=======
>>>>>>> 8e99d2f0690a3e497a67c05053d43b2def142949
		@Override
		public void run() {
				consume();
		} //run
<<<<<<< HEAD
	} // Worker

	// TestQueue1
	public TestQueue1() {
	}

	public void startQueues(long durationSeconds, long waitMs, double errorRate, long consumerThreads, String nodeName) {
		Queue<Task> queue1 = new LinkedList<>();

		ScheduledExecutorService ses = Executors.newScheduledThreadPool(4);
=======
	} // Consumer

	// TestQueue1
	public TestQueue1() {
		configureLogging();
	}

	public void startQueues(int durationSeconds, int waitMs, int errorRate, int consumerThreads, String nodeName) {
		Queue<Task> queue1 = new LinkedBlockingQueue<>(); // Threadsafe blocking queue

		ScheduledExecutorService ses = Executors.newScheduledThreadPool(consumerThreads+1);
>>>>>>> 8e99d2f0690a3e497a67c05053d43b2def142949

		for (int threadN=1; threadN<=consumerThreads; threadN++) {
			Consumer c = new Consumer(nodeName+Integer.toString(threadN), queue1);
			//ses.scheduleAtFixedRate(c, r1.nextInt(10), 1, TimeUnit.SECONDS);
<<<<<<< HEAD
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
=======
			ses.scheduleWithFixedDelay(c, threadN, 1, TimeUnit.SECONDS);
		}

		Producer p = new Producer("Producer1", queue1, 10);
		ses.scheduleWithFixedDelay(p, 10, 2, TimeUnit.SECONDS); // new task after the previous task has finished

		// Wait for tasks to complete
		log(String.format("Waiting for thread termination %d seconds",durationSeconds));
		try {
			ses.awaitTermination((long)durationSeconds, TimeUnit.SECONDS);
			log("Shutdown");
			ses.shutdownNow();
		} catch (Exception e ) {
			log(String.format("Exception: %s",e));
		}
		log("Complete");
	} // startQueues

	public void configureLogging() {
		try {
			FileHandler fh = new FileHandler("TestQueue1.log");
			fh.setFormatter(new SimpleFormatter() {
						@Override
						public synchronized String format(LogRecord lr) {
								try {
									DateFormat df = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z");
									df.setTimeZone(TimeZone.getDefault());
									//return String.format("[%s ] %s%n",df.format(new Date(lr.getMillis())), lr.getMessage());
									return String.format("%s%n",lr.getMessage());
								} catch (Exception e) {
									return String.format("SimpleFormatter Failed with Exception %s", e);
								}
						}
				});
			//fh.setFormatter(new SimpleFormatter());
			log1.addHandler(fh);
		} catch (Exception e) {
			log("Exception: " + e);
		}
	} // configureLogging

	// Main
	public static void main(String[] args)  {
		int durationSeconds, waitMs, nodes, errorRate;
>>>>>>> 8e99d2f0690a3e497a67c05053d43b2def142949
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
<<<<<<< HEAD
			errorRate = Double.parseDouble(args[2]);
=======
			errorRate = Integer.parseInt(args[2]);
>>>>>>> 8e99d2f0690a3e497a67c05053d43b2def142949
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
<<<<<<< HEAD
=======
		log(String.format("Starting %d", durationSeconds));
>>>>>>> 8e99d2f0690a3e497a67c05053d43b2def142949
		ta1.startQueues(durationSeconds, waitMs, errorRate, nodes, nodeName);
	} // main
} // TestQueue1

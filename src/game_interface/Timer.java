package game_interface;

/**
 * This timer is designed to only ever run a single task at once and will
 * interrupt a current task if the next task is called via the schedule method.
 * The timer creates two threads, the first taskCallerThread to keep track of
 * the time interval between successive task calls and the second
 * taskRunnerThread to run the task provided. The task is any runnable method
 * provided.
 * 
 * @author Lucas Brown
 */
public class Timer {

	private boolean isDaemon;

	private long delay, period;
	private Runnable task;

	private CallerThread taskCallerThread;
	private Thread taskRunnerThread;

	/**
	 * Creates a new instance of a Timer object and assigns the daemon state of both threads.
	 * @param isDaemon whether the Timer is considered a daemon.
	 */
	public Timer(boolean isDaemon) {
		this.setDaemon(isDaemon);
	}

	/**
	 * Tells a task caller thread to start a second runner thread to run the task
	 * after a specified delay. It then interrupts and restarts the runner thread
	 * with the task every period.
	 * 
	 * @param task   the task to be run at fixed intervals.
	 * @param delay  how long after this method is called to start the first task.
	 * @param period the frequency by which the the task is called.
	 */
	public void schedule(Runnable task, long period, long delay) {
		this.task = task;
		this.delay = delay;
		this.period = period;

		if (this.taskRunnerThread != null) {
			this.taskRunnerThread.interrupt();
		}
		if (this.taskCallerThread != null) {
			this.taskCallerThread.interrupt();
		}

		this.taskRunnerThread = new Thread(this.task);
		this.taskRunnerThread.setName("Task Runner Thread");
		this.taskCallerThread = new CallerThread("Task Caller Thread");
		this.setDaemon(isDaemon);

		this.taskCallerThread.start();
	}

	/**
	 *  Equivalent to calling schedule(task, 0, period)
	 * @param task  the task to be scheduled
	 * @param period  the period of the task to be scheduled
	 */
	public void schedule(Runnable task, long period) {
		this.schedule(task, period, 0);
	}

	/**
	 * Stop the currently scheduled task.
	 */
	public void stop() {
		this.taskCallerThread.interrupt();
		this.taskRunnerThread.interrupt();
	}

	/**
	 * Gets the daemon state.
	 * @return the state of the daemon of both threads.
	 */
	public boolean isDaemon() {
		return this.isDaemon;
	}

	/**
	 * Sets the state of the daemon.
	 * @param isDaemon the daemon state.
	 */
	public void setDaemon(boolean isDaemon) {
		this.isDaemon = isDaemon;
		if (this.taskCallerThread != null) {
			this.taskCallerThread.setDaemon(this.isDaemon);
		}
		if (this.taskRunnerThread != null) {
			this.taskRunnerThread.setDaemon(this.isDaemon);
		}
	}

	/**
	 * This resets the game.
	 */
	public void reset() {
		this.taskCallerThread.reset();
	}

	/**
	 * The purpose of this thread is to continually and safely interrupt the old
	 * taskRunnerThread, create a new thread for the task, and start the task on a
	 * fixed timed interval after a set delay.
	 */
	private class CallerThread extends Thread {

		private long startTime;

		public CallerThread(String name) {
			super(name);
		}

		@Override
		public void run() {
			startTime = System.currentTimeMillis(); // Get start time
			while (System.currentTimeMillis() < startTime + delay) {
			} // wait for the duration of the delay

			taskRunnerThread.start(); // Start the task

			startTime = System.currentTimeMillis(); // Reset start time
			while (!Thread.interrupted()) { // While the thread has not been told to stop
				if (System.currentTimeMillis() >= startTime + period) { // Check if the next period has elapsed
					taskRunnerThread.interrupt(); // Interrupt current task if it has not already terminated
					try {
						taskRunnerThread.join(); // Wait for the interrupt
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					taskRunnerThread = new Thread(task); // Dispose of old thread and create new one designated to the
															// task
					taskRunnerThread.setName("Task Runner Thread");
					taskRunnerThread.setDaemon(isDaemon);
					taskRunnerThread.start(); // Start the task
					startTime = System.currentTimeMillis(); // Reset start time
				}
			}
		}

		/**
		 * Immediately calls the next task, disregarding the period. 
		 */
		public void reset() {
			this.startTime = System.currentTimeMillis() - period;
		}

	}

}

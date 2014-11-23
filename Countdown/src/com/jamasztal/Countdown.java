package com.jamasztal;

/*
 * This class defines a Countdown.
 * 
 * @author Jan Masztal
 */
public class Countdown{
	private long lastUpdateTime;
	private long countdownTime;
	private long userCountdownTime;
	private int state;

	public static final int DEFAULT = 0;
	public static final int RUNNING = 1;
	public static final int PAUSED  = 2;

	/*
	 * Construct an item with 10 as a default value.
	 * 
	 */
	public Countdown()
	{
		this(10);
	}

	/*
	 * Construct an item with specific values.
	 *
	 * @param t The time to set the countdown to
	 */
	public Countdown(int t)
	{
		userCountdownTime = countdownTime = t * 1000;
	}

	/*
	 * Sets count down state to PAUSED.
	 */
	public void pause()
	{
		state = PAUSED;
	}

	/*
	 * Sets count down state to Running.
	 */
	public void resume()
	{
		state = RUNNING;
	}

	/*
	 * Sets count down state to DEFAULT.
	 */
	public void reset()
	{
		state = DEFAULT;
		countdownTime = userCountdownTime;
		lastUpdateTime = System.currentTimeMillis();
	}

	/*
	 * Updates to decrease coutdownTime.
	 *
	 * @param currentTime The current time of proccess when update was called
	 */
	public void update(long currentTime)
	{
		long elapsedTime = currentTime - lastUpdateTime;

		if (state == RUNNING)
			countdownTime -= elapsedTime;

		lastUpdateTime = currentTime;
	}

	/*
	 * Return count down time.
	 * 
	 * @return countdownTime
	 */
	public long getCountdownTime()
	{
		return countdownTime;
	}

	/*
	 * Return count down current state.
	 * 
	 * @return state
	 */
	public int getState()
	{
		return state;
	}

	/*
	 * Set coutdown to user defined time.
	 * 
	 * @param time
	 */
	public void setUserCountdownTime(long time)
	{
		userCountdownTime = countdownTime = time * 1000;
	}

	/*
	 * Print countdown object
	 *
	 *@return countdownString
	 */
	public String toString(){
		String countdownString = "Last Update Time: " + lastUpdateTime + "\n"
				+"Count Down Time: " + countdownTime + "\n"
				+"User Count Down Time: " + userCountdownTime + "\n"
				+"State: " + state + "\n";
		return countdownString;
	}
}
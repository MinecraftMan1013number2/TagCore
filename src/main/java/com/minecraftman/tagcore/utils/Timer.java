package com.minecraftman.tagcore.utils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Timer {
	private final Date endDate;
	
	public Timer(int seconds) {
		this(seconds, 0);
	}
	public Timer(int seconds, int minutes) {
		// 4ms needs to be added to get an accurate formatted time for some reason
		// im guessing it's execution time, but idk
		int ms = seconds*1000 + minutes*60000 + 4;
		endDate = new Date();
		endDate.setTime(endDate.getTime() + ms);
	}
	
	public long getMinutesLeft() {
		long ms = endDate.getTime() - new Date().getTime();
		return TimeUnit.MINUTES.convert(ms, TimeUnit.MILLISECONDS);
	}
	
	public long getSecondsLeft() {
		long ms = endDate.getTime() - new Date().getTime();
		return TimeUnit.SECONDS.convert(ms, TimeUnit.MILLISECONDS);
	}
	
	public String getFormattedTimeRemaining() {
		long s = getSecondsLeft() % 60;
		return getMinutesLeft() + ":" + (s < 10 ? "0" : "") + s;
	}
	
	public boolean isFinished() {
		return (endDate.getTime() - new Date().getTime()) <= 0;
	}
}

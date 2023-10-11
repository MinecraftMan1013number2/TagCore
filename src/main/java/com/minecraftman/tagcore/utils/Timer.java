package com.minecraftman.tagcore.utils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Timer {
	private final Date endDate;
	
	public Timer(int seconds) {
		this(seconds, 0);
	}
	public Timer(int seconds, int minutes) {
		int ms = seconds*1000 + minutes*60000;
		endDate = new Date();
		endDate.setTime(endDate.getTime() + ms);
	}
	
	public double getMinutesLeft() {
		long ms = Math.abs(endDate.getTime() - new Date().getTime());
		return TimeUnit.MINUTES.convert(ms, TimeUnit.MILLISECONDS) + TimeUnit.SECONDS.convert(ms, TimeUnit.MILLISECONDS)/60.0;
	}
	
	public boolean timerFinished() {
		return (endDate.getTime() - new Date().getTime()) > 0;
	}
}

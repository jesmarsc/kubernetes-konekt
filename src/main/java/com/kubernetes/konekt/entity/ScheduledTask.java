package com.kubernetes.konekt.entity;

import javax.validation.constraints.*;

public class ScheduledTask {
	
	

	
	
	

	@Min(value = 0, message="Must be a number between 0 and 59.")
	@Max(value = 59, message="Must be a number between 0 and 59.")
	@NotNull(message="Must enter a value.")
	private Integer second;
	
	@Min(value = 0, message="Must be a number between 0 and 59.")
	@Max(value = 59, message="Must be a number between 0 and 59.")
	@NotNull(message="Must enter a value.")
	private Integer minute;
	
	@Min(value = 0, message="Must be a number between 0 and 23.")
	@Max(value = 23, message="Must be a number between 0 and 23.")
	@NotNull(message="Must enter a value.")
	private Integer hour;
	
	@Min(value = 1, message="Must be a number between 1 and 31.")
	@Max(value = 31, message="Must be a number between 1 and 31.")
	@NotNull(message="Must enter a value.")
	private Integer date_;
	
	@Min(value = 1, message="Must be a number between 1 and 12.")
	@Max(value = 12, message="Must be a number between 1 and 12.")
	@NotNull(message="Must enter a value.")
	private Integer month;
	
	@Min(value = 1, message="Must be a number between 1 and 7.")
	@Max(value = 7, message="Must be a number between 1 and 7.")
	@NotNull(message="Must enter a value.")
	private Integer dayofWeek;
	

	
	
	
	public ScheduledTask() {


}

	
	public Integer getSecond() {
		return second;
	}


	public void setSecond(Integer second) {
		this.second = second;
	}


	public Integer getMinute() {
		return minute;
	}


	public void setMinute(Integer minute) {
		this.minute = minute;
	}







	public Integer getHour() {
		return hour;
	}







	public void setHour(Integer hour) {
		this.hour = hour;
	}







	public Integer getDate_() {
		return date_;
	}







	public void setDate_(Integer date_) {
		this.date_ = date_;
	}







	public Integer getMonth() {
		return month;
	}







	public void setMonth(Integer month) {
		this.month = month;
	}







	public Integer getDayofWeek() {
		return dayofWeek;
	}







	public void setDayofWeek(Integer dayofWeek) {
		this.dayofWeek = dayofWeek;
	}







	}




	


	
	
	

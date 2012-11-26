/**
 * Task.java
 * 2012-9-25 Qixuan Zhang Created
 */

package com.taskit.shared;

import java.util.Date;

/*
 * This class is used to store task information.
 * Every task has a unique ID. Name, priority, date and status are required. Others are optional.
 */
public class Task {
	
	private int id;
	private String name;
	private String people;
	private String location;
	private String description;
	private int priority;
	private int status;
	private Date date;
	
	// Constructor
	public Task(int taskId, String taskName, String taskPeople, String taskLocation, String taskDescription, int taskPriority, int taskStatus, Date taskDate) {
		id = taskId;
		name = taskName;
		people = taskPeople;
		location = taskLocation;
		description = taskDescription;
		priority = taskPriority;
		status = taskStatus;
		date = taskDate;
	}
	
	// Getters and Setters
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setPeople(String people) {
		this.people = people;
	}
	
	public String getPeople() {
		return people;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public Date getDate() {
		return date;
	}
	
	// Checker for status. 0 - normal, 1 - completed, 2 - canceled
	public boolean isNormal() {
		if (this.status == 0) {
			return true;
		}
		else return false;
	}
	
	public boolean isCompleted() {
		if (this.status == 1) {
			return true;
		}
		else return false;
	}
	
	public boolean isCanceled() {
		if (this.status == 2) {
			return true;
		}
		else return false;
	}
}

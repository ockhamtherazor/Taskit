/**
 * LoadTasks.java
 * 2012-11-25 OckhamTheRazor
 */
package com.taskit.client.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.TasksRequestInitializer;
import com.google.api.services.tasks.model.Task;

public class UseTasksAPI {
	
	private static final String API_KEY = "";
	private static final String ERROR_TAG = "UseTasksAPI.java";
	
	public List<Task> loadCurrentTasks(GoogleCredential credential) {
		
		List<Task> tasks = new ArrayList<Task>();
		List<Task> currentTasks = new ArrayList<Task>();
		
		// get an instance of Tasks API service
		Tasks service = new Tasks.Builder(new NetHttpTransport(),
				new JacksonFactory(),
				credential)
		.setApplicationName("Taskit")
		.setTasksRequestInitializer(new TasksRequestInitializer(API_KEY))
		.build();
		
		try {
			tasks = service.tasks().list("@default").execute().getItems();
			for (Task t : tasks) {
				if (t.getStatus().equals("needsAction")) {
					currentTasks.add(t);
				}
			}
		} catch (IOException e) {
			Log.e(ERROR_TAG, "fail to load current tasks", e);
		}
		
		return currentTasks;
		
	}
	
	public List<Task> loadHistoryTasks(GoogleCredential credential) {
		
		List<Task> tasks = new ArrayList<Task>();
		List<Task> historyTasks = new ArrayList<Task>();
		
		// get an instance of Tasks API service
		Tasks service = new Tasks.Builder(new NetHttpTransport(),
				new JacksonFactory(),
				credential)
		.setApplicationName("Taskit")
		.setTasksRequestInitializer(new TasksRequestInitializer(API_KEY))
		.build();
		
		try {
			tasks = service.tasks().list("@default").execute().getItems();
			for (Task t : tasks) {
				if (t.getStatus().equals("completed")) {
					historyTasks.add(t);
				}
			}
		} catch (IOException e) {
			Log.e(ERROR_TAG, "fail to load current tasks", e);
		}
		
		return historyTasks;
		
	}
	
	public Task getTask(GoogleCredential credential,
			String taskId) {
		
		Task task = new Task();;
		
		// get an instance of Tasks API service
		Tasks service = new Tasks.Builder(new NetHttpTransport(),
				new JacksonFactory(),
				credential)
		.setApplicationName("Taskit")
		.setTasksRequestInitializer(new TasksRequestInitializer(API_KEY))
		.build();
		
		try {
			task = service.tasks().get("@default", taskId).execute();
		} catch (IOException e) {
			Log.e(ERROR_TAG, "fail to set task as completed", e);
		}
		
		return task;
	}

	public Boolean addNewTask(GoogleCredential credential,
			String name,
			String description,
			String location,
			Long date,
			int priority) {

		// convert data to API format
		String note = String.valueOf(priority)
				+ "~@#"
				+ location
				+ "~@#"
				+ description;
		DateTime dateTime = new DateTime(date, -5);

		// get an instance of Tasks API service
		Tasks service = new Tasks.Builder(new NetHttpTransport(),
				new JacksonFactory(),
				credential)
		.setApplicationName("Taskit")
		.setTasksRequestInitializer(new TasksRequestInitializer(API_KEY))
		.build();
		
		try {
			
			Task task = new Task();
			task.setTitle(name).setNotes(note).setDue(dateTime);
			service.tasks().insert("@default", task).execute();
			
			return true;
			
		} catch (IOException e) {
			Log.e(ERROR_TAG, "fail to add new task", e);
			return false;
		}		
	}
	
	public Boolean updateTask(GoogleCredential credential,
			String taskId,
			String name,
			String description,
			String location,
			Long date,
			int priority) {

		// convert data to API format
		String note = String.valueOf(priority)
				+ "~@#"
				+ location
				+ "~@#"
				+ description;
		DateTime dateTime = new DateTime(date, -5);

		// get an instance of Tasks API service
		Tasks service = new Tasks.Builder(new NetHttpTransport(),
				new JacksonFactory(),
				credential)
		.setApplicationName("Taskit")
		.setTasksRequestInitializer(new TasksRequestInitializer(API_KEY))
		.build();
		
		try {
			
			Task task = service.tasks().get("@default", taskId).execute();
			task.setTitle(name).setNotes(note).setDue(dateTime);
			service.tasks().update("@default", task.getId(), task).execute();
			
			return true;
			
		} catch (IOException e) {
			Log.e(ERROR_TAG, "fail to add new task", e);
			return false;
		}		
	}
	
	public Boolean setTaskAsCompleted(GoogleCredential credential,
			String taskId) {

		// get an instance of Tasks API service
		Tasks service = new Tasks.Builder(new NetHttpTransport(),
				new JacksonFactory(),
				credential)
		.setApplicationName("Taskit")
		.setTasksRequestInitializer(new TasksRequestInitializer(API_KEY))
		.build();
		
		try {
			Task task = service.tasks().get("@default", taskId).execute();
			task.setStatus("completed");
			service.tasks().update("@default", task.getId(), task).execute();
			
			return true;
			
		} catch (IOException e) {
			Log.e(ERROR_TAG, "fail to set task as completed", e);
			return false;
		}		
	}
	
	public Boolean setTaskAsDeleted(GoogleCredential credential,
			String taskId) {

		// get an instance of Tasks API service
		Tasks service = new Tasks.Builder(new NetHttpTransport(),
				new JacksonFactory(),
				credential)
		.setApplicationName("Taskit")
		.setTasksRequestInitializer(new TasksRequestInitializer(API_KEY))
		.build();
		
		try {
			service.tasks().delete("@default", taskId).execute();
			
			return true;
			
		} catch (IOException e) {
			Log.e(ERROR_TAG, "fail to set task as deleted", e);
			return false;
		}		
	}
	
}

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
	
	private static final String API_KEY = "AIzaSyD_H4i4MdHS5OVPXHbmosl7YKuIVZRvz5I";
	private static final String ERROR_TAG = "UseTasksAPI.java";
	
	public List<Task> loadTasks(GoogleCredential credential) {
		
		List<Task> tasks = new ArrayList<Task>();
		
		// get an instance of Tasks API service
		Tasks service = new Tasks.Builder(new NetHttpTransport(),
				new JacksonFactory(),
				credential)
		.setApplicationName("Taskit")
		.setTasksRequestInitializer(new TasksRequestInitializer(API_KEY))
		.build();
		
		try {
			tasks = service.tasks().list("@taskit").execute().getItems();
		} catch (IOException e) {
			Log.e(ERROR_TAG, "fail to load current tasks", e);
		}
		
		return tasks;
		
	}

	public Boolean addNewTask(GoogleCredential credential,
			String name,
			String description,
			String location,
			DateTime date,
			int priority) {

		// get an instance of Tasks API service
		Tasks service = new Tasks.Builder(new NetHttpTransport(),
				new JacksonFactory(),
				credential)
		.setApplicationName("Taskit")
		.setTasksRequestInitializer(new TasksRequestInitializer(API_KEY))
		.build();
		
		try {
			Task task = new Task();
			task.setTitle(name);
			Task result = service.tasks().insert("@taskit", task).execute();
			
			return true;
		} catch (IOException e) {
			Log.e(ERROR_TAG, "fail to add new task", e);
			return false;
		}		
	}
}

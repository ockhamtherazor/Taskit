/**
 * LoadTasks.java
 * 2012-11-25 OckhamTheRazor
 */
package com.taskit.client.util;

import java.io.IOException;
import java.util.List;

import android.util.Log;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.model.Task;

public class UseTasksAPI {
	
	private static final String ERROR_TAG = "UseTasksAPI.java";
	
	public void loadTasks(GoogleCredential credential) {
		
		Tasks service = new Tasks(new NetHttpTransport(),
				new JacksonFactory(), 
				credential);
		
		try {
			List<Task> tasks = service
					.tasks()
					.list("@default")
					.execute()
					.getItems();
			for (Task temp : tasks) {
				Log.d("test", temp.toString());
			}
		} catch (IOException e) {
			Log.e(ERROR_TAG, "fail to load tasks");
		}
		
	}
}

/**
 * TaskUtil.java
 * 2012-12-4 OckhamTheRazor
 */
package com.taskit.client.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.api.services.tasks.model.Task;

public class TaskUtil {

	public List<Task> sortTasks(List<Task> tasks) {
		
		List<Task> result = tasks;
		
		Collections.sort(result, new Comparator<Task>() {
			public int compare(Task one, Task other) {
				String noteOne[] = one.getNotes().split("~@#");
				String noteOther[] = other.getNotes().split("~@#");;
				return noteOther[0].compareTo(noteOne[0]);
			}
		});
				
		return result;
	}
	
}

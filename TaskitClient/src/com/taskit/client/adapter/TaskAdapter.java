/**
 * TaskAdapter.java
 * 2012-12-1 OckhamTheRazor
 */
package com.taskit.client.adapter;

import java.util.List;

import com.google.api.services.tasks.model.Task;
import com.taskit.client.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TaskAdapter extends ArrayAdapter<Task> {

	Activity activity;
	int layoutResourceId;
	List<Task> tasksList = null;
	
	public TaskAdapter(Activity activity, int layoutResourceId,
			List<Task> objects) {
		super(activity, layoutResourceId, objects);
		this.activity = activity;
		this.layoutResourceId = layoutResourceId;
		this.tasksList = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View row = convertView;
		TaskHolder holder = null;
		
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater)activity
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(layoutResourceId, parent, false);
			
			holder = new TaskHolder();
			holder.priority = (ImageView) row.findViewById(R.id.item_priority);
			holder.name = (TextView) row.findViewById(R.id.item_title);
			holder.date = (TextView) row.findViewById(R.id.item_description);
			holder.id = (TextView) row.findViewById(R.id.item_id);
			row.setTag(holder);
			
		} else {
			holder = (TaskHolder) row.getTag();
		}
		
		Task task = tasksList.get(position);
		holder.name.setText(task.getTitle());
		holder.id.setText(task.getId());
		
		if (task.getDue() != null) {
			holder.date.setText(task.getDue().toString());
		}
		
		if (task.getNotes() != null) {
			String note[] = task.getNotes().split("@#*");
			switch (Integer.valueOf(note[0])) {
				case 0: holder.priority.setImageResource(R.drawable.priority_green);
						break;
				case 1: holder.priority.setImageResource(R.drawable.priority_yellow);
						break;
				case 2: holder.priority.setImageResource(R.drawable.priority_red);
						break;
			}
		}

		row.setClickable(true);
		row.setFocusable(true);
		
		return row;
		
	}

}

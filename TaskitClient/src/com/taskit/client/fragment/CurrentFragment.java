/**
 * CurrentFragment.java
 * 2012-10-24 OckhamTheRazor
 */
package com.taskit.client.fragment;

import com.taskit.client.R;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class CurrentFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater,   
		    ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_current, container, false);
		
		Button newTaskButton = (Button) view.findViewById(R.id.add_task_button_1);
		
		newTaskButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		return view;
	}
	
}

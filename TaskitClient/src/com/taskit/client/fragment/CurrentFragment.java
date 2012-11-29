/**
 * CurrentFragment.java
 * 2012-10-24 OckhamTheRazor
 */
package com.taskit.client.fragment;

import com.taskit.client.NewTaskActivity;
import com.taskit.client.R;
import com.taskit.client.util.IDataPuller;

import android.accounts.Account;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class CurrentFragment extends Fragment {
	
	// generic interface used to pull data from activity
	private IDataPuller dataPuller;
	
	// user's account
	private Account account;
	
	@Override
	public void onAttach(Activity activity) {
	    super.onAttach(activity);
	    dataPuller = (IDataPuller) activity;
	    account = dataPuller.getAccount();
	    Log.d("test", account.toString());
	    // TODO       
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,   
		    ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_current, container, false);
		
		Button newTaskButton = (Button) view.findViewById(R.id.add_task_button_1);
		newTaskButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				Intent intent = new Intent(getActivity().getBaseContext(),
						NewTaskActivity.class);
				intent.putExtra("account", account);
				startActivity(intent);
				
			}
		});
		
		return view;
	}
	
}

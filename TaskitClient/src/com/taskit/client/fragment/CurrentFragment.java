/**
 * CurrentFragment.java
 * 2012-10-24 OckhamTheRazor
 */
package com.taskit.client.fragment;

import java.io.IOException;
import java.util.List;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.tasks.model.Task;
import com.taskit.client.NewTaskActivity;
import com.taskit.client.R;
import com.taskit.client.adapter.TaskAdapter;
import com.taskit.client.util.IDataPuller;
import com.taskit.client.util.ProgressDialogUtil;
import com.taskit.client.util.UseTasksAPI;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class CurrentFragment extends ListFragment {
	
	// generic interface used to pull data from activity
	private IDataPuller dataPuller;
	
	// user account
	private Account account;
	
	// user tasks
	private List<Task> currentTasks;
	
	// display a progress dialog when loading tasks
	private ProgressDialog dialog;
	ProgressDialogUtil dialogUtil = new ProgressDialogUtil();
		
	// handle message returned from tasks API
	private Handler handler;
		
	private static final String ERROR_TAG = "TaskActivity.java";
	private static final String AUTH_TOKEN_TYPE = "oauth2:https://www.googleapis.com/auth/tasks";
	private static final String OPERATION_CANCLED_MESSAGE = "user denied access";
	private static final String AUTHENTICATOR_ERROR_MESSAGE = "authenticator met an error";
	private static final String IO_ERROR_MESSAGE = "Java IO error";
	
	@Override
	public void onAttach(Activity activity) {
	    super.onAttach(activity);
		dataPuller = (IDataPuller) getActivity();
	    account = dataPuller.getAccount();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// handle message returned from tasks API
    	handler = new Handler(new Handler.Callback() {
			
			public boolean handleMessage(Message msg) {
				if (msg.getData().containsKey("status")) {
					String text = msg.getData().getString("status");
					if (text.equals("load complete")) {
						dialogUtil.dismissProgressDialog(dialog,
							"Complete loading tasks");
						TaskAdapter adapter = new TaskAdapter(getActivity(),
								R.layout.list_row,
								currentTasks);
						setListAdapter(adapter);
					}
				} else {
					Log.e(ERROR_TAG, "fail to receive message");
					dialogUtil.dismissProgressDialog(dialog, 
							"Fail loading tasks");
				}
				return true;
			}
		});
    	
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
	
	@Override
	public void onResume() {
		super.onResume();
		
		AccountManager accountManager = 
				AccountManager.get(getActivity());
		accountManager.getAuthToken(account,
				AUTH_TOKEN_TYPE,
				null,
				getActivity(), 
				new OnTokenAcquired(),
				null);
		
	}
	
	private class OnTokenAcquired implements AccountManagerCallback<Bundle> {    	
    	public void run(AccountManagerFuture<Bundle> future) {
    		
    		try {
    			
    			// get the result of the operation from the AccountManagerFuture
    	        Bundle bundle = future.getResult();
    	        String token = bundle.getString(AccountManager.KEY_AUTHTOKEN);
    	        
    	        final GoogleCredential credential = new GoogleCredential();
    	        credential.setAccessToken(token);
    	        
    	        // show dialog indicating that the application is loading tasks
    	        dialog = new ProgressDialog(getActivity());
    	        dialogUtil.setUpProgressDialog(dialog, "Loading tasks...");
    	        dialog.show();
    	        
    	        // load tasks in worker thread
    	        new Thread(new Runnable() {
    	        	
    	        	private void sendMsg(String msgText) {
    	 				Bundle bundle = new Bundle();
    	 				Message msg = new Message();
    	 				bundle.putString("status", msgText);
    	 				msg.setData(bundle);
    	 				handler.sendMessage(msg);
    	 			}
    	 			
    	 			public void run() {
    	 				UseTasksAPI useTasksAPI = new UseTasksAPI();
    	 				currentTasks = useTasksAPI.loadCurrentTasks(credential);
    	 				sendMsg("load complete");
    	 			}
    	 			
    	        }).start();
    	        
    		} catch (OperationCanceledException e) {
    			Log.e(ERROR_TAG, OPERATION_CANCLED_MESSAGE, e);
    		} catch (AuthenticatorException e) {
    			Log.e(ERROR_TAG, AUTHENTICATOR_ERROR_MESSAGE, e);
    		} catch (IOException e) {
    			Log.e(ERROR_TAG, IO_ERROR_MESSAGE, e);
    		}
            
    	}

    }
	
}

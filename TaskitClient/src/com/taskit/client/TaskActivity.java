package com.taskit.client;

import java.io.IOException;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.taskit.client.fragment.CurrentFragment;
import com.taskit.client.fragment.HistoryFragment;
import com.taskit.client.fragment.SettingFragment;
import com.taskit.client.util.ProgressDialogUtil;
import com.taskit.client.util.UseTasksAPI;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class TaskActivity extends Activity {

	private Account account;
	
	// display a progress dialog when loading tasks
	private ProgressDialog dialog;
	ProgressDialogUtil dialogUtil = new ProgressDialogUtil();
	
	// to handle message returned from tasks API
	private Handler handler;
	
	private static final String ERROR_TAG = "TaskActivity.java";
	private static final String AUTH_TOKEN_TYPE = "oauth2:https://www.googleapis.com/auth/tasks";
	private static final String OPERATION_CANCLED_MESSAGE = "user denied access";
	private static final String AUTHENTICATOR_ERROR_MESSAGE
	= "authenticator met an error";
	private static final String IO_ERROR_MESSAGE = "Java IO error";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        
        // setup action bar for tabs
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);

        // add all 3 tabs
        Tab tab = actionBar.newTab()
        		.setText(R.string.tab_current)
        		.setTabListener(new TaskitTabListener<CurrentFragment>(
        				this, "current", CurrentFragment.class));
        actionBar.addTab(tab);
        
        tab = actionBar.newTab()
        		.setText(R.string.tab_history)
        		.setTabListener(new TaskitTabListener<HistoryFragment>(
        				this, "history", HistoryFragment.class));
        actionBar.addTab(tab);
        
        tab = actionBar.newTab()
        		.setText(R.string.tab_setting)
        		.setTabListener(new TaskitTabListener<SettingFragment>(
        				this, "settings", SettingFragment.class));
        actionBar.addTab(tab);
        
        handler = new Handler(new Handler.Callback() {
			
			public boolean handleMessage(Message msg) {
				if (msg.getData().containsKey("status")) {
					String text = msg.getData().getString("status");
					if (text.equals("load complete")) {
						dialogUtil.dismissProgressDialog(dialog,
								"Complete loading tasks");
						// TODO
					}
				} else {
					Log.e(ERROR_TAG, "fail to receive message");
				}
				return true;
			}
		});
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	
    	Bundle extras = getIntent().getExtras();
    	account = extras.getParcelable("account");
    	
    	AccountManager accountManager = 
				AccountManager.get(TaskActivity.this);
		accountManager.getAuthToken(account,
				AUTH_TOKEN_TYPE,
				null,
				TaskActivity.this, 
				new OnTokenAcquired(),
				null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_task, menu);
        return true;
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
    	        dialog = new ProgressDialog(TaskActivity.this);
    	        dialogUtil.setUpProgressDialog(dialog, "Loading Tasks...");
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
    	 				//useTasksAPI.loadTasks(credential);
    	 				// TODO
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

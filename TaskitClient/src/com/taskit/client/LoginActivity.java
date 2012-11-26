package com.taskit.client;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
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
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends Activity {

	// setting logging level
	private static final Level LOGGING_LEVEL = Level.OFF;
		
	// builder for account selecting dialog
	private AlertDialog.Builder builder;

	// account selected by user to access Google API
	private Account selectedAccount;
	
	// to handle message returned from tasks API
	private Handler handler;
	
	// display a progress dialog when loading tasks
	private ProgressDialog dialog;
	ProgressDialogUtil dialogUtil = new ProgressDialogUtil();
	
	private static final String AUTH_TOKEN_TYPE = "oauth2:https://www.googleapis.com/auth/tasks";
	private static final String ERROR_TAG = "LoginActivity.java";
	private static final String OPERATION_CANCLED_MESSAGE = "user denied access";
	private static final String AUTHENTICATOR_ERROR_MESSAGE
	= "authenticator met an error";
	private static final String IO_ERROR_MESSAGE = "Java IO error";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button accountButton =
        		(Button) findViewById(R.id.select_account_button);
        final Button loginButton = (Button) findViewById(R.id.login_button);
        
        // enable logging
        Logger.getLogger("com.google.api.client").setLevel(LOGGING_LEVEL);
        
        // get authentication token
        loginButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				AccountManager accountManager = 
						AccountManager.get(LoginActivity.this);
				accountManager.getAuthToken(selectedAccount,
						AUTH_TOKEN_TYPE,
						null,
						LoginActivity.this, 
						new OnTokenAcquired(),
						null);
			}
		});
        
        // clicking the button will show an alert that let user choose his/her
        // Google account
        accountButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// show account selecting dialog
				createAccountDialog();
				builder.create().show();
			}
		});
        
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_login, menu);
        return true;
    }

    // create an alert that allow user to select a Google account
    private AlertDialog.Builder createAccountDialog() {
    	
    	builder = new AlertDialog.Builder(LoginActivity.this);
    	builder.setTitle("Select your Google account");
    	builder.setCancelable(true);
    	
    	// get all google accounts of the device
    	AccountManager accountManager = AccountManager.get(LoginActivity.this);
    	final Account[] accounts = accountManager.getAccountsByType("com.google");
    	final int size = accounts.length;
		String[] accountNames = new String[size];
		for (int i = 0; i < size; i++) {
			accountNames[i] = accounts[i].name;
		}
		
		builder.setItems(accountNames, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				selectedAccount = accounts[which];
			}
		});
		
		return builder;
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
    	        dialog = new ProgressDialog(LoginActivity.this);
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
    	 				useTasksAPI.loadTasks(credential);
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

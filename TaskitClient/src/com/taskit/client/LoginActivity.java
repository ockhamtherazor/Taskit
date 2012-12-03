package com.taskit.client;

import com.taskit.client.util.ProgressDialogUtil;

import android.os.Bundle;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends Activity {
		
	// builder for account selecting dialog
	private AlertDialog.Builder builder;
	ProgressDialogUtil dialogUtil = new ProgressDialogUtil();

	// account selected by user to access Google API
	private Account selectedAccount = null;
	
	private Button accountButton;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        accountButton = (Button) findViewById(R.id.select_account_button);
        final Button loginButton = (Button) findViewById(R.id.login_button);
        final Button exitButton = (Button) findViewById(R.id.exit_button);
        
        // clicking the button will show an alert that let user choose his/her
        // Google account
        accountButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// show account selecting dialog
				createAccountDialog();
				builder.create().show();
			}
		});
        
        loginButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				if (selectedAccount == null) {
					Toast.makeText(getBaseContext(),
							"Please select a Google Account",
							Toast.LENGTH_SHORT)
							.show();
				} else {
					Intent intent = new Intent(LoginActivity.this, TaskActivity.class);
	    	        intent.putExtra("account", selectedAccount);
	    	        startActivity(intent);
	    	        finish();
				}
				
			}
		});
        
        exitButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				finish();
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
				accountButton.setText(selectedAccount.name);
			}
		});
		
		return builder;
    }
}

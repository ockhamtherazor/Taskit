package com.taskit.client;

import android.os.Bundle;
import android.accounts.Account;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class NewTaskActivity extends Activity {

	// user's account
	private Account account;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        
        Button cancelButton = (Button) findViewById(R.id.cancel_button);
        Button addTaskButton = (Button) findViewById(R.id.add_task_button);
        
        Bundle extras = getIntent().getExtras();
    	account = extras.getParcelable("account");
    	
    	cancelButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				finish();				
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_new_task, menu);
        return true;
    }
}

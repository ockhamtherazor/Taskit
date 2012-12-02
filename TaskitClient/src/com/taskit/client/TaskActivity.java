package com.taskit.client;

import com.taskit.client.fragment.CurrentFragment;
import com.taskit.client.fragment.HistoryFragment;
import com.taskit.client.util.IDataPuller;

import android.os.Bundle;
import android.accounts.Account;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.view.Menu;

public class TaskActivity extends Activity implements IDataPuller {
	
	// the selected user account
	private Account account;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        
        Bundle extras = getIntent().getExtras();
    	account = extras.getParcelable("account");
    	
        // setup action bar for tabs
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);
        
        // add all 2 tabs
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
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_task, menu);
        return true;
    }
    
    // pass user account to fragments
	public Account getAccount() {
		return account;
	}
    
}

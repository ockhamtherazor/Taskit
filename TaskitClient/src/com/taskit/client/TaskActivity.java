package com.taskit.client;

import com.taskit.client.fragment.CurrentFragment;
import com.taskit.client.fragment.HistoryFragment;
import com.taskit.client.fragment.SettingFragment;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.view.Menu;

public class TaskActivity extends Activity {

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_task, menu);
        return true;
    }
}

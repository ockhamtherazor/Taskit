/**
 * TaskitTabListener.java
 * 2012-10-24 OckhamTheRazor
 */
package com.taskit.client;

import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

public class TaskitTabListener<T extends Fragment> implements TabListener {
	
	private Fragment fragment;
	private final Activity activity;
	private final String tag;
	private final Class<T> fragmentClass;

	public TaskitTabListener(Activity activity,
			String tag,
			Class<T> c) {
		this.activity = activity;
		this.tag = tag;
		this.fragmentClass = c;
    }
	
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// check if the fragment is already instantiate
		if (fragment == null) {
			fragment = Fragment.instantiate(activity, fragmentClass.getName());
			ft.add(android.R.id.content, fragment, tag);
		} else {
			ft.attach(fragment);
		}
	}

	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// Detach the fragment
		if (fragment != null) {
			ft.detach(fragment);
		}
	}

}

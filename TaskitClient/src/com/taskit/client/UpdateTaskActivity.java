package com.taskit.client;

import java.io.IOException;
import java.util.Calendar;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.util.DateTime;
import com.google.api.services.tasks.model.Task;
import com.taskit.client.util.DateTimeUtil;
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
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class UpdateTaskActivity extends Activity {
	
	// user account
	private Account account;
	
	// access token
	private GoogleCredential credential;
	
	// id of the task to be updated
	private String taskId;
	
	// task to be updated
	private Task task;
	
	// calendar instance to get date and time
	private Calendar calendar = Calendar.getInstance();
	private DateTimeUtil dateTimeUtil = new DateTimeUtil();
	
	// priority of the task
	private int priority;
	private static final String[] OPTIONS = { "Low", "Medium", "High" };
	
	// handle message returned from tasks API
	private Handler handler;
	private ProgressDialog dialog;
	ProgressDialogUtil dialogUtil = new ProgressDialogUtil();
	
	private static final String ERROR_TAG = "NewTaskActivity.java";
	private static final String AUTH_TOKEN_TYPE = "oauth2:https://www.googleapis.com/auth/tasks";
	private static final String OPERATION_CANCLED_MESSAGE = "user denied access";
	private static final String AUTHENTICATOR_ERROR_MESSAGE
	= "authenticator met an error";
	private static final String IO_ERROR_MESSAGE = "Java IO error";
	
	private EditText nameText;
    private EditText descriptionText;
    private EditText locationText;
	
	private Button dateButton;
    private Button timeButton;
    private Button priorityButton;
    private Button cancelButton;
    private Button updateTaskButton;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task);
        
        nameText = (EditText) findViewById(R.id.update_name);
        descriptionText = (EditText) findViewById(R.id.update_desc);
        locationText = (EditText) findViewById(R.id.update_location);
        
        dateButton = (Button) findViewById(R.id.update_date_button);
        timeButton = (Button) findViewById(R.id.update_time_button);
        priorityButton = (Button) findViewById(R.id.update_priority_button);
        cancelButton = (Button) findViewById(R.id.update_cancel_button);
        updateTaskButton = (Button) findViewById(R.id.update_task_button);
        
        Bundle extras = getIntent().getExtras();
    	account = extras.getParcelable("account");
    	taskId = extras.getString("task id");
    	
    	AccountManager accountManager = 
				AccountManager.get(UpdateTaskActivity.this);
		accountManager.getAuthToken(account,
				AUTH_TOKEN_TYPE,
				null,
				UpdateTaskActivity.this, 
				new OnTokenAcquired(),
				null);
    	
    	handler = new Handler(new Handler.Callback() {
			
			public boolean handleMessage(Message msg) {
				if (msg.getData().containsKey("status")) {
					String text = msg.getData().getString("status");
					if (text.equals("load complete")) {
						
						dialogUtil.dismissProgressDialog(dialog,
								"Complete loading task");
						
						nameText.setText(task.getTitle());
						
						if (task.getDue() != null) {
							DateTime dateTime = task.getDue();
							calendar.setTimeInMillis(dateTime.getValue());
							String dateString = dateTimeUtil.getDateString(calendar);
							String timeString = dateTimeUtil.getTimeString(calendar);
							dateButton.setText(dateString);
							timeButton.setText(timeString);
						}
						
						if (task.getNotes() != null) {
							String note[] = task.getNotes().split("@#*");
							descriptionText.setText(note[2]);
							locationText.setText(note[1]);
							switch (Integer.valueOf(note[0])) {
								case 0: priority = 0;
										priorityButton
										.setText("Priority: " + OPTIONS[0]);
										break;
								case 1: priority = 1;
										priorityButton
										.setText("Priority: " + OPTIONS[1]);
										break;
								case 2: priority = 2;
										priorityButton
										.setText("Priority: " + OPTIONS[2]);
										break;
							}
						}
						
					} else if (text.equals("update complete")) {
						dialogUtil.dismissProgressDialog(dialog,
								"Complete updating task");
						finish();
					}
				} else {
					Log.e(ERROR_TAG, "fail to add task");
					dialogUtil.dismissProgressDialog(dialog, 
							"Fail Loading task");
				}
				return true;
			}
		});
    	
    	dateButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				pickDate();
			}
		});
        
        timeButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				pickTime();
			}
		});
        
        priorityButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				pickPriority();				
			}
		});
    	
    	cancelButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				finish();				
			}
		});
    	
    	updateTaskButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				final String name = nameText.getText().toString();
				final String description = descriptionText.getText().toString();
				final String location = locationText.getText().toString();
				final Long dateTime = calendar.getTimeInMillis();
				
				if (name == null || name.equals("")) {
					
					Toast.makeText(UpdateTaskActivity.this,
							"name cannot be empty", 
							Toast.LENGTH_SHORT).show();
					
				} else {
					
					dialog = new ProgressDialog(UpdateTaskActivity.this);
	    	        dialogUtil.setUpProgressDialog(dialog, "Updating task...");
	    	        dialog.show();
	    	        
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
	    	 				Boolean result = useTasksAPI.updateTask(credential,
	    	 						task.getId(),
	    	 						name,
	    	 						description,
	    	 						location,
	    	 						dateTime,
	    	 						priority);
	    	 				
	    	 				if (result == true) {
	    	 					sendMsg("update complete");
	    	 				} else {
	    	 					sendMsg("update fail");
	    	 				}
	    	 			}
	    	 			
	    	        }).start();
					
				}
				
			}
		});
    	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_update_task, menu);
        return true;
    }
    
    private void pickDate() {
    	
    	new DatePickerDialog(UpdateTaskActivity.this,
				dateListener,
				calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH))
		.show();
    	
    }
    
    private void pickTime() {
    	
		new TimePickerDialog(UpdateTaskActivity.this,
				timeListener,
				calendar.get(Calendar.HOUR_OF_DAY), 
				calendar.get(Calendar.MINUTE),
				true)
		.show();
    	
    }
    
    private void pickPriority() {
    	
    	new AlertDialog.Builder(UpdateTaskActivity.this)
    	.setTitle("Select Priority")
    	.setSingleChoiceItems(OPTIONS, priority, priorityListener)
    	.setPositiveButton("OK", dialogListener)
    	.create().show();
    	
    }
    
    DatePickerDialog.OnDateSetListener dateListener = 
    		new DatePickerDialog.OnDateSetListener() {
		
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			calendar.set(year, monthOfYear, dayOfMonth);
			
			String dateString = dateTimeUtil.getDateString(calendar);
			dateButton.setText(dateString);
		}
	};
    
	TimePickerDialog.OnTimeSetListener timeListener =
			new TimePickerDialog.OnTimeSetListener() {
		
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
			calendar.set(Calendar.MINUTE, minute);
			
			String timeString = dateTimeUtil.getTimeString(calendar);
			timeButton.setText(timeString);
		}
	};
	
	AlertDialog.OnClickListener priorityListener = 
			new DialogInterface.OnClickListener() {
				
		public void onClick(DialogInterface dialog, int which) {
			priority = which;
		}
	};
	
	AlertDialog.OnClickListener dialogListener =
			new DialogInterface.OnClickListener() {
				
		public void onClick(DialogInterface dialog, int which) {
			priorityButton.setText("Priority: " + OPTIONS[priority]);
		}
	};
	
	private class OnTokenAcquired implements AccountManagerCallback<Bundle> {    	
    	public void run(AccountManagerFuture<Bundle> future) {
    		
    		try {
    			
    			// get the result of the operation from the AccountManagerFuture
    	        Bundle bundle = future.getResult();
    	        String token = bundle.getString(AccountManager.KEY_AUTHTOKEN);
    	        
    	        credential = new GoogleCredential();
    	        credential.setAccessToken(token);
    	        
    	        // show dialog indicating that the application is adding the task
    	        dialog = new ProgressDialog(UpdateTaskActivity.this);
    	        dialogUtil.setUpProgressDialog(dialog, "Loading task...");
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
    	 				task = useTasksAPI.getTask(credential, taskId);
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

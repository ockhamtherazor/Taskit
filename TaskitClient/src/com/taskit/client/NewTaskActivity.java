package com.taskit.client;

import java.io.IOException;
import java.util.Calendar;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
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

public class NewTaskActivity extends Activity {

	// user's account
	private Account account;
	
	private String name;
	private String description;
	private String location;
	private Long dateTime;
	
	// calendar instance to get date and time
	private Calendar calendar = Calendar.getInstance();
	private DateTimeUtil dateTimeUtil = new DateTimeUtil();
	
	// priority of the task
	// by default it's low
	private int priority = 0;
	private static final String[] OPTIONS = { "Low", "Medium", "High" };
	
	private ProgressDialog dialog;
	ProgressDialogUtil dialogUtil = new ProgressDialogUtil();
	
	// handle message returned from tasks API
	private Handler handler;
	
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
    private Button addTaskButton;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        
        nameText = (EditText) findViewById(R.id.name);
        descriptionText = (EditText) findViewById(R.id.desc);
        locationText = (EditText) findViewById(R.id.location);
        
        dateButton = (Button) findViewById(R.id.date_button);
        timeButton = (Button) findViewById(R.id.time_button);
        priorityButton = (Button) findViewById(R.id.priority_button);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        addTaskButton = (Button) findViewById(R.id.add_task_button);
        
        // get user account from intent
        Bundle extras = getIntent().getExtras();
    	account = extras.getParcelable("account");
        
    	handler = new Handler(new Handler.Callback() {
			
			public boolean handleMessage(Message msg) {
				if (msg.getData().containsKey("status")) {
					String text = msg.getData().getString("status");
					if (text.equals("add complete")) {
						dialogUtil.dismissProgressDialog(dialog,
								"Completed adding task");
						finish();
					}
				} else {
					Log.e(ERROR_TAG, "fail to add task");
					dialogUtil.dismissProgressDialog(dialog, 
							"Fail adding task");
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
    	
    	addTaskButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				name = nameText.getText().toString();
				description = descriptionText.getText().toString();
				location = locationText.getText().toString();
				dateTime = calendar.getTimeInMillis();
				
				if (name == null || name.equals("")) {
					
					Toast.makeText(NewTaskActivity.this, 
							"name cannot be empty", 
							Toast.LENGTH_SHORT).show();
					
				} else {
					
					AccountManager accountManager = 
							AccountManager.get(NewTaskActivity.this);
					accountManager.getAuthToken(account,
							AUTH_TOKEN_TYPE,
							null,
							NewTaskActivity.this, 
							new OnTokenAcquired(),
							null);
					
				}
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_new_task, menu);
        return true;
    }
    
    private void pickDate() {
    	
    	new DatePickerDialog(NewTaskActivity.this,
				dateListener,
				calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH))
		.show();
    	
    }
    
    private void pickTime() {
    	
		new TimePickerDialog(NewTaskActivity.this,
				timeListener,
				calendar.get(Calendar.HOUR_OF_DAY), 
				calendar.get(Calendar.MINUTE),
				true)
		.show();
    	
    }
    
    private void pickPriority() {
    	
    	new AlertDialog.Builder(NewTaskActivity.this)
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
    	        
    	        final GoogleCredential credential = new GoogleCredential();
    	        credential.setAccessToken(token);
    	        
    	        // show dialog indicating that the application is adding the task
    	        dialog = new ProgressDialog(NewTaskActivity.this);
    	        dialogUtil.setUpProgressDialog(dialog, "Adding task...");
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
    	 				Boolean result = useTasksAPI.addNewTask(credential,
    	 						name,
    	 						description,
    	 						location,
    	 						dateTime,
    	 						priority);
    	 				if (result == true) {
    	 					sendMsg("add complete");
    	 				} else {
    	 					sendMsg("add fail");
    	 				}
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

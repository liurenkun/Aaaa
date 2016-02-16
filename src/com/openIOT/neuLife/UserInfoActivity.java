package com.openIOT.neuLife;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.SurfaceHolder;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.openIOT.neuLife.EditTextWithDate;
import com.openIOT.neuLife.R;


public class UserInfoActivity extends Activity 
{
	private Button startButton;
	private EditText firstName, lastName, height, weight;
	private EditTextWithDate birthday;
	private Calendar calendar;
	private int year, month, day;
	private Switch gender; 
	private String firstNameValue, lastNameValue, heightValue, weightValue, genderValue, heightUnit, weightUnit, emailValue, uidValue, birthdayValue;  
    private SharedPreferences spEmail, sp; 
    private static final String[] heightSpinnerValue={"cm","inch"};
    private static final String[] weightSpinnerValue={"kg","pound"};
    private Spinner heightSpinner, weightSpinner;
    private ArrayAdapter<String> heightAdapter, weightAdapter;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        
        initUI();
        //read the email account
        spEmail = getSharedPreferences("SignUpInfo", Context.MODE_WORLD_READABLE);  
		emailValue = spEmail.getString("EMAIL", "");
		uidValue = spEmail.getString("UID", "");
		
		if(emailValue.equals("")||uidValue.equals(""))
		{
			Toast.makeText(UserInfoActivity.this, "system cannot read email from local database", Toast.LENGTH_SHORT).show();  
            finish();
		}
		else
		{
			Toast.makeText(UserInfoActivity.this, emailValue, Toast.LENGTH_LONG).show();
		}

		//set the user info dedicated for this email account
        sp = this.getSharedPreferences(emailValue + "_userInfo", Context.MODE_WORLD_READABLE);  
        firstName = (EditText) findViewById(R.id.firstNameInput);  
        lastName = (EditText) findViewById(R.id.lastNameInput);  
        height = (EditText) findViewById(R.id.heightInput); 
        weight = (EditText) findViewById(R.id.weightInput);
        birthday = (EditTextWithDate) findViewById(R.id.birthdayInput);
        gender = (Switch) findViewById(R.id.gender);
        heightSpinner = (Spinner) findViewById(R.id.spinner_height);
        weightSpinner = (Spinner) findViewById(R.id.spinner_weight);
        calendar = Calendar.getInstance();
        
        setHeightSpinner();
        setWeightSpinner();
    }
	
	public void setHeightSpinner()
	{
        heightAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, heightSpinnerValue);
        heightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        heightSpinner.setAdapter(heightAdapter);
        heightSpinner.setOnItemSelectedListener(new SpinnerItemSelectedListener());
        heightSpinner.setVisibility(View.VISIBLE);
        //heightUnit = heightAdapter.toString(); 
	}
	
	private class  SpinnerItemSelectedListener implements OnItemSelectedListener
	{          
        @Override  
        public void onItemSelected(AdapterView<?> adapter,View view,int position,long id) 
        {  
            String sInfo=adapter.getItemAtPosition(position).toString();
            if(sInfo.equals(heightSpinnerValue[0]) || sInfo.equals(heightSpinnerValue[1]))
            {
            	heightUnit = sInfo;
            }
            else if(sInfo.equals(weightSpinnerValue[0]) || sInfo.equals(weightSpinnerValue[1]))
            {
            	weightUnit = sInfo;
            }
            else
            {
            	Toast.makeText(UserInfoActivity.this, "System Error! List doesn't exist!", Toast.LENGTH_SHORT).show();  
                return; 
            }
        }

		@Override
		public void onNothingSelected(AdapterView<?> arg0) 
		{
			// TODO Auto-generated method stub
		}    
    } 
	
	public void setWeightSpinner()
	{
        weightAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, weightSpinnerValue);
        weightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weightSpinner.setAdapter(weightAdapter);
        weightSpinner.setOnItemSelectedListener(new SpinnerItemSelectedListener());
        weightSpinner.setVisibility(View.VISIBLE);
        //weightUnit = weightAdapter.getContext().toString();  
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) 
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void initUI()
    {
    	startButton = (Button)findViewById(R.id.start);
    	startButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{
				//save the user info, send it to server,verify user name, start the use page
            	firstNameValue = firstName.getText().toString();  
            	lastNameValue = lastName.getText().toString();
            	heightValue = height.getText().toString();
            	weightValue = weight.getText().toString();
            	genderValue = gender.getText().toString();
            	birthdayValue = birthday.getText().toString();
            	
            	if(firstNameValue.isEmpty())
            	{  
                    Toast.makeText(UserInfoActivity.this, "First name shall not be empty", Toast.LENGTH_SHORT).show();  
                    return;  
                }
            	
            	// get the date of today
            	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); 
            	String date=sdf.format(new java.util.Date()); 
            	
            	// birthday cannot be beyond today
            	if(!birthdayValue.isEmpty() && birthdayValue.compareTo(date) >= 0)
            	{
            		Toast.makeText(UserInfoActivity.this, "Oh my God, you are from the future :D", Toast.LENGTH_SHORT).show();  
                    return; 
            	}
            	
            	//check network connection
            	
            	//send user info to server(uid, user name, age, height, weight, height unit, weight unit)
            	
            	//listen to server response
            	int serverFeedBack = 1; //0: OK; 1: FAIL
            	
            	
            	
            	switch(serverFeedBack)
            	{
            	case 1:
            		//save user info locally
            		Toast.makeText(UserInfoActivity.this, "Your information is saved. Welcome to join ur.life", Toast.LENGTH_SHORT).show();
            	
            		Editor editor = sp.edit();  
            		editor.putString("FIRST_NAME", firstNameValue);  
            		editor.putString("LAST_NAME",lastNameValue);
            		editor.putString("BIRTHDAY",birthdayValue);
            		editor.putString("GENDER",genderValue);
            		editor.putString("HEIGHT",heightValue);
            		editor.putString("WEIGHT",weightValue);
            		editor.putString("HEIGHT_UNIT",heightUnit);
            		editor.putString("WEIGHT_UNIT",weightUnit);
            		editor.commit();  
            		//go to use page

            		break;
            	case 0:
            		Toast.makeText(UserInfoActivity.this,"Fail to save your information, please try again.", Toast.LENGTH_SHORT).show();
					return;
					
				default:
					return;
            	}
			}
		});
		
    }
    
}
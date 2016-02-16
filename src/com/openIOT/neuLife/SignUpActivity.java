package com.openIOT.neuLife;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.SurfaceHolder;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.SurfaceView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.amazonaws.regions.Regions;
import com.openIOT.neuLife.R;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;


public class SignUpActivity extends Activity 
{

	private Button submitButton, backButton;
	private EditText email, password;  
	private CheckBox agreement; 
	private String emailValue,passwordValue, token, uid, code;  
    private SharedPreferences sp; 
    private boolean status;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        
        initUI();
        
        sp = this.getSharedPreferences("SignUpInfo", Context.MODE_WORLD_READABLE);  
        email = (EditText) findViewById(R.id.emailInput);  
        password = (EditText) findViewById(R.id.passwordInput);  
        agreement = (CheckBox) findViewById(R.id.agreement);
 
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
    	backButton = (Button)findViewById(R.id.back);
    	backButton.setOnClickListener(new OnClickListener()
		{
    		public void onClick(View v) 
			{
    			finish();
    			/*
				Intent intentSignUp = new Intent();
				intentSignUp.setClass(SignUpActivity.this, MainActivity.class);
				startActivity(intentSignUp);	
				*/		         
			}
		});
    	
    	submitButton = (Button)findViewById(R.id.submit);
        submitButton.setOnClickListener(new OnClickListener()
        {
        	public void onClick(View v) 
        	{
        		//save the user info, send it to server, get the token, and use token to login user info page
        		//read the input email and password
                if(agreement.isChecked())  
                { 
                	emailValue = email.getText().toString();  
                	passwordValue = password.getText().toString(); 
                	
                	//check email address input is not empty
                	if(emailValue.isEmpty())
                	{  
                        Toast.makeText(SignUpActivity.this, "email address shall not be empty", Toast.LENGTH_SHORT).show();  
                        return;  
                    }  
                	
                	//check email address is legal
                	String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
                	Pattern p = Pattern.compile(str);
                	Matcher m = p.matcher(emailValue);
                	if(m.matches() == false)
                	{  
                        Toast.makeText(SignUpActivity.this, "email address is illegal", Toast.LENGTH_SHORT).show();  
                        return;  
                    }  
                	
                	//check password length is long enough
                	if(passwordValue.isEmpty())
                	{  
                        Toast.makeText(SignUpActivity.this, "password shall be longer than 6 words", Toast.LENGTH_SHORT).show();  
                        return;  
                    }
                	               	
                	//send email and password to server 	
                	//listen to server response

					// Create an instance of CognitoCachingCredentialsProvider
					CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
							getApplicationContext(),
							"us-east-1:cf2244e6-e58d-48ad-93fc-107823887999",  // Identity Pool ID
							Regions.US_EAST_1  // Region
					);

					// Create a LambdaInvokerFactory, to be used to instantiate the Lambda proxy
					LambdaInvokerFactory factory = new LambdaInvokerFactory(
							SignUpActivity.this.getApplicationContext(),
							Regions.US_EAST_1,
							credentialsProvider);

					// Create the Lambda proxy object with default Json data binder.
					// You can provide your own data binder by implementing
					// LambdaDataBinder
					final MyInterface myInterface = factory.build(MyInterface.class);

					RequestClass input = new RequestClass("signup");
					input.addDataEntry("email", emailValue);
					input.addDataEntry("password", passwordValue);

					// The Lambda function invocation results in a network call
					// Make sure it is not called from the main thread

					new AsyncTask<RequestClass, Void, ResponseClass>() {
						@Override
						protected ResponseClass doInBackground(RequestClass... params) {
							// invoke "echo" method. In case it fails, it will throw a
							// LambdaFunctionException.
							try {
								return myInterface.Auth(params[0]);
							} catch (LambdaFunctionException lfe) {
								Log.e("Tag", "Failed to invoke echo", lfe);
								return null;
							}
						}

						@Override
						protected void onPostExecute(ResponseClass result) {
							if (result == null) {
								return;
							}
							
							status = result.getOk();
							 
							if (!status)
							{
								//get error code and return
								code = result.getDataEntry("code");
								
	        					Toast.makeText(SignUpActivity.this, code, Toast.LENGTH_SHORT).show();
	        					return;
							}
							else
							{
								//get uid and token from the server feed back message
								uid = result.getDataEntry("id");
								token = result.getDataEntry("token");
								
								//sign up successfully
								Toast.makeText(SignUpActivity.this,"Sign Up Successfully", Toast.LENGTH_SHORT).show();   
								 
	        					//save the email and password 
	        					Editor editor = sp.edit();
	        					editor.putString("UID", uid);
	        					editor.putString("EMAIL", emailValue);  
	        					editor.putString("PASSWORD",passwordValue);
	        					editor.putString("TOKEN",token);
	        					editor.commit();  
	               
	        					// log in user info page
	        					Intent intentUserInfo = new Intent();
	        					intentUserInfo.setClass(SignUpActivity.this, UserInfoActivity.class);
	        					startActivity(intentUserInfo);
	        					//terminate this activity, so back button cannot lead us here again.
	        					SignUpActivity.this.finish();

							}

							// Do a toast
							Toast.makeText(SignUpActivity.this, "result ok? " + result.getOk(), Toast.LENGTH_LONG).show();
						}
					}.execute(input);

/**
 					**********************************************
					//enable this block when debug without network
					**********************************************
					int serverFeedBack = 1;
        			switch(serverFeedBack)  
        			{  
        				case 1:
        					Toast.makeText(SignUpActivity.this,"Sign Up Successfully", Toast.LENGTH_SHORT).show();   
 
        					//save the email and password 
        					Editor editor = sp.edit();
        					editor.putString("UID", uid);
        					editor.putString("EMAIL", emailValue);  
        					editor.putString("PASSWORD",passwordValue);
        					editor.putString("TOKEN",token);
        					editor.commit();  
               
        					// log in user info page
        					Intent intentUserInfo = new Intent();
        					intentUserInfo.setClass(SignUpActivity.this, UserInfoActivity.class);
        					startActivity(intentUserInfo);
        					//terminate this activity, so back button cannot lead us here again.
        					SignUpActivity.this.finish();
        					break;
        					
        				case 0:
        					Toast.makeText(SignUpActivity.this, code, Toast.LENGTH_SHORT).show();
        					return;
        					
        				default:
        					return;
        			}
**/
        		}
                else
                {
                	Toast.makeText(SignUpActivity.this,"Please Check The Agreement", Toast.LENGTH_SHORT).show(); 
                }
        	}
        });
    }
     
}
    

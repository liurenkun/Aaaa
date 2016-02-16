package com.openIOT.neuLife;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.openIOT.neuLife.R;


public class MainActivity extends Activity 
{
	
	private Button signInButton, signUpButton;
	public final String appName = "NeuLife";
	private SharedPreferences spToken;
	private String tokenValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // check first start of the app
        SharedPreferences setting = getSharedPreferences("FirstStart", 0);  
        boolean user_first = setting.getBoolean("FIRST",true);  
        if(user_first)
        {  
        	setting.edit().putBoolean("FIRST", false).commit();
        	//if this's its first start, go to welcome pages
        	Intent welcome = new Intent();
			welcome.setClass(MainActivity.this, WelcomeActivity.class);
			startActivity(welcome);
        }
              
        if(hasToken())
        {
        	//go to sign in page
        	initUI();//when sign in page is finished, remove this
        	//Toast.makeText(MainActivity.this,"token is" + tokenValue, Toast.LENGTH_SHORT).show();
        }
        else
        {
        	initUI();
        }
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
    
    /** 
     * initial the welcome page
     */
    private void initUI()
    {
    	//when sign up button is pressed, jump to sign up page
    	signUpButton = (Button)findViewById(R.id.sign_up);
    	signUpButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{
				Intent intentSignUp = new Intent();
				intentSignUp.setClass(MainActivity.this, SignUpActivity.class);
				startActivity(intentSignUp);			         
			}
		});
		
    	//when sign in button is pressed, jump to sign in page
		signInButton = (Button)findViewById(R.id.sign_in);
		signInButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{
				//do something
				         
			}
		});

    }
    
    /** 
     * check the App is fully installed or not
     */
    private boolean appInstalled(Context context, String appName) 
    {  
        PackageManager packageManager = context.getPackageManager();  
        boolean installed = false;  
        try {  
        	packageManager.getPackageInfo(appName, PackageManager.GET_ACTIVITIES);  
            installed = true;  
        } catch (PackageManager.NameNotFoundException e) {  
            installed = false;  
        }  
        return installed;  
    } 
    
    /** 
     * check the token exists or not
     */
    private boolean hasToken()
    {
        spToken = getSharedPreferences("SignUpInfo", Context.MODE_WORLD_READABLE);  
		tokenValue = spToken.getString("TOKEN", "");
		if(tokenValue.equals(""))
		{
			return false;
		}
    	return true;
    }
    
    /** 
     * catch back press
     */ 
    @Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {  
            ExitDialog(MainActivity.this).show();  
            return true;  
        }  
          
        return super.onKeyDown(keyCode, event);  
    } 
    
    /** 
     * system exit confirmation 
     * @param context 
     * @return 
     */  
    public Dialog ExitDialog(Context context) {  
        AlertDialog.Builder builder = new AlertDialog.Builder(context);  
        builder.setIcon(R.drawable.ic_launcher);  
        builder.setTitle("System Info");  
        builder.setMessage("Are you sure to exit NeuLife?");  
        builder.setPositiveButton("Exit",  
                new DialogInterface.OnClickListener() {  
                    public void onClick(DialogInterface dialog, int whichButton) {           
                        finish();  
                    }  
                });  
        builder.setNegativeButton("Cancle",  
                new DialogInterface.OnClickListener() {  
                    public void onClick(DialogInterface dialog, int whichButton) {  
                    }  
                });  
        return builder.create();  
    }  

}

package com.openIOT.neuLife;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.openIOT.neuLife.R;


public class WelcomeActivity extends Activity 
{
	    private ViewPager viewPager;
	    private ArrayList<View> pageViews;    
	    private ImageView imageView;
	    private ImageView[] imageViews;
	    private ViewGroup viewPics;
	    private ViewGroup viewPoints;
	     
	    /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) 
	    {
	        super.onCreate(savedInstanceState);
	         
	        //put the view into list
	        LayoutInflater inflater = getLayoutInflater();
	        pageViews = new ArrayList<View>();
	        pageViews.add(inflater.inflate(R.layout.welcome1, null));
	        pageViews.add(inflater.inflate(R.layout.welcome2, null));
	        pageViews.add(inflater.inflate(R.layout.welcome3, null));

	        imageViews = new ImageView[pageViews.size()];
	        viewPics = (ViewGroup) inflater.inflate(R.layout.activity_welcome, null);
	         

	        viewPoints = (ViewGroup) viewPics.findViewById(R.id.viewGroup);
	        viewPager = (ViewPager) viewPics.findViewById(R.id.guidePages);
	         

	        for(int i=0;i<pageViews.size();i++)
	        {
	            imageView = new ImageView(WelcomeActivity.this);
	            imageView.setLayoutParams(new LayoutParams(20,20));
	            imageView.setPadding(20, 0, 20, 0);
	            imageViews[i] = imageView;
	             
	            if(i==0)
	            {
	                imageViews[i].setBackgroundResource(R.drawable.page_indicator_focused);
	            }
	            else
	            {
	                imageViews[i].setBackgroundResource(R.drawable.page_indicator);
	            }
	             
	            viewPoints.addView(imageViews[i]);
	        }
	         
	        setContentView(viewPics);
	         
	        viewPager.setAdapter(new GuidePageAdapter());
	        viewPager.setOnPageChangeListener(new GuidePageChangeListener());        
	    }
	     
	    private Button.OnClickListener  Button_OnClickListener = new Button.OnClickListener() 
	    {
	        public void onClick(View v) 
	        {         
	            //Intent mainIntent = new Intent();
	            //mainIntent.setClass(WelcomeActivity.this, MainActivity.class);
	            //WelcomeActivity.this.startActivity(mainIntent);
	            WelcomeActivity.this.finish();
	        }
	    };

	    class GuidePageAdapter extends PagerAdapter
	    {
	 
	    	@Override
	    	public void destroyItem(View v, int position, Object arg2) 
	    	{
	    		// TODO Auto-generated method stub
	    		((ViewPager)v).removeView(pageViews.get(position));
	             
	    	}
	 
	        @Override
	        public void finishUpdate(View arg0) 
	        {
	            // TODO Auto-generated method stub	             
	        }
	         
	        //obtain the current page number
	        @Override
	        public int getCount() 
	        {
	            // TODO Auto-generated method stub
	            return pageViews.size();
	        }
	 
	        //initialize the position
	        @Override
	        public Object instantiateItem(View v, int position) 
	        {
	            // TODO Auto-generated method stub
	            ((ViewPager) v).addView(pageViews.get(position));  
	             
	             // set the button on click listener in page 3  
	            if (position == 2) 
	            {  
	                Button btn = (Button) v.findViewById(R.id.button);  
	                btn.setOnClickListener(Button_OnClickListener);  
	            }  
	             
	            return pageViews.get(position);  
	        }
	 
	        @Override
	        public boolean isViewFromObject(View v, Object arg1) 
	        {
	            // TODO Auto-generated method stub
	            return v == arg1;
	        }
	 
	 
	 
	        @Override
	        public void startUpdate(View arg0)
	        {
	            // TODO Auto-generated method stub
	             
	        }
	 
	        @Override
	        public int getItemPosition(Object object) 
	        {
	            // TODO Auto-generated method stub
	            return super.getItemPosition(object);
	        }
	 
	        @Override
	        public void restoreState(Parcelable arg0, ClassLoader arg1) 
	        {
	            // TODO Auto-generated method stub
	             
	        }
	 
	        @Override
	        public Parcelable saveState() {
	            // TODO Auto-generated method stub
	            return null;
	        }
	    }
	     
	     
	    class GuidePageChangeListener implements OnPageChangeListener
	    {
	 
	        @Override
	        public void onPageScrollStateChanged(int arg0) 
	        {
	            // TODO Auto-generated method stub
	             
	        }
	 
	        @Override
	        public void onPageScrolled(int arg0, float arg1, int arg2)
	        {
	            // TODO Auto-generated method stub
	             
	        }
	 
	        @Override
	        public void onPageSelected(int position) 
	        {
	            // TODO Auto-generated method stub
	            for(int i=0;i<imageViews.length;i++)
	            {
	                imageViews[position].setBackgroundResource(R.drawable.page_indicator_focused);
	                if(position !=i)
	                {
	                    imageViews[i].setBackgroundResource(R.drawable.page_indicator);
	                }
	            }
	             
	        }
	    }
}
	
package group14.tue.nl;

//import kankan.wheel.R;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.LinearLayout;


public class ThermostatActivity extends Activity
{
    Handler clock;
    public static int day; //0 to 6
    public static int minute; //0 to 24 * 60
    public static boolean override;

    private Double[] temperatures;
    Handler heater;
    double dayTemp = 22;
    double nightTemp = 18;
    double actualTemp = 18;
    
    Pin activePin;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setUpWheels();
        setUpTempSelectors();
        setUpTimeLines();
        setUpOverrideEvents();
        setUpHelpMenu();
        
        Calendar cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_WEEK) - 1;
        minute = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);
        
        clock = new Handler();
        clock.postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				minute++;
				
				if(minute >= 24 * 60)
				{
					minute = 0;
					day++;
					if(day == 7)
					{
						day = 0;
					}
				}
				
				timeUpdated();
				
				clock.postDelayed(this, 200);
			}
		}, 200);

        startActualTemperatureTransition();
    }
    void setUpHelpMenu(){
    	final ImageButton helpButton = (ImageButton)findViewById(R.id.helpButton);
  
    	helpButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Log.v("test", "test");
				onHelpClick();
				
				
}
		});      

    }
    void onHelpClick(){

    	final Dialog helpDialog = new Dialog(this);

    	helpDialog.setContentView(R.layout.helpdialog);
    	helpDialog.setTitle("Help Information");
    	Button returnButton = (Button)helpDialog.findViewById(R.id.returnToApp);
		returnButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				helpDialog.dismiss();
				
			
			}
		});
    	helpDialog.show();
    	
    }
    void setUpTempSelectors(){
        final TextView dayTempDisp = (TextView)findViewById(R.id.dayDisp);
    	final TextView nightTempDisp = (TextView)findViewById(R.id.nightDisp);
		final TextView nightTempButtonInc = (TextView) findViewById(R.id.nightDec);
    	final TextView nightTempButtonDec = (TextView)findViewById(R.id.nightTempDec);
    	final TextView dayTempButtonInc = (TextView)findViewById(R.id.dayTempInc);
    	final TextView dayTempButtonDec = (TextView)findViewById(R.id.dayTempDec);

    	
    	
    	nightTempDisp.setText(Double.toString(nightTemp)+" \u2103"); 
    	
    	dayTempDisp.setText(Double.toString(dayTemp)+" \u2103");


    	nightTempButtonInc.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				nightTemp += 0.1;
				nightTemp = Math.round(nightTemp*10);
				nightTemp = nightTemp/10;
				
				
				nightTempDisp.setText(Double.toString(nightTemp)+" \u2103");
}
		});        
    	
    	nightTempButtonDec.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				nightTemp -= 0.1;
				nightTemp = Math.round(nightTemp*10);
				nightTemp = nightTemp/10;
				
				
				nightTempDisp.setText(Double.toString(nightTemp)+" \u2103");
}
		});    
    	
    	dayTempButtonInc.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				dayTemp += 0.1;
				dayTemp = Math.round(dayTemp*10);
				dayTemp = dayTemp/10;
				
				
				dayTempDisp.setText(Double.toString(dayTemp)+" \u2103");
}
		});  
    	dayTempButtonDec.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				dayTemp -= 0.1;
				dayTemp = Math.round(dayTemp*10);
				dayTemp = dayTemp/10;
				
				
				dayTempDisp.setText(Double.toString(dayTemp)+" \u2103");
}
		}); 
    	
    	
    	
    	
    }
    void setUpWheels()
    {
        final WheelView tempWheel = (WheelView)findViewById(R.id.tempWheel);
        double mintemp, maxtemp, increment;
               
        mintemp = 5;
        maxtemp = 30.1;
        increment = 0.1;
        
        temperatures = new Double[(int)((maxtemp-mintemp)/increment)];
        for(int i = 0; mintemp <= maxtemp; mintemp = (mintemp+increment)){
        	temperatures[i] = (double)Math.round(mintemp*10)/10;
        	i++;
        }
        tempWheel.setViewAdapter(new ArrayWheelAdapter<Double>(this, temperatures ));
        tempWheel.setCurrentItem(170);

        tempWheel.addScrollingListener( new OnWheelScrollListener()
        {
        	@Override
            public void onScrollingStarted(WheelView tempWheel){}
            @Override
            public void onScrollingFinished(WheelView tempWheel)
            {
                startActualTemperatureTransition();
            }
        });
        
    }
    void setUpTimeLines()
    {
    	((TimeLineView)findViewById(R.id.timeLineView7)).dayIndex = 0;
    	((TimeLineView)findViewById(R.id.timeLineView1)).dayIndex = 1;
    	((TimeLineView)findViewById(R.id.timeLineView2)).dayIndex = 2;
    	((TimeLineView)findViewById(R.id.timeLineView3)).dayIndex = 3;
    	((TimeLineView)findViewById(R.id.timeLineView4)).dayIndex = 4;
    	((TimeLineView)findViewById(R.id.timeLineView5)).dayIndex = 5;
    	((TimeLineView)findViewById(R.id.timeLineView6)).dayIndex = 6;
    }
    void setUpOverrideEvents()
    {
    	final WheelView tempWheel = (WheelView)findViewById(R.id.tempWheel);
        tempWheel.addScrollingListener(new OnWheelScrollListener()
        {
			@Override
			public void onScrollingStarted(WheelView wheel)
			{
				enableOverride();
			}
			
        	@Override
            public void onScrollingFinished(WheelView tempWheel){}
        });
        
        final Button disableOverrideButton = (Button)findViewById(R.id.disableOverrideButton);
        disableOverrideButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				disableOverride();
			}
		});
    
        final CheckBox vacationCb = (CheckBox)findViewById(R.id.vacationCb);
        vacationCb.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if(isChecked)
				{
					enableOverride();
					Toast.makeText(getApplicationContext(), "Select temperature in scroll wheel", 2000).show();
				}
				else
					disableOverride();
			}
		});
    }

    TimeLineView getActiveTimeLine()
    {
    	return getTimeLine(day);
    }
    TimeLineView getTimeLine(int dayIndex)
    {
    	switch(dayIndex)
    	{
    	case 0: return (TimeLineView)findViewById(R.id.timeLineView7); //SUNDAY
    	case 1: return (TimeLineView)findViewById(R.id.timeLineView1);
    	case 2: return (TimeLineView)findViewById(R.id.timeLineView2);
    	case 3: return (TimeLineView)findViewById(R.id.timeLineView3);
    	case 4: return (TimeLineView)findViewById(R.id.timeLineView4);
    	case 5: return (TimeLineView)findViewById(R.id.timeLineView5);
    	case 6: return (TimeLineView)findViewById(R.id.timeLineView6); //SATURDAY
    	}
    	
    	return null;
    }
    
    void timeUpdated()
    {
    	final TextView actualTimeText = (TextView)findViewById(R.id.actualTimeText);
    	int actualHour = minute / 60;
    	int actualMinute = minute % 60;
    	DecimalFormat df = new DecimalFormat("00");
    	actualTimeText.setText(df.format(actualHour) + ":" + df.format(actualMinute));

    	getActiveTimeLine().invalidate();
    	
    	if(!vacationModeActive())
    	{
	    	Pin p = getActiveTimeLine().getPin(minute);
	    	if(activePin != p && (activePin = p) != null)
	    	{
	    		//Temperature change
    			if(override)
    				disableOverride();
    			
    			setTargetTemperature(p.day ? dayTemp : nightTemp);
    			startActualTemperatureTransition();
	    	}
    	}
    }

    boolean vacationModeActive()
    {
    	return ((CheckBox)findViewById(R.id.vacationCb)).isChecked();
    }
    
    double getTargetTemperature()
    {
    	return temperatures[((WheelView)findViewById(R.id.tempWheel)).getCurrentItem()];
    }
    void setTargetTemperature(double temperature)
    {
		int index = 0;
		for(int i = 0; i < temperatures.length; i++)
		{
			if(temperatures[i] == temperature)
				index = i;
		}
		((WheelView)findViewById(R.id.tempWheel)).setCurrentItem(index);
    }
    
    double getActualTemperature()
    {
    	return actualTemp;
    }
    void setActualTemperature(double temp)
    {
    	actualTemp = temp;
    	DecimalFormat df = new DecimalFormat("#.0");
    	String text = df.format(actualTemp) + " \u2103";
    	((TextView)findViewById(R.id.actualTempText)).setText(text);
    }
    
    void startActualTemperatureTransition()
    {
    	if(heater != null || getTargetTemperature() == getActualTemperature())
    		return;
    	
        heater = new Handler();
    	heater.postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				if(getTargetTemperature() > getActualTemperature())
				{
					setActualTemperature(getActualTemperature() + 0.1);
					
					if(getTargetTemperature() > getActualTemperature())
						heater.postDelayed(this, 1000);
					else
						stop();
				}
				else if(getTargetTemperature() < getActualTemperature())
				{
					setActualTemperature(getActualTemperature() - 0.1);

					if(getTargetTemperature() < getActualTemperature())
						heater.postDelayed(this, 1000);
					else
						stop();
				}
			}
			
			void stop()
			{
				setActualTemperature(getTargetTemperature());
				heater = null;
			}
		}, 1000);
    }
    
    void enableOverride()
    {
    	((Button)findViewById(R.id.disableOverrideButton)).setVisibility(Button.VISIBLE);

    	String text = (String)getText(R.string.overrideEnabled);
    	if(vacationModeActive())
    		text += " (vacation mode enabled)";
    	else
    		text += " (until next change on week program)";
    	
    	TextView overrideText = (TextView)findViewById(R.id.overrideText);
    	overrideText.setText(text);
    	overrideText.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
    	
    	final LinearLayout timeLine = (LinearLayout)findViewById(R.id.timeLineLayout);
    	timeLine.setAlpha(0.2f);
    	for(int i = 0; i < 7; i++)
    	{
    		getTimeLine(i).setEnabled(false);
    	}
    	
    	override = true;
    }
    void disableOverride()
    {
    	((Button)findViewById(R.id.disableOverrideButton)).setVisibility(Button.GONE);

    	TextView overrideText = (TextView)findViewById(R.id.overrideText);
    	overrideText.setText(R.string.overrideDisabled);
    	overrideText.getLayoutParams().height = LayoutParams.FILL_PARENT;
    	
    	final LinearLayout timeLine = (LinearLayout)findViewById(R.id.timeLineLayout);
    	timeLine.setAlpha(1);
    	for(int i = 0; i < 7; i++)
    	{
    		getTimeLine(i).setEnabled(true);
    	}
    	
    	((CheckBox)findViewById(R.id.vacationCb)).setChecked(false);
    	
    	if(activePin != null)
    	{
    		setTargetTemperature(activePin.day ? dayTemp : nightTemp);
    		startActualTemperatureTransition();
    	}
    	
    	override = false;
    }
}

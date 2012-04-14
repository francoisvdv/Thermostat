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
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.LinearLayout;


public class ThermostatActivity extends Activity
{
    Handler clock;
    public static int day; //0 to 6
    public static int minute; //0 to 59
    public static boolean override;
    
    private boolean scrolling = false;
    double dayTemp = 22;
    double nightTemp = 18;

    
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
        
        Calendar cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_WEEK) - 2;
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
    	final TextView currentTemp = (TextView)findViewById(R.id.actualTempText);
        final WheelView tempWheel = (WheelView)findViewById(R.id.tempWheel);
        double mintemp, maxtemp, increment;
               
        mintemp = 5;
        maxtemp = 30;
        increment = 0.1;
        
        final Double[] temperatures = new Double[(int)((maxtemp-mintemp)/increment)];
        for(int i = 0; mintemp <= maxtemp; mintemp = (mintemp+increment)){
        	temperatures[i] = (double)Math.round(mintemp*10)/10;
        	i++;
        }
        currentTemp.setText(""+temperatures[170] + " \u2103");
        tempWheel.setViewAdapter(new ArrayWheelAdapter<Double>(this, temperatures ));
        tempWheel.setCurrentItem(170);
        

        
        tempWheel.addChangingListener(new OnWheelChangedListener() 
        {
        	@Override
			public void onChanged(WheelView tempWheel, int oldValue, int newValue)
			{
			    if (scrolling)
			    {
			        currentTemp.setText(Double.toString(temperatures[newValue])+" \u2103");
			    }
			}
		});
        
        tempWheel.addScrollingListener( new OnWheelScrollListener()
        {
        	@Override
            public void onScrollingStarted(WheelView tempWheel)
            {
                scrolling = true;
            }
            @Override
            public void onScrollingFinished(WheelView tempWheel)
            {
                scrolling = false;
            }
        });
        
    }
    void setUpTimeLines()
    {
    	((TimeLineView)findViewById(R.id.timeLineView1)).dayIndex = 0;
    	((TimeLineView)findViewById(R.id.timeLineView2)).dayIndex = 1;
    	((TimeLineView)findViewById(R.id.timeLineView3)).dayIndex = 2;
    	((TimeLineView)findViewById(R.id.timeLineView4)).dayIndex = 3;
    	((TimeLineView)findViewById(R.id.timeLineView5)).dayIndex = 4;
    	((TimeLineView)findViewById(R.id.timeLineView6)).dayIndex = 5;
    	((TimeLineView)findViewById(R.id.timeLineView7)).dayIndex = 6;
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
    	case 0: return (TimeLineView)findViewById(R.id.timeLineView1);
    	case 1: return (TimeLineView)findViewById(R.id.timeLineView2);
    	case 2: return (TimeLineView)findViewById(R.id.timeLineView3);
    	case 3: return (TimeLineView)findViewById(R.id.timeLineView4);
    	case 4: return (TimeLineView)findViewById(R.id.timeLineView5);
    	case 5: return (TimeLineView)findViewById(R.id.timeLineView6);
    	case 6: return (TimeLineView)findViewById(R.id.timeLineView7);
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
    }

    boolean vacationModeActive()
    {
    	return ((CheckBox)findViewById(R.id.vacationCb)).isChecked();
    }
    
    void enableOverride()
    {
    	((Button)findViewById(R.id.disableOverrideButton)).setVisibility(Button.VISIBLE);

    	String text = (String)getText(R.string.overrideEnabled);
    	if(vacationModeActive())
    		text += " (VACATION MODE)";
    	else
    		text += " UNTIL NEXT PLANNED SWITCH";
    	
    	((TextView)findViewById(R.id.overrideText)).setText(text);
    	
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
    	((TextView)findViewById(R.id.overrideText)).setText(R.string.overrideDisabled);
    	
    	final LinearLayout timeLine = (LinearLayout)findViewById(R.id.timeLineLayout);
    	timeLine.setAlpha(1);
    	for(int i = 0; i < 7; i++)
    	{
    		getTimeLine(i).setEnabled(true);
    	}
    	
    	((CheckBox)findViewById(R.id.vacationCb)).setChecked(false);
    	
    	override = false;
    }
}
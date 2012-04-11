package group14.tue.nl;

//import kankan.wheel.R;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.*;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import group14.tue.nl.R;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


public class ThermostatActivity extends Activity
{
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
        final WheelView tempWheel = (WheelView)findViewById(R.id.wheelDayTemp1);
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
        
        tempWheel.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView tempWheel, int oldValue, int newValue) {
			    if (scrolling) {
			        currentTemp.setText(Double.toString(temperatures[newValue])+" \u2103");
			        
			    }
			}
		});
        
        tempWheel.addScrollingListener( new OnWheelScrollListener() {
            public void onScrollingStarted(WheelView tempWheel) {
                scrolling = true;
            }
            public void onScrollingFinished(WheelView tempWheel) {
                scrolling = false;
                
            }
        });
        
    }
    
}
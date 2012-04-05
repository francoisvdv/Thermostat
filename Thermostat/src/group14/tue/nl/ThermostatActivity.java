package group14.tue.nl;

import kankan.wheel.R;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
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
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        setUpWheels();
    }
    
    void setUpWheels()
    {
        WheelView wheel = (WheelView)findViewById(R.id.wheelDayTemp1);
        wheel.setVisibleItems(3);
        wheel.setViewAdapter(new NumericWheelAdapter(this, 5, 30));
        wheel.setCurrentItem(10);
        
        wheel = (WheelView)findViewById(R.id.wheelDayTemp2);
        wheel.setVisibleItems(3);
        wheel.setViewAdapter(new NumericWheelAdapter(this, 0, 9));
        wheel.setCurrentItem(8);
        
//        wheel = (WheelView)findViewById(R.id.wheelNightTemp1);
//        wheel.setVisibleItems(3);
//        wheel.setViewAdapter(new NumericWheelAdapter(this, 5, 30));
//        wheel.setCurrentItem(10);
//        
//        wheel = (WheelView)findViewById(R.id.wheelNightTemp2);
//        wheel.setVisibleItems(3);
//        wheel.setViewAdapter(new NumericWheelAdapter(this, 0, 9));
//        wheel.setCurrentItem(8);
    }
    void setupPicker(){
    	 DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker1);
    	 

    }
}
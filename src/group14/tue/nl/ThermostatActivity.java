package group14.tue.nl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class ThermostatActivity extends Activity {
	
	//Button add,sub,week;
	ImageView add,sub;
	Button week;
	TextView tvTemp;
	int counter = 20;
	SeekBar seekBar;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
//        add = (Button)findViewById(R.id.bAdd);
//        sub = (Button)findViewById(R.id.bSub);
//        add = (ImageView)findViewById(R.id.bAdd);
//        sub = (ImageView)findViewById(R.id.bSub);
//        week = (Button)findViewById(R.id.week);
//        tvTemp = (TextView)findViewById(R.id.tvTemp);
//        seekBar = (SeekBar)findViewById(R.id.seekBar1);
//        seekBar.setMax(40);
//        seekBar.setProgress(counter);
        
        week.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(v.getContext(), WeekOverview.class);
				startActivity(intent);
			}
		});        
        
        
        add.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				counter++;
				tvTemp.setText(""+counter+" \u2103");
				seekBar.setProgress(counter);
			}
		});
        
        sub.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				counter--;
				tvTemp.setText(""+counter+" \u2103");
				seekBar.setProgress(counter);
			}
		});     
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				tvTemp.setText(""+progress+" \u2103");
				
			}
		});
       
    }
}
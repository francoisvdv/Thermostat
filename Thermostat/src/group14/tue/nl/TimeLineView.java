package group14.tue.nl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.SortedMap;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class TimeLineView extends RelativeLayout implements OnLongClickListener, OnTouchListener
{
	public final ArrayList<Pin> pins = new ArrayList<Pin>();
	public final ArrayList<PinView> pinViews = new ArrayList<PinView>();
	
	public int dayIndex = 0;
	
	final int pinWidth = 30;
	final int pinHeight = 60;
	
	float mouseX;

	boolean startDay = false;
	
	TextView tv;

	public TimeLineView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub

		setWillNotDraw(false);
		setMinimumHeight(65);
		setOnTouchListener(this);
		setOnLongClickListener(this);

		pins.add(new Pin(0, startDay));
		rebuildPins();
	}
	
	public void rebuildPins()
	{
		for(PinView pv : pinViews)
		{
			((ViewGroup)pv.getParent()).removeView(pv);
		}
		pinViews.clear();
		
		Collections.sort(pins, new Comparator<Pin>()
		{
			@Override
            public int compare(Pin p1, Pin p2)
            {
                if (p1.time > p2.time){
                    return +1;
                }else if (p1.time < p2.time){
                    return -1;
                }else{
                    return 0;
                }
            }
        });
		
		boolean day = startDay;

		for(Pin p : pins)
		{
			p.day = day;
			
			float posX = timeToPosX(p.time);
			
			PinView pv = createPin(pins.indexOf(p) != 0);
			pv.pin = p;
			pv.setX(posX - pinWidth / 2);
			
			if(day)
				pv.setDay();
			else
				pv.setNight();
			
			day = !day;
			
			pinViews.add(pv);
			addView(pv);
		}
	}
	
	public PinView createPin(boolean removeOnHold)
	{
		final PinView pinView = new PinView(getContext(), null);
		pinView.setLayoutParams(new LayoutParams(pinWidth, pinHeight));
		if(removeOnHold)
		{
			pinView.setOnLongClickListener(new OnLongClickListener()
			{
				@Override
				public boolean onLongClick(View v)
				{
					pins.remove(pinView.pin);
					
					rebuildPins();
					return true;
				}
			});
		}
		else
		{
			pinView.setOnLongClickListener(new OnLongClickListener()
			{
				@Override
				public boolean onLongClick(View v)
				{
					startDay = !startDay;

					rebuildPins();
					return true;
				}
			});
		}
		return pinView;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		mouseX = event.getX();
		return false;
	}
	@Override
	public boolean onLongClick(View v)
	{
		Context mContext = getContext();
		
		if(pins.size() >= 11)
		{
			Toast msg = Toast.makeText(mContext, "You can only add up to 5 day-to-night and 5 night-to-day changes to a single day.", 3000);
			msg.show();
			return true;
		}

		final Dialog dialog = new Dialog(mContext);

		dialog.setContentView(R.layout.pindialog);
		dialog.setTitle("Pick Time");
		
		final WheelView wheel1 = (WheelView)dialog.findViewById(R.id.time1);
		final WheelView wheel2 = (WheelView)dialog.findViewById(R.id.time2);

		int time = posXToTime(mouseX);
		
		wheel1.setViewAdapter(new NumericWheelAdapter(mContext, 0, 23));
		wheel1.setCurrentItem(time / 60);
		
		wheel2.setViewAdapter(new NumericWheelAdapter(mContext, 0, 59));
		wheel2.setCurrentItem(time % 60);
		
		Button acceptBtn = (Button)dialog.findViewById(R.id.acceptBtn);
		acceptBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dialog.dismiss();
				
				pins.add(new Pin(wheel1.getCurrentItem() * 60 + wheel2.getCurrentItem(), true));
				rebuildPins();
			}
		});
		
		dialog.show();
		
		return true;
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		Paint paint = new Paint();
		
		for(Pin p : pins)
		{
			paint.setColor(p.day ? getDayColor() : getNightColor());
			
			int nextIndex = pins.indexOf(p) + 1;
			
			float nextX = 0;
			if(nextIndex >= pins.size())
				nextX = getWidth();
			else
				nextX = timeToPosX(pins.get(nextIndex).time);
			
			float posX = timeToPosX(p.time);
			canvas.drawRect(posX, 26, nextX, getHeight() - 5, paint);
		}
		
		if(ThermostatActivity.day == dayIndex)
		{
			paint.setColor(Color.WHITE);
			
			float posX = timeToPosX(ThermostatActivity.minute);
			canvas.drawLine(posX, 26, posX, getHeight() - 5, paint);
		}
	}
	
	float timeToPosX(int time)
	{
		float max = 24 * 60;
		
		float a = time / max;
		return a * getWidth();
	}
	int posXToTime(float posX)
	{
		float max = getWidth();
		
		float a = posX / max;
		return (int)(a * (24 * 60));
	}
	int getDayColor()
	{
		return Color.rgb(219, 117, 31);
	}
	int getNightColor()
	{
		return Color.rgb(33, 42, 63);
	}
}

package group14.tue.nl;

import java.util.ArrayList;
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
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class TimeLineView extends RelativeLayout implements OnLongClickListener
{
	public final ArrayList<Pin> pins = new ArrayList<Pin>();
	public final ArrayList<PinView> pinViews = new ArrayList<PinView>();
	
	final int pinWidth = 30;
	final int pinHeight = 60;
	
	TextView tv;

	public TimeLineView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub

		setWillNotDraw(false);
		setMinimumHeight(65);
		setOnLongClickListener(this);
		
		pins.add(new Pin(0, false));
		rebuildPins();
	}
	
	public void rebuildPins()
	{
		for(PinView p : pinViews)
		{
			removeView(p);
		}
		pinViews.clear();
		
		boolean night = true;
		
		for(Pin p : pins)
		{
			float posX = timeToPosX(p.time);
			
			PinView pv = createPin(pins.indexOf(p) != 0);
			pv.index = pins.indexOf(p);
			pv.setX(posX - pinWidth / 2);
			
			if(night)
				pv.setNight();
			else
				pv.setDay();
			
			addView(pv);
		}
	}
	
	public PinView createPin(boolean removeOnHold)
	{
		final PinView imgBtn = new PinView(getContext(), null);
		imgBtn.setLayoutParams(new LayoutParams(pinWidth, pinHeight));
		if(removeOnHold)
		{
			imgBtn.setOnLongClickListener(new OnLongClickListener()
			{
				@Override
				public boolean onLongClick(View v)
				{
					pins.remove(imgBtn.index);
					
					rebuildPins();
					return false;
				}
			});
		}
		return imgBtn;
	}
	
	@Override
	public boolean onLongClick(View v)
	{
		// TODO Auto-generated method stub
		
		Context mContext = getContext();
		final Dialog dialog = new Dialog(mContext);

		dialog.setContentView(R.layout.pindialog);

		final WheelView wheel1 = (WheelView)dialog.findViewById(R.id.time1);
		final WheelView wheel2 = (WheelView)dialog.findViewById(R.id.time2);
		
		wheel1.setViewAdapter(new NumericWheelAdapter(mContext, 0, 23));
		wheel1.setCurrentItem(17);
		
		wheel2.setViewAdapter(new NumericWheelAdapter(mContext, 0, 59));
		wheel2.setCurrentItem(0);
		
		Button acceptBtn = (Button)dialog.findViewById(R.id.acceptBtn);
		Button cancelBtn = (Button)dialog.findViewById(R.id.cancelBtn);
		
		acceptBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				pins.add(new Pin(wheel1.getCurrentItem() * 60 + wheel2.getCurrentItem(), !pins.get(pins.size() - 1).day));
				dialog.dismiss();
				
				rebuildPins();
			}
		});
		
		dialog.show();
		
		return false;
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
		
	}
	
	float timeToPosX(int time)
	{
		float max = 24 * 60;
		
		float a = time / max;
		return a * getWidth();
	}
	int getDayColor()
	{
		return Color.rgb(113, 128, 151);
	}
	int getNightColor()
	{
		return Color.rgb(33, 42, 63);
	}
}

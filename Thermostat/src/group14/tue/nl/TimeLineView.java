package group14.tue.nl;

import java.util.ArrayList;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
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
	public final ArrayList<Integer> minutes = new ArrayList<Integer>();
	public final ArrayList<PinView> pins = new ArrayList<PinView>();
	
	TextView tv;

	public TimeLineView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub

		setWillNotDraw(false);
		setMinimumHeight(65);
		setOnLongClickListener(this);
		
		
		PinView pin = createPin(false);
		pin.setNight();
		addView(pin);
	}
	
	public void rebuildPins()
	{
		for(PinView p : pins)
		{
			removeView(p);
		}
		pins.clear();
		
		for(int i : minutes)
		{
			float max = 24 * 60;
			
			float a = i / max;
			
			float posX = a * getWidth();
			
			PinView pv = createPin(true);
			pv.index = i;
			pv.setX(posX - pv.getWidth() / 2);
			addView(pv);
		}
	}
	
	public PinView createPin(boolean removeOnHold)
	{
		final PinView imgBtn = new PinView(getContext(), null);
		imgBtn.setLayoutParams(new LayoutParams(30, 60));
		if(removeOnHold)
		{
			imgBtn.setOnLongClickListener(new OnLongClickListener()
			{
				
				@Override
				public boolean onLongClick(View v)
				{
					// TODO Auto-generated method stub
					minutes.remove(imgBtn.index);
					
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
				minutes.add(wheel1.getCurrentItem() * 60 + wheel2.getCurrentItem());
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
	}
}

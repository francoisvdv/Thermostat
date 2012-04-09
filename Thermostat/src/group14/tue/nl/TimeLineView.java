package group14.tue.nl;

import android.content.Context;
import android.graphics.Canvas;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class TimeLineView extends LinearLayout
{
	
	
	public TimeLineView(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
		

	}
	
	public ImageButton createPin()
	{
		ImageButton imgBtn = new ImageButton(getContext());
		imgBtn.setOnDragListener(new OnDragListener()
		{
			@Override
			public boolean onDrag(View arg0, DragEvent arg1)
			{
				// TODO Auto-generated method stub
				return false;
			}
		});
		
		return imgBtn;
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		
	}
}

package group14.tue.nl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TableLayout.LayoutParams;

public class ShadedView extends View
{
	TimeLineView tlv;
	int dayColor;
	int nightColor;
	
	public ShadedView(Context context, AttributeSet attrs, TimeLineView tlv)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		this.tlv = tlv;
		dayColor = Color.GRAY;
		nightColor = Color.DKGRAY;
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		
	}
}

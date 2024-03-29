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

public class PinView extends View
{
	public int fillColor;
	public Pin pin;
	
	public PinView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		fillColor = Color.GRAY;
		
		setMinimumWidth(15);
		setMinimumHeight(30);
	}
	
	public void setDay()
	{
		fillColor = Color.rgb(219, 117, 31);
		invalidate();
	}
	public void setNight()
	{
		fillColor = Color.rgb(33, 42, 63);
		invalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		float radius = 0;
		float x = 0;
		float y = 0;
		float lineX1, lineX2 = 0;
		float lineY1, lineY2 = 0;
		
		if(getWidth() <= getHeight())
		{
			radius = getWidth() / 2f - 2;
			x = getWidth() / 2f;
			y = radius;
			lineX1 = lineX2 = x;
			lineY1 = y + radius;
			lineY2 = getBottom();
		}
		else
		{
			radius = getHeight() / 2f - 2;
			x = radius;
			y = getHeight() / 2f;
			lineX1 = x + radius;
			lineX2 = getRight();
			lineY1 = lineY2 = y;
		}

		Paint fillPaint = new Paint();
		fillPaint.setColor(this.fillColor);
		fillPaint.setStyle(Style.FILL);
		
		Paint strokePaint = new Paint();
		strokePaint.setColor(Color.BLACK);
		strokePaint.setStyle(Style.STROKE);
		strokePaint.setStrokeWidth(1);
		strokePaint.setAntiAlias(true);
		
		Path path = new Path();
		path.addCircle(x, y, radius, Path.Direction.CW);
		canvas.drawPath(path, fillPaint);
		canvas.drawPath(path, strokePaint);
		
		canvas.drawLine(lineX1, lineY1, lineX2, lineY2, strokePaint);
		
		
		int i = 0;
		canvas.drawText(getWidth() + " | " + getHeight(), i += 100, 0, fillPaint);
		
	}
}

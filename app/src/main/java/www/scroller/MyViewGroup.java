package www.scroller;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.widget.Toast;

/**
 * Created by uzzz on 13.11.14.
 */
public class MyViewGroup extends ViewGroup {

    private int mTouchSlop;
    private float mXdown, mYdown;
    private boolean slide_horizontal;
    private int oldxDiff;
    private Scroller mScroller;


    public MyViewGroup(Context context) {
        this(context, null);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View v1 = new View(getContext());
        View v2 = new View(getContext());
        v1.setBackgroundColor(Color.BLUE);
        v2.setBackgroundColor(Color.RED);

        this.addView(v1);
        this.addView(v2);

        ViewConfiguration vc = ViewConfiguration.get(context);
        mTouchSlop = vc.getScaledTouchSlop();
        mScroller = new Scroller(context);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int width = getChildAt(0).getMeasuredWidth();
        int height = getChildAt(0).getMeasuredHeight();

        getChildAt(0).layout(l, t, l + width, b);
        getChildAt(1).layout(l - width, t, l, b);
/*
        getChildAt(0).layout(0, 0, width/2, b + height);
        getChildAt(1).layout(width/2, 0, width, b + height);
*/
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        getChildAt(0).measure(widthMeasureSpec, heightMeasureSpec);
        getChildAt(1).measure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(width, height);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        final int action = ev.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mXdown = ev.getX();
                mYdown = ev.getY();
                oldxDiff = 0;
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev){

        final int action = ev.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_UP: {
                Log.e("Scrolling", String.valueOf("uppppp"));
                mXdown = 0;
                mYdown = 0;
                int x = (int)getX();
                View v = getChildAt(0);
                int middle = v.getWidth() / 2;
                if(x > middle) {
                    mScroller.startScroll(x, 0, 1000, 0, 1000);
                    HandleSlide handleSlide = new HandleSlide();
                }
                else {
                    mScroller.startScroll(x, 0, -1000, 0, 1000);
                }

                return false;
            }
            case MotionEvent.ACTION_MOVE: {

                final int xDiff = - (int) (mXdown - ev.getX());

                Log.e("Scrolling", String.valueOf(xDiff));

                final int yDiff = - (int) (mYdown - ev.getY());
                if (Math.abs(xDiff) > Math.abs(yDiff)) {
                    if (Math.abs(xDiff) > mTouchSlop) {
                        slide_horizontal = true;
                        Toast.makeText(getContext(), "Скролл", Toast.LENGTH_SHORT).show();
                        moveView(xDiff-oldxDiff);
                        oldxDiff = xDiff;
                        return true;
                    }
                }
                break;
            }
        }
        return true;
    }

    private void moveView(int slide) {
        View v0 = getChildAt(0);
        View v1 = getChildAt(1);
        v0.offsetLeftAndRight(slide);
        v1.offsetLeftAndRight(slide);
    }

    private class HandleSlide extends Handler {
        @Override
        public void handleMessage (Message msg) {

        }

    }



}

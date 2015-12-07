package com.angcyo.overscrollerdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.OverScroller;

/**
 * Created by angcyo on 2015/12/7 13:06.
 */
public class CustomeScrollView extends FrameLayout {
    private OverScroller scroller;
    private GestureDetector detector;
    private int height;

    public CustomeScrollView(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    public CustomeScrollView(Context context, AttributeSet set) {
        this(context, set, 0);

    }

    public CustomeScrollView(Context context, AttributeSet set, int def) {
        super(context, set, def);
        scroller = new OverScroller(context);
//        scroller = new OverScroller(context, new AccelerateInterpolator());
        detector = new GestureDetector(context, new SimpleGestureListener());
    }

    //调用startScroll可以进行滚动
    public void startScroll() {
        scroller.startScroll(0, 0, -400, 0, 1000);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        getChildAt(0).measure(widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.UNSPECIFIED));
        height = getChildAt(0).getMeasuredHeight();
    }

    @Override
    public void computeScroll() {
        // TODO Auto-generated method stub
        //super.computeScroll();
        //自定义view必须实现computeScroll函数，调用Scroller.computeScorllOffset去计算当前滚动的位置，滚动到终点，computeScrollOffset返回false
        if (scroller.computeScrollOffset()) {

            Log.e("1computeScroll:" + scroller.getCurrY(), "getScrollY:" + getScrollY());
            int scroll = scroller.getCurrY();

            scrollTo(0, scroll);
            Log.e("2computeScroll:" + scroller.getCurrY(), "getScrollY:" + getScrollY());

            if (getScrollY() + getHeight() > height) {
                scrollTo(0, height - getHeight());
            } else if (getScrollY() < 0) {
                scrollTo(0, 0);
            }

//            safeScroll(-scroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        /// super.onTouchEvent(event);
        // super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                scroller.forceFinished(true);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        detector.onTouchEvent(event);
        return true;
    }

    private float safeScroll(float distanceY) {
        if (distanceY + getScrollY() + getHeight() > height) {
            distanceY -= distanceY + getScrollY() + getHeight() - height;
        } else if (distanceY + getScrollY() < 0) {
            distanceY = Math.abs(distanceY) - getScrollY();
        }
        scrollBy(0, (int) distanceY);
        return distanceY;
    }

    class SimpleGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
//            scroller.fling(scroller.getCurrX(), scroller.getCurrY(), (int) velocityX, (int) velocityY, 0, 0, 100, 200);
            Log.e("velocityY:", velocityY + "");
            int scroll = (int) (Math.abs(velocityY) / 3);
            if (velocityY > 0) {//手势下滑
                scroller.startScroll(0, getScrollY(), 0, -scroll, 500);
            } else if (velocityY < 0) {//手势上滑
                scroller.startScroll(0, getScrollY(), 0, scroll, 500);
            }

            invalidate();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            distanceY = safeScroll(distanceY);

//            if (distanceY > 0) {//手势向上
//                if (getScrollY() + getHeight() > height) {
//                    return true;
//                } else {
//                    scrollBy(0, (int) distanceY);
//                }
//            } else if (distanceY < 0) {//手势向下
//                if (getScrollY() < 0) {
//                    return true;
//                } else {
//                    scrollBy(0, (int) distanceY);
//                }
//            }

            Log.e("onScroll:" + distanceY, "getScrollY:" + getScrollY());
            postInvalidate();

            return true;
        }
    }
}

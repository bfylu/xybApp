/**
 The MIT License (MIT)

 Copyright (c) 2014 singwhatiwanna
 https://github.com/singwhatiwanna
 http://blog.csdn.net/singwhatiwanna

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */

package cn.payadd.majia.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Toast;

import cn.payadd.majia.adapter.ProfitDayRecordAdapter;
import cn.payadd.majia.adapter.WithdrawRecordAdapter;

public class PinnedHeaderExpandableListView extends ExpandableListView implements NestedScrollingChild,OnScrollListener {
    private static final boolean DEBUG = true;

    private NestedScrollingChildHelper mScrollingChildHelper;



    public interface OnHeaderUpdateListener {
        /**
         * 返回一个view对象即可
         * 注意：view必须要有LayoutParams
         */
        public View getPinnedHeader();

        public void updatePinnedHeader(View headerView, int firstVisibleGroupPos);
    }

    private View mHeaderView;
    private int mHeaderWidth;
    private int mHeaderHeight;

    private View mTouchTarget;

    private OnScrollChangeListener mScrollChangeListener;
    private OnIconVisibleListener mOnIconVisibleListener;

    private boolean mActionDownHappened = false;
    protected boolean mIsHeaderGroupClickable = true;


    public PinnedHeaderExpandableListView(Context context) {
        super(context);
        initView();
    }

    public PinnedHeaderExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PinnedHeaderExpandableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        mScrollingChildHelper = new NestedScrollingChildHelper(this);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            setNestedScrollingEnabled(true);
//        }
        setNestedScrollingEnabled(true);
        setFadingEdgeLength(0);
        setOnScrollListener(this);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);


    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int
            totalItemCount) {

        Log.d("onScroll", "scrollState:"+firstVisibleItem);
    }
//    @Override
//    public void setOnScrollListener(OnScrollListener l) {
////        if (l != this) {
////            mScrollListener = l;
////        } else {
////            mScrollListener = null;
////        }
//        super.setOnScrollListener(l);
//    }

//    /**
//     * 给group添加点击事件监听
//     * @param onGroupClickListener 监听
//     * @param isHeaderGroupClickable 表示header是否可点击<br/>
//     * note : 当不想group可点击的时候，需要在OnGroupClickListener#onGroupClick中返回true，
//     * 并将isHeaderGroupClickable设为false即可
//     */
//    public void setOnGroupClickListener(OnGroupClickListener onGroupClickListener, boolean isHeaderGroupClickable) {
//        mIsHeaderGroupClickable = isHeaderGroupClickable;
//        super.setOnGroupClickListener(onGroupClickListener);
//    }
//
//    public void setOnHeaderUpdateListener(OnHeaderUpdateListener listener) {
//        mHeaderUpdateListener = listener;
//        if (listener == null) {
//            mHeaderView = null;
//            mHeaderWidth = mHeaderHeight = 0;
//            return;
//        }
//        mHeaderView = listener.getPinnedHeader();
//        int firstVisiblePos = getFirstVisiblePosition();
//        int firstVisibleGroupPos = getPackedPositionGroup(getExpandableListPosition(firstVisiblePos));
//        listener.updatePinnedHeader(mHeaderView, firstVisibleGroupPos);
//        requestLayout();
//        postInvalidate();
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
//        if (mHeaderView == null) {
//            return;
//        }
//        int delta = mHeaderView.getTop();
//        mHeaderView.layout(0, delta, mHeaderWidth, mHeaderHeight + delta);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
//        if (mHeaderView != null) {
//            drawChild(canvas, mHeaderView, getDrawingTime());
//        }
    }



    private boolean isTouchPointInView(View view, int x, int y) {
        if (view.isClickable() && y >= view.getTop() && y <= view.getBottom()
                && x >= view.getLeft() && x <= view.getRight()) {
            return true;
        }
        return false;
    }



    public void requestRefreshHeader() {
//        refreshHeader();
    }
//    protected void refreshHeader() {
//        if (mHeaderView == null) {
//            return;
//        }
//        int firstVisiblePos = getFirstVisiblePosition();
//        int pos = firstVisiblePos + 1;
//        int firstVisibleGroupPos = getPackedPositionGroup(getExpandableListPosition(firstVisiblePos));
//        int group = getPackedPositionGroup(getExpandableListPosition(pos));
//
//
//        if (group == firstVisibleGroupPos + 1) {
//            View view = getChildAt(1);
//            if (view == null) {
//                return;
//            }
//            if (view.getTop() <= mHeaderHeight) {
//                int delta = mHeaderHeight - view.getTop();
//                mHeaderView.layout(0, -delta, mHeaderWidth, mHeaderHeight - delta);
//            } else {
//                //TODO : note it, when cause bug, remove it
//                mHeaderView.layout(0, 0, mHeaderWidth, mHeaderHeight);
//            }
//        } else {
//            mHeaderView.layout(0, 0, mHeaderWidth, mHeaderHeight);
//        }
//
//        if (mHeaderUpdateListener != null) {
//            mHeaderUpdateListener.updatePinnedHeader(mHeaderView, firstVisibleGroupPos);
//        }
//    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mScrollingChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mScrollingChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mScrollingChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mScrollingChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, int[] offsetInWindow) {
        return mScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);

    }
    public interface OnIconVisibleListener {
        void onVisible(boolean visible);
    }
    public void setOnIconVisibleListener (OnIconVisibleListener l){
        this.mOnIconVisibleListener = l;
    }


}
package cn.payadd.majia.view;

import android.content.Context;
import android.databinding.adapters.AbsListViewBindingAdapter;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;

/**
 * Created by df on 2017/9/27.
 */

public class SmartNestedScrollView extends NestedScrollView implements View.OnTouchListener{
    private boolean isScrolledToTop = true;// 初始化的时候设置一下值
    private boolean isScrolledToBottom = false;
    private OnScrollListener mOnScrollListener;
    public SmartNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
    }

    private ISmartScrollChangedListener mSmartScrollChangedListener;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                View childView = getChildAt(0);
                if (childView  != null && childView .getMeasuredHeight() <= getScrollY() + getHeight()) {
                    isScrolledToBottom = true;
                    isScrolledToTop = false;
                } else if (getScrollY() == 0) {
                    isScrolledToTop = true;
                    isScrolledToBottom = false;
                }
                notifyScrollChangedListeners();
                break;
        }
        return false;
    }



    /** 定义监听接口 */
    public interface ISmartScrollChangedListener {
        void onScrolledToBottom();
        void onScrolledToTop();
    }
    public void setSmartScrollChangedListener(ISmartScrollChangedListener smartScrollChangedListener) {
        mSmartScrollChangedListener = smartScrollChangedListener;
    }


    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if (scrollY == 0) {
            isScrolledToTop = clampedY;
            isScrolledToBottom = false;
        } else {
            isScrolledToTop = false;
            isScrolledToBottom = clampedY;
        }
        notifyScrollChangedListeners();
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        if(ev.getAction() == MotionEvent.ACTION_MOVE){
//
//        }
//        return super.onInterceptTouchEvent(ev);
//    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        // API 9及之后走onOverScrolled方法监听
            if (getScrollY() == 0) {    // 小心踩坑1: 这里不能是getScrollY() <= 0
                isScrolledToTop = true;
                isScrolledToBottom = false;
            } else if (getScrollY() + getHeight() - getPaddingTop()-getPaddingBottom() == getChildAt(0).getHeight()) {
                // 小心踩坑2: 这里不能是 >=
                // 小心踩坑3（可能忽视的细节2）：这里最容易忽视的就是ScrollView上下的padding　
                isScrolledToBottom = true;
                isScrolledToTop = false;
            } else {
                isScrolledToTop = false;
                isScrolledToBottom = false;
                if(mOnScrollListener != null){
                    mOnScrollListener.onScrolled(l, t, oldl, oldt);
                }

            }
            notifyScrollChangedListeners();

        // 有时候写代码习惯了，为了兼容一些边界奇葩情况，上面的代码就会写成<=,>=的情况，结果就出bug了
        // 我写的时候写成这样：getScrollY() + getHeight() >= getChildAt(0).getHeight()
        // 结果发现快滑动到底部但是还没到时，会发现上面的条件成立了，导致判断错误
        // 原因：getScrollY()值不是绝对靠谱的，它会超过边界值，但是它自己会恢复正确，导致上面的计算条件不成立
        // 仔细想想也感觉想得通，系统的ScrollView在处理滚动的时候动态计算那个scrollY的时候也会出现超过边界再修正的情况
    }

    private void notifyScrollChangedListeners() {
        if (isScrolledToTop) {
            if (mSmartScrollChangedListener != null) {
                mSmartScrollChangedListener.onScrolledToTop();
            }
        } else if (isScrolledToBottom) {
            if (mSmartScrollChangedListener != null) {
                mSmartScrollChangedListener.onScrolledToBottom();
            }
        }
    }

    public boolean isScrolledToTop() {
        return isScrolledToTop;
    }

    public boolean isScrolledToBottom() {
        return isScrolledToBottom;
    }
    /** 定义监听接口 */
    public interface OnScrollListener {
        void onScrolled(int l, int t, int oldl, int oldt);
    }
    public void setOnScrollListener(OnScrollListener l) {
        mOnScrollListener = l;
    }

}

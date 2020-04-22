package cn.payadd.majia.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

public class PinnedHeaderListView extends ExpandableListView implements AbsListView.OnScrollListener {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	
	public static final int PINNED_HEADER_GONE = 0;
	public static final int PINNED_HEADER_VISIBLE = 1;
	public static final int PINNED_HEADER_PUSHED_UP = 2;

	private View mHeaderView;
	private PinnedHeaderListener mPinnedHeaderListener;
	private OnScrollListener mScrollListener;
	
	// ===========================================================
	// Constructors
	// ===========================================================

	public PinnedHeaderListView(Context context) {
		super(context);
		
		super.setOnScrollListener(this);
	}
	
	public PinnedHeaderListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		super.setOnScrollListener(this);
	}
	
	public PinnedHeaderListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		
		super.setOnScrollListener(this);
	}
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public void setPinnedHeader(View pHeader) {
		mHeaderView = pHeader;
		
		requestLayout();
	}
	
	protected View getHeaderView() {
		return mHeaderView;
	}
	
	public void setPinnedHeaderListener(PinnedHeaderListener listener) {
		mPinnedHeaderListener = listener;
	}
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
		
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		
		controlPinnedHeader(getFirstVisiblePosition());
	}
	
	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mScrollListener != null) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		controlPinnedHeader(firstVisibleItem);
		if (mScrollListener != null) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}
	}
	
	// ===========================================================
	// Methods
	// ===========================================================

	private int getPinnedHeaderState(int firstVisibleItem) {
		if (getExpandableListAdapter().isEmpty() || firstVisibleItem == AdapterView.INVALID_POSITION) {
			return PINNED_HEADER_GONE;
		}
		
		if (firstVisibleItem == 0) {
			View firstView = getChildAt(0);
			if (firstView == null || firstView.getTop() >= 0) {
				return PINNED_HEADER_GONE;
			}
		}
		
		long expandableListPosition = getExpandableListPosition(firstVisibleItem);
		
		int childPos = getPackedPositionChild(expandableListPosition);
		int nextChildPos = getPackedPositionChild(getExpandableListPosition(firstVisibleItem + 1));
		
		if (childPos == -1 && nextChildPos == -1) {
			return PINNED_HEADER_GONE;
		} else if (nextChildPos == -1) {
			View view = getChildAt(1);
			
			int height = mHeaderView.getHeight();
			
			if (view != null && view.getTop() <= height) {
				return PINNED_HEADER_PUSHED_UP;
			}
		} else if (childPos >= 0) {
			for (int i = 1; i < getChildCount(); ++i) {
				if (getPackedPositionChild(getExpandableListPosition(i + firstVisibleItem)) == -1) {
					View view = getChildAt(i);
					if (view != null && view.getTop() <= mHeaderView.getHeight()) {
						return PINNED_HEADER_PUSHED_UP;
					} else {
						break;
					}
				}
			}
		}

		return PINNED_HEADER_VISIBLE;
	}
	
	private void controlPinnedHeader(int firstVisibleItem) {
		if (null == mHeaderView || null == mPinnedHeaderListener) {
			return;
		}
		
		int pinnedHeaderState = getPinnedHeaderState(firstVisibleItem);
		
		switch (pinnedHeaderState) {
		case PINNED_HEADER_GONE:
			mHeaderView.setVisibility(View.GONE);
			break;

		case PINNED_HEADER_VISIBLE:
			mPinnedHeaderListener.onVisible(this, mHeaderView, firstVisibleItem);
			mHeaderView.setVisibility(View.VISIBLE);
			mHeaderView.setY(mHeaderView.getTop());
			break;
			
		case PINNED_HEADER_PUSHED_UP:
			mPinnedHeaderListener.onVisible(this, mHeaderView, firstVisibleItem);
			mHeaderView.setVisibility(View.VISIBLE);
			
			int moveY = 0;
			for (int i = 0; i < getChildCount(); ++i) {
				if (getPackedPositionChild(getExpandableListPosition(i + firstVisibleItem)) == -1) {
					View view = getChildAt(i);
					if (view != null && view.getTop() < mHeaderView.getHeight()) {
						moveY = view.getTop() - mHeaderView.getHeight();
						break;
					}
				}
			}
			
			if (mHeaderView.getTranslationY() != moveY) {
				mHeaderView.setY(moveY);
			}
			
			break;
		}
		
	}
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	
	public interface PinnedHeaderListener {
		void onVisible(PinnedHeaderListView listView, View headerView, int position);
	}

}

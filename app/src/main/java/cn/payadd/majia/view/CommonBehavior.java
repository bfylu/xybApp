package cn.payadd.majia.view;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by df on 2017/9/24.
 */

public class CommonBehavior<V extends View> extends CoordinatorLayout.Behavior<V>{
    /**
     * Default constructor for instantiating a FancyBehavior in code.
     */
    public CommonBehavior() {
    }
    /**
     * Default constructor for inflating a FancyBehavior from layout.
     *
     * @param context The {@link Context}.
     * @param attrs The {@link AttributeSet}.
     */
    public CommonBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Extract any custom attributes out
        // preferably prefixed with behavior_ to denote they
        // belong to a behavior
    }
}

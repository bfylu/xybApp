package cn.payadd.majia.view;

import android.content.Context;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TableRow;

public class BadgeFactory {

	public static BadgeView create(Context context) {
		return new BadgeView(context);
	}
	
	public static BadgeView createDot(Context context) {
		return new BadgeView(context).setBadgeLayoutParams(10, 10)
				.setTextSize(0)
				.setBadgeGravity(Gravity.RIGHT | Gravity.TOP,0,0,0,0)
				.setBackgroundShape(BadgeView.SHAPE_CIRCLE);
	}

	public static BadgeView createCircle(Context context) {

		return new BadgeView(context).setBadgeLayoutParams(12, 12)
				.setTextSize(12)
				.setBadgeGravity(Gravity.RIGHT | Gravity.TOP,10,0,0,50)
				.setBackgroundShape(BadgeView.SHAPE_CIRCLE);
	}

	public static BadgeView createRectangle(Context context) {
		return new BadgeView(context).setBadgeLayoutParams(2, 20)
				.setTextSize(12)
				.setBadgeGravity(Gravity.RIGHT | Gravity.TOP,0,0,0,0)
				.setBackgroundShape(BadgeView.SHAPE_RECTANGLE);
	}

	public static BadgeView createOval(Context context) {
		return new BadgeView(context).setBadgeLayoutParams(25, 20)
				.setTextSize(12)
				.setBadgeGravity(Gravity.RIGHT | Gravity.TOP,0,0,0,0)
				.setBackgroundShape(BadgeView.SHAPE_OVAL);
	}

	public static BadgeView createSquare(Context context) {
		return new BadgeView(context).setBadgeLayoutParams(20, 20)
				.setTextSize(12)
				.setBadgeGravity(Gravity.RIGHT | Gravity.TOP,0,0,0,0)
				.setBackgroundShape(BadgeView.SHAPE_SQUARE);
	}

	public static BadgeView createRoundRect(Context context) {
		return new BadgeView(context).setBadgeLayoutParams(25, 20)
				.setTextSize(12)
				.setBadgeGravity(Gravity.RIGHT | Gravity.TOP,0,0,0,0)
				.setBackgroundShape(BadgeView.SHAPTE_ROUND_RECTANGLE);
	}
	
}

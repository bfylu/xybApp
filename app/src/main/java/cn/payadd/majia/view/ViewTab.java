package cn.payadd.majia.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fdsj.credittreasure.R;

/**
 * Created by df on 2017/9/11.
 */

public class ViewTab extends RelativeLayout {

    private ImageView image;
    private TextView text;

    public ViewTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.widget_tab, this, true);
        image = (ImageView) findViewById(R.id.iv_image);
        text = (TextView) findViewById(R.id.tv_text);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ViewTab);
        if (attributes != null) {
            int imageSrc = attributes.getResourceId(R.styleable.ViewTab_image_src,-1);
            if(imageSrc!=-1){
                image.setImageResource(imageSrc);
            }
            String textStr =  attributes.getString(R.styleable.ViewTab_text);
            text.setText(textStr);
        }
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public TextView getText() {
        return text;
    }

    public void setText(TextView text) {
        this.text = text;
    }
}

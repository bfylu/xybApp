package cn.payadd.majia.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fdsj.credittreasure.R;

import cn.payadd.majia.constant.InputMode;
import cn.payadd.majia.util.StringUtil;
import cn.payadd.majia.util.Validator;

/**
 * Created by df on 2017/9/4.
 */

public class CommonEditText extends RelativeLayout {

    private TextView cetLeftText;
    private ImageView cetLeftImage, cetRightImage;
    private EditText cetEditText;
    private View cetBottomLine;


    public CommonEditText(final Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.widget_common_edittext, this, true);
        cetEditText = (EditText) findViewById(R.id.cet_edit_text);
        cetLeftImage = (ImageView) findViewById(R.id.cet_left_image);
        cetRightImage = (ImageView) findViewById(R.id.cet_right_image);
        cetLeftText = (TextView) findViewById(R.id.cet_left_text);
        cetBottomLine = findViewById(R.id.cet_bottom_line);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CommonEditText);
        if (attributes != null) {
            //左边文字
            boolean leftTextVisible = attributes.getBoolean(R.styleable
                    .CommonEditText_left_text_visible, true);
            if (leftTextVisible) {
                cetLeftText.setVisibility(View.VISIBLE);
            } else {
                cetLeftText.setVisibility(View.GONE);
            }
            String leftText = attributes.getString(R.styleable.CommonEditText_left_text);
            if (!TextUtils.isEmpty(leftText)) {
                cetLeftText.setText(leftText);
            } else {
                cetLeftText.setVisibility(View.GONE);
            }
            //左边图片
            boolean leftImageVisible = attributes.getBoolean(R.styleable
                    .CommonEditText_left_image_visible, true);
            if (leftImageVisible) {
                cetLeftImage.setVisibility(View.VISIBLE);
            } else {
                cetLeftImage.setVisibility(View.GONE);
            }
            int leftImageDrawble = attributes.getResourceId(R.styleable
                    .CommonEditText_left_image_drawable, -1);
            if (leftImageDrawble != -1) {
                cetLeftImage.setImageResource(leftImageDrawble);
            } else {
                cetLeftImage.setVisibility(View.GONE);
            }

            //右边图片
            boolean rightImageVisible = attributes.getBoolean(R.styleable
                    .CommonEditText_right_image_visible, true);
            if (rightImageVisible) {
                cetRightImage.setVisibility(View.VISIBLE);
            } else {
                cetRightImage.setVisibility(View.GONE);
            }
           //右边图片
            int rightImageDrawble = attributes.getResourceId(R.styleable.CommonEditText_right_image_drawable, -1);
            if (rightImageDrawble != -1) {
                cetRightImage.setImageResource(rightImageDrawble);
            } else {
                cetRightImage.setVisibility(View.GONE);
            }

            //
            String editTextHint = attributes.getString(R.styleable.CommonEditText_edit_text_hint);
            if (!TextUtils.isEmpty(editTextHint)) {
                cetEditText.setHint(editTextHint);
            }
            //底线
            boolean bottomLineVisible = attributes.getBoolean(R.styleable
                    .CommonEditText_bottom_line_visible, true);
            if (bottomLineVisible) {
                cetBottomLine.setVisibility(View.VISIBLE);
            } else {
                cetBottomLine.setVisibility(View.GONE);
            }
            //input_mode
//            final int inputMode = attributes.getInt(R.styleable.CommonEditText_input_mode,-1);
//            cetEditText.setInputType(inputMode);
            //cet_input_mode
            final String cetInputMode = attributes.getString(R.styleable
                    .CommonEditText_cet_input_mode);
            if (!TextUtils.isEmpty(cetInputMode)) {
                switch (cetInputMode) {
                    case InputMode.BANKCARD:
                        cetEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                        cetEditText.setFilters(new InputFilter[]{
                            new InputFilter() {
                                @Override
                                public CharSequence filter(CharSequence source, int start, int
                                        end, Spanned dest, int dstart, int dend) {
                                    if(dest.toString().replaceAll(" ", "").length() >= 19){
                                        return "";
                                    }
                                    if(!Validator.checkNumber(source.toString())){
                                        return "";
                                    }
                                    String add = null;
                                    //增加
                                    if(dstart - dend == 0){
                                        if (dest != null) {
                                            String content = dest.toString();
                                            String str = content.replaceAll(" ", "");
                                            if (str.length()!= 0 && str.length() % 4 == 0 && (dest.charAt(dest.length()- 1)) != ' ') {
                                                add = " " + source;
                                            }else {
                                                add = source.toString();
                                            }
                                        }
                                    }
                                    return add;
                                }
                            }
                        });
                        break;
                    case InputMode.PHONE:
                        cetEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                        cetEditText.setFilters(new InputFilter[]{
                              new InputFilter() {
                                  @Override
                                  public CharSequence filter(CharSequence source, int start, int
                                          end, Spanned dest, int dstart, int dend) {
                                      if(dest.toString().length() >= 11){
                                          return "";
                                      }
                                      if(!Validator.checkNumber(source.toString())){
                                          return "";
                                      }
                                      return null;
                                  }
                              }
                        });
                        break;
                    case InputMode.EMAIL:
                        break;
                    case InputMode.NUMBER:
                        cetEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        cetEditText.setFilters(new InputFilter[]{
                                new InputFilter() {
                                    @Override
                                    public CharSequence filter(CharSequence source, int start, int
                                            end, Spanned dest, int dstart, int dend) {
                                        if(!StringUtil.isAmount(cetEditText.getText() + source.toString())){
                                            return "";
                                        }
                                        return null;
                                    }
                                }
                        });
                        break;
                    default:
                        break;
                }
            }
        }
    }
    public void setRightImageClickListener(OnClickListener onClickListener) {
        if (onClickListener != null) {
            cetRightImage.setOnClickListener(onClickListener);
        }
    }

    public void setEditTextClickListener(OnClickListener onClickListener) {
        if (onClickListener != null) {
            cetEditText.setOnClickListener(onClickListener);
        }
    }

    public String getValue(){
        if(cetEditText != null){
            return cetEditText.getText().toString().replaceAll(" ","");
        }else{
            return null;
        }

    }

    public TextView getCetLeftText() {
        return cetLeftText;
    }

    public ImageView getCetLeftImage() {
        return cetLeftImage;
    }

    public ImageView getCetRightImage() {
        return cetRightImage;
    }

    public EditText getCetEditText() {
        return cetEditText;
    }
}

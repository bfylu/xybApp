package cn.payadd.majia.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fdsj.credittreasure.R;

/**
 * Created by df on 2017/9/4.
 */

public class TemplateDialog extends Dialog{

    public TemplateDialog(@NonNull Context context) {
        super(context);
    }
    public TemplateDialog(@NonNull Context context,int theme){
        super(context,theme);
    }


    @Override
    public void dismiss() {
        super.dismiss();
    }

    public static class Builder{
        private Context context;
        private String title;
        private String message;
        private String positiveBtnText;
        private String negativeBtnText;
        private View contentView;
        private OnClickListener positiveBtnClickListener;
        private OnClickListener negativeButtonClickListener;
        private Button  positiveBtn;
        private Button  negativeBtn;
        private LinearLayout llBtnArea;
        public Builder(Context context){
            this.context = context;
        }
        public Builder setMessage(String message){
            this.message = message;
            return this;
        }

        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }


        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }


        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Button getPositiveBtn() {
            return positiveBtn;
        }

        public Button getNegativeBtn() {
            return negativeBtn;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveBtnText
         * @return
         */
        public Builder setPositiveButton(int positiveBtnText,
                                         OnClickListener listener) {
            this.positiveBtnText = (String) context
                    .getText(positiveBtnText);
            this.positiveBtnClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveBtnText,
                                         OnClickListener listener) {
            this.positiveBtnText = positiveBtnText;
            this.positiveBtnClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeBtnText,
                                         OnClickListener listener) {
            this.negativeBtnText = (String) context
                    .getText(negativeBtnText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeBtnText,
                                         OnClickListener listener) {
            this.negativeBtnText = negativeBtnText;
            this.negativeButtonClickListener = listener;
            return this;
        }



        public TemplateDialog create() {
            
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final TemplateDialog dialog = new TemplateDialog(context, R.style.dialog);

            View layout = inflater.inflate(R.layout.dialog_template_layout, null);
            //设置关闭按钮
            ImageView closeImage = (ImageView) layout.findViewById(R.id.iv_close);
            closeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            //获取屏幕大小
            WindowManager wmManager=(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            wmManager.getDefaultDisplay().getMetrics(dm);
            layout.setMinimumWidth((int)(dm.widthPixels * 0.8));

            dialog.addContentView(layout,new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            // set the dialog title
            if(TextUtils.isEmpty(title)){
                layout.findViewById(R.id.title).setVisibility(View.GONE);
            }else{
                ((TextView) layout.findViewById(R.id.title)).setText(title);
            }
            llBtnArea = (LinearLayout) layout.findViewById(R.id.ll_btn_area);
            // set the confirm button
            if (positiveBtnText != null) {
                positiveBtn = (Button) layout.findViewById(R.id.positive_btn);
                positiveBtn.setText(positiveBtnText);
                if (positiveBtnClickListener != null) {
                    positiveBtn.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveBtnClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.positive_btn).setVisibility(
                        View.GONE);
            }
            // set the cancel button
            if (negativeBtnText != null) {
                negativeBtn = (Button) layout.findViewById(R.id.negative_btn);
                negativeBtn.setText(negativeBtnText);
                if (negativeButtonClickListener != null) {
                    negativeBtn.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.negative_btn).setVisibility(
                        View.GONE);

//               positiveBtn.willNotDraw();


            }
            // set the content message
            if (message != null) {
                ((TextView) layout.findViewById(R.id.message)).setText(message);
            } else if (contentView != null) {
                // if no message set
                // add the contentView to the dialog body
                ((LinearLayout) layout.findViewById(R.id.content))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content)).addView(
                        contentView,new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            dialog.setContentView(layout);
            return dialog;
        }
    }

}

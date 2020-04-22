package cn.payadd.majia.view;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;

/**
 * Created by df on 2017/9/4.
 */

public class CommonDialog {
    private Context context;
    private int customeLayoutId;
    private String dialogTitle;
    private String dialogMessage;
    private String positiveText;
    private String negativeText;
    private View dialogView;
    private OnDialogListener listener;
    private TemplateDialog dialog;
    private TemplateDialog.Builder builder;
    private Button positiveBtn;
    private Button negativeBtn;
    //带有自定义view的构造器
    public CommonDialog(Context context, int customeLayoutId, String dialogTitle, String positiveText, String negativeText) {
        this.context = context;
        this.customeLayoutId = customeLayoutId;
        this.dialogTitle = dialogTitle;
        this.positiveText = positiveText;
        this.negativeText = negativeText;
        this.dialogView=View.inflate(context,customeLayoutId,null);

    }
    //不带自定义view的构造器
    public CommonDialog(Context context, String dialogMessage,String dialogTitle, String positiveText, String negativeText) {
        this.context = context;
        this.dialogTitle = dialogTitle;
        this.dialogMessage = dialogMessage;
        this.positiveText = positiveText;
        this.negativeText = negativeText;
    }
    public View getDialogView() {
        return dialogView;
    }

    public void setDialogView(View dialogView) {
        this.dialogView = dialogView;
    }

    public Button getPositiveBtn() {
        return positiveBtn;
    }

    public Button getNegativeBtn() {
        return negativeBtn;
    }

    public void dismiss(){
        if(dialog!=null){
            dialog.dismiss();
        }
    }
    public void showDialog(){

        builder=new TemplateDialog.Builder(context);
        builder.setTitle(dialogTitle);//设置标题
        //注意:dialogMessage和dialogView是互斥关系也就是dialogMessage存在dialogView就不存在,dialogView不存在dialogMessage就存在
        if (dialogMessage!=null){
            builder.setMessage(dialogMessage);//设置对话框内容
        }else{
            builder.setContentView(dialogView);//设置对话框的自定义view对象
        }
        /**
         * 尽管有两个点击事件监听器,可以通过我们自定义的监听器设置一个标记变量,从而可以实现将两个点击事件合并成一个
         * 监听器OnDialogListener
         * */
        //确定意图传入positive标记值
        dialog = builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (listener!=null){
                    listener.dialogListener("positive",dialogView,dialogInterface,which);
                }
            }
            //取消意图传入negative标记值
        }).setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (listener!=null){
                    listener.dialogListener("negative",dialogView,dialogInterface,which);
                }
            }
        }).create();
        positiveBtn = builder.getPositiveBtn();
        negativeBtn = builder.getNegativeBtn();
        dialog.setCanceledOnTouchOutside(false);
        
        dialog.show();

    }

    public void setPositiveText(String text, DialogInterface.OnClickListener listener){
        if(listener == null){
            builder.setPositiveButton(text, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }else{
            builder.setPositiveButton(text,listener);
        }

    }
    //注册监听器方法
    public CommonDialog setOnDiaLogListener(OnDialogListener listener){
        this.listener = listener;
        return this;//把当前对象返回,用于链式编程
    }
    //定义一个监听器接口
    public interface OnDialogListener{
        //customView　这个参数需要注意就是如果没有自定义view,那么它则为null
        void dialogListener(String btnType, View customView, DialogInterface dialogInterface, int which);
    }
}

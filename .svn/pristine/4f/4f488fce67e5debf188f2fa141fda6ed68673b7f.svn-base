package cn.payadd.majia.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.fdsj.credittreasure.R;
import com.utils.Utilities;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.payadd.majia.config.AppConfig;
import cn.payadd.majia.util.ImageUtil;

/**
 * Created by df on 2017/11/30.
 */

public class FixedCodeActivity extends BaseActivity{

    private ImageView ivFixedCode;

    private Button btnSave;

    private ExecutorService es = Executors.newSingleThreadExecutor();

    private String fileName = "myFixedQrcode.png";

    public static final int HANDLER_SHOW_IMAGE = 1;

    private Handler handlerUI = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case HANDLER_SHOW_IMAGE: {
                    Object[] obj = (Object[]) msg.obj;
                    Bitmap bitmap = (Bitmap) obj[0];
                    File file = ImageUtil.saveBitmapFile(bitmap, fileName, Bitmap.CompressFormat.PNG);
                    ivFixedCode.setImageBitmap(bitmap);
                    break;
                }
            }
        }
    };
    @Override
    public int getLayoutId() {
        return R.layout.activity_fixedcode2;
    }

    @Override
    public void initView() {
        setTitleCenterText("我的固定码");
        setTitleBackButton();

        ivFixedCode = (ImageView) findViewById(R.id.iv_fixed_code);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap image = ((BitmapDrawable)ivFixedCode.getDrawable()).getBitmap();
                boolean flag = ImageUtil.saveImageToGallery(FixedCodeActivity.this,image,fileName,100);
                if(flag){
                    Toast.makeText(FixedCodeActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(FixedCodeActivity.this,"保存失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void initData() {
        String myQrcodeUrl = AppConfig.getFixedQrcodeInterface();
        getFixedQrcode(myQrcodeUrl);
    }

    @Override
    protected void initPermission() {

    }

    public void getFixedQrcode(final String imgUrl) {

        if (TextUtils.isEmpty(imgUrl)) {
            return;
        }

        es.execute(new Runnable() {
            @Override
            public void run() {
                InputStream input = null;

                try {
                    URL url = new URL(imgUrl + "?sessionToken=" + Utilities.getSessionToKen(FixedCodeActivity.this));
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    input = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(input);

                    Message msg = new Message();
                    msg.what = HANDLER_SHOW_IMAGE;
                    msg.obj = new Object[]{bitmap};
                    handlerUI.sendMessage(msg);

                } catch (Exception e) {
                    e.printStackTrace();

                } finally {
                    if (null != input) {
                        try {
                            input.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }
}

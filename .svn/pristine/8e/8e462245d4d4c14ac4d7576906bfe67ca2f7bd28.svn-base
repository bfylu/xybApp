package cn.payadd.majia.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fdsj.credittreasure.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.payadd.majia.constant.SymbolConstant;
import cn.payadd.majia.task.FileUpdateTask;
import cn.payadd.majia.util.AppLog;
import cn.payadd.majia.util.ImageUtil;
import cn.payadd.majia.util.UriToPathUtil;

/**
 * Created by df on 2017/12/27.
 */

public class GoodsHolder extends BaseViewHolder<Map<String, String>> {
    private ExecutorService es = Executors.newSingleThreadExecutor();

    private TextView tvGoodsAmount;
    private TextView tvGoodsTitle;
    private TextView tvGoodsCount;
    private ImageView ivGoodsImg;
    private TextView tvGoodsUnit;

    public GoodsHolder(ViewGroup parent) {
        super(parent, R.layout.item_goods);
        tvGoodsTitle = $(R.id.tv_goods_title);
        ivGoodsImg = $(R.id.iv_goods_img);
        tvGoodsAmount = $(R.id.tv_goods_amount);
        tvGoodsCount = $(R.id.tv_goods_count);
        tvGoodsUnit = $(R.id.tv_goods_unit);
    }

    @Override
    public void setData(Map<String, String> data) {
        super.setData(data);
        tvGoodsTitle.setText(data.get("goodsName"));
        tvGoodsAmount.setText(SymbolConstant.RMB + data.get("goodsPrice"));
        tvGoodsCount.setText(data.get("purchaseQuantity"));
        String unit = data.get("goodsWeightUnit");
        if (!TextUtils.isEmpty(unit)) {
            tvGoodsUnit.setVisibility(View.VISIBLE);
            tvGoodsUnit.setText("/" + unit);
        }

        try {
            final String refNo = data.get("goodsPic");
            if (ImageLoader.getInstance().isInited() && !TextUtils.isEmpty(refNo)) {
                int w = View.MeasureSpec.makeMeasureSpec(0,
                        View.MeasureSpec.UNSPECIFIED);
                int h = View.MeasureSpec.makeMeasureSpec(0,
                        View.MeasureSpec.UNSPECIFIED);
                ivGoodsImg.measure(w, h);
                final int width = ivGoodsImg.getMeasuredWidth();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        addImage(Config.getPicUrl()+refNo+"&width="+60,refNo);
                    }
                }).start();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final int HANDLER_SHOW_IMAGE = 2;

    private Handler handlerUI = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case HANDLER_SHOW_IMAGE: {
                    Object[] obj = (Object[]) msg.obj;
                    String fileName = (String) obj[0];
                    ImageLoader.getInstance().displayImage(UriToPathUtil.getUri(fileName).toString(), ivGoodsImg);
                    break;
                }
            }
        }
    };

    public void addImage(final String imgUrl,final String refNo){
        try{
            URL url = new URL(imgUrl);
            //打开连接
            URLConnection conn = url.openConnection();
            //打开输入流
            InputStream is = conn.getInputStream();
            //获得长度
            int contentLength = conn.getContentLength();
            //创建文件夹 MyDownLoad，在存储卡下
            String dirName = getContext().getExternalFilesDir("tmp/download").getAbsolutePath();
            File file = new File(dirName);
            //不存在创建
            if (!file.exists()) {
                file.mkdir();
            }
            //下载后的文件名
            String fileName = dirName+"/" + refNo +".jpg";
            File file1 = new File(fileName);
            if (file1.exists()) {
                file1.delete();
            }
            //创建字节流
            byte[] bs = new byte[1024];
            int len;
            OutputStream os = new FileOutputStream(fileName);
            //写数据
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            Message msg = new Message();
            msg.what = HANDLER_SHOW_IMAGE;
            msg.obj = new Object[]{fileName};
            handlerUI.sendMessage(msg);

            os.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
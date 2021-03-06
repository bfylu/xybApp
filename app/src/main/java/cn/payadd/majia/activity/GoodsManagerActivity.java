package cn.payadd.majia.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fdsj.credittreasure.R;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import cn.payadd.majia.config.StoreDirConfig;
import cn.payadd.majia.constant.AppService;
import cn.payadd.majia.constant.ResourceConstants;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.jsInterface.GoodsManangerJSInterface;
import cn.payadd.majia.presenter.GoodsManagePresenter;
import cn.payadd.majia.util.FileFormat;
import cn.payadd.majia.util.FileUtils;
import cn.payadd.majia.util.ImageUtil;
import cn.payadd.majia.util.MyWebChromeClient;
import cn.payadd.majia.util.UriToPathUtil;

/**
 * Created by df on 2017/12/13.
 */

public class GoodsManagerActivity extends BaseActivity implements IActivity{
    private WebView wvContent;
    public final static int FILECHOOSER_RESULTCODE = 2;
    private static final int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 3;
    private static final int REQUEST_CAPTURE_ORIGINAL = 4;
    public ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> mUploadMessageForAndroid5;
    private boolean isAndroid5;
    private File captureFile;

    public static final String SCAN_CALLBACK = "AppCallback.setBarCode";

    private GoodsManagePresenter presenter;
    private Callback callback;
    public static final int WEBVIEW_REQUEST_CODE = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initView() {
        setTitleCenterText("商品管理");
        setTitleBackButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.include_title).setVisibility(View.GONE);
//        findViewById(R.id.include_title).setVisibility(View.GONE);
        /*WebView 配置*/
        //清空临时文件夹
        FileUtils.deleteFile(StoreDirConfig.getTempForUploadDir());
        wvContent = (WebView) findViewById(R.id.wv_content);
        wvContent.getSettings().setJavaScriptEnabled(true);
        wvContent.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wvContent.getSettings().setTextZoom(100);
        // 设置允许JS弹窗
        wvContent.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wvContent.getSettings().setDomStorageEnabled(true);
        wvContent.getSettings().setAllowFileAccessFromFileURLs(true);
        wvContent.getSettings().setAllowUniversalAccessFromFileURLs(true);
        wvContent.addJavascriptInterface(new GoodsManangerJSInterface(this,wvContent), "App");
        MyWebChromeClient myWebChromeClient = new MyWebChromeClient(this);
        myWebChromeClient.setWebCall(new MyWebChromeClient.WebCall() {

            @Override
            public void fileChose(final ValueCallback<Uri> uploadMsg) {
                captureFile = null;
                isAndroid5 = false;

                AlertDialog.Builder builder = new AlertDialog.Builder(GoodsManagerActivity.this);
                builder.setItems(new String[]{"选择文件", "使用相机拍照"}, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            mUploadMessage = uploadMsg;
                            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                            i.addCategory(Intent.CATEGORY_OPENABLE);
                            i.setType("image/*");
                            startActivityForResult(Intent.createChooser(i, "File Chooser"),
                                    FILECHOOSER_RESULTCODE);

                        } else if (which == 1) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    captureFile = new File(StoreDirConfig.getTempForUploadDir(), tag + "_" + System.currentTimeMillis() + ".jpg");
                            captureFile = new File(StoreDirConfig.getTempForUploadDir(),"temp.jpg");
                            if (captureFile.exists()) {
                                captureFile.delete();
                            }
//                    Uri uri = Uri.fromFile(captureFile);
                            Uri uri = FileProvider.getUriForFile(
                                    GoodsManagerActivity.this,
                                    getPackageName() + ".fileprovider",
                                    captureFile);
                            //为拍摄的图片指定一个存储的路径
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                            // 启动相机
                            startActivityForResult(intent, REQUEST_CAPTURE_ORIGINAL);
                        }
                        dialog.dismiss();
                    }
                });

                Dialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

            }

            @Override
            public void fileChose5(ValueCallback<Uri[]> uploadMsg) {
                mUploadMessageForAndroid5 = uploadMsg;
                isAndroid5 = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(GoodsManagerActivity.this);
                builder.setItems(new String[]{"选择文件", "使用相机拍照"}, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                            contentSelectionIntent.setType("image/*");

                            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");

                            startActivityForResult(chooserIntent,
                                    FILECHOOSER_RESULTCODE_FOR_ANDROID_5);

                        } else if (which == 1) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    captureFile = new File(StoreDirConfig.getTempForUploadDir(), tag + "_" + System.currentTimeMillis() + ".jpg");

                            captureFile = new File(StoreDirConfig.getTempForUploadDir(), System.currentTimeMillis() + ".jpg");
                            if (captureFile.exists()) {
                                captureFile.delete();
                            }
//                    Uri uri = Uri.fromFile(captureFile);
                            Uri uri = FileProvider.getUriForFile(
                                    GoodsManagerActivity.this,
                                    getPackageName() + ".fileprovider",
                                    captureFile);
                            //为拍摄的图片指定一个存储的路径
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                            // 启动相机
                            startActivityForResult(intent, REQUEST_CAPTURE_ORIGINAL);
                        }
                        dialog.dismiss();
                    }
                });
                Dialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if(!isAndroid5){
                            mUploadMessage.onReceiveValue(null);
                            mUploadMessage = null;
                        }else {
                            mUploadMessageForAndroid5.onReceiveValue(new Uri[] {});
                            mUploadMessageForAndroid5 = null;
                        }
                    }
                });
                dialog.show();
            }
        });

        wvContent.setWebChromeClient(myWebChromeClient);
        wvContent.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                setTitleCenterText(view.getTitle());
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        if (url.contains("jquery.js")) {
                            Log.i("result", url);
                            return new WebResourceResponse("application/x-javascript", "utf-8", GoodsManagerActivity.this.getAssets().open("js/jquery.min.js"));
                        }
                        if (url.contains("iscroll.js")) {
                            Log.i("result", url);
                            return new WebResourceResponse("application/x-javascript", "utf-8", GoodsManagerActivity.this.getAssets().open("js/iscroll.js"));
                        }
                        if (url.contains("jquery-form.js")) {
                            Log.i("result", url);
                            return new WebResourceResponse("application/x-javascript", "utf-8", GoodsManagerActivity.this.getAssets().open("js/jquery-form.js"));
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("result", "加载本地js错误：" + e.toString());
                }
                return super.shouldInterceptRequest(view, url);
            }
        });
    }


    @Override
    public void initData() {
        presenter = new GoodsManagePresenter(this);
        presenter.getPlatformUrl();

        if (getIntent().hasExtra("callback")) {
            callback = (Callback) getIntent().getSerializableExtra("callback");
        }

    }

    @Override
    protected void initPermission() {

    }

    public WebView getWvContent() {
        return wvContent;
    }

    public void setWvContent(WebView wvContent) {
        this.wvContent = wvContent;
    }
    // 设置回退
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wvContent.canGoBack()) {
            wvContent.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent intent){
        if (requestCode == WEBVIEW_REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != intent) {
                Bundle responseBundle = intent.getExtras();
                if (responseBundle == null) {
                    return;
                }
                if (responseBundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = responseBundle.getString(CodeUtils.RESULT_STRING);
                    wvContent.loadUrl("javascript:" + SCAN_CALLBACK + "('" + result + "')");
                }
            }
        }else if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != RESULT_OK ? null
                    : intent.getData();
            if(result!=null){
                File file = new File(UriToPathUtil.getRealFilePath(this,result));
                String name = file.getName();
                boolean flag = false;
                File save = null;
                if (FileFormat.isImage(name)) {
                    int indexOf = name.lastIndexOf(".");
                    String newName = name.substring(0, indexOf) + "_c" + name.substring(indexOf);
                    save = new File(StoreDirConfig.getTempForUploadDir(), newName);
                    flag = ImageUtil.compress(file, save);
                }
                if(flag){
                    mUploadMessage.onReceiveValue(Uri.fromFile(save));
                }else {
                    mUploadMessage.onReceiveValue(result);
                }
                mUploadMessage = null;
            }else {
                mUploadMessage.onReceiveValue(result);
            }
        } else if (requestCode == FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
            if (null == mUploadMessageForAndroid5)
                return;
            Uri result = (intent == null || resultCode != RESULT_OK) ? null
                    : intent.getData();
            if (result != null) {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[] { result });
            } else {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[] {});
            }
            mUploadMessageForAndroid5 = null;
        }else if(requestCode == REQUEST_CAPTURE_ORIGINAL){

            if (null != captureFile && captureFile.exists()) {
                String name = captureFile.getName();
                int indexOf = name.lastIndexOf(".");
                String newName = name.substring(0, indexOf) + "_c" + name.substring(indexOf);
                File newFile = new File(StoreDirConfig.getTempForUploadDir(), newName);
                boolean flag = ImageUtil.zoomAndCompress(captureFile, newFile);
                if(!isAndroid5){
                    if(flag){
                        mUploadMessage.onReceiveValue(Uri.fromFile(newFile));
                    }else {
                        mUploadMessage.onReceiveValue(Uri.fromFile(captureFile));
                    }
                    mUploadMessage = null;
                }else {
                    if(flag){
                        mUploadMessageForAndroid5.onReceiveValue(new Uri[] {Uri.fromFile(newFile)});
                    }else {
                        mUploadMessageForAndroid5.onReceiveValue(new Uri[] {Uri.fromFile(captureFile)});
                    }
                    mUploadMessageForAndroid5 = null;
                }

            }else{
                if(isAndroid5){
                    mUploadMessageForAndroid5.onReceiveValue(new Uri[] {});
                    mUploadMessageForAndroid5 = null;
                }else {
                    mUploadMessage.onReceiveValue(null);
                }

            }
        }
    }

    @Override
    public void updateModel(String key, Object data) {
        if (AppService.GET_GOOD_MANAGER_URL.equals(key)) {
            Map<String, String> respData = (Map<String, String>) data;
            String respCode = respData.get("respCode");
//            String url = "http://192.168.1.30:8020/agentBg/html/home.html";
            if ("000000".equals(respCode)) {
//                String url = respData.get("goodsManagementUrl");
//                Uri uri = Uri.parse(url);
                String sessionToken = respData.get("sessionToken");
//                wvContent.loadUrl(url + sessionToken);
                Log.d("goodsMan", ResourceConstants.getGoodsManageURI() + "?sessionToken=" + sessionToken);
                wvContent.loadUrl(ResourceConstants.getGoodsManageURI() + "?sessionToken=" + sessionToken);
            }
        }
    }

    @Override
    protected void onDestroy() {
        FileUtils.deleteFile(StoreDirConfig.getTempForUploadDir());
        if(callback!=null){
            callback.exec();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public interface Callback extends Serializable {
        void exec();
    }

}

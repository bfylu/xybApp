package cn.payadd.majia.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.fdsj.credittreasure.R;
import com.zhy.http.okhttp.utils.L;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.payadd.majia.config.AppConfig;
import cn.payadd.majia.config.StoreDirConfig;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.presenter.InstallmentFilePresenter;
import cn.payadd.majia.task.FileUpdateTask;
import cn.payadd.majia.util.AppLog;
import cn.payadd.majia.util.FileFormat;
import cn.payadd.majia.util.FileUtils;
import cn.payadd.majia.util.ImageUtil;

/**
 * 上传签约资料
 * Created by zhengzhen.wang on 2017/6/8.
 */

public class UploadSignInfoActivity extends BaseActivity implements IActivity, View.OnClickListener {

    public static final String LOG_TAG = "UploadSignInfoActivity";

    public static final String KEY_ORDER_NO = "orderNo";

    public static final String TAG_SCENE = "scene";

    public static final String TAG_HAND = "hand";

    public static final String TAG_CONTRACT = "contract";

    private static final int REQUEST_SELECT_FILE = 0;

    private static final int REQUEST_CAPTURE_THUMBNAIL = 1;

    private static final int REQUEST_CAPTURE_ORIGINAL = 2;

    public static final int HANDLER_UPLOAD_FINISH = 1;

    public static final int HANDLER_SHOW_IMAGE = 2;

    private ExecutorService es = Executors.newSingleThreadExecutor();

    private String orderNo;

    private InstallmentFilePresenter filePresenter;

    private LinearLayout llContract;
    private RelativeLayout currentContractView;
    private LayoutInflater inflater;

    private File captureFile;
    private RelativeLayout uploadViewLayout;
    private Map<String, String> tagUrlMap;
    private List<FileUpdateTask> taskQueue;
    private String tag;
    private boolean isUpload;
    private Map<String, View> tagViewMap;
    private ImageView ivScene, ivHand;
    private List<String> contractNameList;
    private Map<String, RelativeLayout> contractNameViewMap;
    private View pendingDeleteView;

    private ScrollView svPanel;

    private Map<String, Boolean> flagMap;

    {
        tagUrlMap = new HashMap<>();
        tagUrlMap.put(TAG_SCENE, AppConfig.getInstallmentUploadInterface() + "/userContractPic");
        tagUrlMap.put(TAG_HAND, AppConfig.getInstallmentUploadInterface() + "/signedPic");
        tagUrlMap.put(TAG_CONTRACT, AppConfig.getInstallmentUploadInterface() + "/contractContentPic");

        contractNameList = new ArrayList<>();
        contractNameList.add("contraceContentPic1");
        contractNameList.add("contraceContentPic2");
        contractNameList.add("contraceContentPic3");
        contractNameList.add("contraceContentPic4");
        contractNameList.add("contraceContentPic5");

        flagMap = new HashMap<>();
        flagMap.put(TAG_SCENE, false);
        flagMap.put(TAG_HAND, false);
        flagMap.put(TAG_CONTRACT, false);

        tagViewMap = new HashMap<>();
        contractNameViewMap = new HashMap<>();

        taskQueue = new ArrayList<>();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_upload_sign_info;
    }

    @Override
    public void initView() {

        setTitleCenterText("上传签约资料");
        setTitleBackButton();

        inflater = LayoutInflater.from(this);

        llContract = (LinearLayout) findViewById(R.id.ll_contract);
        ivScene = (ImageView) findViewById(R.id.iv_scene);
        ivHand = (ImageView) findViewById(R.id.iv_hand);
        svPanel = (ScrollView) findViewById(R.id.sv_panel);

        findViewById(R.id.btn_submit).setOnClickListener(this);

    }

    @Override
    public void initData() {

        orderNo = getIntent().getStringExtra(KEY_ORDER_NO);

        filePresenter = new InstallmentFilePresenter(this);

        filePresenter.showSignInfo(orderNo);
    }

    @Override
    protected void initPermission() {

    }

    public void addContract(View v) {

        if (contractNameList.isEmpty()) {
            Toast.makeText(this, "最多只能上传5张合同", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = contractNameList.get(0);
        tag = name;
        addContract(name, false);
    }

    public void addContract(String name, boolean srcNetwork) {

        RelativeLayout contract = (RelativeLayout) inflater.inflate(R.layout.view_installment_upload_add, null);
        llContract.addView(contract);

        if (null != currentContractView) {
            // 把原来那边图片的“增加”按纽去掉，删除按纽显示
            LinearLayout ll = (LinearLayout) currentContractView.getChildAt(2);
            ll.getChildAt(0).setVisibility(View.VISIBLE);
            ll.getChildAt(1).setVisibility(View.GONE);
        } else {
            // 如果是第一张，“删除”按纽不要
            LinearLayout ll = (LinearLayout) contract.getChildAt(2);
            ll.getChildAt(0).setVisibility(View.GONE);
        }

        if (srcNetwork) {
            contract.setTag(R.id.is_src_network, srcNetwork);
        }
        contract.setTag(R.id.tag_name, name);
        contract.getChildAt(1).setTag(R.id.tag_name, name);
        currentContractView = contract;
        contractNameViewMap.put(name, contract);
        contractNameList.remove(name);

    }

    public void deleteContract(View v) {

        pendingDeleteView = v;
        RelativeLayout rl = (RelativeLayout) v.getParent().getParent();
        Boolean isSrcNetwork = (Boolean) rl.getTag(R.id.is_src_network);
        String name = (String) rl.getTag(R.id.tag_name);

        if (null != isSrcNetwork && isSrcNetwork) {
            filePresenter.deleteContract(orderNo, name);
            return;
        }

        deleteContractLayout(name);
    }

    public void deleteContractLayout(String name) {

        llContract.removeView(((View) pendingDeleteView.getParent().getParent()));
        int childCount = llContract.getChildCount();
        currentContractView = (RelativeLayout) llContract.getChildAt(childCount - 1);
        if (childCount > 1) {
            LinearLayout ll = (LinearLayout) currentContractView.getChildAt(2);
            ll.getChildAt(0).setVisibility(View.VISIBLE);
            ll.getChildAt(1).setVisibility(View.VISIBLE);
        } else {
            LinearLayout ll = (LinearLayout) currentContractView.getChildAt(2);
            ll.getChildAt(0).setVisibility(View.GONE);
            ll.getChildAt(1).setVisibility(View.VISIBLE);
        }
        contractNameList.add(name);

        boolean flag = false;
        for (int i = 0, j = llContract.getChildCount(); i < j; i++) {
            RelativeLayout rl = (RelativeLayout) llContract.getChildAt(i);
            ImageView iv = (ImageView) rl.getChildAt(0);
            Boolean b = (Boolean) iv.getTag(R.id.exist_image);
            if (null != b && b) {
                flag = true;
                break;
            }
        }
        flagMap.put(TAG_CONTRACT, flag);
        checkSubmitStatus();
    }

    @Override
    public void updateModel(String key, final Object data) {

        try {
            if ("sign".equals(key)) {

                try {
                    JSONObject jsonObj = (JSONObject) data;
                    String userContractPic = jsonObj.getString("userContractPic");
                    showUploadImage(ivScene, userContractPic);
                    if (!TextUtils.isEmpty(userContractPic)) {
                        flagMap.put(TAG_SCENE, true);
                    }

                    String signedPic = jsonObj.getString("signedPic");
                    showUploadImage(ivHand, signedPic);
                    if (!TextUtils.isEmpty(signedPic)) {
                        flagMap.put(TAG_HAND, true);
                    }

                    JSONObject contractList = jsonObj.getJSONObject("contractContentPic");
                    if (null != contractList && contractList.length() > 0) {
                        flagMap.put(TAG_CONTRACT, true);
                        Iterator<String> itrs = contractList.keys();
                        while (itrs.hasNext()) {
                            String name = itrs.next();
                            String srcName = name;
                            if (name.endsWith("_c")) {
                                name = name.substring(0, name.indexOf("_c"));
                            }
                            if (!contractNameList.contains(name)) {
                                AppLog.e(LOG_TAG, "命名【" + name + "】不合规则");
                                continue;
                            }
                            addContract(name, true);
                            showUploadImage(contractNameViewMap.get(name), contractList.getString(srcName));
                        }
                    } else {
                        addContract(null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (llContract.getChildCount() == 0) {
                        addContract(null);
                    }
                } finally {
                    checkSubmitStatus();
                }


            } else if ("deleteContract".equals(key)) {
                Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
                deleteContractLayout((String) pendingDeleteView.getTag(R.id.tag_name));

            } else if ("saveContract".equals(key)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("签约资料提交成功！").setMessage("签约成功后如有首付金额，记得向顾客收取首付哦。").setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
                builder.create().show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void upload(View v) {

        final String name = (String) v.getTag(R.id.tag_name);
        tag = (String) v.getTag();
        uploadViewLayout = (RelativeLayout) v.getParent();
        captureFile = null;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(new String[]{"选择文件", "使用相机拍照"}, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    //系统调用Action属性
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_SELECT_FILE);

                } else if (which == 1) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    String filename = name;
                    if (TextUtils.isEmpty(filename)) {
//                        filename = tag + "_" + System.currentTimeMillis() + ".jpg";
                        filename = tag + ".jpg";
                    } else {
                        filename += ".jpg";
                    }
                    captureFile = new File(StoreDirConfig.getTempForUploadDir(), filename);
                    if (captureFile.exists()) {
                        captureFile.delete();
                    }
//                    Uri uri = Uri.fromFile(captureFile);
                    AppLog.d(LOG_TAG, "provider: " +  getPackageName() + ".fileprovider");
                    Uri uri = FileProvider.getUriForFile(
                            UploadSignInfoActivity.this,
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        tagViewMap.put(tag, uploadViewLayout);

        if (requestCode == REQUEST_SELECT_FILE) {

            if (null == data) {
                return;
            }
            Uri uri = data.getData();
            String path = FileUtils.getPath(this, uri);
            File file = new File(path);
            String name = file.getName();
            File f = null;
            int indexOf = name.lastIndexOf(".");
            String tagName = (String) uploadViewLayout.getTag(R.id.tag_name);
            String newName = tagName + "_c" + name.substring(indexOf);
            File newFile = new File(StoreDirConfig.getTempForUploadDir(), newName);
            ImageUtil.zoomAndCompress(file, newFile);
            f = newFile;

            Map<String, Object> params = new HashMap<>();
            params.put("orderNo", orderNo);
            params.put("files", f);
            FileUpdateTask task = new FileUpdateTask(this, tag, handlerUI);
            task.setParamsMap(params);
            task.setActionUrl(tagUrlMap.get(tag));
            taskQueue.add(task);
            startUpload();
            showUploadImage(uploadViewLayout, f);

        } else if (requestCode == REQUEST_CAPTURE_ORIGINAL) {

            if (null != captureFile && captureFile.exists()) {

                FileUpdateTask task = new FileUpdateTask(this, tag, handlerUI);
                Map<String, Object> params = new HashMap<>();
                String name = captureFile.getName();
                int indexOf = name.lastIndexOf(".");
                String newName = name.substring(0, indexOf) + "_c" + name.substring(indexOf);
                File newFile = new File(StoreDirConfig.getTempForUploadDir(), newName);
                ImageUtil.zoomAndCompress(captureFile, newFile);
                params.put("orderNo", orderNo);
                params.put("files", newFile);
                task.setParamsMap(params);
                task.setActionUrl(tagUrlMap.get(tag));
                taskQueue.add(task);
                startUpload();
                showUploadImage(uploadViewLayout, newFile);
            }
        }
    }

    public void startUpload() {

        if (!isUpload) {
            if (null != taskQueue && !taskQueue.isEmpty()) {
                FileUpdateTask task = taskQueue.remove(0);
                task.execute(new String[]{});
            }
        }
    }

    private Handler handlerUI = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case HANDLER_UPLOAD_FINISH: {
                    Map<String, Object> map = (Map<String, Object>) msg.obj;
                    String uploadStatus = (String) map.get("uploadStatus");
                    FileUpdateTask task = (FileUpdateTask) map.get("taskObject");
                    if ("success".equals(uploadStatus)) {
                        RelativeLayout rl = (RelativeLayout) tagViewMap.get(task.getTag());
                        setUploadTip(rl, "上传完成");
                        flagMap.put(task.getTag(), true);
                        ImageView iv = (ImageView) rl.getChildAt(0);
                        iv.setTag(R.id.exist_image, true);
                        checkSubmitStatus();
                    } else {
                        setUploadTip(tagViewMap.get(task.getTag()), "上传失败");
                    }

                    isUpload = false;
                    if (!taskQueue.isEmpty()) {
                        startUpload();
                    }
                    break;
                }

                case HANDLER_SHOW_IMAGE: {

                    Object[] obj = (Object[]) msg.obj;
                    ImageView iv = (ImageView) obj[0];
                    Bitmap bitmap = (Bitmap) obj[1];
                    iv.setImageBitmap(bitmap);
                    iv.setTag(R.id.exist_image, true);
                    break;
                }
            }
        }
    };


    /**
     * 设置上传提示信息
     * @param v
     * @param txtInfo
     */
    public void setUploadTip(View v, String txtInfo) {

        RelativeLayout rl = (RelativeLayout) v;
        TextView tv = (TextView) rl.getChildAt(1);
        tv.setText(txtInfo);
    }

    public void showUploadImage(View v, File file) {

        RelativeLayout rl = (RelativeLayout) v;
        ImageView iv = (ImageView) rl.getChildAt(0);
        Bitmap bitmap = ImageUtil.zoom(file, iv.getWidth(), iv.getHeight());
        iv.setImageBitmap(bitmap);
    }

    public void showUploadImage(final View v, final String imgUrl) {

        AppLog.d(LOG_TAG, "imgUrl -> " + imgUrl);

        if (TextUtils.isEmpty(imgUrl)) {
            return;
        }

        es.execute(new Runnable() {
            @Override
            public void run() {
                InputStream input = null;

                try {

                    URL url = new URL(imgUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    input = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(input);
                    ImageView iv = null;
                    if (v instanceof ImageView) {
                        iv = (ImageView) v;
                    } else {
                        RelativeLayout rl = (RelativeLayout) v;
                        iv = (ImageView) rl.getChildAt(0);
                    }
                    bitmap = ImageUtil.zoom(bitmap, iv.getWidth(), iv.getHeight());
                    Message msg = new Message();
                    msg.what = HANDLER_SHOW_IMAGE;
                    msg.obj = new Object[]{iv, bitmap};
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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_submit: {
                String tag = (String) v.getTag();
                if ("disabled".equals(tag)) {
                    return;
                }
                filePresenter.saveSignInfo(orderNo);
                break;
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (null != es) {
            es.shutdownNow();
        }
    }

    public void checkSubmitStatus() {

        AppLog.d(LOG_TAG, flagMap.toString());
        boolean flag = true;
        Iterator<Boolean> itr = flagMap.values().iterator();
        while (itr.hasNext()) {
            if (!itr.next()) {
                flag = false;
                break;
            }
        }

        TextView tv = (TextView) findViewById(R.id.btn_submit);
        if (flag) {
//            tv.setBackground(getResources().getDrawable(R.mipmap.button_selected));
            tv.setTextColor(getResources().getColor(R.color.blue2));
            tv.setTag("enabled");
        } else {
//            tv.setBackground(getResources().getDrawable(R.mipmap.button_normal));
            tv.setTextColor(getResources().getColor(R.color.grgray));
            tv.setTag("disabled");
        }
    }

}

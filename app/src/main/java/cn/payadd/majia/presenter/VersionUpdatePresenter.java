package cn.payadd.majia.presenter;

import android.app.Activity;
import android.os.Build;
import android.widget.Toast;

import com.fdsj.credittreasure.task.UpdateManger;
import com.utils.AppUtil;
import com.utils.Enums;
import com.utils.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import cn.payadd.majia.constant.AppService2;
import cn.payadd.majia.constant.ResourceConstants;
import cn.payadd.majia.face.IActivity;
import cn.payadd.majia.face.ICallback;
import cn.payadd.majia.task.DownLoaderTask;
import cn.payadd.majia.util.FileUtils;
import cn.payadd.majia.util.StringUtil;
import cn.payadd.majia.util.ZIP;

/**
 * Created by df on 2018/1/18.
 */

public class VersionUpdatePresenter extends NewBasePresenter{

    private IActivity iActivity;

    public VersionUpdatePresenter(Activity activity) {
        super(activity);
        iActivity = (IActivity) activity;
    }

    public void updateApp(String username){
        JSONObject data = new JSONObject();
        try {
            data.put("appVerCode", AppUtil.getVersionCode(ctx));
            data.put("resVerCode", StringUtil.toInt(Utilities.getResourceVerCode(ctx)));
            data.put("sysVerCode", android.os.Build.VERSION.SDK_INT);
            data.put("sysVerName", Build.VERSION.SDK_INT + "");
            data.put("username", username);
            sendJsonToServer(AppService2.CHECK_UPDATE,data,false, new ICallback() {

                @Override
                public void exec(Object params) {
                    try {
                        JSONObject jsonObject = (JSONObject) params;
                        JSONObject response = jsonObject.getJSONObject("data");
                        if (jsonObject.getString("respCode").equals(Enums.apiState.成功.getString())) {
                            //检测Apk版本
                            if ("Y".equals(response.getString("appCheckFlag"))) {
                                UpdateManger updateManger = new UpdateManger(ctx, response.getString("appDownloadUrl"), "");
                                if ("Y".equals(response.getString("appIsForce").toUpperCase())) {
                                    updateManger.showNoticeDialog(response.getString("lastAppUpdateLog"),response.getString("lastAppVerName"));
                                } else {
                                    updateManger.showDialog(response.getString("lastAppUpdateLog"),response.getString("lastAppVerName"));
                                }
                            } else if ("N".equals(response.get("appCheckFlag"))) {
                                //检测资源版本
                                if("Y".equals(response.getString("resCheckFlag"))){
                                    DownLoaderTask downLoaderTask =  new DownLoaderTask(response.getString("resDownloadUrl"),ctx.getExternalFilesDir(null).getAbsolutePath(),ctx);
                                    downLoaderTask.execute();
                                    downLoaderTask.setOnDownloadFinishListener(new DownLoaderTask.OnDownloadFinishListener(){
                                        @Override
                                        public void onSuccess(File file) {
                                            try {
                                                //删除旧的资源包
                                                FileUtils.deleteDir(ctx.getExternalFilesDir
                                                        ("app_res").getAbsolutePath());

                                                ZIP.UnZipFolderByInputStream(new FileInputStream
                                                            (file),file.getParentFile().getAbsolutePath());
                                                //读取资源包配置文件
                                                try {
                                                    FileUtils.readResourceInfo(ctx.getExternalFilesDir
                                                            (ResourceConstants.RES_INFO_PATH).getAbsolutePath()
                                                            , ResourceConstants.RES_INFO_PATH_TEXT_NAME
                                                            , ctx);
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                                iActivity.updateModel(AppService2.CHECK_UPDATE,null);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }else{
                                    Toast.makeText(ctx,"当前已经是最新版本",Toast.LENGTH_SHORT).show();
                                    iActivity.updateModel(AppService2.CHECK_UPDATE,"success");
                                }
                            }

                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }, new ICallback() {
                @Override
                public void exec(Object params) {
                    iActivity.updateModel(AppService2.CHECK_UPDATE,"failure");
//                    Toast.makeText(ctx,"",Toast.LENGTH_SHORT).show();
                }
            }, new ICallback() {
                @Override
                public void exec(Object params) {
                     iActivity.updateModel(AppService2.CHECK_UPDATE,"exception");
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

package cn.payadd.majia.device.pos;

import org.json.JSONObject;

/**
 * Created by ming.li on 2017/8/21.
 */

public class PrintFormat {
    public static JSONObject transJson(int fontSize, String content, int lineFeedCount) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("fontSize", fontSize);
            jsonObject.put("content", content);
            jsonObject.put("lineFeedCount", lineFeedCount);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    public static JSONObject transJson(int fontSize, String content, int lineFeedCount,String align) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("fontSize", fontSize);
            jsonObject.put("content", content);
            jsonObject.put("lineFeedCount", lineFeedCount);
            jsonObject.put("align",align);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}

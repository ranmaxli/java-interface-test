package api.util;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by liran on 2017/1/17.
 */
public class JsonUtil {
    //在jsonstr中插入值，示例
    private static JSONObject createJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("classes", "张三");
        jsonObject.put("uid", "j42eao2lt4zpuhs9dvzrxq");
        jsonObject.put("uid", new Integer(26));
        jsonObject.put("knowledgeid", "");
        jsonObject.put("workdate", new Integer(26));
        jsonObject.put("lessonjson", new Integer(26));
        jsonObject.put("docids", new Integer(26));

        JSONObject innerObj = new JSONObject();
        innerObj.put("university", "某某学校");
        innerObj.put("special", "某某专业");
        jsonObject.put("education", innerObj);
        return jsonObject;
    }
    //获取jsonstr中expression值
    public static String getFastjsonString(String jsonStr, String expression) {
        JSONObject jsonObj = JSONObject.parseObject(jsonStr);
        String[] expArr = expression.split("\\.");
        JSONObject obj = jsonObj;
        if (expArr.length > 1) {
            for (int i = 0, len = expArr.length -1; i < len; i++) {
                String exp = expArr[i];
                String[] keyArr = exp.split("\\[");
                if (keyArr.length == 1) {
                    String key =  keyArr[0];
                    obj = obj.getJSONObject(key);
                } else if (keyArr.length == 2) {
                    String akey =  keyArr[0];
                    int aindx =  Integer.parseInt(keyArr[1].replace("]", ""));
                    obj = obj.getJSONArray(akey).getJSONObject(aindx);
                }
            }
            return obj.getString(expArr[expArr.length -1]);
        } else {
            return jsonObj.getString(expression);
        }
    }

    //获取json数组的长度
    public static int getFastjsonCount(String jsonStr, String expression) {
        JSONObject jsonObj = JSONObject.parseObject(jsonStr);
        String[] expArr = expression.split("\\.");
        JSONObject obj = jsonObj;
        if (expArr.length > 1) {
            for (int i = 0, len = expArr.length -1; i < len; i++) {
                String exp = expArr[i];
                String[] keyArr = exp.split("\\[");
                if (keyArr.length == 1) {
                    String key =  keyArr[0];
                    obj = obj.getJSONObject(key);
                } else if (keyArr.length == 2) {
                    String akey =  keyArr[0];
                    int aindx =  Integer.parseInt(keyArr[1].replace("]", ""));
                    obj = obj.getJSONArray(akey).getJSONObject(aindx);
                }
            }
            return obj.getJSONObject(expArr[expArr.length -1]).size();
        } else {
            return jsonObj.getJSONObject(expression).size();
        }
    }



}

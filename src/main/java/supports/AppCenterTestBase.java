package supports;

import api.util.JsonUtil;
import api.util.MongoDbHelper;
import com.alibaba.fastjson.JSONArray;
import com.github.yongchristophertang.database.annotations.SqlDB;
import com.github.yongchristophertang.database.testng.TestNGDBInjectionModuleFactory;
import com.github.yongchristophertang.engine.web.HttpResult;
import com.github.yongchristophertang.engine.web.WebTemplate;
import com.github.yongchristophertang.engine.web.WebTemplateBuilder;
import com.google.inject.Inject;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testng.annotations.Guice;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

/**
 * Created by liran on 2017/3/1
 */
@SqlDB(config = "mysql_config.properties")
@Guice(moduleFactory = TestNGDBInjectionModuleFactory.class)

public abstract class AppCenterTestBase {

    protected WebTemplate webTemplate = WebTemplateBuilder.defaultConfig().build();

    protected JdbcTemplate jdbcTemplate;

    @Inject
    protected DataSource dataSource;

    protected void initDb() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }


    /**
     * 处理httpResult,将其转换成json串
     *
     * @param httpResult 请求返回的结果.
     * @return
     * @throws IOException
     */
    protected JSONObject getResultJson(HttpResult httpResult) throws IOException {
        JSONObject jsonObject = JSONObject.fromObject(httpResult.getResponseStringContent());
        return jsonObject;
    }


    /**
     * 获取当前时间戳,将当前时间转换为long型
     *
     * @return 时间戳字符串
     */
    public static String getCurrentTimeStamp() {
        Calendar calendar = Calendar.getInstance();
        return String.valueOf(calendar.getTimeInMillis());
    }


    /**
     * 时间转换： 将字符串数据转化为毫秒数
     *
     * @return 时间戳字符串
     */
    public static Long dateStr2long(String dateStr) {
        if (dateStr != null) {
            try {
                Date date = DateUtils.parseDateStrictly(dateStr, "yyyy-MM-dd HH:mm:ss.0");
                return date.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 时间转换：日期转换为字符串 yyyy-MM-dd HH:mm:ss
     */
    public static String dateToString(Date time, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String ctime = formatter.format(time);
        return ctime;
    }

    /**
     * 时间转换：字符串转换为日期 yyyy-MM-dd HH:mm:ss
     */
    public static Date stringToDate(String dstr, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);//小写的mm表示的是分钟
        try {
            Date date = sdf.parse(dstr);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean compareNowDate(String dateStr) {
        if (dateStr != null) {
            try {
                Date date = DateUtils.parseDateStrictly(dateStr, "yyyy-MM-dd HH:mm:ss.S");
                return new Date().before(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 时间转换： "yyyy-MM-dd HH:mm:ss.S"转换为"yyyy年MM月dd日"
     *
     * @return 时间戳字符串
     */
    public static String formatDate1(String dateStr) throws ParseException {

        String pat1 = "yyyy-MM-dd HH:mm:ss.S";
        String pat2 = "yyyy年MM月dd日";
        SimpleDateFormat sdf1 = new SimpleDateFormat(pat1);
        SimpleDateFormat sdf2 = new SimpleDateFormat(pat2);

        Date d = null;
        try {
            d = sdf1.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdf2.format(d);

    }

    /**
     * 将普通字符创转换成application/x-www-from-urlencoded字符串
     *
     * @return 时间戳字符串
     */

    public String urlEncoder(String name) throws UnsupportedEncodingException {

        String urlString = URLEncoder.encode(name, "UTF-8");
        return urlString.toLowerCase();//转换为小写
    }




    public static Map<String, List<com.alibaba.fastjson.JSONObject>> groupAttachMap(JSONArray attchList) {
        Map<String, List<com.alibaba.fastjson.JSONObject>> groupAttachMap = new HashMap<String, List<com.alibaba.fastjson.JSONObject>>();
        for (int i = 0, len = attchList.size(); i < len; i++) {
            com.alibaba.fastjson.JSONObject attach = attchList.getJSONObject(i);
            String tempid = attach.getString("tempid"); // 填空题or简答题
            List<com.alibaba.fastjson.JSONObject> attachList = groupAttachMap.get(tempid);
            if (attachList == null) {
                attachList = new ArrayList<com.alibaba.fastjson.JSONObject>();
                groupAttachMap.put(tempid, attachList);
            }
            attachList.add(attach);
        }
        return groupAttachMap;
    }

    public static Map<String, List<com.alibaba.fastjson.JSONObject>> answerResultsMap(JSONArray attchList) {
        Map<String, List<com.alibaba.fastjson.JSONObject>> answerResultsMap = new HashMap<String, List<com.alibaba.fastjson.JSONObject>>();
        for (int i = 0, len = attchList.size(); i < len; i++) {
            com.alibaba.fastjson.JSONObject attach = attchList.getJSONObject(i);
            String tempid = attach.getString("IsShare"); // 填空题or简答题
            List<com.alibaba.fastjson.JSONObject> attachList = answerResultsMap.get(tempid);
            if (attachList == null) {
                attachList = new ArrayList<com.alibaba.fastjson.JSONObject>();
                answerResultsMap.put(tempid, attachList);
            }
            attachList.add(attach);
        }
        return answerResultsMap;
    }

    public static List<com.alibaba.fastjson.JSONObject> subAnswerResultsList(JSONArray attchList) {
        List<com.alibaba.fastjson.JSONObject> answerResultsList = new ArrayList<com.alibaba.fastjson.JSONObject>();
        for (int i = 0; i < attchList.size(); i++) {
            com.alibaba.fastjson.JSONObject attach = attchList.getJSONObject(i);
            boolean isShare = attach.getBoolean("IsShare"); // 填空题or简答题
            if (isShare) {
                answerResultsList.add(attach);
                continue;
            } else {
               JSONArray stuAnswerResourceList = attach.getJSONArray("SubAnswerResults");
                JSONArray stuArr = new JSONArray();
                for (int j = 0; j < stuAnswerResourceList.size(); j++) {
                    com.alibaba.fastjson.JSONObject stuAnswerResource = stuAnswerResourceList.getJSONObject(j);
                    boolean isShareSub = stuAnswerResource.getBoolean("IsShare");
                    if (isShareSub) {
                        stuArr.add(stuAnswerResource);
                    }
                }
                if (!stuArr.isEmpty()) {
                    attach.put("SubAnswerResults", stuArr);
                    answerResultsList.add(attach);
                }
            }
        }
        return answerResultsList;
    }

    //计算两个时间的差值
    public boolean daysBetween(String date,int days) throws ParseException {
        //获取输入数据的 Date格式
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = (Date) df.parse(date);
        //获取当前时间的 Date格式
        Date date2=new Date();
        df.format(date2);
        //获取差值
        int day = 0;

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1= cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        //不同年
        if(year1 != year2) {
            int timeDistance = 0 ;
            for(int i = year1 ; i < year2 ; i ++) {
                //闰年
                if(i%4==0 && i%100!=0 || i%400==0) {
                    timeDistance += 366;
                }
                //不是闰年
                else {
                    timeDistance += 365;
                }
            }
            System.out.println("不同年: " + (day2-day1));
            day = timeDistance + (day2-day1) ;
        }
        //同一年
        else {
            System.out.println("同一年: " + (day1-day2));
            day = day1-day2;
        }
        //判断时间差返回值
        if(day <= days){
            return true;
        }
        else {
            return false;
        }
    }


    public static void main(String[] args) {
        MongoDbHelper mongoDbHelper = new MongoDbHelper("StuHomework");
        String mongoJson = mongoDbHelper.findFilter(and(eq("StuWorkId", 11172049)));
        JSONArray attchJson = JSONArray.parseArray(JsonUtil.getFastjsonString(mongoJson, "Attachments"));
        Map<String, List<com.alibaba.fastjson.JSONObject>> groupAttachMap = groupAttachMap(attchJson);
        List<com.alibaba.fastjson.JSONObject> attachList = groupAttachMap.get("311839");
        if (attachList != null && !attachList.isEmpty()) {
            for (int i = 0; i < attachList.size(); i++) {
                System.out.println("i:" + attachList.get(i).getString("_id"));
            }
        }
    }

}

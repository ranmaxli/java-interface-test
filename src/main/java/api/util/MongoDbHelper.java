package api.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liran on 2016/8/17
 */
public class MongoDbHelper {
    protected static Logger logger = LoggerFactory.getLogger("MongoDbHelper");
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;
    public MongoDbHelper(String tableName){
        this.init( tableName);
    }
    public MongoDbHelper(String dbName, String tableName){
        this.init( dbName, tableName);
    }
       public  void init(String tableName) {
            // 获取链接
            this.mongoClient = new MongoClient("112.29.172.180", 27017);
            // 获取数据库
            this.database = mongoClient.getDatabase("homework");
            // 进入某个文档集
            this.collection = database.getCollection(tableName);
            //mongoClient.close();
        }

    public  void init(String dbname, String tableName) {
        // 获取链接
        this.mongoClient = new MongoClient("112.29.172.180", 27017);
        // 获取数据库
        this.database = mongoClient.getDatabase(dbname);
        // 进入某个文档集
        this.collection = database.getCollection(tableName);
        //mongoClient.close();
    }
    //获取一组数据无排序，没有排序
    public  String findFilter(Bson bson) {
        String mongoResult;
        mongoResult = collection.find(bson).first().toJson();
        return mongoResult;
    }

    //获取一组数据有排序
    public  String findFilterOrderby(Bson bson, Bson orderby) {
        String mongoResult;
        mongoResult = collection.find(bson).sort(orderby).first().toJson();
        mongoResult = collection.find().toString();
        return mongoResult;
    }
//没有查询条件，获取所有数据
    public  JSONArray findAll() {
        JSONArray jsonArray = new JSONArray();
        FindIterable<Document>  iterable = collection.find();
        iterable.forEach(new Block<Document>() {
            @Override public void apply(final Document document) {
                JSONObject json = JSON.parseObject(document.toJson());
                jsonArray.add(json);
            }
        });
             return jsonArray;
    }
    public  JSONArray findByCondition(Bson where) {
        return findByCondition(where, null);
    }

    public  JSONArray findByCondition(Bson where, Bson orderby) {
        JSONArray jsonArray = new JSONArray();

        FindIterable<Document>  iterable = collection.find(where);
        if (orderby != null) {
            iterable = iterable.sort(orderby);
        }
        iterable.forEach(new Block<Document>() {
            @Override public void apply(final Document document) {
                JSONObject json = JSON.parseObject(document.toJson());
                jsonArray.add(json);
            }
        });
        System.out.println("jsonArray = " + jsonArray.toJSONString());
        return jsonArray;
    }




    public static String getFastjsonString(String jsonStr, String expression) {
        // AnswerResults[0].SubAnswerResults[0].MainId
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

}
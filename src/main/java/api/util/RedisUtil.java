package api.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Created by liran on 2017/1/21.
 */
public class RedisUtil {
    private Jedis jedis;
    public static String workdir = System.getProperty("user.dir");
    //构造函数,创建对象时进行初始化
    public RedisUtil(int i) {
        Properties prop = new Properties();
        try {

            // 读取属性文件a.properties
            InputStream in = new BufferedInputStream(new FileInputStream(
                    workdir + "\\src\\main\\resources\\redis.properties"));
            prop.load(in); // /加载属性列表
            jedis = new Jedis(prop.getProperty("redis.host"), Integer.valueOf(prop.getProperty("redis.port")));
            jedis.auth(prop.getProperty("redis.password"));

        } catch (Exception e) {
            System.out.println(e);
        }

        jedis.select(i);//选择数据库
        }


    public String stringValue(String key){
        String type = jedis.type(key);
       // System.out.println("key:" + key + " 的类型为：" + type);
        return jedis.get(key);
    }

    public List<String> hashValue(String key){
        String type = jedis.type(key);
        String value=null;

        List<String> list = jedis.hvals(key);//hvals(key)返回哈希表 key 中所有域的值

        Iterator<String> it=list.iterator();
        while(it.hasNext()){
            value= it.next();
            // System.out.println("hash value:"+value);
            return list;
        }
        return null;
        /*获取所有的hash-key
        Set<String> set = jedis.hkeys(key);
        Iterator<String> itSet=set.iterator();
        while(itSet.hasNext()){
            value= itSet.next();
            // System.out.println("hash key:"+value);
        }
        */
    }

    public static void main(String[] args){
        RedisUtil jedis =new RedisUtil(1);

                //String score=jsonObject.getString("TotalScore");
        //System.out.println("score:"+score);
        System.out.println("值："+jedis.stringValue("urn:tredisworkclass:opsiasqnmkbpc6puiash2a:wyyradelhkbev-mvxyhjhw"));
       // System.out.println(jedis.hashValue("urn:sredishomework:--bsanqm-rjlnmzv2hhpag:-ktoanem1opl-r8c9uqltw"));
       // JSONObject jsonObject=JSONObject.parseObject(jedis.stringValue("urn:EpdCollection:1pdlawwmhklahc2hi-vp9g"));
        //System.out.println(jsonObject.getString("TypeName"));
        Set<String> setvalues=jedis.jedis.smembers("urn:myteacher:user:jzynaqylra5lfb7k-riuew");
        Iterator<String> itSet=setvalues.iterator();
        while(itSet.hasNext()){
            System.out.println("hash key:"+ itSet.next());
        }
        JSONArray jsonArray=JSONArray.parseArray(jedis.stringValue("urn:tredisworkclass:opsiasqnmkbpc6puiash2a:wyyradelhkbev-mvxyhjhw"));
        System.out.println(jsonArray.get(0));
        JSONObject jsonObject=jsonArray.getJSONObject(1);
        System.out.println(jsonObject.getString("TypeName"));
    }

    }


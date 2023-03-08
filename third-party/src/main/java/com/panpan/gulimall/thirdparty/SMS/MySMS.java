package com.panpan.gulimall.thirdparty.SMS;

import com.panpan.common.constant.AuthConst;
import lombok.Data;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author panpan
 * @create 2021-09-06 下午9:46
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.gulimall.sms")
public class MySMS {
    private  String host;
    private  String path;
    private  String method;
    private  String appcode;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RedissonClient redissonClient;

    public  void sendSMS(String phoneNum, String code) {
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", phoneNum);
        querys.put("param", "code:"+code);
        querys.put("tpl_id", "TP1711063");
        Map<String, String> bodys = new HashMap<String, String>();
        RLock lock = redissonClient.getLock(phoneNum + code);
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        // 防止远程超时重复调用
        try { lock.lock(20l,TimeUnit.SECONDS);
        if (StringUtils.isEmpty(ops.get(AuthConst.SMS_CODE_PREFIX.getValue() +phoneNum))){
                /**
                 * 重要提示如下:
                 * HttpUtils请从
                 * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
                 * 下载
                 * 相应的依赖请参照
                 * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
                 */
//                 HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
//                获取response的body
//                if(response.getStatusLine().getStatusCode()==200){
//                    ops.set(AuthConst.SMS_CODE_PREFIX.getValue() +phoneNum,code,59l, TimeUnit.SECONDS);
//                }

            ops.set(AuthConst.SMS_CODE_PREFIX.getValue() +phoneNum,code,80l, TimeUnit.SECONDS);

        }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}

package com.msb.apipsaaenger.service;

import com.msb.apipsaaenger.remote.ServiceVefificationcodeClient;
import com.msb.common.dto.ResponseResult;
import com.msb.common.responese.NumberCodeResponse;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class VerificationCodeService {

    @Autowired
    ServiceVefificationcodeClient serviceVefificationcodeClient;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private String vefificationcodePrefix = "passenger-vefification-code-";

    public ResponseResult generatorCode(String passengerPhone){
        // 调用验证码服务，获取验证码
        System.out.println("调用验证码服务，获取验证码");
        ResponseResult<NumberCodeResponse> numberCodeResponse = serviceVefificationcodeClient.getNumberCode(6);
        int numberCode = numberCodeResponse.getData().getNumberCode();
        // 存入redis
        System.out.println("numberCode:" + numberCode);
        System.out.println("存入redis");
        // key,value,过期时间
        String key = vefificationcodePrefix+passengerPhone;

        stringRedisTemplate.opsForValue().set(key,numberCode+"",2, TimeUnit.MINUTES);

        JSONObject result = new JSONObject();
        result.put("code",200);
        result.put("message","success");
        return ResponseResult.success("");
    }
}

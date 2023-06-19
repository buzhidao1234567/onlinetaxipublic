package com.msb.apipsaaenger.service;

import com.msb.apipsaaenger.remote.ServiceVefificationcodeClient;
import com.msb.common.dto.ResponseResult;
import com.msb.common.responese.NumberCodeResponse;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificationCodeService {

    @Autowired
    ServiceVefificationcodeClient serviceVefificationcodeClient;

    public String generatorCode(String passengerPhone){
        // 调用验证码服务，获取验证码
        System.out.println("调用验证码服务，获取验证码");
        ResponseResult<NumberCodeResponse> numberCodeResponse = serviceVefificationcodeClient.getNumberCode(6);
        int numberCode = numberCodeResponse.getData().getNumberCode();
        // 存入redis
        System.out.println("numberCode:" + numberCode);
        System.out.println("存入redis");

        JSONObject result = new JSONObject();
        result.put("code",200);
        result.put("message","success");
        return result.toString();
    }
}

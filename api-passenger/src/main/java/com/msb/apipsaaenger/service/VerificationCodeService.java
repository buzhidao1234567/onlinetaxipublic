package com.msb.apipsaaenger.service;

import com.msb.apipsaaenger.remote.ServicePassengerUserClient;
import com.msb.apipsaaenger.remote.ServiceVefificationcodeClient;
import com.msb.common.constant.CommonStatusEnum;
import com.msb.common.dto.ResponseResult;
import com.msb.common.request.VerificationCodeDTO;
import com.msb.common.responese.NumberCodeResponse;
import com.msb.common.responese.TokenResponse;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
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
    @Autowired
    private ServicePassengerUserClient servicePassengerUserClient;

    private String vefificationcodePrefix = "passenger-vefification-code-";

    /**
     * 生成验证码
     * @param passengerPhone
     * @return
     */
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
        result.put("data",numberCode);
        return ResponseResult.success(result);
    }

    /**
     * 校验验证码
     * @param passengerPhone
     * @param verificationCode
     * @return
     */
    public ResponseResult checkCode(String passengerPhone, String verificationCode) {
        // 根据手机号，去redis读取验证码
        // 生成key
        System.out.println("根据手机号，去redis读取验证码");
//        String key = RedisPrefixUtils.generatorKeyByPhone(passengerPhone,IdentityConstants.PASSENGER_IDENTITY) ;
        String key2 = generatorKeyByPhone(passengerPhone) ;

        // 根据key获取value
        String codeRedis = stringRedisTemplate.opsForValue().get(key2);
        System.out.println("redis中的value："+codeRedis);

        // 校验验证码
        System.out.println("根据手机号，去redis读取验证码");
        if (StringUtils.isBlank(codeRedis)){
            return ResponseResult.fail(CommonStatusEnum.VERIFICATION_CODE_ERROR.getCode(),CommonStatusEnum.VERIFICATION_CODE_ERROR.getValue());
        }
        if (!verificationCode.trim().equals(codeRedis.trim())){
            return ResponseResult.fail(CommonStatusEnum.VERIFICATION_CODE_ERROR.getCode(),CommonStatusEnum.VERIFICATION_CODE_ERROR.getValue());
        }

        // 判断原来是否有用户，并进行对应的处理
        System.out.println("判断原来是否有用户，并进行对应的处理");
        VerificationCodeDTO verificationCodeDTO = new VerificationCodeDTO();
        verificationCodeDTO.setPassengerPhone(passengerPhone);
        servicePassengerUserClient.loginOrRegister(verificationCodeDTO);

//        // 颁发令牌，不应该用魔法值，用常量
//        String accessToken = JwtUtils.generatorToken(passengerPhone, IdentityConstants.PASSENGER_IDENTITY, TokenConstants.ACCESS_TOKEN_TYPE);
//        String refreshToken = JwtUtils.generatorToken(passengerPhone, IdentityConstants.PASSENGER_IDENTITY ,TokenConstants.REFRESH_TOKEN_TYPE);
//
//        // 将token存到redis当中
//        String accessTokenKey = RedisPrefixUtils.generatorTokenKey(passengerPhone , IdentityConstants.PASSENGER_IDENTITY , TokenConstants.ACCESS_TOKEN_TYPE);
//        stringRedisTemplate.opsForValue().set(accessTokenKey , accessToken , 30, TimeUnit.DAYS);
//
//        String refreshTokenKey = RedisPrefixUtils.generatorTokenKey(passengerPhone , IdentityConstants.PASSENGER_IDENTITY , TokenConstants.REFRESH_TOKEN_TYPE);
//        stringRedisTemplate.opsForValue().set(refreshTokenKey , refreshToken , 31, TimeUnit.DAYS);
//
//        // 响应
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken("accessToken");
        tokenResponse.setRefreshToken("refreshToken");
        return ResponseResult.success(tokenResponse);
    }

    public  String generatorKeyByPhone(String phone){
        return vefificationcodePrefix + phone;
    }
}

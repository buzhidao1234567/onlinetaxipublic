package com.msb.apipsaaenger.controller;

import com.msb.apipsaaenger.request.VerificationCodeDTO;
import com.msb.apipsaaenger.service.VerificationCodeService;
import com.msb.common.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VerificationCodeController {

    @Autowired
    private VerificationCodeService verificationCodeService;

    @GetMapping("/verification-code")
    public ResponseResult verificationCode(@RequestBody VerificationCodeDTO verificationCodeDTO){
        String passengerPhone = verificationCodeDTO.getPassengerPhone();
        System.out.println("接受手机号为：" + passengerPhone);
        return verificationCodeService.generatorCode(passengerPhone);
    }

}

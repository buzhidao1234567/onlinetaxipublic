package com.msb.common.responese;

import lombok.Data;

@Data
public class TokenResponse {

    private String accessToken;

    private String refreshToken;
}

package com.example.backend.model.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JWTLoginResponse {

    private String accessToken;
    private String tokenType = "Bearer";
}
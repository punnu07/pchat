package com.example.pchat;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppConfig {


    private static String BASE_URL = "https://www.flarespeech.com";
    static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}

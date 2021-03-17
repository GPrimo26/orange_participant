package com.maksat.participantapp.interfaces;

import com.maksat.participantapp.Variables;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Server {
    public static <T> T GetServer(Class<T> unknownClass) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request newRequest = chain.request().newBuilder()
                            .build();
                    return chain.proceed(newRequest);
                }).build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(Variables.ip)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(unknownClass);
    }

    public static <T> T GetServerWithToken(Class<T> unknownClass, String token) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + token)
                            .build();
                    return chain.proceed(newRequest);
                }).build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(Variables.ip)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(unknownClass);
    }
}

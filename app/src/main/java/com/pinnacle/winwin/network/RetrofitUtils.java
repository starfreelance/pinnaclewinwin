package com.pinnacle.winwin.network;

import com.pinnacle.winwin.BuildConfig;
import com.pinnacle.winwin.app.AppConstant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {

    private static RetrofitUtils retrofitUtils;
    private ApiCalls apiCalls;
    private Retrofit retrofit;

    private RetrofitUtils(String baseUrl) {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClientInstance())
                .build();

        apiCalls = retrofit.create(ApiCalls.class);
    }

    private static HttpLoggingInterceptor getInterceptor() {
        if (BuildConfig.DEBUG) {
            return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE);
        }
    }

    private static OkHttpClient getOkHttpClientInstance() {
        return new OkHttpClient.Builder()
                .connectTimeout(AppConstant.TIME_OUT_PERIOD, TimeUnit.MINUTES)
                .readTimeout(AppConstant.TIME_OUT_PERIOD, TimeUnit.MINUTES)
                .writeTimeout(AppConstant.TIME_OUT_PERIOD, TimeUnit.MINUTES)
                .addInterceptor(getInterceptor())
                /*.addInterceptor(new CryptoInterceptor())*/
                .build();
    }

    public static synchronized RetrofitUtils getRetrofitInstance(String baseUrl) {
        if (retrofitUtils == null) {
            retrofitUtils = new RetrofitUtils(baseUrl);
        }

        return retrofitUtils;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public ApiCalls getApiCalls() {
        return apiCalls;
    }
}

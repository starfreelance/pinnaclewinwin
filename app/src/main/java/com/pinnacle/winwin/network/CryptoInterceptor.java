package com.pinnacle.winwin.network;

import com.pinnacle.winwin.security.RSAUtility;
import com.pinnacle.winwin.utils.LogUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public class CryptoInterceptor implements Interceptor {

    private static final String TAG = CryptoInterceptor.class.getSimpleName();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        RequestBody oldBody = request.body();
        Buffer buffer = new Buffer();
        oldBody.writeTo(buffer);
        String strOldBody = buffer.readUtf8();
//        MediaType mediaType = request.body().contentType();
        String strNewBody = RSAUtility.encrypt(strOldBody, RSAUtility.publicKey);
        LogUtils.e(TAG, strNewBody);
        /*RequestBody body = RequestBody.create(mediaType, strNewBody);
        request.newBuilder().post(body);*/

        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), strNewBody);
        request = request.newBuilder()
                .post(requestBody)
                .build();



        return chain.proceed(request);
    }
}

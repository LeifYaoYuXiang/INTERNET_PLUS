package com.example.demo0426;


import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpClientHelper {
    public static String idInformation;
    public static String imageInformation;

    public static void sendWithOKHttpImage(Uri uri){
        OkHttpClient client=new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(60 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout( 60 * 1000, TimeUnit.MILLISECONDS)
                .writeTimeout(60 * 1000, TimeUnit.MILLISECONDS).build();


        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.ALTERNATIVE);
        File file = null;
        try {
            file = new File(new URI(uri.toString()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (file != null) {
            Log.i("TAG","File Not Null");
            RequestBody fileBody=RequestBody.create(MediaType.parse("image/jpg"), file);
            builder.addFormDataPart("CameraImages", file.getName(), fileBody);
        }
        Log.i("TAG","information of file: "+file.getName()+" "+file.length());
        MultipartBody multipartBody = builder.build();
        final Request request = new Request.Builder()
                .url("http://172.21.12.229:8500/uploadimage")//请求地址
                .post(multipartBody)
                .addHeader("Connection","close")//添加请求体参数
                .build();



            Call call = client.newCall(request);
            OKHttpCallbackImage okHttpCallbackImage=new OKHttpCallbackImage();
            call.enqueue(okHttpCallbackImage);
            imageInformation=okHttpCallbackImage.getResponseStr();
        }

    public static String[] sendWithOKHttpArea(int id, final Uri uri){
        OkHttpClient client=new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(60 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout( 60 * 1000, TimeUnit.MILLISECONDS)
                .writeTimeout(60 * 1000, TimeUnit.MILLISECONDS).build();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.ALTERNATIVE);
        builder.addFormDataPart("id",id+"");
        MultipartBody multipartBody = builder.build();
        final Request request = new Request.Builder()
                .url("http://172.21.12.229:8500/loadarea")//请求地址
                .post(multipartBody)
                .addHeader("Connection","close")//添加请求体参数
                .build();
        Call call = client.newCall(request);
        OKHttpCallbackID okHttpCallback=new OKHttpCallbackID(uri);
        call.enqueue(okHttpCallback);
        idInformation=okHttpCallback.getResponseStr();
        String[] feedBack=new String[]{idInformation,imageInformation};
        return feedBack;
    }

}

class OKHttpCallbackID implements Callback {
    private String ResponseStr;
    private Uri uri;
    public OKHttpCallbackID(Uri uri){
        this.uri=uri;
    }
    @Override
    public void onFailure(Call call, IOException e) {
        e.printStackTrace();
        Log.e("TAG","上传失败"+e.getLocalizedMessage());
        ResponseStr=null;
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        ResponseStr=response.body().string();
        Log.e("TAG","上传成功"+ResponseStr);
        HttpClientHelper.sendWithOKHttpImage(uri);
    }

    public String getResponseStr(){
        return this.ResponseStr;
    }
}

class OKHttpCallbackImage implements Callback{
    private String ResponseStr;
    @Override
    public void onFailure(Call call, IOException e) {
        e.printStackTrace();
        Log.e("TAG","上传失败"+e.getLocalizedMessage());
        ResponseStr=null;
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        ResponseStr=response.body().string();
        Log.e("TAG","上传成功"+ResponseStr);
    }

    public String getResponseStr(){
        return this.ResponseStr;
    }
}

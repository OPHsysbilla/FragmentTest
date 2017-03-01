package shu.fragmenttest.network;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import shu.fragmenttest.UploadClothItemActivity;

/**
 * Created by eva on 2017/2/24.
 */

public class useOkhttp {
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private static final MediaType NORMAL_CONTENT_TYPE = MediaType.parse("application/x-www-form-urlencoded");
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    static public OkHttpClient okHttpClient = new OkHttpClient();

    static public void postJson(String url,String json) {
        //申明给服务端传递一个json串
        //创建一个OkHttpClient对象
        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
        RequestBody requestBody = RequestBody.create(JSON, json);
        //创建一个请求对象
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        //发送请求获取响应
        try {
//            Response response = okHttpClient.newCall(request).execute();
            okHttpClient.newBuilder()
                    .readTimeout(5000, TimeUnit.MILLISECONDS)
                    .build()
                    .newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("postJson success:", "failed");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //判断请求是否成功
                    if (response.isSuccessful()) {
                        //打印服务端返回结果
                        Log.e("postJson success:", response.body().string());
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //异步
    public static void post_file(final String url, final Map<String, Object> map, String sendFilePath) {
        File file = new File(sendFilePath);
        OkHttpClient client = new OkHttpClient();
        // form 表单形式上传

        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (file != null) {
            // MediaType.parse() 里面是上传的文件类型。
            RequestBody body = RequestBody.create(MediaType.parse("image/jpg"), file);
            String filename = file.getName();
            // 参数分别为， 请求key ，文件名称 ， RequestBody
            requestBody.addFormDataPart("headImage", filename, body);
        }

//        if (map != null) {
//            // map 里面是请求中所需要的 key 和 value
//            for (Map.Entry entry : map.entrySet()) {
//                Log.d("requestBody:::::",valueOf(entry.getKey())+valueOf(entry.getValue()));
////                requestBody.addFormDataPart(valueOf(entry.getKey()), valueOf(entry.getValue()));
//                requestBody.addPart(Headers.of("Content-Disposition", "form-data; name=\"" +valueOf(entry.getKey()) + "\""),
//                        RequestBody.create(null, valueOf(entry.getValue())));
//            }
//        }
//        if(file != null) {
//            RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
//            requestBody.addPart(Headers.of("Content-Disposition",
//                    "form-data; name=\"" + "headImage" + "\"; filename=\"" + file.getName() + "\""),
//                    fileBody);
//        }

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody.build())
                .build();
        // readTimeout("请求超时时间" , 时间单位);
        client.newBuilder()
                .readTimeout(5000, TimeUnit.MILLISECONDS)
                .build()
                .newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("lfq", "onFailure");
                Message message = new Message();
                message.what = UploadClothItemActivity.PHOTO_POST_ASYNC_ERROR;
                UploadClothItemActivity.handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // 返回图片uuid和diskpath String
                    String str = response.body().string();
                    Log.i("post_file:::::one", response.message() + " , body:" + str);
                    String uuid = null;
                    String imgUrl = null;

                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        uuid = jsonObject.getString("uuid");
                        imgUrl = jsonObject.getString("diskpath");
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                    Bundle bundle = new Bundle();
                    bundle.putString("uuid",uuid);
                    bundle.putString("imgUrl",imgUrl);
                    Message message = new Message();
                    message.setData(bundle);
                    message.what = UploadClothItemActivity.PHOTO_POST_ASYNC_COMPLETE;
                    UploadClothItemActivity.handler.sendMessage(message);

                } else {
                    Log.i("post_file:::::one", response.message() + " error : body " + response.body().string());
                }
            }
        });

    }


    public static void postAsynFile(String sendFilePath) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("username", "oph")
                .addFormDataPart("image", "wangshu.jpg",
                        RequestBody.create(MEDIA_TYPE_PNG, new File(sendFilePath)))
                .build();

        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + "...")
                .url("http://" + Constant.baseRequestUrl  + ":8080/hello2/UploadServlet")
                .post(requestBody)
                .build();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("wangshu", response.body().string());
            }
        });
    }


}


//    private Request buildMultipartFormRequest(String url, File[] files,
//                                              String[] fileKeys, Param[] params) {
//        MultipartBuilder builder = new MultipartBuilder()
//                .type(MultipartBuilder.FORM);
//
//        for (Param param : params) {
//            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.key + "\""),
//                    RequestBody.create(null, param.value));
//        }
//        if (files != null) {
//            RequestBody fileBody = null;
//            for (int i = 0; i < files.length; i++) {
//                File file = files[i];
//                String fileName = file.getName();
//                fileBody = RequestBody.create(MediaType.parse("image/*"), file);
//                builder.addPart(Headers.of("Content-Disposition",
//                        "form-data; name=\"" + fileKeys[i] + "\"; filename=\"" + fileName + "\""),
//                        fileBody);
//            }
//        }
//
//        RequestBody requestBody = builder.build();
//        return new Request.Builder()
//                .url(url)
//                .post(requestBody)
//                .tag(TAG)
//                .build();
//    }
//}
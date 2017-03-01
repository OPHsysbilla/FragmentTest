package shu.fragmenttest.network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by eva on 2017/2/23.
 */

public class requestPhotoList {





    public static String getPhotoList() throws Exception {
        String jsonRes = null;

            String username = "oph";
            String data = "method=photoes&username=" + URLEncoder.encode(username, "UTF-8");
            Log.d("data",data);
            String baseUrl = "http://" + Constant.baseRequestUrl  + ":8080/images";
            String httpUrl = "http://" + Constant.baseRequestUrl  + ":8080/hello2/UserServlet";
            Map<String, String> params = new HashMap<String, String>();
//            params.put("username",username);
//            params.put("method","photoes");
//            byte [] data = params.toString().getBytes();
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(3000);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Charset", "utf-8");
            connection.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));

            OutputStream os = connection.getOutputStream();
            os.write(data.getBytes());
            os.flush();

            int response = connection.getResponseCode();            //获得服务器的响应码
            if(response == HttpURLConnection.HTTP_OK) {
                InputStreamReader is = new InputStreamReader(connection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(is);
                StringBuffer stringBuffer = new StringBuffer();
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }
                is.close();
                jsonRes = stringBuffer.toString();
                Log.d("jsonres", jsonRes);
            }

            os.close();
            connection.disconnect();


        return jsonRes;
    }

    /*
    * @param url
    * @param POST or GET
    * @param String data = "method=photoes&username=" + URLEncoder.encode(username, "UTF-8");
    */
    public static String getJsonStr(String httpUrl,String RequestMethod,byte[]data)
    {
        String jsonRes = null;
        try {

            String username = "oph";
//            Log.d("data",data.toString());
//            String baseUrl = "http://" + Constant.baseRequestUrl  + ":8080/images";
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(3000);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Charset", "utf-8");
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));

            OutputStream os = connection.getOutputStream();
            os.write(data);
            os.flush();

            int response = connection.getResponseCode();            //获得服务器的响应码
            if(response == HttpURLConnection.HTTP_OK) {
                InputStreamReader is = new InputStreamReader(connection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(is);
                StringBuffer stringBuffer = new StringBuffer();
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }
                is.close();
                jsonRes = stringBuffer.toString();
                Log.d("jsonres", jsonRes);
            }
            os.close();
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonRes;
    }

    public static  final  String SUCCESS = "1";
    public static  final  String FAILURE = "0";
    public static Boolean uploadFile(String RequestUrl,String sendFilePath)
    {
        String Boundary = UUID.randomUUID().toString();
        String Prefix = "--";
        String LineEnd = "\r\n";
        String ContentType = "multipart/form-data";
        try {
            URL url = new URL(RequestUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(3000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Charset","utf-8");
            conn.setRequestProperty("connection","keep-alive");
            conn.setRequestProperty("Content-Type",ContentType +";boundary="+Boundary);

            if(sendFilePath!=null)
            {
                File file = new File(sendFilePath);
                OutputStream outputStream = conn.getOutputStream();
                DataOutputStream dos = new DataOutputStream(outputStream);
                StringBuffer sb = new StringBuffer();
                sb.append(Boundary);
                sb.append(LineEnd);
                sb.append("Content-Disposition:form-data; name=\"img\"; filename=\""
                        +file.getName()+"\""+LineEnd);
                sb.append(LineEnd);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while((len=is.read(bytes))!=-1)
                {
                    dos.write(bytes,0,len);
                }
                is.close();
                dos.write(LineEnd.getBytes());
                byte[] EndData = (Prefix+Boundary+Prefix+LineEnd).getBytes();
                dos.write(EndData);
                dos.flush();
                int resCode = conn.getResponseCode();
                Log.d("resCode:", String.valueOf(resCode));
                if(resCode == HttpURLConnection.HTTP_OK) {
                    InputStreamReader isConn = new InputStreamReader(conn.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(isConn);
                    StringBuffer stringBuffer = new StringBuffer();
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuffer.append(line);
                    }
                    is.close();
                    String jsonRes = stringBuffer.toString();
                    Log.d("jsonres:::::", jsonRes);
                }
                dos.close();
                conn.disconnect();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

}

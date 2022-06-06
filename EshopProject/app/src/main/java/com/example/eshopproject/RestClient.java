package com.example.eshopproject;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RestClient {

  final String apiAuthorization = "1234567890";
  Context context;

  OkHttpClient client;
  public RestClient() {

    client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(10,TimeUnit.SECONDS).readTimeout(30,TimeUnit.SECONDS).hostnameVerifier(new HostnameVerifier() {
      @Override
      public boolean verify(String hostname, SSLSession session) {
        return true;
      }
    }).build();
  }


  public JSONObject doPost(String url, JSONObject body) throws JSONException, IOException {


    Log.d("Rest call", "started");

    MediaType JSON = MediaType.parse("application/json;charset=utf-8");

    RequestBody requestBody = RequestBody.create(JSON,body.toString());
    Request request = new Request.Builder().url(url).post(requestBody).build();

    Response response = client.newCall(request).execute();

    Log.d("Rest call", "ended");

    String data = response.body().string();

    Log.d("Response code: ", "" + response.code());
    Log.d("Response data: ", data);
    Log.d("Request time:", "" + response.sentRequestAtMillis());
    Log.d("Response time:", "" + response.receivedResponseAtMillis());

    JSONObject jsonObject = new JSONObject(data);

    response.body().close();
    return  jsonObject;
  }

  public JSONArray doGetRequest(String url) throws JSONException,IOException{
    Request newRewuest = new  Request.Builder().url(url).addHeader("Authorization",apiAuthorization).build();
    //Log.d("Request build status:","Request built");
    Response response = client.newCall(newRewuest).execute();
    //Log.d("Execution status:","Request executed");
    String data = response.body().string();
    Log.d("Message extracted",data);

    JSONArray jsonArray = new JSONArray(data);
    //Log.d("Json Array data",jsonArray.toString());
    response.body().close();
    return jsonArray;
  }

  public JSONObject doGetRequestSingleObject(String url) throws JSONException,IOException{
    Request newRewuest = new  Request.Builder().url(url).addHeader("Authorization",apiAuthorization).build();
    //Log.d("Request build status:","Request built");
    Response response = client.newCall(newRewuest).execute();
    //Log.d("Execution status:","Request executed");
    String data = response.body().string();
    Log.d("Message extracted",data);

    JSONObject jsonObject = new JSONObject(data);
    //Log.d("Json Array data",jsonArray.toString());
    response.body().close();
    return jsonObject;
  }

  public static SSLContext getSslContextForCertificateFile(Context context, String fileName) {
    try {
      KeyStore keyStore = getKeyStore(context, fileName);
      SSLContext sslContext = SSLContext.getInstance("SSL");
      TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
      trustManagerFactory.init(keyStore);
      sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
      return sslContext;
    } catch (Exception e) {
      String msg = "Error during creating SslContext for certificate from assets";
      Log.e("SslUtilsAndroid", msg, e);
      throw new RuntimeException(msg);
    }
  }

  private static KeyStore getKeyStore(Context context, String fileName) {
    KeyStore keyStore = null;
    try {
      AssetManager assetManager = context.getAssets();
      CertificateFactory cf = CertificateFactory.getInstance("X.509");
      InputStream caInput = assetManager.open(fileName);
      Certificate ca;
      try {
        ca = cf.generateCertificate(caInput);
        Log.d("SslUtilsAndroid", "ca=" + ((X509Certificate) ca).getSubjectDN());
      } finally {
        caInput.close();
      }

      String keyStoreType = KeyStore.getDefaultType();
      keyStore = KeyStore.getInstance(keyStoreType);
      keyStore.load(null, null);
      keyStore.setCertificateEntry("ca", ca);
    } catch (Exception e) {
      Log.e("SslUtilsAndroid","Error during getting keystore", e);
    }
    return keyStore;
  }

}

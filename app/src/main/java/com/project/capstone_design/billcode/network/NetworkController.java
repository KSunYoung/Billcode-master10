package com.project.capstone_design.billcode.network;

// import com.project.capstone_design.billcode.Network.NetworkInterface;


import com.project.capstone_design.billcode.login.kakaoNetwork.GlobalApplication;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//public class NetworkController extends Application {
public class NetworkController extends GlobalApplication {
    private static NetworkController mInstance;
    private static String URL = "http://3.92.189.19:4500/"; // 최종 IP, 졸업작품 전까지 유지예정
    //private static String URL = "http://35.174.4.10:4500/";
    //private static String URL = "http://192.168.25.38:4500/";
    // private static String URL = "http://192.168.110.1:8081/Billcode/androidDB.jsp";

    private NetworkInterface networkInterface = null;

    public static String getURL(){
        return URL;
    }

    public static NetworkController getInstance(){
        return mInstance;
    }

    public NetworkInterface getNetworkInterface() {
        return this.networkInterface;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        buildNetworkService();
    }

    public void buildNetworkService(){

        OkHttpClient.Builder okClient = new OkHttpClient.Builder(); // Setting for Log of Http connection
        //okClient.interceptors().add(new AddCookiesInterceptor());
        //okClient.interceptors().add(new ReceieveCookiesInterceptor());

        this.networkInterface = (NetworkInterface) new Retrofit.Builder()
                .baseUrl(URL) // Setting URL
                .addConverterFactory(GsonConverterFactory.create()) // 서버에서 json 형식으로 데이터를 보내고 이를 파싱해서 받아오기 위해서 사용
                .client(okClient.build()) // Checking for Http connection
                .build() // Build
                .create(NetworkInterface.class); // Create
    }
}



package com.project.capstone_design.billcode;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;

import com.project.capstone_design.billcode.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {
    private ISessionCallback callback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 스플래쉬 동안 상단 StatusBar 안보이게
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if(OurLoggedInCheckUp());
        else{
            // 세션의 연결여부 검사는 카카오 -> 페이스북 -> 자체로그인 순서
            callback = new ISessionCallback() {
                @Override
                public void onSessionOpened() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            goToMainActivity();
                        }
                    }, 1000); }

                @Override
                public void onSessionOpenFailed(KakaoException exception) { redirectToLoginActivity(); }
            };

            // 연결된 세션이 없으면 로그인 화면으로
            Session.getCurrentSession().addCallback(callback);
            if(!Session.getCurrentSession().checkAndImplicitOpen()){
                FacebookLoggedInCheckUp();
            }
        }




    }

    private boolean OurLoggedInCheckUp(){
        SharedPreferences mAppData = getSharedPreferences("AppData", MODE_PRIVATE);
        boolean loggedIn = mAppData.getBoolean("USER_AUTO_LOGIN", false);

        if (loggedIn){ // 자동로그인인경우 메인으로 바로 이동
            goToMainActivity();
            return true;
        }else
            return false;
    }

    private void FacebookLoggedInCheckUp(){
        boolean loggedIn = AccessToken.getCurrentAccessToken() == null;
        if (loggedIn)//현재 로그인이 아닐 때만 로그인한다.
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    redirectToLoginActivity();
                }
            }, 1000);
        else
            new Handler().postDelayed(new Runnable() { // 엑세스 토큰이 있으면 메인으로
                @Override
                public void run() {
                    goToMainActivity();
                }
            }, 1000);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private void goToMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void redirectToLoginActivity() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
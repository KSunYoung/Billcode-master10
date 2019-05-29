package com.project.capstone_design.billcode;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.JsonObject;
import com.project.capstone_design.billcode.addItem.AddItemActivity;
import com.project.capstone_design.billcode.home.Home;
import com.project.capstone_design.billcode.itemList.ItemList;
import com.project.capstone_design.billcode.login.LoginActivity;
import com.project.capstone_design.billcode.network.NetworkController;
import com.project.capstone_design.billcode.notification.Activity_Notification_Example;
import com.project.capstone_design.billcode.setting.Activity_Setting;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private BackPressCloseHandler backPressCloseHandler;
    // private Context mContext; // kakao 디버깅 해쉬 추출용

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Fragment mFragment = new Home(); // 프래그먼트로 한번 해볼까?
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content_fragment_layout, mFragment);
        ft.commit();
        //mContext = getApplicationContext(); // kakao 디버깅 해쉬 추출용

        // FCM 토큰 관련부분, 추후에 MyFirebaseMessagingService에서 토큰 refresh 처리할것
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("IMPORTANT_FCM", "getInstanceId failed", task.getException());
                            return;
                        }

                        SharedPreferences mAppData = getSharedPreferences("AppData", MODE_PRIVATE);
                        String internal_user_id = mAppData.getString("USER_ID", null);

                        // Get new Instance ID token
                        String user_token = task.getResult().getToken();

                        // 토큰 체크 함수
                        CheckToken(internal_user_id,user_token);

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, user_token);
                        Log.d("IMPORTANT_FCM", msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });


        //getHashKey(mContext);
        backPressCloseHandler = new BackPressCloseHandler(this); // 뒤로 두번 누르면 종료


        DrawerLayout drawer = findViewById(R.id.main_drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab_camera = findViewById(R.id.fab_camera);
        fab_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show(); // 리스너에 버튼 클릭 시키게 가능(클릭리스너)
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.main_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            backPressCloseHandler.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment mFragment = null;
        // Fragment mFragment = null; // 프래그먼트로 한번 해볼까?
        //Fragment mFragment = new Home(); // 프래그먼트로 한번 해볼까?

        if (id == R.id.nav_home) {
            mFragment = new Home();
            // Handle the camera action
            //Toast.makeText(this, "홈은 준비중입니다.", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_lists) {
            //Intent intent = new Intent(MainActivity.this, ItemListActivity.class);
            //startActivity(intent);

            mFragment = new ItemList();
            //Toast.makeText(this, "상품목록은 준비중입니다.", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_buy) {
            Toast.makeText(this, "재구매는 준비중입니다.", Toast.LENGTH_SHORT).show();
            mFragment=new RepurchaseActivity();
        } else if (id == R.id.nav_tools) {
            Intent intent = new Intent(MainActivity.this, Activity_Setting.class);
            startActivity(intent);
            //mFragment = new Setting();
            //Toast.makeText(this, "설정 탭은 준비중입니다.", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_share) {
            Toast.makeText(this, "공유 탭은 준비중입니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, Activity_Notification_Example.class);
            startActivity(intent);
        } else if (id == R.id.nav_send) {
            Toast.makeText(this, "보내기 탭은 준비중입니다.", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_logout) {
            // 저장된 개인정보 지우기
            SharedPreferences mAppData = getSharedPreferences("AppData", MODE_PRIVATE);
            SharedPreferences.Editor editor = mAppData.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            finish();
        }

        if (mFragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_fragment_layout, mFragment);
            ft.commit();
        }


        DrawerLayout drawer = findViewById(R.id.main_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void CheckToken(final String user_id, final String new_token){
        Call<JsonObject> mCall = NetworkController.getInstance().getNetworkInterface().CheckToken(user_id);
        mCall.enqueue(new Callback<JsonObject>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()) {
                    JsonObject tempObj = (JsonObject)response.body().get("data");
                    String db_token = tempObj.get("user_token").toString();
                    db_token = db_token.substring(1, db_token.length() - 1);
                    Log.i("MyTag","토큰값 :" + db_token);
                    if (!new_token.equals(db_token)) {
                        UpdateToken(user_id, new_token);
                    }else{
                        Log.i("MyTag","현재 토큰이 최신토큰입니다. :" + db_token);
                    }
                }else{
                    int statusCode = response.code();
                    Toast.makeText(getBaseContext(),"토큰이 존재하지 않습니다.." +statusCode,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.i("MyTag","서버 onFailure 에러내용 :" + t.getMessage());
            }
        });
    }

    public void UpdateToken(String user_id, final String new_token){
        Call<JsonObject> mCall = NetworkController.getInstance().getNetworkInterface().UpdateToken(user_id, new_token);
        mCall.enqueue(new Callback<JsonObject>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful() && response.body().get("success").toString().equals("true"))
                    Log.i("MyTag","토큰 업데이트 성공여부 :" + response.body().get("success").toString());
                else{
                    int statusCode = response.code();
                    Toast.makeText(getBaseContext(),"토큰 업데이트를 실패하였습니다." +statusCode,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.i("MyTag","서버 onFailure 에러내용 :" + t.getMessage());
            }
        });
    }

}

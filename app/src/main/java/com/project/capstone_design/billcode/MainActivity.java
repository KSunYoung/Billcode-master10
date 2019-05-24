package com.project.capstone_design.billcode;


import android.content.Intent;
import android.os.Build;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.project.capstone_design.billcode.addItem.AddItemActivity;
import com.project.capstone_design.billcode.home.Home;
import com.project.capstone_design.billcode.itemList.ItemList;
import com.project.capstone_design.billcode.login.LoginActivity;
import com.project.capstone_design.billcode.notification.Activity_Notification_Example;

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

}

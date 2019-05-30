/*
package com.project.capstone_design.billcode.home.banner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.project.capstone_design.billcode.R;

import java.util.ArrayList;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

public class AutoScroll extends AppCompatActivity {

    AutoScrollViewPager mAutoViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);


        ArrayList<String> imgUrl = new ArrayList<>(); //이미지 url를 저장하는 arraylist
        imgUrl.add("http://3.92.189.19:4500/banner_images/banner_img_1.jpg");
        imgUrl.add("http://3.92.189.19:4500/banner_images/banner_img_2.jpg");
        imgUrl.add("http://3.92.189.19:4500/banner_images/banner_img_3.jpg");
        imgUrl.add("http://3.92.189.19:4500/banner_images/banner_img_4.png");

        mAutoViewPager = (AutoScrollViewPager)findViewById(R.id.autoViewPager);
        AutoScrollAdapter scrollAdapter = new AutoScrollAdapter(this, imgUrl);
        mAutoViewPager.setAdapter(scrollAdapter); //Auto Viewpager에 Adapter 장착
        mAutoViewPager.setInterval(2000); // 페이지 넘어갈 시간 간격 설정
        mAutoViewPager.startAutoScroll(); //Auto Scroll 시작



    }
}
*/
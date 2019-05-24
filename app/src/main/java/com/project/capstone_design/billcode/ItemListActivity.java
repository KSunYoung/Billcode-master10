package com.project.capstone_design.billcode;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.project.capstone_design.billcode.login.LoginActivity;
import com.squareup.picasso.Picasso;

public class ItemListActivity extends AppCompatActivity {

    ImageView list_zero_img;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemlist_ifzero);

        list_zero_img = findViewById(R.id.list_zero_img);
        Button list_zero_button = findViewById(R.id.list_zero_button);

        //RecyclerView mRecyclerView = findViewById(R.id.RecyclerViewItemList);
        // setRecyclerView(); // 리사이클러뷰를 어답터에 연결, 여러가지 기본 세팅해준다.

        list_zero_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso mPicasso = new Picasso.Builder(getApplicationContext())
                        .listener(new Picasso.Listener() {
                            @Override
                            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                                exception.printStackTrace();
                            }
                        })
                        .build();
                //mPicasso.load("http://35.174.4.10:4500/images/8801056086190.jpg").centerCrop().fit().into(list_zero_img);
                mPicasso.load("http://nick.mtvnimages.com/nick/promos-thumbs/videos/spongebob-squarepants/rainbow-meme-video/spongebob-rainbow-meme-video-16x9.jpg").centerCrop().fit().into(list_zero_img);
            }
        });

    }

}

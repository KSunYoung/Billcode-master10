package com.project.capstone_design.billcode;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.project.capstone_design.billcode.addItem.AddItemActivity;

//import java.util.ArrayList;
//import java.util.List;

public class mainAct extends Fragment {

    LinearLayout layout;

    private BackPressCloseHandler backPressCloseHandler;
    private LinearLayout indicator;

    ViewPager viewPager;

    public static boolean ClickCount = true;

    int movePosition;
    public mainAct() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.activity_main, container, false);
        // layout을 가져와 메소드에서 layout.으로 뷰 호출

        SettingComponent();


        return layout;


    }

/*
    //버튼 클릭시 리스트 페이지로 넘어가기
    public void intentToList(String str) {
        Intent intent = new Intent(getActivity(), List.class);
        intent.putExtra("category", str);
        startActivity(intent);
    }
*/

    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    public void threadContents(){
        viewPager.setCurrentItem(movePosition);
        movePosition = movePosition + 1;

    }

    //컴포넌트 세팅
    public void SettingComponent() {
        Button btnAdd = (Button)layout.findViewById(R.id.BtnAdd);
        Button btnExpdateList = (Button)layout.findViewById(R.id.BtnExpdateList);
        Button btnRebuy = (Button)layout.findViewById(R.id.BtnRebuy);
        Button btnMod = (Button)layout.findViewById(R.id.BtnMod);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddItemActivity.class);
                startActivity(intent);
            }
        });

        btnExpdateList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "준비중 입니다.", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(getActivity(), IntroduceClub.class);
                //startActivity(intent);
            }
        });

        btnRebuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "준비중 입니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), RepurchaseActivity.class);
                startActivity(intent);
            }
        });

        btnMod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "준비중 입니다.", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(getActivity(), IntroduceClub.class);
                //startActivity(intent);
            }
        });


    }

}
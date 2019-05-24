package com.project.capstone_design.billcode;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RepurchaseActivity extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = (View) inflater.inflate(R.layout.activity_repurchase, container, false);

        TextView lotteLink = (TextView)mView.findViewById(R.id.LOTTE);

        lotteLink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://m.lottemart.com"));
                startActivity(intent);
            }
        });

        TextView homeplusLink = (TextView)mView.findViewById(R.id.HOMEPLUS);

        homeplusLink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://m.homeplus.co.kr"));
                startActivity(intent);
            }
        });

        TextView emartLink = (TextView)mView.findViewById(R.id.EMART);

        emartLink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://m.emart.ssg.com"));
                startActivity(intent);
            }
        });

        TextView costcoLink = (TextView)mView.findViewById(R.id.COSTCO);

        costcoLink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.costco.co.kr"));
                startActivity(intent);
            }
        });
        return mView;
    }
}
package com.project.capstone_design.billcode.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.capstone_design.billcode.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by charlie on 2017. 4. 24..
 */

public class Home_RecyclerAdapter extends RecyclerView.Adapter<Home_RecyclerAdapter.ItemViewHolder> {

    private ArrayList<Home_RecyclerItem> mItems;

    public Home_RecyclerAdapter(ArrayList<Home_RecyclerItem> items) {
        mItems = items;
    }


    // 새로운 뷰 홀더 생성
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_home, parent, false);
        return new ItemViewHolder(mView);
    }

    // View 의 내용을 해당 포지션의 데이터로 바꿉니다.
    @Override
    public void onBindViewHolder(final ItemViewHolder viewHolder, final int position) {
        String mExpDate,disExpDate;

        //Picasso.get().load("http://35.174.4.10:4500/images/" + mItems.get(position).getImage() + ".jpg").fit().into(viewHolder.mProductImageView); // 이미지 50 50으로 자름, centerCrop()등 가능
        Picasso.get().load("http://nick.mtvnimages.com/nick/promos-thumbs/videos/spongebob-squarepants/rainbow-meme-video/spongebob-rainbow-meme-video-16x9.jpg").centerCrop().fit().into(viewHolder.mProductImageView);
        // -> 추후에는 내부 DB에서 이미지 가져와서 시간절약
        viewHolder.mProductNameView.setText(mItems.get(position).getName());
        mExpDate = mItems.get(position).getExpDate();
        disExpDate = "20"+mExpDate.substring(0,2)+"년"+mExpDate.substring(2,4)+"월"+mExpDate.substring(4,6)+"일";
        viewHolder.mProductExpDateView.setText(disExpDate);
    }

    // 커스텀 뷰홀더
// item layout 에 존재하는 위젯들을 바인딩합니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mProductNameView;
        private TextView mProductExpDateView;
        private ImageView mProductImageView;

        private ItemViewHolder(View itemView) {
            super(itemView);
            mProductNameView = itemView.findViewById(R.id.CardViewItemName);
            mProductExpDateView = itemView.findViewById(R.id.CardViewItemExpDate);
            mProductImageView = itemView.findViewById(R.id.CardViewItemImage);
        }
    }

    // 데이터 셋의 크기를 리턴해줍니다.
    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
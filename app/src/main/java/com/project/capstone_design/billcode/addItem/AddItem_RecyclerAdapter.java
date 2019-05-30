package com.project.capstone_design.billcode.addItem;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.capstone_design.billcode.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by charlie on 2017. 4. 24..
 */

public class AddItem_RecyclerAdapter extends RecyclerView.Adapter<AddItem_RecyclerAdapter.ItemViewHolder> {

    private ArrayList<AddItem_RecyclerItem> mItems;

    public AddItem_RecyclerAdapter(ArrayList<AddItem_RecyclerItem> items) {
        mItems = items;
    }


    // 새로운 뷰 홀더 생성
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_additem, parent, false);
        return new ItemViewHolder(mView);
    }


    // View 의 내용을 해당 포지션의 데이터로 바꿉니다.
    @Override
    public void onBindViewHolder(final ItemViewHolder viewHolder, final int position) {
        //viewHolder.getAdapterPosition();
        String mExpDate, disExpDate;
        final boolean mPushChecked;
        //Log.i("IMPORTANT", ">>>>>>>>>> 홀더에 포지션   :  " + position);
        //Log.i("IMPORTANT", ">>>>>>>>>> 피곤하다 IMAGE값   :  " + mItems.get(position).getImage());
        //Picasso.get().load("http://172.20.10.6:4500/images/" + mItems.get(position).getImage() + ".jpg").fit().into(viewHolder.mProductImageView); // 이미지 50 50으로 자름, centerCrop()등 가능
        Picasso.get().load("http://3.92.189.19:4500/images/" + mItems.get(position).getImage() + ".jpg").fit().into(viewHolder.mProductImageView); // 이미지 50 50으로 자름, centerCrop()등 가능
        //Log.i("IMPORTANT", ">>>>>>>>>> 피곤하다 NAME값   :  " + mItems.get(position).getProduct_code());
        viewHolder.mProductNameView.setText(mItems.get(position).getProduct_name());

        mExpDate = mItems.get(position).getExpDate();
        disExpDate = "20" + mExpDate.substring(0, 2) + "년" + mExpDate.substring(2, 4) + "월" + mExpDate.substring(4, 6) + "일";
        Log.i("IMPORTANT", ">>>>>>>>>> 피곤하다 DATE값   :  " + mExpDate);
        viewHolder.mProductExpDateView.setText(disExpDate);

        if(mItems.get(position).getPushChecked() == 1) {
            viewHolder.mPushCheckedTextView.setVisibility(View.VISIBLE);
            viewHolder.mPushUnCheckedTextView.setVisibility(View.GONE);
        }
        else{
            viewHolder.mPushCheckedTextView.setVisibility(View.GONE);
            viewHolder.mPushUnCheckedTextView.setVisibility(View.VISIBLE);
        }
        viewHolder.mPushCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItems.get(position).setPushChecked(0);
                viewHolder.mPushCheckedTextView.setVisibility(View.GONE);
                viewHolder.mPushUnCheckedTextView.setVisibility(View.VISIBLE);
            }
        });

        viewHolder.mPushUnCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItems.get(position).setPushChecked(1);
                viewHolder.mPushCheckedTextView.setVisibility(View.VISIBLE);
                viewHolder.mPushUnCheckedTextView.setVisibility(View.GONE);
            }
        });

        /*
        viewHolder.mPushCheckedView.setChecked(mItems.get(position).isPushChecked());
        viewHolder.mPushCheckedView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    mItems.get(position).setPush_alert(true);
                else
                    mItems.get(position).setPush_alert(false);
            }
        });
        */

    }

    // 커스텀 뷰홀더
// item layout 에 존재하는 위젯들을 바인딩합니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mProductNameView;
        private TextView mProductExpDateView;
        private ImageView mProductImageView;
        //private AppCompatCheckBox mPushCheckedView;
        private TextView mPushCheckedTextView;
        private TextView mPushUnCheckedTextView;
        private ItemViewHolder(View itemView) {
            super(itemView);
            mProductNameView = itemView.findViewById(R.id.CardViewItemName);
            mProductExpDateView = itemView.findViewById(R.id.CardViewItemExpDate);
            mProductImageView = itemView.findViewById(R.id.CardViewItemImage);
            mPushCheckedTextView = itemView.findViewById(R.id.CardViewItemPushTextOn);
            mPushUnCheckedTextView = itemView.findViewById(R.id.CardViewItemPushTextOff);
            //mPushCheckedView = itemView.findViewById(R.id.CardViewItemCheckBox);
        }
    }

    // 데이터 셋의 크기를 리턴해줍니다.
    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
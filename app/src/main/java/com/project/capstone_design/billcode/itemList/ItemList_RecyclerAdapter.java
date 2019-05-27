package com.project.capstone_design.billcode.itemList;

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

public class ItemList_RecyclerAdapter extends RecyclerView.Adapter<ItemList_RecyclerAdapter.ItemViewHolder> {
    private ArrayList<ItemList_RecyclerItem> mItems;

    public ItemList_RecyclerAdapter(ArrayList<ItemList_RecyclerItem> items) {
        mItems = items;
    }


    // 새로운 뷰 홀더 생성
    @NonNull
    @Override
    public ItemList_RecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_itemlist, parent, false);
        return new ItemList_RecyclerAdapter.ItemViewHolder(mView);
    }

    // View 의 내용을 해당 포지션의 데이터로 바꿉니다.
    @Override
    public void onBindViewHolder(final ItemList_RecyclerAdapter.ItemViewHolder viewHolder, final int position) {
        String mExpDate,disExpDate;

        //Picasso.get().load("http://35.174.4.10:4500/images/" + mItems.get(position).getImage() + ".jpg").fit().into(viewHolder.mProductImageView); // 이미지 50 50으로 자름, centerCrop()등 가능
        //Picasso.get().load("http://192.168.2.103:4500/images/" + mItems.get(position).getImage() + ".jpg").fit().into(viewHolder.mProductImageView); // 이미지 50 50으로 자름, centerCrop()등 가능

        //viewHolder.getAdapterPosition();
        final boolean mPushChecked;
        //Picasso.get().load("http://10.80.72.173:4500/images/" + mItems.get(position).getImage() + ".jpg").fit().into(viewHolder.mProductImageView); // 이미지 50 50으로 자름, centerCrop()등 가능
        // Picasso.get().load("http://35.174.4.10:4500/images/" + mItems.get(position).getImage() + ".jpg").fit().into(viewHolder.mProductImageView); // 이미지 50 50으로 자름, centerCrop()등 가능
        Picasso.get().load("http://192.168.2.103:4500/images/" +mItems.get(position).getImage() + ".jpg").centerCrop().fit().into(viewHolder.mProductImageView);
        viewHolder.mProductNameView.setText(mItems.get(position).getName());

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

package com.project.capstone_design.billcode.itemList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.project.capstone_design.billcode.MainActivity;
import com.project.capstone_design.billcode.R;
import com.project.capstone_design.billcode.network.NetworkController;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.internal.EverythingIsNonNull;

import static android.content.Context.MODE_PRIVATE;

public class ItemList_RecyclerAdapter extends RecyclerView.Adapter<ItemList_RecyclerAdapter.ItemViewHolder> {
    private ArrayList<ItemList_RecyclerItem> mItems;
    private Context mContext;

    public ItemList_RecyclerAdapter(ArrayList<ItemList_RecyclerItem> items, Context context) {
        mItems = items;
        mContext = context;
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
        Picasso.get().load("http://3.92.189.19:4500/images/" + mItems.get(position).getProduct_code() + ".jpg").centerCrop().fit().into(viewHolder.mProductImageView); // 이미지 50 50으로 자름, centerCrop()등 가능
        Log.i("IMPORTANT", "이미지주소   :  " + "http://3.92.189.19:4500/images/" + mItems.get(position).getProduct_code() + ".jpg");
        //Picasso.get().load("http://172.20.10.6:4500/images/" +mItems.get(position).getImage() + ".jpg").centerCrop().fit().into(viewHolder.mProductImageView);
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
    class ItemViewHolder extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener {
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
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override // 컨텍스트 메뉴를 생성하고 메뉴 항목 선택시 호출되는 리스너
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem mEdit = menu.add(Menu.NONE, 1001, 1, "유통기한 편집");
            MenuItem mDelete = menu.add(Menu.NONE, 1002, 2, "삭제");
            mEdit.setOnMenuItemClickListener(mOnEditMenu);
            mDelete.setOnMenuItemClickListener(mOnEditMenu);
        }

        private final MenuItem.OnMenuItemClickListener mOnEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == 1001) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    View mView = LayoutInflater.from(mContext)
                            .inflate(R.layout.edit_box, null, false);
                    builder.setView(mView);
                    final DatePicker mDatePicker = mView.findViewById(R.id.edit_text_datePicker);
                    final Button mButton_cancel = mView.findViewById(R.id.edit_box_button_cancel);
                    final Button mButton_confirm = mView.findViewById(R.id.edit_box_button_confirm);

                    // datePicker 의 기본 날짜 정할지 여부 확인해서 적용할것

                    final AlertDialog dialog = builder.create();

                    mButton_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    mButton_confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String mExpDate = "" + mDatePicker.getYear() + mDatePicker.getMonth() + mDatePicker.getDayOfMonth();
                            Log.i("IMPORTANT", "||||||||||변경된 날짜   :  " + mExpDate);
                            // 리사이클러뷰의 해당 포지션 유통기한 값 변경
                            //mItems.get(getAdapterPosition()).setExpDate(mExpDate);

                            // 9. 어댑터에서 RecyclerView에 반영하도록 합니다.
                            notifyItemChanged(getAdapterPosition());
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else if (item.getItemId() == 1002) {
                    // 네트워크에서 지워주기
                    String product_code = mItems.get(getAdapterPosition()).getProduct_code();
                    String expDate = mItems.get(getAdapterPosition()).getExpDate();
                    Intent mIntent = new Intent("delete-message");
                    mIntent.putExtra("product_code",product_code);
                    mIntent.putExtra("expDate",expDate);
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(mIntent);

                    // 리사이클러 뷰에서 제거
                    mItems.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(), mItems.size());
                }
                return false;
            }
        };

    }

    // 데이터 셋의 크기를 리턴해줍니다.
    @Override
    public int getItemCount() {
        return mItems.size();
    }
}

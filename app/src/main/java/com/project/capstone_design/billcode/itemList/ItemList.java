package com.project.capstone_design.billcode.itemList;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.project.capstone_design.billcode.MainActivity;
import com.project.capstone_design.billcode.R;
import com.project.capstone_design.billcode.addItem.AddItem_RecyclerAdapter;
import com.project.capstone_design.billcode.addItem.AddItem_RecyclerItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ItemList extends Fragment {

    String str = null;
    public String[] array;
    private RecyclerView.Adapter adapter;
    private RecyclerView mRecyclerView;
    private ArrayList<AddItem_RecyclerItem> mItems = new ArrayList<>();
    ImageView List_zero_img;
    Button List_zero_button;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        //LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.activity_itemlist_ifzero, container, false);
        View mView = (View) inflater.inflate(R.layout.activity_itemlist_ifzero, container, false);

        List_zero_img = (ImageView)mView.findViewById(R.id.list_zero_img);
        List_zero_button= (Button)mView.findViewById(R.id.list_zero_button);

        final MainActivity mActivity = (MainActivity) getActivity();
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.ItemList_RecyclerViewItemList);
        // setRecyclerView(); // 리사이클러뷰를 어답터에 연결, 여러가지 기본 세팅해준다.

        List_zero_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso mPicasso = new Picasso.Builder(getActivity().getApplicationContext())
                        .listener(new Picasso.Listener() {
                            @Override
                            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                                exception.printStackTrace();
                            }
                        })
                        .build();
                mPicasso.load("http://nick.mtvnimages.com/nick/promos-thumbs/videos/spongebob-squarepants/rainbow-meme-video/spongebob-rainbow-meme-video-16x9.jpg").centerCrop().fit().into(List_zero_img);

                        //mPicasso.load("http://nick.mtvnimages.com/nick/promos-thumbs/videos/spongebob-squarepants/rainbow-meme-video/spongebob-rainbow-meme-video-16x9.jpg").centerCrop().fit().into(List_zero_img);
            }
        });
        //return layout;

        return mView;
    }

    private void setRecyclerView() {
        // 각 Item 들이 RecyclerView 의 전체 크기를 변경하지 않는 다면
        // setHasFixedSize() 함수를 사용해서 성능을 개선할 수 있습니다.
        // 변경될 가능성이 있다면 false 로 , 없다면 true를 설정해주세요.
        mRecyclerView.setHasFixedSize(false);

        // RecyclerView에 Adapter를 설정해줍니다.
        adapter = new AddItem_RecyclerAdapter(mItems); // 어답터에 아이템 연결
        mRecyclerView.setAdapter(adapter); // 리사이클러뷰에 어답터 연결

        // 다양한 LayoutManager 가 있습니다. 원하시는 방법을 선택해주세요.
        // 지그재그형의 그리드 형식
        //mainBinding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        // 그리드 형식
        //mainBinding.recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        // 가로 또는 세로 스크롤 목록 형식
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setData();
    }

    private void setData() {
        mItems.clear();

        if (array == null) {
            // getActivity().onRestart();
        }

// RecyclerView 에 들어갈 데이터를 추가합니다.
        for (String name : array) {
            //mItems.add(new AddItem_RecyclerItem(name));
            //mItems.add(new AddItem_RecyclerItem(name));
        }
// 데이터 추가가 완료되었으면 notifyDataSetChanged() 메서드를 호출해 데이터 변경 체크를 실행합니다.
        adapter.notifyDataSetChanged();
    }
}

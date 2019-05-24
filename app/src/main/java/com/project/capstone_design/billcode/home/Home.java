package com.project.capstone_design.billcode.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.capstone_design.billcode.MainActivity;
import com.project.capstone_design.billcode.R;

import java.util.ArrayList;

public class Home extends Fragment {
    private RecyclerView.Adapter adapter;
    private RecyclerView mRecyclerView;
    private ArrayList<Home_RecyclerItem> mItems = new ArrayList<>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        //LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.activity_itemlist_ifzero, container, false);
        View mView = (View) inflater.inflate(R.layout.fragment_home, container, false);
        final MainActivity mActivity = (MainActivity) getActivity();

        mRecyclerView = mView.findViewById(R.id.Home_RecyclerViewItemList);
        adapter = new Home_RecyclerAdapter(mItems); // 어답터에 아이템 연결
        setRecyclerView(); // 리사이클러뷰를 어답터에 연결, 여러가지 기본 세팅해준다.
        setData();

        return mView;
    }

    private void setRecyclerView() {
        // 각 Item 들이 RecyclerView 의 전체 크기를 변경하지 않는 다면
        // setHasFixedSize() 함수를 사용해서 성능을 개선할 수 있습니다.
        // 변경될 가능성이 있다면 false 로 , 없다면 true를 설정해주세요
        mRecyclerView.setHasFixedSize(false);


        // RecyclerView에 Adapter를 설정해줍니다.
        mRecyclerView.setAdapter(adapter); // 리사이클러뷰에 어답터 연결

        // 다양한 LayoutManager 가 있습니다. 원하시는 방법을 선택해주세요.
        // 지그재그형의 그리드 형식
        //mainBinding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        // 그리드 형식
        //mainBinding.recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        // 가로 또는 세로 스크롤 목록 형식
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // setData();
    }

    private void setData() {
        mItems.clear();

        // RecyclerView 에 들어갈 데이터를 추가합니다. (일단 임의로 추가하고 추후에 내부 DB에서 꺼내 쓸 것)
        //for (int i = 0; i < listName.length; i++) {
        //    mItems.add(new Home_RecyclerItem(listName[i], listExpDate[i], listName[i]));
        //}
        mItems.add(new Home_RecyclerItem("임시 1","20110101",""));
        mItems.add(new Home_RecyclerItem("임시 2","20120202",""));
        mItems.add(new Home_RecyclerItem("임시 3","20130303",""));
        mItems.add(new Home_RecyclerItem("임시 4","20140404",""));
        mItems.add(new Home_RecyclerItem("임시 5","20150505",""));
        mItems.add(new Home_RecyclerItem("임시 6","20160606",""));
        mItems.add(new Home_RecyclerItem("임시 7","20170707",""));
        mItems.add(new Home_RecyclerItem("임시 8","20180808",""));
        mItems.add(new Home_RecyclerItem("임시 9","20190909",""));
        mItems.add(new Home_RecyclerItem("임시 10","20201010",""));

        // 구코드
        //for (String name,ExpDate : listName,listExpDate) {
        //    mItems.add(new AddItem_RecyclerItem(name, "20190123", name));
        //    //mItems.add(new AddItem_RecyclerItem(name));
        //}
// 데이터 추가가 완료되었으면 notifyDataSetChanged() 메서드를 호출해 데이터 변경 체크를 실행합니다.
        adapter.notifyDataSetChanged();
    }
}

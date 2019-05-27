package com.project.capstone_design.billcode.itemList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.SharedPreferences;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.project.capstone_design.billcode.MainActivity;
import com.project.capstone_design.billcode.R;
import com.project.capstone_design.billcode.network.NetworkController;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class ItemList extends Fragment {

    // 저장된 아이디 처리관련
    private SharedPreferences mAppData;

    String str = null;
    public String[] array;
    private RecyclerView.Adapter adapter;
    private RecyclerView mRecyclerView;
    private ArrayList<ItemList_RecyclerItem> mItems = new ArrayList<>();
    ImageView List_zero_img;
    Button List_zero_button;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        //LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.activity_itemlist_ifzero, container, false);
        View mView = inflater.inflate(R.layout.fragment_itemlist, container, false);

        mAppData = getActivity().getSharedPreferences("AppData", getActivity().MODE_PRIVATE);
        final String internal_user_id = InternalIDLoad();

        //List_zero_img = (ImageView)mView.findViewById(R.id.list_zero_img);
        //List_zero_button= (Button)mView.findViewById(R.id.list_zero_button);

        final MainActivity mActivity = (MainActivity) getActivity();
        mRecyclerView = mView.findViewById(R.id.ItemList_RecyclerViewItemList);
        adapter = new ItemList_RecyclerAdapter(mItems); // 어답터에 아이템 연결
        // setRecyclerView(); // 리사이클러뷰를 어답터에 연결, 여러가지 기본 세팅해준다.

        setRecyclerView();
        GetUserItemList(internal_user_id);

        /*
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
        */
        //return layout;


        return mView;
    }

    private void setRecyclerView() {
        // 각 Item 들이 RecyclerView 의 전체 크기를 변경하지 않는 다면
        // setHasFixedSize() 함수를 사용해서 성능을 개선할 수 있습니다.
        // 변경될 가능성이 있다면 false 로 , 없다면 true를 설정해주세요.
        mRecyclerView.setHasFixedSize(false);

        // RecyclerView에 Adapter를 설정해줍니다.
        //adapter = new ItemList_RecyclerAdapter(mItems); // 어답터에 아이템 연결
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
// RecyclerView 에 들어갈 데이터를 추가합니다.
        /*
        for (String name : array) {
            //mItems.add(new AddItem_RecyclerItem(name));
            //mItems.add(new AddItem_RecyclerItem(name));
        }*/
// 데이터 추가가 완료되었으면 notifyDataSetChanged() 메서드를 호출해 데이터 변경 체크를 실행합니다.
        adapter.notifyDataSetChanged();
    }

    private void GetUserItemList(String user_id){
        Call<JsonObject> mCall = NetworkController.getInstance().getNetworkInterface().GetUserItemList(user_id);
        mCall.enqueue(new Callback<JsonObject>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    Log.i("MyTag","response 찍어보기 :" +response.body());
                    JsonObject responseBody = response.body();
                    JsonArray tempJsonArray = (JsonArray)responseBody.get("data");
                    //JsonArray jArray= responseBody.get("data");
                    for(int i=0;i<tempJsonArray.size();i++){
                        JsonObject tempObj = (JsonObject) tempJsonArray.get(i);

                        String tempStrName = tempObj.get("product_name").toString();
                        String tempImage = tempObj.get("product_code").toString();
                        String tempStrExpDate = tempObj.get("product_expiration_date").toString();
                        int tempPushChecked = tempObj.get("push_alert").getAsInt();

                        String tempExpDate = "";
                        tempExpDate += tempStrExpDate.substring(3,5);
                        tempExpDate += tempStrExpDate.substring(6,8);
                        tempExpDate += tempStrExpDate.substring(9,11);
                        Log.i("MyTag","임시 유통기한: " +tempExpDate);
                        Log.i("MyTag","코드가 미쳤다.."+i+"번째 이름: " +tempStrName);
                        Log.i("MyTag","코드가 미쳤다.."+i+"번째 유통기한: " +tempObj.get("product_expiration_date"));
                        Log.i("MyTag","코드가 미쳤다.."+i+"번째 푸쉬알림설정: " +tempObj.get("push_alert"));
                        mItems.add(new ItemList_RecyclerItem(tempStrName.substring(1,tempStrName.length()-1),tempImage.substring(1,tempImage.length()-1),tempExpDate,tempPushChecked));
                    }
                    /*
                    String temp_str = responseBody.get("data").toString();
                    temp_str.
                    String[] temp_strArray = temp_str.split("product_name");

                    for(int i=0;i<temp_strArray.length;i++)
                        Log.i("MyTag","따로따로 찍어본다~ "+i+"string :" +temp_strArray[i]);
                    */
                    /*
                    for(int i=0;i<temp_str.length();i++){
                        if(i==0)
                            Log.i("MyTag","따로따로 찍어본다~ "+i+"string :" +temp_strArray[i].substring(18,(temp_strArray[i].length()-2)));
                        else if(i==temp_strArray.length-1)
                            Log.i("MyTag","따로따로 찍어본다~ "+i+"string :" +temp_strArray[i].substring(17,(temp_strArray[i].length()-3)));
                        else
                            Log.i("MyTag","따로따로 찍어본다~ "+i+"string :" +temp_strArray[i].substring(17,(temp_strArray[i].length()-2)));
                    }*/


                    /*
                    for (int i = 0; i < mItems.size(); i++) {
                        if(i==0)
                            mItems.get(i).name(temp_strArray[i].substring(18,(temp_strArray[i].length()-2)));
                        else if(i==newnewpart.length-1)
                            mItems.get(i).setProduct_name(temp_strArray[i].substring(17,(temp_strArray[i].length()-3)));
                        else
                            mItems.get(i).setProduct_name(temp_strArray[i].substring(17,(temp_strArray[i].length()-2)));
                    }*/

                    // 데이터 추가가 완료되었으면 notifyDataSetChanged() 메서드를 호출해 데이터 변경 체크를 실행합니다.
                    adapter.notifyDataSetChanged();

                    //Toast.makeText(getActivity(),"이름받아오는데 성공" + response.body().toString(),Toast.LENGTH_LONG).show();
                }else{
                    int statusCode = response.code();
                    Toast.makeText(getActivity(),"이름받아오는데 실패" +statusCode,Toast.LENGTH_LONG).show();
                    Log.i("MyTag","응답코드 :" + statusCode);
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.i("MyTag","서버 onFailure 에러내용 :" + t.getMessage());
            }
        });
    }
    private void GetUserItemListByExpDateUp(){
        Call<JsonObject> mCall = NetworkController.getInstance().getNetworkInterface().GetUserItemList("mike12");
        mCall.enqueue(new Callback<JsonObject>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    Log.i("MyTag","response 찍어보기 :" +response.body());
                    JsonObject responseBody = response.body();
                    JsonArray tempJsonArray = (JsonArray)responseBody.get("data");
                    //JsonArray jArray= responseBody.get("data");
                    for(int i=0;i<tempJsonArray.size();i++){
                        JsonObject tempObj = (JsonObject) tempJsonArray.get(i);

                        String tempStrName = tempObj.get("product_name").toString();
                        String tempImage = tempObj.get("product_code").toString();
                        String tempStrExpDate = tempObj.get("product_expiration_date").toString();
                        int tempPushChecked = tempObj.get("push_alert").getAsInt();

                        String tempExpDate = "";
                        tempExpDate += tempStrExpDate.substring(3,5);
                        tempExpDate += tempStrExpDate.substring(6,8);
                        tempExpDate += tempStrExpDate.substring(9,11);
                        //Log.i("MyTag","이미지 넣어지는값 :" +tempImage.substring(1,tempImage.length()-1));
                        mItems.add(new ItemList_RecyclerItem(tempStrName.substring(1,tempStrName.length()-1),tempImage.substring(1,tempImage.length()-1),tempExpDate,tempPushChecked));
                    }

                    // 데이터 추가가 완료되었으면 notifyDataSetChanged() 메서드를 호출해 데이터 변경 체크를 실행합니다.
                    adapter.notifyDataSetChanged();

                    //Toast.makeText(getActivity(),"이름받아오는데 성공" + response.body().toString(),Toast.LENGTH_LONG).show();
                }else{
                    int statusCode = response.code();
                    Toast.makeText(getActivity(),"이름받아오는데 실패" +statusCode,Toast.LENGTH_LONG).show();
                    Log.i("MyTag","응답코드 :" + statusCode);
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.i("MyTag","서버 onFailure 에러내용 :" + t.getMessage());
            }
        });
    }

    // 설정값을 불러오는 함수
    private String InternalIDLoad() {
        // SharedPreferences 객체.get타입( 저장된 이름, 기본값 )
        // 저장된 이름이 존재하지 않을 시 기본값
        //savedAppData = mAppData.getBoolean("SAVED_APP_DATA", false);
        return mAppData.getString("USER_ID", null);
    }
}

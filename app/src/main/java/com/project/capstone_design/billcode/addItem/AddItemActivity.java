package com.project.capstone_design.billcode.addItem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.project.capstone_design.billcode.R;
import com.project.capstone_design.billcode.ScanActivity;
import com.project.capstone_design.billcode.model.ExpirationData;
import com.project.capstone_design.billcode.model.ProductCode;
import com.project.capstone_design.billcode.network.NetworkController;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;


public class AddItemActivity extends AppCompatActivity {

    /* QR code scanner 객체 */
    IntentIntegrator scan;

    private static final String TAG = "IMPORTANT";

    // 저장된 아이디 처리관련
    private SharedPreferences mAppData;
    //SharedPreferences mAppData = getSharedPreferences("AppData", MODE_PRIVATE);

    //////////////푸쉬 데이터 처리//////////////
    private boolean isPushChecked;
    private boolean isTotalPushChecked;

    ////////////// 리싸이클러 뷰 //////////////////
    //private static final int LAYOUT = R.layout.activity_additem;
    private RecyclerView.Adapter adapter;
    private RecyclerView mRecyclerView;
    private ArrayList<AddItem_RecyclerItem> mItems = new ArrayList<>();

    /////////////////데이터 업로드 처리/////////////////
    ArrayList<ExpirationData> mExpirationData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);
        Button btnCancel = findViewById(R.id.ItemCancel);
        Button btnConfirm = findViewById(R.id.ItemConfirm);
        mRecyclerView = findViewById(R.id.AddItem_RecyclerViewAddItem);
        adapter = new AddItem_RecyclerAdapter(mItems); // 어답터에 아이템 연결


        // 로그인 데이터 보관 부분
        mAppData = getSharedPreferences("AppData", MODE_PRIVATE);
        final String internal_user_id = InternalIDLoad();


        mExpirationData = new ArrayList<>();

        isPushChecked = mAppData.getBoolean("IS_PUSH_CHECKED", false);
        isTotalPushChecked = mAppData.getBoolean("IS_TOTAL_PUSH_CHECKED", false);

        //scan = new Intent(this);
        scan = new IntentIntegrator(this);
        scan.setCaptureActivity(ScanActivity.class);
        scan.setOrientationLocked(true);
        scan.initiateScan();
        scan.setPrompt("여기보세요");

        setRecyclerView(); // 리사이클러뷰를 어답터에 연결, 여러가지 기본 세팅해준다.

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Toast.makeText(getApplicationContext(), "값을 넘기지 않았습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent nextIntent = new Intent(this,Itemlist);
                //sqliteDB.insert()
                mExpirationData.clear();
                //Toast.makeText(getApplicationContext(), "성공적으로 전달되었습니다.", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < mItems.size(); i++) {
                    ExpirationData miniExpirationData = new ExpirationData(
                            internal_user_id, mItems.get(i).getProduct_code(), mItems.get(i).getExpDate(), mItems.get(i).getPushChecked());
                    //Log.i(TAG, ">>> mItems 개체" + i + "번째 이름: " + mItems.get(i).getName());
                    mExpirationData.add(miniExpirationData);
                }
                ExpDateUpload();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        String str;
        String[] array;
        String[] arrayCode;
        String[] arrayExpDate;

        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_CANCELED) {
                // 취소됨
                Log.e(TAG, ">>> requestCode2 = " + requestCode + ", resultCode = " + resultCode);
                Toast.makeText(this, "취소되었습니다.", Toast.LENGTH_SHORT).show();
                this.finish();
            } else if (resultCode == RESULT_OK) {
                Toast.makeText(this, "정상작동", Toast.LENGTH_SHORT).show();
                Log.e(TAG, ">>> requestCode3 = " + requestCode + ", resultCode = " + resultCode);
                str = scanResult.getContents();
                array = str.split(","); // ',' 로 구분하여 일단 품목을 나누어본다
                arrayCode = str.split(",");
                arrayExpDate = str.split(",");
                for (int i = 0; i < array.length; i++)
                    Log.i(TAG, ">>> array 1차" + i + "번째 : " + array[i]);

                //arrayCode = array;
                //arrayExpDate = array;
                for (int i = 0; i < array.length; i++) {
                    Log.i(TAG, ">>> array" + i + "번째 : " + array[i]);
                    arrayCode[i] = array[i].substring(0, 13);
                    Log.i(TAG, ">>> arrayCode" + i + "번째 : " + arrayCode[i]);
                    arrayExpDate[i] = (array[i]).substring(13, 19);
                    Log.i(TAG, ">>> arrayExpData" + i + "번째 : " + arrayExpDate[i]);
                }

                for (int i = 0; i < array.length; i++)
                    Log.i(TAG, ">>> array 2차" + i + "번째 : " + array[i]);
                //NetworkProcess();
                // 스캔된 QRCode --> result.getContents()
                setData(arrayCode, arrayExpDate); // data처리에서 array를 사용하고자 위치변경(원래 setReCyclerView 맨 하단에 위치)

                //adapter.notifyDataSetChanged();

                //for (int i = 0; i < array.length; i++){
                //    GetProductNameOne(arrayCode[i]);
                //}
                GetProductNameMany(arrayCode);

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            Toast.makeText(this, "뭐지?", Toast.LENGTH_SHORT).show();
        }

    }

    private void setRecyclerView() {
// 각 Item 들이 RecyclerView 의 전체 크기를 변경하지 않는 다면
// setHasFixedSize() 함수를 사용해서 성능을 개선할 수 있습니다.
// 변경될 가능성이 있다면 false 로 , 없다면 true를 설정해주세요.
        mRecyclerView.setHasFixedSize(false);


// RecyclerView에 Adapter를 설정해줍니다.

        mRecyclerView.setAdapter(adapter); // 리사이클러뷰에 어답터 연결

// 다양한 LayoutManager 가 있습니다. 원하시는 방법을 선택해주세요.
// 지그재그형의 그리드 형식
//mainBinding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
// 그리드 형식
//mainBinding.recyclerView.setLayoutManager(new GridLayoutManager(this,4));
// 가로 또는 세로 스크롤 목록 형식
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // setData();

    }


    private void setData(String[] listName, String[] listExpDate) {
        mItems.clear();

        if (listName == null || listExpDate == null) {
            this.onRestart();
        }


// RecyclerView 에 들어갈 데이터를 추가합니다.
        for (int i = 0; i < listName.length; i++) {
            mItems.add(new AddItem_RecyclerItem(listName[i], listExpDate[i], listName[i]));
            if (isPushChecked && isTotalPushChecked)
                mItems.get(i).setPushChecked(1);
            else
                mItems.get(i).setPushChecked(0);
        }

        //GetProductName();

        // 구코드
        //for (String name,ExpDate : listName,listExpDate) {
        //    mItems.add(new AddItem_RecyclerItem(name, "20190123", name));
        //    //mItems.add(new AddItem_RecyclerItem(name));
        //}
// 데이터 추가가 완료되었으면 notifyDataSetChanged() 메서드를 호출해 데이터 변경 체크를 실행합니다.
        adapter.notifyDataSetChanged();
    }


    private void ExpDateUpload() {
        Call<JsonObject> mCall = NetworkController.getInstance().getNetworkInterface().PostExpirationData(mExpirationData);
        mCall.enqueue(new Callback<JsonObject>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getBaseContext(), "서버로 등록되었습니다." + response.body().toString(), Toast.LENGTH_LONG).show();
                } else {
                    int statusCode = response.code();
                    Toast.makeText(getBaseContext(), "서버등록에 실패하였습니다." + statusCode, Toast.LENGTH_LONG).show();
                    Log.i("MyTag", "응답코드 :" + statusCode);
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.i("MyTag", "서버 onFailure 에러내용 :" + t.getMessage());
            }
        });
    }

    private void GetProductNameMany(String[] product_code) {

        ArrayList<ProductCode> mProductCode = new ArrayList<>();
        for (int i = 0; i < product_code.length; i++) {
            ProductCode miniProductCode = new ProductCode(product_code[i]);
            mProductCode.add(miniProductCode);
        }

        Call<JsonObject> mCall = NetworkController.getInstance().getNetworkInterface().GetProductNameMany(mProductCode);
        mCall.enqueue(new Callback<JsonObject>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {

                    JsonObject responseBody = response.body();
                    JsonArray tempJsonArray = (JsonArray) responseBody.get("data");
                    for (int i = 0; i < tempJsonArray.size(); i++) {
                        JsonObject tempObj = (JsonObject) tempJsonArray.get(i);
                        Log.i("MyTag", "코드가 미쳤다.." + i + "번째 이름: " + tempObj.get("product_name"));
                        String tempStr = tempObj.get("product_name").toString();
                        mItems.get(i).setProduct_name(tempStr.substring(1, tempStr.length() - 1));
                    }

                    // 데이터 추가가 완료되었으면 notifyDataSetChanged() 메서드를 호출해 데이터 변경 체크를 실행합니다.
                    adapter.notifyDataSetChanged();


                    Toast.makeText(getBaseContext(), "이름받아오는데 성공" + response.body().toString(), Toast.LENGTH_LONG).show();
                } else {
                    int statusCode = response.code();
                    Toast.makeText(getBaseContext(), "이름받아오는데 실패" + statusCode, Toast.LENGTH_LONG).show();
                    Log.i("MyTag", "응답코드 :" + statusCode);
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.i("MyTag", "서버 onFailure 에러내용 :" + t.getMessage());
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
    /*
    // 내가생각해도 진짜 그지같은 코드다
    public void NetworkProcess() {
        mItems.clear();

        if (array == null) {
            this.onRestart();
        }

        q
        for (String productCode : array) {
            Log.i(TAG, ">>> productCode2   :  " + productCode);
            Call<ProductResponse> mCall = NetworkController.getInstance().getNetworkInterface().getProductByCode(productCode);
            mCall.enqueue(new Callback<ProductResponse>() {
                @Override
                public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                    ProductResponse mProductResponse = response.body();
                    Log.i(TAG, ">>> response.body()   :  " + response.body().toString());
                    Log.i(TAG, ">>> mProductResponse.get_id   :  " + mProductResponse.get_id());
                    Log.i(TAG, ">>> mProductResponse.getProductName   :  " + mProductResponse.getProductName());
                    Log.i(TAG, ">>> mProductResponse.getProductImage   :  " + mProductResponse.getProductImage());
                    Log.i(TAG, ">>> mProductResponse.getProductCode   :  " + mProductResponse.getProductCode());
                }

                @Override
                public void onFailure(Call<ProductResponse> call, Throwable t) {
                    Log.i(TAG, ">>> Fail   :  " + t.getMessage());
                }
            });
        }
        q

        for (final String productCode : array) {
            Log.i(TAG, ">>> productCode2   :  " + productCode);
            Call<JsonObject> mCall = NetworkController.getInstance().getNetworkInterface().getProductByCode(productCode);
            mCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        Log.i(TAG, ">>> response.body().get(\"data\").toString()  전 :  " + response.body().get("data").toString());
                        mItems.add(new AddItem_RecyclerItem(response.body().get("data").toString(), "20190414", productCode));
                        Log.i(TAG, ">>> response.body().get(\"data\").toString()  후 :  " + response.body().get("data").toString());
                        // Log.i(TAG, ">>> response.body().get(\"data\").toString()   :  " + response.body().get("data").toString());
                        // Log.i(TAG, ">>> response.body().get(\"productName\")   :  " + response.body().get("productName"));

                        // adapter = new AddItem_RecyclerAdapter(response.body()); // 어답터에 아이템 연결
                        // mRecyclerView.setAdapter(adapter); // 리사이클러뷰에 어답터 연결
                        //String str2 = response.body().getAsString();
                        // mItems.add(new AddItem_RecyclerItem(response.body().toString(),response.body().toString(),response.body().toString()));
                        // mItems.add(new AddItem_RecyclerItem(response.body()));
                        // mItems.add(new AddItem_RecyclerItem(response.toString()));
                        // response.body().toString();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.i(TAG, ">>> Fail   :  " + t.getMessage());
                }
            });
        }
        adapter.notifyDataSetChanged();


    }
    */
}
/*

        if (resultCode == IntentIntegrator.REQUEST_CODE
            //Activity.RESULT_OK
        ) {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            Log.i(TAG, ">>> result.getContents()   :  " + scanResult.getContents());
            Log.i(TAG, ">>> result.getFormatName()   :  " + scanResult.getFormatName());

        }
    }
}
*/
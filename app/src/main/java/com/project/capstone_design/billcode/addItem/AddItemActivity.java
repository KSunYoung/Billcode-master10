package com.project.capstone_design.billcode.addItem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.project.capstone_design.billcode.R;
import com.project.capstone_design.billcode.ScanActivity;
import com.project.capstone_design.billcode.model.ExpirationData;
import com.project.capstone_design.billcode.network.NetworkController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;


public class AddItemActivity extends AppCompatActivity {



    /* QR code scanner 객체 */
    private IntentIntegrator scan;
    //private Intent scan;
    private static final String TAG = "IMPORTANT";

    //////////////푸쉬 데이터 처리//////////////
    private boolean isPushChecked;
    private boolean isTotalPushChecked;

    ////////////// 리싸이클러 뷰 //////////////////
    //private static final int LAYOUT = R.layout.activity_itemlist;
    private RecyclerView.Adapter adapter;
    private RecyclerView mRecyclerView;
    private ArrayList<AddItem_RecyclerItem> mItems = new ArrayList<>();
    SQLiteDatabase sqliteDB = null;

    /////////////////데이터 업로드 처리/////////////////
    ArrayList<ExpirationData> mExpirationData;
    /////////////////내장 데이터 아이디 처리 위해서///////////////
    private SharedPreferences mAppData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemlist);
        Button btnCancel = findViewById(R.id.ItemCancel);
        Button btnConfirm = findViewById(R.id.ItemConfirm);
        mRecyclerView = findViewById(R.id.ItemList_RecyclerViewItemList);
        adapter = new AddItem_RecyclerAdapter(mItems); // 어답터에 아이템 연결

        mExpirationData = new ArrayList<ExpirationData>();
        SharedPreferences mAppData = getSharedPreferences("AppData", MODE_PRIVATE);

        isPushChecked = mAppData.getBoolean("IS_PUSH_CHECKED", false);
        isTotalPushChecked = mAppData.getBoolean("IS_TOTAL_PUSH_CHECKED", false);

        //scan = new Intent(this);
        scan = new IntentIntegrator(this);
        scan.setCaptureActivity(ScanActivity.class);
        scan.setOrientationLocked(true);
        scan.initiateScan();
        scan.setPrompt("여기보세요");


        // 내부 DB 초기화
        //sqliteDB = init_Internal_DB() ;
        //init_Internal_DB_tables(); //테이블 초기화


        /*
        String sqlCreateTbl = "CREATE TABLE IF NOT EXISTS ORDER_T (HELLO INTEGER, NAME TEXT)" ;

        sqliteDB.execSQL(sqlCreateTbl) ;

        String sqlInsert = "INSERT INTO ORDER_T (HELLO, NAME) VALUES (1, 'ppotta')" ;

        sqliteDB.execSQL(sqlInsert) ;

         String sqlDelete = "DELETE FROM ORDER_T WHERE NO=2" ;

        sqliteDB.execSQL(sqlDelete) ;
        */

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
                Toast.makeText(getApplicationContext(), "성공적으로 전달되었습니다.", Toast.LENGTH_SHORT).show();
                for(int i=0;i< mItems.size();i++){
                    ExpirationData miniExpirationData = new ExpirationData(
                            "mike123",mItems.get(i).getName(),mItems.get(i).getExpDate());
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
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            Toast.makeText(this, "뭐지?", Toast.LENGTH_SHORT).show();
        }

        /*
        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (scanResult == null) {
                // 취소됨
                Log.e(TAG, ">>> requestCode2 = " + requestCode + ", resultCode = " + resultCode);
                Toast.makeText(this, "취소되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Log.e(TAG, ">>> requestCode3 = " + requestCode + ", resultCode = " + resultCode);
                str = scanResult.getContents();
                array = str.split(","); // ',' 로 구분하여 일단 품목을 나누어본다
                // 스캔된 QRCode --> result.getContents()
                setData(); // data처리에서 array를 사용하고자 위치변경(원래 setReCyclerView 맨 하단에 위치)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            Toast.makeText(this, "뭐지?", Toast.LENGTH_SHORT).show();
        }
         */
        /*
        super.onActivityResult(requestCode, resultCode, data);


        Log.e(TAG, ">>> requestCode = " + requestCode + ", resultCode = " + resultCode);

        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);


        Log.i(TAG, ">>> result.getContents()   :  " + scanResult.getContents());
        Log.i(TAG, ">>> result.getFormatName()   :  " + scanResult.getFormatName());
        // scanResult.getFormatName() : 바코드 종류
        // scanResult.getContents() : 바코드 값
        str = scanResult.getContents();

        array = str.split(","); // ',' 로 구분하여 일단 품목을 나누어본다
        Log.i("추출: ", ">>> : " +str);
        //String[] array = str.split(",");
        Log.i("추출: ", ">>> : " +array[0]);
        Log.i("추출: ", ">>> : " +array[1]);

        setData(); // data처리에서 array를 사용하고자 위치변경(원래 setReCyclerView 맨 하단에 위치)

        // 품목을 나눌때 20180102 유통기한은 8자리로 고정되어있음을 참고
        // 아이템별로 가져야 할 값 = 유통기한, 상품번호, if 유통기한 없으면 null
        // ex 1+6+6/8
        */

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
            if(isPushChecked && isTotalPushChecked)
                mItems.get(i).setPushChecked(true);
        }

        // 구코드
        //for (String name,ExpDate : listName,listExpDate) {
        //    mItems.add(new AddItem_RecyclerItem(name, "20190123", name));
        //    //mItems.add(new AddItem_RecyclerItem(name));
        //}
// 데이터 추가가 완료되었으면 notifyDataSetChanged() 메서드를 호출해 데이터 변경 체크를 실행합니다.
        adapter.notifyDataSetChanged();
    }


    private SQLiteDatabase init_Internal_DB() {

        SQLiteDatabase db = null;
        // File file = getDatabasePath("contact.db") ;
        File file = new File(getFilesDir(), "billcode.db");

        System.out.println("PATH : " + file.toString());
        try {
            db = SQLiteDatabase.openOrCreateDatabase(file, null);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        if (db == null) {
            System.out.println("DB creation failed. " + file.getAbsolutePath());
        }

        return db;
    }

    private void init_Internal_DB_tables() {

        if (sqliteDB != null) {
            String sqlCreateTbl = "CREATE TABLE IF NOT EXISTS MY_PRODUCT (" +
                    "P_NUM" + "INTEGER NOT NULL," +
                    "P_NAME" + "TEXT," +
                    "P_IMAGE " + "BLOB," +
                    "P_EXPDATE" + "INTEGER" + ")";

            System.out.println(sqlCreateTbl);

            sqliteDB.execSQL(sqlCreateTbl);
        }
    }
    private void ExpDateUpload(){
        Call<JsonObject> mCall = NetworkController.getInstance().getNetworkInterface().PostExpirationData(mExpirationData);
        mCall.enqueue(new Callback<JsonObject>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getBaseContext(),"등록되었습니다." + response.body().toString(),Toast.LENGTH_LONG).show();
                }else{
                    int statusCode = response.code();
                    Toast.makeText(getBaseContext(),"실패하였습니다." +statusCode,Toast.LENGTH_LONG).show();
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
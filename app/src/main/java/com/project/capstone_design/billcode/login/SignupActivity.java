package com.project.capstone_design.billcode.login;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import java.util.TimeZone;

import com.google.gson.JsonObject;
import com.project.capstone_design.billcode.MainActivity;
import com.project.capstone_design.billcode.model.UserData;
import com.project.capstone_design.billcode.network.NetworkController;
import com.project.capstone_design.billcode.R;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class SignupActivity extends AppCompatActivity {

    EditText signUpID, signUpName, signUpPhone, signUpEmail, signUpPw, signUpPwVerify;
    TextView signUpBirthdayClick;
    Button signUpConfirmBtn;
    DatePicker signUpBirthday;
    ConstraintLayout layout;
    DatePickerDialog dd;
    UserData mUserData;

    String realNewUserBirthday = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        layout = (ConstraintLayout) findViewById(R.id.signupActivity);
        signUpID = (EditText) findViewById(R.id.SignupID);
        signUpName = (EditText) findViewById(R.id.SignupName);
        signUpPhone = (EditText) findViewById(R.id.SignupPhone);
        signUpEmail = (EditText) findViewById(R.id.SignupEmail);
        signUpBirthdayClick = (TextView) findViewById(R.id.SignupBirthdayClick);
        // signUpBirthdayClick = (Button) findViewById(R.id.SignupBirthdayClick);
        signUpPw = (EditText) findViewById(R.id.SignupPw);
        signUpPwVerify = (EditText) findViewById(R.id.SignupPwVerify);

        signUpConfirmBtn = (Button) findViewById(R.id.SignUpConfirmBtn);

        //signUpBirthday = (DatePicker) findViewById(R.id.SignupBirthday);
        //signUpBirthdayClick.setClickable(true);

        // 현재 날짜 얻어오기 위함
        Calendar calendar = new GregorianCalendar(Locale.KOREA);
        dd = new DatePickerDialog(this, listener, calendar.get(Calendar.YEAR) , calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));


        /*
        signUpBirthdayClick.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                signUpBirthday.init(signUpBirthday.getYear(), signUpBirthday.getMonth(), signUpBirthday.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String str = String.format("%d %d %d", year, monthOfYear + 1, dayOfMonth);
                        signUpBirthdayClick.setText(str);
                    }

                });
                return false;
            }
        });*/

        signUpBirthdayClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dd.show();

                // signUpBirthday.setEnabled(true);
                /*
                signUpBirthday.init(signUpBirthday.getYear(), signUpBirthday.getMonth(), signUpBirthday.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String str = String.format("%d %d %d", year, monthOfYear + 1, dayOfMonth);
                        signUpBirthdayClick.setText(str);
                    }
                });*/
            }
        });


        signUpConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUserID = signUpID.getText().toString();
                String newUserName = signUpName.getText().toString();
                String newUserPhone = signUpPhone.getText().toString();
                String newUserEmail = signUpEmail.getText().toString();
                String newUserBirthday = realNewUserBirthday;
                String newUserPw = signUpPw.getText().toString();
                String newUserPwVerify = signUpPwVerify.getText().toString();

                if (!(newUserPw.equals(newUserPwVerify))) {
                    Toast.makeText(SignupActivity.this, "비밀번호를 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    // 서버에 보낼 객체에 값 저장
                    mUserData = new UserData(newUserID, newUserPw, newUserName, newUserEmail, newUserPhone); // 로그인 데이터(id, pw)

                    RegisterMethod();
                    Log.d("IMPORTANT", "userName = " + newUserName + "userEmail = " + newUserEmail + "userBirthday" + newUserBirthday + " userPw = " + newUserPw);
                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        KeyboardControl();
    }

    //빈공간 터치시 키보드 숨김
    public void KeyboardControl() {
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager touch_hide = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                touch_hide.hideSoftInputFromWindow(layout.getWindowToken(), 0);
            }
        });
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

        @Override

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            realNewUserBirthday = String.format("%d%d%d", year, monthOfYear + 1, dayOfMonth);
            signUpBirthdayClick.setText(String.format("%d년 %d월 %d일", year, monthOfYear + 1, dayOfMonth));
            Toast.makeText(getApplicationContext(), realNewUserBirthday, Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(), year + "년" + (monthOfYear+1) + "월" + dayOfMonth +"일", Toast.LENGTH_SHORT).show();
        }
    };

    public void RegisterMethod(){
        Call<JsonObject> mCall = NetworkController.getInstance().getNetworkInterface().Register_User(mUserData);
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
}

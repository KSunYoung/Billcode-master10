package com.project.capstone_design.billcode.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.content.SharedPreferences;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.JsonObject;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.project.capstone_design.billcode.BackPressCloseHandler;
import com.project.capstone_design.billcode.MainActivity;
import com.project.capstone_design.billcode.R;
import com.project.capstone_design.billcode.network.NetworkController;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class LoginActivity extends AppCompatActivity {

    ConstraintLayout layout;

    private BackPressCloseHandler backPressCloseHandler; // 뒤로 두번 누르면 종료
    private SharedPreferences mAppData;

    EditText userId, userPw;
    Button loginBtn, ourSignUpBtn;
    Button fake_btn_kakao_login, fake_btn_facebook_login;
    LoginButton btn_kakao_login;
    AppCompatCheckBox checkBox;
    com.facebook.login.widget.LoginButton btn_facebook_login; // facebook Login Btn

    private static final String TAG = "IMPORTANT";
    private static final String TAG_Kakao = "IMPORTANT_Kakao";
    private static final String TAG_Facebook = "IMPORTANT_Facebook";

    protected SessionCallback kakao_callback; // kakao
    CallbackManager callbackManager; // facebook

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        layout = findViewById(R.id.LoginLayout); // 키보드 숨김 위해서
        backPressCloseHandler = new BackPressCloseHandler(this);

        fake_btn_facebook_login = findViewById(R.id.fake_facebook_login_button);
        fake_btn_kakao_login = findViewById(R.id.fake_kakao_login_button);
        checkBox = findViewById(R.id.CheckBox2);

        userId = findViewById(R.id.UserId);
        userPw = findViewById(R.id.UserPw);
        loginBtn = findViewById(R.id.LoginBtn);
        ourSignUpBtn = findViewById(R.id.our_signup_button);

        // 로그인 데이터 보관 부분
        mAppData = getSharedPreferences("AppData", MODE_PRIVATE);

        // 카카오 로그인 부분
        btn_kakao_login = findViewById(R.id.real_kakao_login_button);
        kakao_callback = new SessionCallback();
        Session.getCurrentSession().addCallback(kakao_callback);
        btn_kakao_login.performClick();
        //requestMe();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(userId.getText().toString().equals("") && userPw.getText().toString().equals(""))
                    Toast.makeText(getBaseContext(),"아이디와 비밀번호를 입력하세요.",Toast.LENGTH_SHORT).show();
                else if (userId.getText().toString().equals(""))
                    Toast.makeText(getBaseContext(),"아이디를 입력하세요.",Toast.LENGTH_SHORT).show();
                else if(userPw.getText().toString().equals(""))
                    Toast.makeText(getBaseContext(),"비밀번호를 입력하세요.",Toast.LENGTH_SHORT).show();
                else if(userId.getText().toString().equals("test") && userPw.getText().toString().equals("1234")){
                    InternalDSave("test");
                    Intent nextIntent = new Intent(LoginActivity.this, MainActivity.class);
                    LoginActivity.this.startActivity(nextIntent);
                    finish();
                    Toast.makeText(getBaseContext(),"테스트계정으로 로그인되었습니다.",Toast.LENGTH_SHORT).show();
                }
                else
                    UserCheck(userId.getText().toString(), userPw.getText().toString());
            }

        });

        fake_btn_kakao_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_kakao_login.performClick();
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                InternalAutoLoginSave(isChecked);
            }
        });
        /*
        fake_btn_kakao_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        /*
        btn_kakao_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        ourSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent = new Intent(LoginActivity.this, SignupActivity.class);
                LoginActivity.this.startActivity(nextIntent);
            }
        });


        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

        KeyboardControl();

        init();

    }

    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
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

    // 로그인 activity를 이용하여 sdk에서 필요로 하는 activity를 띄우기 때문에 해당 결과를 세션에도 넘겨줘서 처리할 수 있도록 Session#handleActivityResult를 호출해 줍니다.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
            // kakao
        }
        else if(callbackManager.onActivityResult(requestCode, resultCode, data)){
            return;
            // facebook
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    // 세션의 상태가 변경될 때 불리는 세션 콜백을 삭제합니다.
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(kakao_callback);
    }

    // ISessionCallback
    // 세션의 체크시 상태 변경에 따른 콜백. 세션이 오픈되었을 때, 세션이 닫혔을 때 세션 콜백을 넘기게 된다.
    // SessionCallback이 상속받아 구현되어있다.
    private class SessionCallback implements ISessionCallback {

        // access token을 성공적으로 발급 받아 valid access token을 가지고 있는 상태. 일반적으로 로그인 후의 다음 activity로 이동한다.
        @Override
        public void onSessionOpened() {
            requestMe();
            //redirectMainActivity();
        }

        // memory와 cache에 session 정보가 전혀 없는 상태. 일반적으로 로그인 버튼이 보이고 사용자가 클릭시 동의를 받아 access token 요청을 시도한다.
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Log.e(TAG_Kakao, "ErrorCode = ", exception);
            }
        }
    }

    // 로그인 다음화면으로 이동동
    protected void redirectSignupActivity() {
        final Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
        finish();
    }

    protected void redirectLoginActivity() {
        final Intent intent = new Intent(this, com.project.capstone_design.billcode.login.LoginActivity.class);
        startActivity(intent);
        finish();
    }

    protected void redirectMainActivity() {
        final Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // 앱 연결해제 == 회원탈퇴(?)
    // 로그아웃 요청과는 달리 "앱 연결 해제" 요청 성공시에만 세션이 삭제됩니다. 따라서 실패시에 기존 세션을 이용하여 재시도 등을 할 수 있습니다.

    private void onClickUnlink() {
        final String appendMessage = getString(R.string.com_kakao_confirm_unlink);
        new AlertDialog.Builder(this)
                .setMessage(appendMessage)
                .setPositiveButton(getString(R.string.com_kakao_ok_button),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() {

                                    // 앱 연결 해제에 실패한 경우로 에러 결과를 받습니다.
                                    @Override
                                    public void onFailure(ErrorResult errorResult) {
                                        Logger.e(errorResult.toString());
                                    }

                                    // 세션이 닫혀 실패한 경우로 에러 결과를 받습니다.
                                    @Override
                                    public void onSessionClosed(ErrorResult errorResult) {
                                        //redirectLoginActivity();
                                    }

                                    @Override
                                    public void onNotSignedUp() {
                                        //redirectSignupActivity();
                                    }

                                    // 앱 연결 해제을 성공한 경우로 앱 연결 해제된 사용자 ID를 받습니다.
                                    @Override
                                    public void onSuccess(Long userId) {
                                        //redirectLoginActivity();
                                    }
                                });
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(getString(R.string.com_kakao_cancel_button),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();

    }

    // property 키들을 지정하여 원하는 유저 정보값을 가져옴옴
    private void requestMe() {
        List<String> keys = new ArrayList<>();
        keys.add("user_id");
        keys.add("properties.nickname"); // 카카오톡 또는 카카오스토리의 닉네임 정보
        keys.add("kakao_account.email"); // 사용자 카카오계정의 이메일 소유여부, 이메일 값, 이메일 인증여부, 이메일 유효여부
        keys.add("properties.profile_image"); // 640px * 640px 크기의 프로필 이미지 URL (2017/08/22 이전 가입자에 대해서는 480px * 480px ~ 1024px * 1024px 크기를 가질 수 있음)
        keys.add("properties.thumbnail_image"); // 110px * 110px 크기의 썸네일 프로필 이미지 URL (2017/08/22 이전 가입자에 대해서는 160px * 213px 크기를 가질 수 있음)
        keys.add("kakao_account.age_range"); // 카카오 계정의 연령대 소유여부, 연령대 값
        keys.add("kakao_account.birthday"); // 사용자 카카오계정의 생일 소유여부, 생일 값
        keys.add("kakao_account.gender"); // 사용자 카카오계정의 성별 소유여부, 성별 값
        UserManagement.getInstance().me(keys, new MeV2ResponseCallback() {

            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = " failed to get user info. msg = " + errorResult;
                Log.d(TAG_Kakao, message);
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                // Log.d(TAG, "onSessionClosed1 =" + errorResult);
                Log.d(TAG_Kakao, "onSessionClosedBy = " + errorResult);
                //redirectLoginActivity();
            }

            @Override
            public void onSuccess(MeV2Response response) {
                Log.d(TAG_Kakao, "user id: " + response.getId()); // Long 앱 유저 아이디
                Log.d(TAG_Kakao, "nickname: " + response.getNickname()); // String 유저 닉네임
                Log.d(TAG_Kakao, "email: " + response.getKakaoAccount().getEmail()); // String 이메일
                Log.d(TAG_Kakao, "profile image: " + response.getProfileImagePath()); // String 이미지 저장 URL
                Log.d(TAG_Kakao, "thumbnail image: " + response.getThumbnailImagePath()); // String 썸네일 저장 URL
                Log.d(TAG_Kakao, "age_range: " + response.getKakaoAccount().getAgeRange()); // AgeRange 연령대
                Log.d(TAG_Kakao, "birthday: " + response.getKakaoAccount().getBirthday()); // String 생일 MMDD형식
                Log.d(TAG_Kakao, "gender: " + response.getKakaoAccount().getGender()); // Gender 성별(FEMALE, MALE)
                redirectMainActivity();
            }

            /*
            private void handleScopeError(UserAccount account) {
                List<String> neededScopes = new ArrayList<>();
                if (account.needsScopeAccountEmail()) {
                    neededScopes.add("account_email");
                }
                if (account.needsScopeGender()) {
                    neededScopes.add("gender");
                }
                /*
                Session.getCurrentSession().updateScopes(this, neededScopes, new
                        AccessTokenCallback() {
                            @Override
                            public void onAccessTokenReceived(AccessToken accessToken) {
                                // 유저에게 성공적으로 동의를 받음. 토큰을 재발급 받게 됨.
                            }

                            @Override
                            public void onAccessTokenFailure(ErrorResult errorResult) {
                                // 동의 얻기 실패
                            }
                        })

            }*/
        });
    }

    /////////////////////////////////////////////////////// FaceBook /////////////////////////////////////////
    private void init() {
        callbackManager = CallbackManager.Factory.create();//페이스북의 로그인 콜백을 담당하는 클래스.

        btn_facebook_login = findViewById(R.id.real_facebook_login_button);//로그인 버튼. 실제 기능 다수가 이 안에 담겨있다.
        btn_facebook_login.setReadPermissions("email");

        /* Fake Login Btn 구현
        loginbtn = (Button) findViewById(R.id.login_2);  // Facebook Fake 로그인 버튼
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean loggedIn = AccessToken.getCurrentAccessToken() == null;
                if (loggedIn == true)//현재 로그인이 아닐 때만 로그인한다.
                    btn_facebook_login.performClick();//클릭 효과를 옮겨서 이 버튼을 클릭한 효과를 낸다.
            }
        });
        */

        fake_btn_facebook_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_facebook_login.performClick(); //클릭 효과를 옮겨서 이 버튼을 클릭한 효과를 낸다.
            }
        });
        /*
        btn_facebook_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //boolean loggedIn = AccessToken.getCurrentAccessToken() == null;
                //if (loggedIn == true)//현재 로그인이 아닐 때만 로그인한다.
                //    btn_facebook_login.performClick();//클릭 효과를 옮겨서 이 버튼을 클릭한 효과를 낸다.
            }
        });*/


        // 버튼에 대한 Callback registration
        btn_facebook_login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) { //로그인 성공
                Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                GraphRequest request = GraphRequest.newMeRequest(//데이터를 받아내기 위해서
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {//데이터를 받아낸 후(네트워크 연결 후)의 콜백
                            @Override
                            public void onCompleted(//완료되었을때 실행된다.
                                                    JSONObject object,
                                                    GraphResponse response) {
                                try {
                                    String email = object.getString("email");
                                    String name = object.getString("name");
                                    String age = object.getString("age");
                                    Toast.makeText(getApplicationContext(), email, Toast.LENGTH_SHORT).show();
                                    Log.d(TAG_Facebook, "e-mail from facebook = " + email);
                                    Log.d(TAG_Facebook, "name from facebook = " + name);
                                    Log.d(TAG_Facebook, "age from facebook = " + age);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                // Application code
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "email");//데이터를 전부 받아오지 않고 email만 받아온다. "email,name,age" 의 형식으로 받아올수 있음.
                parameters.putString("fields", "name");//데이터를 전부 받아오지 않고 email만 받아온다. "email,name,age" 의 형식으로 받아올수 있음.
                parameters.putString("fields", "age");//데이터를 전부 받아오지 않고 email만 받아온다. "email,name,age" 의 형식으로 받아올수 있음.
                request.setParameters(parameters);
                request.executeAsync();

                //Log.d(TAG_Facebook, "New ID from facebook = " + Profile.getCurrentProfile().getId());
                //Log.d(TAG_Facebook, "New name from facebook = " + Profile.getCurrentProfile().getName());
                //Log.d(TAG_Facebook, "New profileImage from facebook = " + Profile.getCurrentProfile().getProfilePictureUri(100,100));

                Intent nextIntent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(nextIntent);
                finish();
            }

            @Override
            public void onCancel() {
                // App code
                Log.d(TAG_Facebook,"페이스북 로그인 실패");
                Toast.makeText(getApplicationContext(), "페이스북 로그인 실패", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.d(TAG_Facebook,"페이스북 로그인 에러");
                Toast.makeText(getApplicationContext(), "페이스북 로그인 에러", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void UserCheck(final String user_id, String user_pw){
        Call<JsonObject> mCall = NetworkController.getInstance().getNetworkInterface().UserCheck(user_id, user_pw);
        mCall.enqueue(new Callback<JsonObject>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                // 메세지가 성공일때(ok_result), 실패일때(no_result) 구분
                String tempMessage = response.body().get("message").toString();
                int len_of_message = tempMessage.length(); // 메세지 길이
                tempMessage = tempMessage.substring(1,len_of_message-1); // 메세지에서 "" 표시 제거

                if(response.isSuccessful() && tempMessage.equals("ok_result")){ // 정상인 경우 메인페이지로
                    InternalDSave(user_id);
                    Intent nextIntent = new Intent(LoginActivity.this, MainActivity.class);
                    LoginActivity.this.startActivity(nextIntent);
                    finish();
                    Toast.makeText(getBaseContext(),"성공적으로 로그인되었습니다.",Toast.LENGTH_SHORT).show();
                }
                else if(response.isSuccessful() && tempMessage.equals("no_result")){ // 비정상인 경우 알림
                    Toast.makeText(getBaseContext(),"아이디 혹은 비밀번호를 확인해주세요.",Toast.LENGTH_SHORT).show();
                }
                else{ // 아예 서버로부터 값이 없는경우우
                   int statusCode = response.code();
                    Toast.makeText(getBaseContext(),"서버와 통신에 실패하였습니다." +statusCode,Toast.LENGTH_SHORT).show();
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

    // 설정값을 저장하는 함수
    private void InternalDSave(String user_id) {
        // SharedPreferences 객체만으론 저장 불가능 Editor 사용
        SharedPreferences.Editor editor = mAppData.edit();
        editor.putString("USER_ID",user_id);
        editor.apply();
    }

    // 설정값을 저장하는 함수
    private void InternalAutoLoginSave(Boolean isChecked) {
        // SharedPreferences 객체만으론 저장 불가능 Editor 사용
        SharedPreferences.Editor editor = mAppData.edit();
        if(isChecked)
            Log.i(TAG, "저장전 ischecked: true");
        else
            Log.i(TAG, "저장전 ischecked: false");

        editor.putBoolean("USER_AUTO_LOGIN",isChecked);
        editor.apply();
    }

}


/* requestMe 기존코드
UserManagement.getInstance().requestMe(new MeResponseCallback() {
    @Override

    public void onFailure(ErrorResult errorResult) {
        String message = "failed to get user info. msg=" + errorResult;

        ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
        if (result == ErrorCode.CLIENT_ERROR_CODE) {
            //에러로 인한 로그인 실패
        // finish();
        } else {
            //redirectMainActivity();
        }
    }

    @Override
    public void onSessionClosed(ErrorResult errorResult) {}

    @Override
    public void onNotSignedUp() {}

    @Override
    public void onSuccess(UserProfile userProfile){
        //로그인에 성공하면 로그인한 사용자의 일련번호, 닉네임, 이미지url등을 리턴합니다.
        //사용자 ID는 보안상의 문제로 제공하지 않고 일련번호는 제공합니다.
        // Log.e("UserProfile", userProfile.toString());
        // Log.e("UserProfile", userProfile.getId() + "");

        long number=userProfile.getId();
        Intent nextIntent=new Intent(LoginActivity.this,MainActivity.class);
        LoginActivity.this.startActivity(nextIntent);
        finish();
        }
    // 세션 실패시
    @Override
    public void onSessionOpenFailed(KakaoException exception) {}
});
*/

/*
    public void requestMe() {
        //유저의 정보를 받아오는 함수

        UserManagement.getInstance().requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Log.e(TAG, "error message=" + errorResult);
//                super.onFailure(errorResult);
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {

                Log.d(TAG, "onSessionClosed1 =" + errorResult);
            }

            @Override
            public void onNotSignedUp() {
                //카카오톡 회원이 아닐시
                Log.d(TAG, "onNotSignedUp ");

            }
            @Override
            public void onSuccess(UserProfile result) {
                Log.e("UserProfile", result.toString());
                Log.e("UserProfile", result.getId() + "");
            }
        });
    }
    }
*/
/* 로그인 관련
    private void onClickSignup() {
    final Map<String, String> properties = new HashMap<String, String>();
    properties.put("nickname", "leo");
    properties.put("age", "33");

    UserManagement.getInstance().requestSignup(new SignupResponseCallback() {

        // 앱연결을 성공한 경우로 앱연결된 사용자 ID를 받습니다.
        @Override
        public void onSuccess(final long userId) {
            redirectMainActivity();
        }
        // 세션이 닫혀 실패한 경우로 에러 결과를 받습니다.
        @Override
        public void onSessionClosed(final ErrorResult errorResult) {
            redirectLoginActivity();
        }
        // 앱연결이 실패한 경우로 에러 결과를 받습니다.
        @Override
        public void onFailure(final ErrorResult errorResult) {
            Logger.e(this, "failed to sign up. msg = " + errorResult);
        }
    }, properties);
}
*/
/* 동적 동의
private void handleScopeError(UserAccount account) {
    List<String> neededScopes = new ArrayList<>();
    if (account.needsScopeAccountEmail()) {
        neededScopes.add("account_email");
    }
    if (account.needsScopeGender()) {
        neededScopes.add("gender");
    }
    Session.getCurrentSession().updateScopes(this, neededScopes, new
    AccessTokenCallback() {
        @Override
        public void onAccessTokenReceived(AccessToken accessToken) {
            // 유저에게 성공적으로 동의를 받음. 토큰을 재발급 받게 됨.
        }

        @Override
        public void onAccessTokenFailure(ErrorResult errorResult) {
            // 동의 얻기 실패
        }
    })
}
 */
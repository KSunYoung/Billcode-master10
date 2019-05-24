package com.project.capstone_design.billcode;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.project.capstone_design.billcode.login.LoginActivity;
//import com.project.capstone_design.billcode.Login.KakaoNetwork.SessionCallback;

import java.util.ArrayList;

public class Setting extends Fragment {

    public Setting() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void onClickLogout() {
        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_setting, container, false);

        ListView listView = (ListView) layout.findViewById(R.id.setting_item);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager touch_hide = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                touch_hide.hideSoftInputFromWindow(container.getWindowToken(), 0);
            }
        });

        Intent intent = getActivity().getIntent();
        //clubnum = intent.getStringExtra("clubIdNumber");
        //String noLogin = "동아리 관리자 설정";
        //clubNB.setTextColor(Color.BLUE);

        /*
        if (clubnum == null) {
            clubNB.setText(noLogin);
        } else {
            c_num = Integer.parseInt(clubnum);
            getClubName();
        }
        */

        final ArrayList<String> settingList = new ArrayList<String>();
        settingList.add("로그인 설정");
        settingList.add("푸쉬알림 설정");
        settingList.add("만든 사람들");
        settingList.add("로그아웃");
        //settingList.add("동아리 사진 수정");
        //settingList.add("온라인 신청서 만들기");

        /*
        if (clubNB.getText().toString() == noLogin) {
            settingList.add("로그인");
        } else {
            settingList.add("로그아웃");
        }
        */

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, settingList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView textview = (TextView) view.findViewById(android.R.id.text1);
                textview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                textview.setPadding(100,30,0,30);
                //textview.setPadding(10, 70, 10, 50);

                return view;
            }
        };

        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (settingList.get(i).toString() == "로그아웃") {
                    LoginManager.getInstance().logOut(); // fb
                    onClickLogout(); // kakao
                    Toast.makeText(getActivity(), "로그아웃 되셨습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "준비중 입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    /*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (settingList.get(5).toString() == "로그인") {
                    if (settingList.get(i).toString() == "로그인") {
                        Intent intent = new Intent(getActivity(), Loading.class);
                        intent.putExtra("listValue", settingList.get(i).toString());
                        intent.putExtra("clubIdNumber", clubnum);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), "로그인이 필요한 서비스 입니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    if (settingList.get(5).toString() == "로그아웃") {
                        if (settingList.get(i).toString() == "로그아웃") {
                            getActivity().finish();
                        } else {

                        }
                        Intent intent = new Intent(getActivity(), Loading.class);
                        intent.putExtra("listValue", settingList.get(i).toString());
                        intent.putExtra("clubIdNumber", clubnum);
                        startActivity(intent);

                    }
                }
            }
        });
    */

        return layout;
    }
}
/* 카카오 회원탈퇴
   // 앱 연결해제 == 회원탈퇴(?)
    // 로그아웃 요청과는 달리 "앱 연결 해제" 요청 성공시에만 세션이 삭제됩니다. 따라서 실패시에 기존 세션을 이용하여 재시도 등을 할 수 있습니다.
    // 원본코드에 activity 넣어 해결
    private void onClickUnlink() {
        final String appendMessage = getString(R.string.com_kakao_confirm_unlink);
        new AlertDialog.Builder(getActivity()) // 원래 context인 this 였음
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
                                        redirectLoginActivity();
                                    }

                                    @Override
                                    public void onNotSignedUp() {
                                        redirectSignupActivity();
                                    }

                                    // 앱 연결 해제을 성공한 경우로 앱 연결 해제된 사용자 ID를 받습니다.
                                    @Override
                                    public void onSuccess(Long userId) {
                                        redirectLoginActivity();
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
 */
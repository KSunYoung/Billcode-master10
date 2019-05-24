package com.project.capstone_design.billcode;

import android.os.Bundle;

import com.journeyapps.barcodescanner.CaptureActivity;

public class ScanActivity extends CaptureActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    //////////////// 레이아웃 ////////////////////
    // 상단 타이틀 뷰
    //LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    //barcode_scan_title_view title_view = new barcode_scan_title_view(this);
    //this.addContentView(title_view, layoutParams);
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

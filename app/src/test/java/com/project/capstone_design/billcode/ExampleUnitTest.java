package com.project.capstone_design.billcode;

import android.content.Context;

import android.content.pm.PackageInfo;

import android.content.pm.PackageManager;

import android.content.pm.Signature;

import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.util.Base64;

import android.util.Log;
import org.junit.Test;

import static android.content.ContentValues.TAG;
import static com.kakao.util.helper.Utility.getPackageInfo;
import static org.junit.Assert.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
}


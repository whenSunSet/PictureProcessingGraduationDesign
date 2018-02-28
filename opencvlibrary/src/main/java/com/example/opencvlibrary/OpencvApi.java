package com.example.opencvlibrary;

/**
 * Created by whensunset on 2018/2/28.
 */

public class OpencvApi {
    static {
        System.loadLibrary("native-lib");
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public static native String stringFromJNI();
}

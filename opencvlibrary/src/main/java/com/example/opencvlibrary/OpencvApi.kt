package com.example.opencvlibrary

/**
 * Created by whensunset on 2018/2/28.
 */

object OpencvApi {
    init {
        System.loadLibrary("native-lib")
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String
}

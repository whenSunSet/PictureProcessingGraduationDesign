package com.example.whensunset.pictureprocessinggraduationdesign.base;

import android.databinding.Observable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by whensunset on 2018/3/10.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected Observable.OnPropertyChangedCallback showToast() {
        return new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                Toast.makeText(BaseActivity.this, ObserverParamMap.getToastMessage(observable) , Toast.LENGTH_SHORT).show();
            }
        };
    }

    protected void showToast(String message) {
        Toast.makeText(BaseActivity.this, message , Toast.LENGTH_SHORT).show();
    }
}

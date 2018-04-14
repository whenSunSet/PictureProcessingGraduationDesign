package com.example.whensunset.pictureprocessinggraduationdesign.base;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.ObserverParamMap;
import com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel.BaseVM;
import com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel.ChildBaseVM;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

/**
 * Created by whensunset on 2018/3/10.
 */

public abstract class BaseActivity extends AppCompatActivity {
    public static final String TAG = "何时夕:BaseActivity";

    private Map<ObservableField , Observable.OnPropertyChangedCallback> mRegisteredViewModelFiledObserverMap = new HashMap<>();

    public void initListener(ChildBaseVM childVM, ObservableAction observableAction, Integer... listenerPositions) {
        if (childVM == null) {
            throw new RuntimeException("被监听的childVM为null");
        }
        if (observableAction == null) {
            throw new RuntimeException("被监听的行为为null");
        }
        if (listenerPositions == null) {
            throw new RuntimeException("被传入的监听器的position列表为null");
        }

        // 监听 某个childVM的变化 以运行observableAction
        Flowable.fromArray(listenerPositions)
                .filter(position -> {
                    MyLog.d(TAG, "initListener", "状态:position:childVM.mClickListenerList.size():", "过滤监听器", position, childVM.getEventListenerList().size());
                    return !(position == null || position >= childVM.getEventListenerList().size());
                }).map((Function<Integer, ObservableField<? super Object>>) childVM.getEventListenerList()::get)
                .subscribe(observableField -> {
                    Observable.OnPropertyChangedCallback onPropertyChangedCallback = new Observable.OnPropertyChangedCallback() {
                        @Override
                        public void onPropertyChanged(Observable observable, int i) {
                            observableAction.onPropertyChanged(observable, i);
                        }
                    };
                    registered(observableField , onPropertyChangedCallback);
                    observableField.addOnPropertyChangedCallback(onPropertyChangedCallback);
                });
    }

    private void registered(ObservableField observableField , Observable.OnPropertyChangedCallback onPropertyChangedCallback) {
        mRegisteredViewModelFiledObserverMap.put(observableField , onPropertyChangedCallback);
    }

    protected void showToast(BaseVM baseVM) {
        if (baseVM == null) {
            throw new RuntimeException("被监听的ViewModel不可为null");
        }

        Observable.OnPropertyChangedCallback onPropertyChangedCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                Toast.makeText(BaseActivity.this, ObserverParamMap.getToastMessage(observable) , Toast.LENGTH_SHORT).show();
            }
        };

        registered(baseVM.getShowToastListener() , onPropertyChangedCallback);
        baseVM.getShowToastListener().addOnPropertyChangedCallback(onPropertyChangedCallback);
    }

    protected void showToast(String message) {
        Toast.makeText(BaseActivity.this, message , Toast.LENGTH_SHORT).show();
    }

    public <T extends ViewModel> T getViewModel(@NonNull Class<T> modelClass)  {
        return ViewModelProviders.of(this).get(modelClass);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (ObservableField observableField : mRegisteredViewModelFiledObserverMap.keySet()) {
               observableField.removeOnPropertyChangedCallback(mRegisteredViewModelFiledObserverMap.get(observableField));
        }
        mRegisteredViewModelFiledObserverMap.clear();
    }

    protected void registeredViewModelFiledsObserver() {

    }
}

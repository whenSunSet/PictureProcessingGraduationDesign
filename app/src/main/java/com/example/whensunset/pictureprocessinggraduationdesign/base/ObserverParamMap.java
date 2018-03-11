package com.example.whensunset.pictureprocessinggraduationdesign.base;

import android.databinding.Observable;
import android.databinding.ObservableField;

import java.util.HashMap;
import java.util.Map;

import static com.example.whensunset.pictureprocessinggraduationdesign.staticParam.ObserverMapKey.SHOW_TOAST_MESSAGE;

/**
 * Created by whensunset on 2018/3/10.
 */

public class ObserverParamMap {
    private Map<Object , Object> mObjectMap = new HashMap<>();

    public static ObserverParamMap setToastMessage(String message) {
        ObserverParamMap observerParamMap = new ObserverParamMap();
        observerParamMap.mObjectMap.put(SHOW_TOAST_MESSAGE , message);
        return observerParamMap;
    }

    public static String getToastMessage(Observable observable) {
        ObserverParamMap observerParamMap = ((ObservableField<ObserverParamMap>) observable).get();
        String message = (String) observerParamMap.mObjectMap.get(SHOW_TOAST_MESSAGE);
        return message;
    }

    public static ObserverParamMap staticSet(Object key , Object value) throws RuntimeException {
        if (key == null || value == null) {
            throw new RuntimeException("创建观察者参数map的时候，key和value不可为null");
        }
        ObserverParamMap observerParamMap = new ObserverParamMap();
        observerParamMap.mObjectMap.put(key , value);
        return observerParamMap;
    }

    public static <V> V staticGetValue(Observable observable , Object key) throws RuntimeException {
        if (observable == null || key == null) {
            throw new RuntimeException("获取观察者参数的时候，观察者参数map和需要的参数type不能为null");
        }

        ObserverParamMap observerParamMap;
        try {
            observerParamMap = ((ObservableField<ObserverParamMap>) observable).get();
        } catch (Exception e) {
            throw new RuntimeException("获取观察者参数的时候，传入参数必须为的观察者参数map");
        }

        if (observerParamMap == null) {
            throw new RuntimeException("创建观察者参数map的时候，传入的观察者参数map不可为null");
        }

        V v;
        try {
            v = (V) observerParamMap.mObjectMap.get(key);
        } catch (Exception e) {
            throw new RuntimeException("创建观察者参数map的时候，传入的观察者参数value的类型与目前所需类型不符");
        }
        if (v == null) {
            throw new RuntimeException("创建观察者参数map的时候，传入的观察者参数value不能为null");
        }

        return v;
    }

    private ObserverParamMap() {
    }

    public ObserverParamMap set(Object key , Object value) throws RuntimeException {
        if (key == null || value == null) {
            throw new RuntimeException("创建观察者参数map的时候，key和value不可为null");
        }
        mObjectMap.put(key , value);
        return this;
    }

    public <V> V getValue(Object key) throws RuntimeException {
        if (key == null) {
            throw new RuntimeException("获取观察者参数的时候，观察者参数map和需要的参数type不能为null");
        }

        V v;
        try {
            v = (V) mObjectMap.get(key);
        } catch (Exception e) {
            throw new RuntimeException("创建观察者参数map的时候，传入的观察者参数value的类型与目前所需类型不符");
        }
        if (v == null) {
            throw new RuntimeException("创建观察者参数map的时候，传入的观察者参数value不能为null");
        }

        return v;
    }
}

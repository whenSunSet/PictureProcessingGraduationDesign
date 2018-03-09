package com.example.whensunset.pictureprocessinggraduationdesign.base;

/**
 * Created by whensunset on 2018/3/8.
 */

public interface MyConsumer<IN , OUT> {

    OUT onNewResult(IN oldResult);

    void onFailure(Throwable throwable);

    void onCancellation();
}

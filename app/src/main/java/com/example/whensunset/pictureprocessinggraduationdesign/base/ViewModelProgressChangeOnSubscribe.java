package com.example.whensunset.pictureprocessinggraduationdesign.base;

import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

/**
 * Created by whensunset on 2018/3/16.
 */

public class ViewModelProgressChangeOnSubscribe implements FlowableOnSubscribe<BaseVM> {
    final BaseVM mBaseVM;
    public ViewModelProgressChangeOnSubscribe(BaseVM baseVM) {
        this.mBaseVM = baseVM;
    }

    @Override
    public void subscribe(FlowableEmitter<BaseVM> emitter) throws Exception {
        BaseVM.OnProgressChangedListener listener = baseVM -> {
            if (!emitter.isCancelled()) {
                emitter.onNext(baseVM);
            }
        };
        mBaseVM.setOnProgressChangedListener(listener);
    }
}

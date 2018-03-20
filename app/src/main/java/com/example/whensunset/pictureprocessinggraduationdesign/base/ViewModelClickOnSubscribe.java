package com.example.whensunset.pictureprocessinggraduationdesign.base;

import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

/**
 * Created by whensunset on 2018/3/16.
 */

public class ViewModelClickOnSubscribe implements FlowableOnSubscribe<BaseVM> {
    final BaseVM mBaseVM;
    public ViewModelClickOnSubscribe(BaseVM baseVM) {
        this.mBaseVM = baseVM;
    }

    @Override
    public void subscribe(FlowableEmitter<BaseVM> emitter) throws Exception {
        BaseVM.OnClickListener listener = baseVM -> {
            if (!emitter.isCancelled()) {
                emitter.onNext(baseVM);
            }
        };
        mBaseVM.setOnClickListener(listener);
    }
}

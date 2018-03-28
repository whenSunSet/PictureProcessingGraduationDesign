package com.example.whensunset.pictureprocessinggraduationdesign.viewModel.includeLayoutVM;

import android.databinding.ObservableField;
import android.graphics.Color;

import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyLog;
import com.example.whensunset.pictureprocessinggraduationdesign.base.uiaction.ProgressChangedUIAction;
import com.example.whensunset.pictureprocessinggraduationdesign.base.uiaction.UIActionManager;
import com.example.whensunset.pictureprocessinggraduationdesign.base.viewmodel.ChildBaseVM;
import com.example.whensunset.pictureprocessinggraduationdesign.mete.ColorPickerView;

import static com.example.whensunset.pictureprocessinggraduationdesign.base.uiaction.UIActionManager.PROGRESS_CHANGED_ACTION;

/**
 * Created by whensunset on 2018/3/26.
 */

public class PictureTextParamDialogVM extends ChildBaseVM{
    public static final String TAG = "何时夕:PictureTextParamDialogVM";

    public static final int PICTURE_TEXT_THROTTLE_MILLISECONDS = 100;
    public static final int PROGRESS_MAX = 256;

    public static final int TEXT_SIZE_CHANGED = 0;
    public static final int TEXT_COLOR_RGB_CHANGED = 1;
    public static final int TEXT_COLOR_WB_CHANGED = 2;
    public static final int STOP_TEXT_PARAM_DIALOG_VM = 3;

    public ObservableField<Integer> mTextColorRGBProgress = new ObservableField<>();
    public ObservableField<Integer> mTextColorWBProgress = new ObservableField<>();
    public ObservableField<Integer> mTextSizeProgress = new ObservableField<>();

    public ObservableField<Integer> mTextColor = new ObservableField<>();
    public ObservableField<Integer> mTextSize = new ObservableField<>();
    public ObservableField<String> mTypefaceName = new ObservableField<>();

    private int mFinalTextColor = Color.BLACK;
    private int mFinalTextSize = 10;
    private int mFinalTextSizeProgress = 10;
    private int mFinalTextColorRGBProgress = 128;
    private int mFinalTextColorWBProgress = 128;

    public PictureTextParamDialogVM(String typefaceName) {
        super(4);
        initDefaultUIActionManager();

        mTypefaceName.set(typefaceName);
        initProgressChanged();
    }

    @Override
    public void resume() {
        super.resume();
        mTextSize.set(mFinalTextSize);
        mTextColor.set(mFinalTextColor);
        mTextSizeProgress.set(mFinalTextSizeProgress);
        mTextColorRGBProgress.set(mFinalTextColorRGBProgress);
        mTextColorWBProgress.set(mFinalTextColorWBProgress);
    }

    @Override
    public void stop() {
        super.stop();
        mFinalTextSize = mTextSize.get();
        mFinalTextColor = mTextColor.get();
        mFinalTextSizeProgress = mTextSizeProgress.get();
        mFinalTextColorRGBProgress = mTextColorRGBProgress.get();
        mFinalTextColorWBProgress = mTextColorWBProgress.get();
        mEventListenerList.get(STOP_TEXT_PARAM_DIALOG_VM).notifyChange();
    }

    @Override
    protected void initDefaultUIActionManager() {
        mUIActionManager = new UIActionManager(this , PROGRESS_CHANGED_ACTION);
    }

    private void initProgressChanged() {
        mUIActionManager
                .<ProgressChangedUIAction>getDefaultThrottleFlowable(PICTURE_TEXT_THROTTLE_MILLISECONDS , PROGRESS_CHANGED_ACTION)
                .filter(progressChangedUIAction -> {
                    Integer position = progressChangedUIAction.getLastEventListenerPosition();
                    if (position == TEXT_SIZE_CHANGED) {
                        int textSizeProgress = progressChangedUIAction.getProgress();
                        mTextSizeProgress.set(textSizeProgress);
                        mTextSize.set(textSizeProgress);
                        MyLog.d(TAG, "initProgressChanged", "状态:position:textSizeProgress:", "更改了字体大小"  , position , textSizeProgress);
                        return false;
                    }
                    return true;
                }).filter(progressChangedUIAction -> {
                    Integer position = progressChangedUIAction.getLastEventListenerPosition();
                    if (position == TEXT_COLOR_RGB_CHANGED) {
                        int textColorRGBProgress = progressChangedUIAction.getProgress();
                        int rgbColor = ColorPickerView.changeColorFromHexToRGB(textColorRGBProgress);
                        mTextColorRGBProgress.set(textColorRGBProgress);
                        mTextColor.set(rgbColor);
                        MyLog.d(TAG, "initProgressChanged", "状态:position:textColorRGBProgress:rgbColor:", "更改了字体RGB颜色"  , position , textColorRGBProgress , Integer.toHexString(rgbColor));
                        return false;
                    }
                    return true;
                }).map(ProgressChangedUIAction::getProgress)
                .subscribe(textColorWBProgress -> {
                    int wbColor = ColorPickerView.changeColorFromHexToRGBWB(textColorWBProgress);
                    mTextColorWBProgress.set(textColorWBProgress);
                    mTextColor.set(wbColor);
                    MyLog.d(TAG, "initProgressChanged", "状态:textColorWBProgress:wbColor:", "更改了字体颜色" , textColorWBProgress , Integer.toHexString(wbColor));
                });

    }

    public int getFinalTextColor() {
        return mFinalTextColor;
    }

    public int getFinalTextSize() {
        return mFinalTextSize;
    }
}

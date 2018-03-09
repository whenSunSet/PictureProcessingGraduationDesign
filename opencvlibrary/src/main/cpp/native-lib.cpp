#include <jni.h>
#include <opencv2/core/cvdef.h>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include<vector>

using namespace cv;
using namespace std;
#include <string>
#include <iostream>

extern Mat RGBToLab(Mat &m);


extern "C"
JNIEXPORT void JNICALL
Java_com_example_whensunset_pictureprocessinggraduationdesign_pictureProcessing_CutMyConsumer_cut(
        JNIEnv *env, jobject instance, jlong in_mat_addr, jlong out_mat_addr, jint x, jint y,
        jint width, jint height) {

    Mat& oldMat = *(Mat *) in_mat_addr;
    Mat& newMat = *(Mat *) out_mat_addr;
    Rect rect(x , y , width , height);
    oldMat(rect).copyTo(newMat);
    return;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_whensunset_pictureprocessinggraduationdesign_pictureProcessing_FlipMyConsumer_flip(
        JNIEnv *env, jclass type, jlong src_nativeObj, jlong dst_nativeObj, jint flipCode) {
    Mat& oldMat = *(Mat *) src_nativeObj;
    Mat& newMat = *(Mat *) dst_nativeObj;

    flip(oldMat , newMat , flipCode);
    return;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_whensunset_pictureprocessinggraduationdesign_pictureProcessing_RotateMyConsumer_rotate(
        JNIEnv *env, jobject instance, jlong in_mat_addr, jlong out_mat_addr, jdouble angle,
        jdouble scale) {

    Mat& oldMat = *(Mat *) in_mat_addr;
    Mat& newMat = *(Mat *) out_mat_addr;

    Point2f center(oldMat.cols / 2, oldMat.rows / 2);
    Mat rot = getRotationMatrix2D(center, angle, scale);

    Rect bbox = RotatedRect(center, oldMat.size(), angle).boundingRect();

    rot.at<double>(0, 2) += bbox.width / 2.0 - center.x;
    rot.at<double>(1, 2) += bbox.height / 2.0 - center.y;

    warpAffine(oldMat , newMat , rot, bbox.size());
    return;
}
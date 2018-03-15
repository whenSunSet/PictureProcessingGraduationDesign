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

#define CLIP_RANGE(value, min, max)  ( (value) > (max) ? (max) : (((value) < (min)) ? (min) : (value)) )
#define COLOR_RANGE(value)  CLIP_RANGE(value, 0, 255)
extern int adjustBrightnessContrast(InputArray src, OutputArray dst, int brightness, int contrast);
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
}extern "C"
JNIEXPORT void JNICALL
Java_com_example_whensunset_pictureprocessinggraduationdesign_impl_WhiteBalanceMyConsumer_whiteBalance(
        JNIEnv *env, jobject instance, jlong in_mat_addr, jlong out_mat_addr) {

    Mat& oldMat = *(Mat *) in_mat_addr;
    Mat& newMat = *(Mat *) out_mat_addr;
    vector<Mat> imageRGB;

    //RGB三通道分离
    split(oldMat, imageRGB);

    //求原始图像的RGB分量的均值
    double R, G, B;
    B = mean(imageRGB[0])[0];
    G = mean(imageRGB[1])[0];
    R = mean(imageRGB[2])[0];

    //需要调整的RGB分量的增益
    double KR, KG, KB;
    KB = (R + G + B) / (3 * B);
    KG = (R + G + B) / (3 * G);
    KR = (R + G + B) / (3 * R);

    //调整RGB三个通道各自的值
    imageRGB[0] = imageRGB[0] * KB;
    imageRGB[1] = imageRGB[1] * KG;
    imageRGB[2] = imageRGB[2] * KR;

    //RGB三通道图像合并
    merge(imageRGB, newMat);
    return;
}extern "C"
JNIEXPORT void JNICALL
Java_com_example_whensunset_pictureprocessinggraduationdesign_impl_PictureParamMyConsumer_pictureParamChange(
        JNIEnv *env, jobject instance, jlong in_mat_addr, jlong out_mat_addr, jint brightness,
        jint contrast, jint saturation, jint tonal) {
    Mat& oldMat = *(Mat *) in_mat_addr;
    Mat& newMat = *(Mat *) out_mat_addr;

    adjustBrightnessContrast(oldMat , newMat , brightness , contrast);
}

/**
 * Adjust Brightness and Contrast
 *
 * @param src [in] InputArray
 * @param dst [out] OutputArray
 * @param brightness [in] integer, value range [-255, 255]
 * @param contrast [in] integer, value range [-255, 255]
 *
 * @return 0 if success, else return error code
 */
int adjustBrightnessContrast(InputArray src, OutputArray dst, int brightness, int contrast)
{
    Mat input = src.getMat();
    if( input.empty() ) {
        return -1;
    }

    dst.create(src.size(), src.type());
    Mat output = dst.getMat();

    brightness = CLIP_RANGE(brightness, -255, 255);
    contrast = CLIP_RANGE(contrast, -255, 255);

    /**
    Algorithm of Brightness Contrast transformation
    The formula is:
        y = [x - 127.5 * (1 - B)] * k + 127.5 * (1 + B);

        x is the input pixel value
        y is the output pixel value
        B is brightness, value range is [-1,1]
        k is used to adjust contrast
            k = tan( (45 + 44 * c) / 180 * PI );
            c is contrast, value range is [-1,1]
    */

    double B = brightness / 255.;
    double c = contrast / 255. ;
    double k = tan( (45 + 44 * c) / 180 * M_PI );

    Mat lookupTable(1, 256, CV_8U);
    uchar *p = lookupTable.data;
    for (int i = 0; i < 256; i++)
        p[i] = COLOR_RANGE( (i - 127.5 * (1 - B)) * k + 127.5 * (1 + B) );

    LUT(input, lookupTable, output);

    return 0;
}


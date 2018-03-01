#include <jni.h>
#include <opencv2/core/cvdef.h>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include<vector>

using namespace cv;
using namespace std;
#include <string>

extern Mat RGBToLab(Mat &m);

extern "C"
JNIEXPORT jstring

JNICALL Java_com_example_opencvlibrary_OpencvApi_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello my picture processing graduation design!";
    Mat *mat = new Mat();
    return env->NewStringUTF(hello.c_str());
}
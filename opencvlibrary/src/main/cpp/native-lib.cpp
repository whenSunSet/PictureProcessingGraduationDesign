#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring

JNICALL
Java_com_example_opencvlibrary_OpencvApi_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello my picture processing graduation design!";
    return env->NewStringUTF(hello.c_str());
}

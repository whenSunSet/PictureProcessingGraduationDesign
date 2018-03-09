cmake_minimum_required(VERSION 3.4.1)
set(OpenCV_DIR /Users/whensunset/AndroidStudioProjects/OpenCV-android-sdk/sdk/native/jni)
find_package(OpenCV 3.4 REQUIRED java)
add_library(native-lib
             SHARED
             src/main/cpp/native-lib.cpp
             src/main/cpp/test.cpp)
find_library( log-lib
              log )
target_link_libraries(native-lib
                       ${log-lib}
                       ${OpenCV_LIBS})
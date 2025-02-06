#!/bin/bash

cd ../androidMain/cpp
ls
cmake \
      -H/Users/ayodelekehinde/AndroidStudioProjects/InferKt/library/src/native \
      -DCMAKE_SYSTEM_NAME=Android \
      -DCMAKE_EXPORT_COMPILE_COMMANDS=ON \
      -DCMAKE_SYSTEM_VERSION=24 \
      -DANDROID_PLATFORM=android-24 \
      -DANDROID_ABI=arm64-v8a \
      -DCMAKE_ANDROID_ARCH_ABI=arm64-v8a \
      -DANDROID_NDK=/Users/ayodelekehinde/Library/Android/sdk/ndk/26.1.10909125 \
      -DCMAKE_ANDROID_NDK=/Users/ayodelekehinde/Library/Android/sdk/ndk/26.1.10909125 \
      -DCMAKE_TOOLCHAIN_FILE=/Users/ayodelekehinde/Library/Android/sdk/ndk/26.1.10909125/build/cmake/android.toolchain.cmake \
      -DCMAKE_LIBRARY_OUTPUT_DIRECTORY=/Users/ayodelekehinde/AndroidStudioProjects/InferKt/library/build-android \
      -DCMAKE_RUNTIME_OUTPUT_DIRECTORY=/Users/ayodelekehinde/AndroidStudioProjects/InferKt/library/build/build-android \
      -DGGML_USE_CPU \
      -DGGML_USE_CPU_AARCH64 \
      -B build-android \
      && \
      cmake --build build-android --config Release
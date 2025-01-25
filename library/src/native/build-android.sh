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
      -DCMAKE_MAKE_PROGRAM=/Users/ayodelekehinde/Library/Android/sdk/cmake/3.22.1/bin/ninja \
      -DCMAKE_LIBRARY_OUTPUT_DIRECTORY=/Users/ayodelekehinde/AndroidStudioProjects/InferKt/library/build/intermediates/cxx/Release/331u1l71/obj/arm64-v8a \
      -DCMAKE_RUNTIME_OUTPUT_DIRECTORY=/Users/ayodelekehinde/AndroidStudioProjects/InferKt/library/build/intermediates/cxx/Release/331u1l71/obj/arm64-v8a \
      -B/Users/ayodelekehinde/AndroidStudioProjects/InferKt/library/.cxx/Release/331u1l71/arm64-v8a \
      -GNinja \
      -DLLAMA_BUILD_COMMON=ON \
      -DBUILD_SHARED_LIBS=OFF \
      -DGGML_LLAMAFILE=OFF \
      -DGGML_OPENMP=OFF \
      -B build-android \
      && \
      cmake --build build-android --config Release
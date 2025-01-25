cmake \
      -G Xcode \
      -DCMAKE_TOOLCHAIN_FILE=ios.toolchain.cmake \
      -DGGML_METAL=OFF \
      -DINFER_BUILD_APPLE=ON \
      -DLLAMA_BUILD_TESTS=OFF \
      -DLLAMA_BUILD_EXAMPLES=OFF \
      -DLLAMA_BUILD_SERVER=OFF \
      -DPLATFORM=OS64COMBINED \
      -DLLAMA_BUILD_COMMON=ON \
      -DBUILD_SHARED_LIBS=OFF \
      -DGGML_LLAMAFILE=OFF \
      -DGGML_OPENMP=OFF \
      -B build \
      && \
      cmake --build build --config Release \
      && \
      cmake --install build --config Release --prefix /Users/ayodelekehinde/AndroidStudioProjects/InferKt/library/src/native/install
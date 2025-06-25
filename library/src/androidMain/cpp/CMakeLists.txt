cmake_minimum_required(VERSION 3.22.1)

project(inferkt-android)

add_subdirectory(../../native ../../native/build)

add_library(
        inferkt-android
        SHARED
        infer-android.cpp
)
target_link_libraries(inferkt-android inferkt log android)

target_compile_options(inferkt PRIVATE -O3 -DNDEBUG)

target_compile_options(inferkt PRIVATE -DGGML_USE_CPU -DGGML_USE_CPU_AARCH64 -pthread)
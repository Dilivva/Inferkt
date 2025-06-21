cmake_minimum_required(VERSION 3.22.1)

project("infer-android")

add_subdirectory(../../native ../../native/build)


function(build_library target_name cpu_flags)
    message("Building for: ${target_name}, ${cpu_flags}")
    add_library(
            ${target_name}
            SHARED
            infer-android.cpp
    )
    target_link_libraries(${target_name} inferkt log android)

    target_compile_options(inferkt PRIVATE -DGGML_USE_CPU -DGGML_USE_CPU_AARCH64 -pthread)


    target_compile_options(inferkt PRIVATE -O3 -DNDEBUG)
endfunction()

build_library(${CMAKE_PROJECT_NAME} "")

if (${ANDROID_ABI} STREQUAL "arm64-v8a")
    message("Building for: ${ANDROID_ABI}")
    # ARM64 targets
    # Removing fp16 for now as it leads to issues with some models like deepseek r1 distills
    # https://github.com/mybigday/llama.rn/pull/110#issuecomment-2609918310
    build_library("inferkt_v8" "-march=armv8-a")
    build_library("inferkt_v8_2" "-march=armv8.2-a")
    build_library("inferkt_v8_2_dotprod" "-march=armv8.2-a+dotprod")
    build_library("inferkt_v8_2_f16_dotprod" "-march=armv8.2-afp16+dotprod")
    build_library("inferkt_v8_2_i8mm" "-march=armv8.2-a+i8mm")
    build_library("inferkt_v8_2_dotprod_i8mm" "-march=armv8.2-a+dotprod+i8mm")

    # https://github.com/ggerganov/llama.cpp/blob/master/docs/android.md#cross-compile-using-android-ndk
    # llama.cpp will deal with the cpu features
    # build_library("rnllama_v8_7" "-march=armv8.7-a")
    # TODO: Add support runtime check for cpu features
    # At the moment runtime check is failing.

elseif (${ANDROID_ABI} STREQUAL "x86_64")
    # x86_64 target
    build_library("inferkt_x86_64" "-march=x86-64" "-mtune=intel" "-msse4.2" "-mpopcnt")

endif ()

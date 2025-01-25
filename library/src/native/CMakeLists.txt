cmake_minimum_required(VERSION 3.22.1)

project(inferkt CXX)
set(CMAKE_CXX_STANDARD 17)
if (INFER_BUILD_APPLE)
    enable_language(OBJC)
    add_definitions(
            -DNDEBUG
            -DO3
            -DLM_GGML_USE_CPU
            -DLM_GGML_USE_ACCELERATE
            -DLM_GGML_USE_METAL
    )
endif ()

include(FetchContent)
FetchContent_Declare(
        llama
        GIT_REPOSITORY https://github.com/ggerganov/llama.cpp
        GIT_TAG        b4466
)

option(INFER_BUILD_APPLE "Build infer for Apple targets" OFF)

FetchContent_MakeAvailable(llama)


add_library(inferkt STATIC
        src/inferkt.cpp
        src/Inference.cpp
)

if (INFER_BUILD_APPLE)
    message("Building for apple platforms")
    target_link_libraries(inferkt PRIVATE
            "-framework Accelerate"
            "-framework Foundation"
            "-framework Metal"
            "-framework MetalKit"
    )
endif ()

target_include_directories(inferkt PUBLIC include)

target_link_libraries(inferkt PRIVATE common llama)

target_compile_features(inferkt PRIVATE cxx_std_17)

install(TARGETS inferkt LIBRARY PUBLIC_HEADER)
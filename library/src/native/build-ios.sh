#!/bin/bash


function build_framework() {
  echo "Building for $1"
  cmake \
        -GXcode \
        -DCMAKE_SYSTEM_NAME=iOS \
        -DINFER_BUILD_APPLE=YES \
        -DINFER_IOS_BUILD=$1 \
        -DCMAKE_OSX_ARCHITECTURES="$2" \
        -DCMAKE_OSX_SYSROOT=$1 \
        -DCMAKE_INSTALL_PREFIX=`pwd`/install \
        -DCMAKE_XCODE_ATTRIBUTE_ONLY_ACTIVE_ARCH=NO \
        -DCMAKE_IOS_INSTALL_COMBINED=YES \
        -B build-ios \

  echo "Building release"
  cmake --build build-ios --config Release --target inferkt

}

rm -rf build-ios

build_framework "iphoneos" "arm64"
build_framework "iphonesimulator" "arm64;x86_64"

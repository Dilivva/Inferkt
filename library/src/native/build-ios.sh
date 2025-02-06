#!/bin/bash

rm -rf build-ios \
&& \
cmake \
      -GXcode \
      -DCMAKE_SYSTEM_NAME=iOS \
      -DINFER_BUILD_APPLE=YES \
      -DCMAKE_OSX_ARCHITECTURES="arm64" \
      -DCMAKE_OSX_SYSROOT=iphoneos \
      -DCMAKE_INSTALL_PREFIX=`pwd`/install \
      -DCMAKE_XCODE_ATTRIBUTE_ONLY_ACTIVE_ARCH=NO \
      -DCMAKE_IOS_INSTALL_COMBINED=YES \
      -B build-ios \
      && \
      cmake --build build-ios --config Release --target inferkt

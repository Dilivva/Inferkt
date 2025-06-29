#!/bin/bash -e

#git submodule init
#git submodule update --recursive

cp ./llama.cpp/include/llama.h ./include/llama.h
cp ./llama.cpp/include/llama-cpp.h ./include/llama-cpp.h

cp ./llama.cpp/ggml/include/ggml.h ./include/ggml.h
cp ./llama.cpp/ggml/include/ggml-alloc.h ./include/ggml-alloc.h
cp ./llama.cpp/ggml/include/ggml-backend.h ./include/ggml-backend.h
cp ./llama.cpp/ggml/include/ggml-cpu.h ./include/ggml-cpu.h
cp ./llama.cpp/ggml/include/ggml-cpp.h ./include/ggml-cpp.h
cp ./llama.cpp/ggml/include/ggml-opt.h ./include/ggml-opt.h
cp ./llama.cpp/ggml/include/ggml-metal.h ./include/ggml-metal.h
cp ./llama.cpp/ggml/include/gguf.h ./include/gguf.h


cp ./llama.cpp/ggml/src/ggml-metal/ggml-metal.m ./src/ggml-metal.m
cp ./llama.cpp/ggml/src/ggml-metal/ggml-metal-impl.h ./include/ggml-metal-impl.h

cp ./llama.cpp/ggml/src/ggml-cpu/ggml-cpu.c ./src/ggml-cpu.c
cp ./llama.cpp/ggml/src/ggml-cpu/ggml-cpu.cpp ./src/ggml-cpu.cpp
cp ./llama.cpp/ggml/src/ggml-cpu/ggml-cpu-impl.h ./include/ggml-cpu-impl.h
cp ./llama.cpp/ggml/src/ggml-cpu/ggml-cpu-aarch64.h ./include/ggml-cpu-aarch64.h
cp ./llama.cpp/ggml/src/ggml-cpu/ggml-cpu-aarch64.cpp ./src/ggml-cpu-aarch64.cpp
cp ./llama.cpp/ggml/src/ggml-cpu/ggml-cpu-quants.h ./include/ggml-cpu-quants.h
cp ./llama.cpp/ggml/src/ggml-cpu/ggml-cpu-quants.c ./src/ggml-cpu-quants.c
cp ./llama.cpp/ggml/src/ggml-cpu/ggml-cpu-traits.h ./include/ggml-cpu-traits.h
cp ./llama.cpp/ggml/src/ggml-cpu/ggml-cpu-traits.cpp ./src/ggml-cpu-traits.cpp

cp -r ./llama.cpp/ggml/src/ggml-cpu/amx ./src/

cp ./llama.cpp/ggml/src/ggml-cpu/llamafile/sgemm.h ./include/sgemm.h
cp ./llama.cpp/ggml/src/ggml-cpu/llamafile/sgemm.cpp ./src/sgemm.cpp

cp ./llama.cpp/ggml/src/ggml.c ./src/ggml.c
cp ./llama.cpp/ggml/src/ggml-impl.h ./include/ggml-impl.h
cp ./llama.cpp/ggml/src/ggml-alloc.c ./src/ggml-alloc.c
cp ./llama.cpp/ggml/src/ggml-backend.cpp ./src/ggml-backend.cpp
cp ./llama.cpp/ggml/src/ggml-backend-impl.h ./include/ggml-backend-impl.h
cp ./llama.cpp/ggml/src/ggml-backend-reg.cpp ./src/ggml-backend-reg.cpp
cp ./llama.cpp/ggml/src/ggml-common.h ./include/ggml-common.h
cp ./llama.cpp/ggml/src/ggml-opt.cpp ./src/ggml-opt.cpp
cp ./llama.cpp/ggml/src/ggml-quants.h ./include/ggml-quants.h
cp ./llama.cpp/ggml/src/ggml-quants.c ./src/ggml-quants.c
cp ./llama.cpp/ggml/src/ggml-threading.cpp ./src/ggml-threading.cpp
cp ./llama.cpp/ggml/src/ggml-threading.h ./include/ggml-threading.h
cp ./llama.cpp/ggml/src/gguf.cpp ./src/gguf.cpp

cp ./llama.cpp/src/llama.cpp ./src/llama.cpp
cp ./llama.cpp/src/llama-chat.h ./include/llama-chat.h
cp ./llama.cpp/src/llama-chat.cpp ./src/llama-chat.cpp
cp ./llama.cpp/src/llama-context.h ./include/llama-context.h
cp ./llama.cpp/src/llama-context.cpp ./src/llama-context.cpp
cp ./llama.cpp/src/llama-mmap.h ./include/llama-mmap.h
cp ./llama.cpp/src/llama-mmap.cpp ./src/llama-mmap.cpp
cp ./llama.cpp/src/llama-kv-cache.h ./include/llama-kv-cache.h
cp ./llama.cpp/src/llama-kv-cache.cpp ./src/llama-kv-cache.cpp
cp ./llama.cpp/src/llama-model-loader.h ./include/llama-model-loader.h
cp ./llama.cpp/src/llama-model-loader.cpp ./src/llama-model-loader.cpp
cp ./llama.cpp/src/llama-model.h ./include/llama-model.h
cp ./llama.cpp/src/llama-model.cpp ./src/llama-model.cpp
cp ./llama.cpp/src/llama-adapter.h ./include/llama-adapter.h
cp ./llama.cpp/src/llama-adapter.cpp ./src/llama-adapter.cpp
cp ./llama.cpp/src/llama-arch.h ./include/llama-arch.h
cp ./llama.cpp/src/llama-arch.cpp ./src/llama-arch.cpp
cp ./llama.cpp/src/llama-batch.h ./include/llama-batch.h
cp ./llama.cpp/src/llama-batch.cpp ./src/llama-batch.cpp
cp ./llama.cpp/src/llama-cparams.h ./include/llama-cparams.h
cp ./llama.cpp/src/llama-cparams.cpp ./src/llama-cparams.cpp
cp ./llama.cpp/src/llama-hparams.h ./include/llama-hparams.h
cp ./llama.cpp/src/llama-hparams.cpp ./src/llama-hparams.cpp
cp ./llama.cpp/src/llama-impl.h ./include/llama-impl.h
cp ./llama.cpp/src/llama-impl.cpp ./src/llama-impl.cpp

cp ./llama.cpp/src/llama-vocab.h ./include/llama-vocab.h
cp ./llama.cpp/src/llama-vocab.cpp ./src/llama-vocab.cpp
cp ./llama.cpp/src/llama-grammar.h ./include/llama-grammar.h
cp ./llama.cpp/src/llama-grammar.cpp ./src/llama-grammar.cpp
cp ./llama.cpp/src/llama-sampling.h ./include/llama-sampling.h
cp ./llama.cpp/src/llama-sampling.cpp ./src/llama-sampling.cpp

cp ./llama.cpp/src/unicode.h ./include/unicode.h
cp ./llama.cpp/src/unicode.cpp ./src/unicode.cpp
cp ./llama.cpp/src/unicode-data.h ./include/unicode-data.h
cp ./llama.cpp/src/unicode-data.cpp ./src/unicode-data.cpp

cp ./llama.cpp/common/log.h ./include/log.h
cp ./llama.cpp/common/log.cpp ./src/log.cpp
cp ./llama.cpp/common/common.h ./include/common.h
cp ./llama.cpp/common/common.cpp ./src/common.cpp
cp ./llama.cpp/common/sampling.h ./include/sampling.h
cp ./llama.cpp/common/sampling.cpp ./src/sampling.cpp
cp ./llama.cpp/common/chat.cpp ./src/chat.cpp
cp ./llama.cpp/common/chat.hpp ./include/chat.hpp
cp ./llama.cpp/common/chat-template.hpp ./include/chat-template.hpp



# List of files to process
files_add_lm_prefix=(
  "./cpp/llama-impl.h"
  "./cpp/llama-impl.cpp"
  "./cpp/llama-vocab.h"
  "./cpp/llama-vocab.cpp"
  "./cpp/llama-grammar.h"
  "./cpp/llama-grammar.cpp"
  "./cpp/llama-sampling.h"
  "./cpp/llama-sampling.cpp"
  "./cpp/llama-adapter.h"
  "./cpp/llama-adapter.cpp"
  "./cpp/llama-arch.h"
  "./cpp/llama-arch.cpp"
  "./cpp/llama-batch.h"
  "./cpp/llama-batch.cpp"
  "./cpp/llama-chat.h"
  "./cpp/llama-chat.cpp"
  "./cpp/llama-context.h"
  "./cpp/llama-context.cpp"
  "./cpp/llama-kv-cache.h"
  "./cpp/llama-kv-cache.cpp"
  "./cpp/llama-model-loader.h"
  "./cpp/llama-model-loader.cpp"
  "./cpp/llama-model.h"
  "./cpp/llama-model.cpp"
  "./cpp/llama-mmap.h"
  "./cpp/llama-mmap.cpp"
  "./cpp/llama-hparams.h"
  "./cpp/llama-hparams.cpp"
  "./cpp/llama-cparams.h"
  "./cpp/llama-cparams.cpp"
  "./cpp/log.h"
  "./cpp/log.cpp"
  "./cpp/llama.h"
  "./cpp/llama.cpp"
  "./cpp/sampling.cpp"
  "./cpp/sgemm.h"
  "./cpp/sgemm.cpp"
  "./cpp/common.h"
  "./cpp/common.cpp"
  "./cpp/ggml-common.h"
  "./cpp/ggml.h"
  "./cpp/ggml.c"
  "./cpp/gguf.h"
  "./cpp/gguf.cpp"
  "./cpp/ggml-impl.h"
  "./cpp/ggml-cpp.h"
  "./cpp/ggml-opt.h"
  "./cpp/ggml-opt.cpp"
  "./cpp/ggml-metal.h"
  "./cpp/ggml-metal.m"
  "./cpp/ggml-metal-impl.h"
  "./cpp/ggml-quants.h"
  "./cpp/ggml-quants.c"
  "./cpp/ggml-alloc.h"
  "./cpp/ggml-alloc.c"
  "./cpp/ggml-backend.h"
  "./cpp/ggml-backend.cpp"
  "./cpp/ggml-backend-impl.h"
  "./cpp/ggml-backend-reg.cpp"
  "./cpp/ggml-cpu-impl.h"
  "./cpp/ggml-cpu.h"
  "./cpp/ggml-cpu.c"
  "./cpp/ggml-cpu.cpp"
  "./cpp/ggml-cpu-aarch64.h"
  "./cpp/ggml-cpu-aarch64.cpp"
  "./cpp/ggml-cpu-quants.h"
  "./cpp/ggml-cpu-quants.c"
  "./cpp/ggml-cpu-traits.h"
  "./cpp/ggml-cpu-traits.cpp"
  "./cpp/ggml-threading.h"
  "./cpp/ggml-threading.cpp"
  "./cpp/amx/amx.h"
  "./cpp/amx/amx.cpp"
  "./cpp/amx/mmq.h"
  "./cpp/amx/mmq.cpp"
  "./cpp/amx/common.h"
)

## Loop through each file and run the sed commands
#OS=$(uname)
#for file in "${files_add_lm_prefix[@]}"; do
#  # Add prefix to avoid redefinition with other libraries using ggml like whisper.rn
#  if [ "$OS" = "Darwin" ]; then
#    sed -i '' 's/GGML_/LM_GGML_/g' $file
#    sed -i '' 's/ggml_/lm_ggml_/g' $file
#    sed -i '' 's/GGUF_/LM_GGUF_/g' $file
#    sed -i '' 's/gguf_/lm_gguf_/g' $file
#    sed -i '' 's/GGMLMetalClass/LMGGMLMetalClass/g' $file
#  else
#    sed -i 's/GGML_/LM_GGML_/g' $file
#    sed -i 's/ggml_/lm_ggml_/g' $file
#    sed -i 's/GGUF_/LM_GGUF_/g' $file
#    sed -i 's/gguf_/lm_gguf_/g' $file
#    sed -i 's/GGMLMetalClass/LMGGMLMetalClass/g' $file
#  fi
#done
#
#files_iq_add_lm_prefix=(
#  "./cpp/ggml-quants.h"
#  "./cpp/ggml-quants.c"
#  "./cpp/ggml.c"
#)
#
#for file in "${files_iq_add_lm_prefix[@]}"; do
#  # Add prefix to avoid redefinition with other libraries using ggml like whisper.rn
#  if [ "$OS" = "Darwin" ]; then
#    sed -i '' 's/iq2xs_init_impl/lm_iq2xs_init_impl/g' $file
#    sed -i '' 's/iq2xs_free_impl/lm_iq2xs_free_impl/g' $file
#    sed -i '' 's/iq3xs_init_impl/lm_iq3xs_init_impl/g' $file
#    sed -i '' 's/iq3xs_free_impl/lm_iq3xs_free_impl/g' $file
#  else
#    sed -i 's/iq2xs_init_impl/lm_iq2xs_init_impl/g' $file
#    sed -i 's/iq2xs_free_impl/lm_iq2xs_free_impl/g' $file
#    sed -i 's/iq3xs_init_impl/lm_iq3xs_init_impl/g' $file
#    sed -i 's/iq3xs_free_impl/lm_iq3xs_free_impl/g' $file
#  fi
#done
#
#echo "Replacement completed successfully!"
#
#yarn example
#
## Apply patch
#patch -p0 -d ./cpp < ./scripts/common.h.patch
#patch -p0 -d ./cpp < ./scripts/common.cpp.patch
#patch -p0 -d ./cpp < ./scripts/log.cpp.patch
#patch -p0 -d ./cpp < ./scripts/ggml-metal.m.patch
#patch -p0 -d ./cpp < ./scripts/ggml-backend-reg.cpp.patch
#patch -p0 -d ./cpp < ./scripts/ggml.c.patch
#patch -p0 -d ./cpp < ./scripts/ggml-quants.c.patch
#patch -p0 -d ./cpp < ./scripts/llama-mmap.cpp.patch
#
#if [ "$OS" = "Darwin" ]; then
#  # Build metallib (~2.6MB)
#  cd llama.cpp/ggml/src/ggml-metal
#  xcrun --sdk iphoneos metal -c ggml-metal.metal -o ggml-metal.air
#  xcrun --sdk iphoneos metallib ggml-metal.air   -o default.metallib
#  rm ggml-metal.air
#  mv ./default.metallib ../../../../cpp/default.metallib
#
#  cd -
#
#  # Generate .xcode.env.local in iOS example
#  cd example/ios
#  echo export NODE_BINARY=$(command -v node) > .xcode.env.local
#fi

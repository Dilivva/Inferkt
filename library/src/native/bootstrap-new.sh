#!/bin/bash -e

#git submodule init
#git submodule update --recursive

dirs=("src/ggml-cpu" "src/minja" "src/tools/mtmd")

echo "--- Processing single-level directories ---"
for dir_name in "${dirs[@]}"; do
  if [ ! -d "$dir_name" ]; then
    echo "Directory '$dir_name' does not exist. Creating it now..."
    mkdir -p "$dir_name"
  else
    # If it already exists, print a message
    echo "Directory '$dir_name' already exists."
  fi
done

cp ./llama.cpp/include/llama.h ./src/llama.h
cp ./llama.cpp/include/llama-cpp.h ./src/llama-cpp.h

cp ./llama.cpp/ggml/include/ggml.h ./src/ggml.h
cp ./llama.cpp/ggml/include/ggml-alloc.h ./src/ggml-alloc.h
cp ./llama.cpp/ggml/include/ggml-backend.h ./src/ggml-backend.h
cp ./llama.cpp/ggml/include/ggml-cpu.h ./src/ggml-cpu.h
cp ./llama.cpp/ggml/include/ggml-cpp.h ./src/ggml-cpp.h
cp ./llama.cpp/ggml/include/ggml-opt.h ./src/ggml-opt.h
cp ./llama.cpp/ggml/include/ggml-metal.h ./src/ggml-metal.h
cp ./llama.cpp/ggml/include/gguf.h ./src/gguf.h

cp ./llama.cpp/ggml/src/ggml-metal/ggml-metal.m ./src/ggml-metal.m
cp ./llama.cpp/ggml/src/ggml-metal/ggml-metal-impl.h ./src/ggml-metal-impl.h
cp ./llama.cpp/ggml/src/ggml-metal/ggml-metal.metal ./src/ggml-metal.metal

cp ./llama.cpp/ggml/src/ggml-cpu/ggml-cpu.c ./src/ggml-cpu/ggml-cpu.c
cp ./llama.cpp/ggml/src/ggml-cpu/ggml-cpu.cpp ./src/ggml-cpu/ggml-cpu.cpp
cp ./llama.cpp/ggml/src/ggml-cpu/ggml-cpu-impl.h ./src/ggml-cpu/ggml-cpu-impl.h
cp ./llama.cpp/ggml/src/ggml-cpu/ggml-cpu-aarch64.h ./src/ggml-cpu/ggml-cpu-aarch64.h
cp ./llama.cpp/ggml/src/ggml-cpu/ggml-cpu-aarch64.cpp ./src/ggml-cpu/ggml-cpu-aarch64.cpp
cp ./llama.cpp/ggml/src/ggml-cpu/ggml-cpu-quants.h ./src/ggml-cpu/ggml-cpu-quants.h
cp ./llama.cpp/ggml/src/ggml-cpu/ggml-cpu-quants.c ./src/ggml-cpu/ggml-cpu-quants.c
cp ./llama.cpp/ggml/src/ggml-cpu/ggml-cpu-traits.h ./src/ggml-cpu/ggml-cpu-traits.h
cp ./llama.cpp/ggml/src/ggml-cpu/ggml-cpu-traits.cpp ./src/ggml-cpu/ggml-cpu-traits.cpp
cp ./llama.cpp/ggml/src/ggml-cpu/common.h ./src/ggml-cpu/common.h

cp ./llama.cpp/ggml/src/ggml-cpu/unary-ops.h ./src/ggml-cpu/unary-ops.h
cp ./llama.cpp/ggml/src/ggml-cpu/unary-ops.cpp ./src/ggml-cpu/unary-ops.cpp
cp ./llama.cpp/ggml/src/ggml-cpu/binary-ops.h ./src/ggml-cpu/binary-ops.h
cp ./llama.cpp/ggml/src/ggml-cpu/binary-ops.cpp ./src/ggml-cpu/binary-ops.cpp
cp ./llama.cpp/ggml/src/ggml-cpu/vec.h ./src/ggml-cpu/vec.h
cp ./llama.cpp/ggml/src/ggml-cpu/vec.cpp ./src/ggml-cpu/vec.cpp
cp ./llama.cpp/ggml/src/ggml-cpu/simd-mappings.h ./src/ggml-cpu/simd-mappings.h
cp ./llama.cpp/ggml/src/ggml-cpu/ops.h ./src/ggml-cpu/ops.h
cp ./llama.cpp/ggml/src/ggml-cpu/ops.cpp ./src/ggml-cpu/ops.cpp

cp -r ./llama.cpp/ggml/src/ggml-cpu/amx ./src/ggml-cpu/

cp ./llama.cpp/ggml/src/ggml-cpu/llamafile/sgemm.h ./src/ggml-cpu/sgemm.h
cp ./llama.cpp/ggml/src/ggml-cpu/llamafile/sgemm.cpp ./src/ggml-cpu/sgemm.cpp

cp ./llama.cpp/ggml/src/ggml.c ./src/ggml.c
cp ./llama.cpp/ggml/src/ggml-impl.h ./src/ggml-impl.h
cp ./llama.cpp/ggml/src/ggml-alloc.c ./src/ggml-alloc.c
cp ./llama.cpp/ggml/src/ggml-backend.cpp ./src/ggml-backend.cpp
cp ./llama.cpp/ggml/src/ggml-backend-impl.h ./src/ggml-backend-impl.h
cp ./llama.cpp/ggml/src/ggml-backend-reg.cpp ./src/ggml-backend-reg.cpp
cp ./llama.cpp/ggml/src/ggml-common.h ./src/ggml-common.h
cp ./llama.cpp/ggml/src/ggml-opt.cpp ./src/ggml-opt.cpp
cp ./llama.cpp/ggml/src/ggml-quants.h ./src/ggml-quants.h
cp ./llama.cpp/ggml/src/ggml-quants.c ./src/ggml-quants.c
cp ./llama.cpp/ggml/src/ggml-threading.cpp ./src/ggml-threading.cpp
cp ./llama.cpp/ggml/src/ggml-threading.h ./src/ggml-threading.h
cp ./llama.cpp/ggml/src/gguf.cpp ./src/gguf.cpp

cp ./llama.cpp/src/llama.cpp ./src/llama.cpp
cp ./llama.cpp/src/llama-chat.h ./src/llama-chat.h
cp ./llama.cpp/src/llama-chat.cpp ./src/llama-chat.cpp
cp ./llama.cpp/src/llama-context.h ./src/llama-context.h
cp ./llama.cpp/src/llama-context.cpp ./src/llama-context.cpp
cp ./llama.cpp/src/llama-mmap.h ./src/llama-mmap.h
cp ./llama.cpp/src/llama-mmap.cpp ./src/llama-mmap.cpp
cp ./llama.cpp/src/llama-kv-cache.h ./src/llama-kv-cache.h
cp ./llama.cpp/src/llama-kv-cache.cpp ./src/llama-kv-cache.cpp
cp ./llama.cpp/src/llama-model-loader.h ./src/llama-model-loader.h
cp ./llama.cpp/src/llama-model-loader.cpp ./src/llama-model-loader.cpp
cp ./llama.cpp/src/llama-model.h ./src/llama-model.h
cp ./llama.cpp/src/llama-model.cpp ./src/llama-model.cpp
cp ./llama.cpp/src/llama-adapter.h ./src/llama-adapter.h
cp ./llama.cpp/src/llama-adapter.cpp ./src/llama-adapter.cpp
cp ./llama.cpp/src/llama-arch.h ./src/llama-arch.h
cp ./llama.cpp/src/llama-arch.cpp ./src/llama-arch.cpp
cp ./llama.cpp/src/llama-batch.h ./src/llama-batch.h
cp ./llama.cpp/src/llama-batch.cpp ./src/llama-batch.cpp
cp ./llama.cpp/src/llama-cparams.h ./src/llama-cparams.h
cp ./llama.cpp/src/llama-cparams.cpp ./src/llama-cparams.cpp
cp ./llama.cpp/src/llama-hparams.h ./src/llama-hparams.h
cp ./llama.cpp/src/llama-hparams.cpp ./src/llama-hparams.cpp
cp ./llama.cpp/src/llama-impl.h ./src/llama-impl.h
cp ./llama.cpp/src/llama-impl.cpp ./src/llama-impl.cpp

cp ./llama.cpp/src/llama-vocab.h ./src/llama-vocab.h
cp ./llama.cpp/src/llama-vocab.cpp ./src/llama-vocab.cpp
cp ./llama.cpp/src/llama-grammar.h ./src/llama-grammar.h
cp ./llama.cpp/src/llama-grammar.cpp ./src/llama-grammar.cpp
cp ./llama.cpp/src/llama-sampling.h ./src/llama-sampling.h
cp ./llama.cpp/src/llama-sampling.cpp ./src/llama-sampling.cpp

cp ./llama.cpp/src/unicode.h ./src/unicode.h
cp ./llama.cpp/src/unicode.cpp ./src/unicode.cpp
cp ./llama.cpp/src/unicode-data.h ./src/unicode-data.h
cp ./llama.cpp/src/unicode-data.cpp ./src/unicode-data.cpp

cp ./llama.cpp/src/llama-graph.h ./src/llama-graph.h
cp ./llama.cpp/src/llama-graph.cpp ./src/llama-graph.cpp
cp ./llama.cpp/src/llama-io.h ./src/llama-io.h
cp ./llama.cpp/src/llama-io.cpp ./src/llama-io.cpp
cp ./llama.cpp/src/llama-memory.h ./src/llama-memory.h
cp ./llama.cpp/src/llama-memory.cpp ./src/llama-memory.cpp

cp ./llama.cpp/common/log.h ./src/log.h
cp ./llama.cpp/common/log.cpp ./src/log.cpp
cp ./llama.cpp/common/common.h ./src/common.h
cp ./llama.cpp/common/common.cpp ./src/common.cpp
cp ./llama.cpp/common/sampling.h ./src/sampling.h
cp ./llama.cpp/common/sampling.cpp ./src/sampling.cpp
cp ./llama.cpp/common/json-schema-to-grammar.h ./src/json-schema-to-grammar.h
cp ./llama.cpp/common/json-schema-to-grammar.cpp ./src/json-schema-to-grammar.cpp
cp ./llama.cpp/common/json.hpp ./src/json.hpp

cp ./llama.cpp/common/chat.h ./src/chat.h
cp ./llama.cpp/common/chat.cpp ./src/chat.cpp

cp ./llama.cpp/common/minja/minja.hpp ./src/minja/minja.hpp
cp ./llama.cpp/common/minja/chat-template.hpp ./src/minja/chat-template.hpp

# Copy multimodal files from tools/mtmd
cp ./llama.cpp/tools/mtmd/mtmd.h ./src/tools/mtmd/mtmd.h
cp ./llama.cpp/tools/mtmd/mtmd.cpp ./src/tools/mtmd/mtmd.cpp
cp ./llama.cpp/tools/mtmd/clip.h ./src/tools/mtmd/clip.h
cp ./llama.cpp/tools/mtmd/clip.cpp ./src/tools/mtmd/clip.cpp
cp ./llama.cpp/tools/mtmd/clip-impl.h ./src/tools/mtmd/clip-impl.h
cp ./llama.cpp/tools/mtmd/mtmd-helper.cpp ./src/tools/mtmd/mtmd-helper.cpp
cp ./llama.cpp/common/stb_image.h ./src/tools/mtmd/stb_image.h

# List of files to process
files_add_lm_prefix=(
  "./src/llama-impl.h"
  "./src/llama-impl.cpp"
  "./src/llama-vocab.h"
  "./src/llama-vocab.cpp"
  "./src/llama-grammar.h"
  "./src/llama-grammar.cpp"
  "./src/llama-sampling.h"
  "./src/llama-sampling.cpp"
  "./src/llama-adapter.h"
  "./src/llama-adapter.cpp"
  "./src/llama-arch.h"
  "./src/llama-arch.cpp"
  "./src/llama-batch.h"
  "./src/llama-batch.cpp"
  "./src/llama-chat.h"
  "./src/llama-chat.cpp"
  "./src/llama-context.h"
  "./src/llama-context.cpp"
  "./src/llama-kv-cache.h"
  "./src/llama-kv-cache.cpp"
  "./src/llama-model-loader.h"
  "./src/llama-model-loader.cpp"
  "./src/llama-model.h"
  "./src/llama-model.cpp"
  "./src/llama-mmap.h"
  "./src/llama-mmap.cpp"
  "./src/llama-hparams.h"
  "./src/llama-hparams.cpp"
  "./src/llama-cparams.h"
  "./src/llama-cparams.cpp"
  "./src/llama-graph.h"
  "./src/llama-graph.cpp"
  "./src/llama-io.h"
  "./src/llama-io.cpp"
  "./src/llama-memory.h"
  "./src/llama-memory.cpp"
  "./src/log.h"
  "./src/log.cpp"
  "./src/llama.h"
  "./src/llama.cpp"
  "./src/sampling.cpp"
  "./src/ggml-cpu/sgemm.h"
  "./src/ggml-cpu/sgemm.cpp"
  "./src/common.h"
  "./src/common.cpp"
  "./src/json-schema-to-grammar.h"
  "./src/chat.cpp"
  "./src/ggml-common.h"
  "./src/ggml.h"
  "./src/ggml.c"
  "./src/gguf.h"
  "./src/gguf.cpp"
  "./src/ggml-impl.h"
  "./src/ggml-cpp.h"
  "./src/ggml-opt.h"
  "./src/ggml-opt.cpp"
  "./src/ggml-metal.h"
  "./src/ggml-metal.m"
  "./src/ggml-metal-impl.h"
  "./src/ggml-quants.h"
  "./src/ggml-quants.c"
  "./src/ggml-alloc.h"
  "./src/ggml-alloc.c"
  "./src/ggml-backend.h"
  "./src/ggml-backend.cpp"
  "./src/ggml-backend-impl.h"
  "./src/ggml-backend-reg.cpp"
  "./src/ggml-cpu.h"
  "./src/ggml-cpu/ggml-cpu-impl.h"
  "./src/ggml-cpu/ggml-cpu.c"
  "./src/ggml-cpu/ggml-cpu.cpp"
  "./src/ggml-cpu/ggml-cpu-aarch64.h"
  "./src/ggml-cpu/ggml-cpu-aarch64.cpp"
  "./src/ggml-cpu/ggml-cpu-quants.h"
  "./src/ggml-cpu/ggml-cpu-quants.c"
  "./src/ggml-cpu/ggml-cpu-traits.h"
  "./src/ggml-cpu/ggml-cpu-traits.cpp"
  "./src/ggml-cpu/common.h"
  "./src/ggml-threading.h"
  "./src/ggml-threading.cpp"
  "./src/ggml-cpu/amx/amx.h"
  "./src/ggml-cpu/amx/amx.cpp"
  "./src/ggml-cpu/amx/mmq.h"
  "./src/ggml-cpu/amx/mmq.cpp"
  "./src/ggml-cpu/amx/common.h"
  "./src/ggml-cpu/unary-ops.h"
  "./src/ggml-cpu/unary-ops.cpp"
  "./src/ggml-cpu/binary-ops.h"
  "./src/ggml-cpu/binary-ops.cpp"
  "./src/ggml-cpu/vec.h"
  "./src/ggml-cpu/vec.cpp"
  "./src/ggml-cpu/simd-mappings.h"
  "./src/ggml-cpu/ops.h"
  "./src/ggml-cpu/ops.cpp"
  # Multimodal files
  "./src/tools/mtmd/mtmd.h"
  "./src/tools/mtmd/mtmd.cpp"
  "./src/tools/mtmd/clip.h"
  "./src/tools/mtmd/clip.cpp"
  "./src/tools/mtmd/clip-impl.h"
  "./src/tools/mtmd/mtmd-helper.cpp"
)

#echo "--- Seding ---"
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
#  "./src/ggml-quants.h"
#  "./src/ggml-quants.c"
#  "./src/ggml.c"
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

#yarn example

# Apply patch
patch -p0 -d ./src < ./patches/common.h.patch
patch -p0 -d ./src < ./patches/common.cpp.patch
#patch -p0 -d ./src < ./patches/chat.h.patch
#patch -p0 -d ./src < ./patches/chat.cpp.patch
#patch -p0 -d ./src < ./patches/log.cpp.patch
#patch -p0 -d ./src < ./patches/ggml-metal.m.patch
#patch -p0 -d ./src < ./patches/ggml.c.patch
#patch -p0 -d ./src < ./patches/ggml-quants.c.patch
#patch -p0 -d ./src < ./patches/llama-mmap.cpp.patch
#rm -rf ./src/*.orig
#
#echo "Generating MetalLib"
#
#if [ "$OS" = "Darwin" ]; then
#  # Build metallib (~2.6MB)
#  cd llama.cpp/ggml/src/ggml-metal
#
#  # Create a symbolic link to ggml-common.h in the current directory
#  ln -sf ../ggml-common.h .
#
#  xcrun --sdk iphoneos metal -c ggml-metal.metal -o ggml-metal.air -DGGML_METAL_USE_BF16=1
#  xcrun --sdk iphoneos metallib ggml-metal.air   -o ggml-llama.metallib
#  rm ggml-metal.air
#  mv ./ggml-llama.metallib ../../../../src/ggml-llama.metallib
#
#  xcrun --sdk iphonesimulator metal -c ggml-metal.metal -o ggml-metal.air -DGGML_METAL_USE_BF16=1
#  xcrun --sdk iphonesimulator metallib ggml-metal.air   -o ggml-llama.metallib
#  rm ggml-metal.air
#  mv ./ggml-llama.metallib ../../../../src/ggml-llama-sim.metallib
#
#  # Remove the symbolic link
#  rm ggml-common.h
#
#  cd -
#
#  # Generate .xcode.env.local in iOS example
#  cd example/ios
#  echo export NODE_BINARY=$(command -v node) > .xcode.env.local
#fi

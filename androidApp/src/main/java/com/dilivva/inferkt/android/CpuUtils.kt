package com.dilivva.inferkt.android

import android.os.Build
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException


object CpuUtils {

    fun getCpuFeatures(): String {
        val file = File("/proc/cpuinfo")
        val stringBuilder = StringBuilder()
        try {
            val bufferedReader = BufferedReader(FileReader(file))
            var line: String
            while ((bufferedReader.readLine().also { line = it }) != null) {
                if (line.startsWith("Features")) {
                    stringBuilder.append(line)
                    break
                }
            }
            bufferedReader.close()
            return stringBuilder.toString()
        } catch (e: IOException) {
            println("Couldn't read /proc/cpuinfo")
            return ""
        }
    }

    fun loadLibrary(){
        val cpuFeatures = getCpuFeatures()
        println("Cpu: $cpuFeatures")
        val hasFp16 = cpuFeatures.contains("fp16") || cpuFeatures.contains("fphp")
        val hasDotProd = cpuFeatures.contains("dotprod") || cpuFeatures.contains("asimddp")
        val hasSve = cpuFeatures.contains("sve")
        val hasI8mm = cpuFeatures.contains("i8mm")
        val isAtLeastArmV82 =
            cpuFeatures.contains("asimd") && cpuFeatures.contains("crc32") && cpuFeatures.contains("aes")
        val isAtLeastArmV84 = cpuFeatures.contains("dcpop") && cpuFeatures.contains("uscat")
        println("- hasFp16: $hasFp16")
        println("- hasDotProd: $hasDotProd")
        println("- hasSve: $hasSve")
        println("- hasI8mm: $hasI8mm")
        println("- isAtLeastArmV82: $isAtLeastArmV82")
        println("- isAtLeastArmV84: $isAtLeastArmV84")

        if (hasFp16 && hasDotProd && isAtLeastArmV82){
            System.loadLibrary("inferkt_v8_2")
            println("Loaded omogbon")
            return
        }

        if (isArm64V8a()) {
            if (hasDotProd && hasI8mm) {
                println("Loading v8_2_dotprod_i8mm.so")
                System.loadLibrary("inferkt_v8_2_dotprod_i8mm")
            } else if (hasDotProd) {
                println("Loading v8_2_dotprod.so")
                System.loadLibrary("inferkt_v8_2_dotprod")
            } else if (hasI8mm) {
                println("Loading v8_2_i8mm.so")
                System.loadLibrary("inferkt_v8_2_i8mm")
            } else if (hasFp16) {
                println("Loading v8_2.so")
                System.loadLibrary("inferkt_v8_2")
            } else {
                println("Loading default v8.so")
                System.loadLibrary("inferkt_v8")
            }
        } else if (isX86_64()) {
            println("Loading x86_64.so")
            System.loadLibrary("inferkt_x86_64")
        } else {
            println("Loading default .so")
            System.loadLibrary("inferkt-android")
        }
    }

    private fun isArm64V8a(): Boolean {
        return Build.SUPPORTED_ABIS[0] == "arm64-v8a"
    }

    private fun isX86_64(): Boolean {
        return Build.SUPPORTED_ABIS[0] == "x86_64"
    }
}
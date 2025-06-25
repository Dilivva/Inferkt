[![Kotlin](https://img.shields.io/badge/kotlin-2.1.21-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![Maven Central](https://img.shields.io/maven-central/v/com.dilivva/inferkt
)](http://kotlinlang.org)

# InferKt

**InferKt** is a [llama.cpp](https://github.com/ggml-org/llama.cpp) binding for Kotlin multiplatform API for common use on (Android, iOS).

## How to use

> This library is experimental and api may be subject to change.

### Add the dependency

1. Add the dependency to your module's `build.gradle.kts` file:
```kotlin
    kotlin {
       sourceSets {
         commonMain.dependencies{
            implementation("com.dilivva:inferkt:${version}")
        } 
       }
    }
```
 On iOS: Add **Accelerate.framework** and **Metal.framework** to your project on **Xcode**.

2. Explore API in common code: 

```kotlin
//Create an instance:
private val inference =  createInference()

//Load a model:
val modelSettings = ModelSettings(
    val modelPath: String, //model absolute path
    val numberOfGpuLayers: Int = 0, //number of GPU layers to use for computation. Defaults to 0 (CPU only).
    val useMmap: Boolean = true, //whether to use memory mapping for model loading. Defaults to true.
    val useMlock: Boolean = true, //whether to lock the model in memory. Defaults to half of the total threads.
    val numberOfThreads: Int = -1, //number of threads to use for inference. Defaults to -1 (half of the total threads).
    val context: Int = 512, //context window size for the model. Defaults to 512. Setting higher context sizes may result in out-of-memory errors.
    val batchSize: Int = 512 
)
inference.preloadModel(modelSettings =  modelSettings, progressCallback: { progress: Float -> true })

//Set sampling settings you can tune the model before each inference:
val samplingSettings = SamplingSettings(..)
inference.setSamplingParams(samplingSettings)

//Completion
inference.completion(prompt: "I am a nice cat:", maxTokens: 100, onGenerate: { event: GenerationEvent -> })

//Chat
inference.chat(prompt: "Tell me a joke about Kotlin", maxTokens: 100, onGenerate: { event: GenerationEvent -> })

//Observe events:
when(event){
    is GenerationEvent.Error -> println("Error: ${it.error}")
    GenerationEvent.Generated -> // Done generating
    is GenerationEvent.Generating -> Streaming generated text
    GenerationEvent.Loading -> // Evaluating prompt
}
```


## Acknowledgements

[llama.cpp](https://github.com/ggml-org/llama.cpp): for their awesome work on local inference.

[llama.rn](https://github.com/mybigday/llama.rn): inspired the build process implemented in this project.

[Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html): awesome cross-platform framework.



## Contributing

We welcome contributions to InferKt!

## License

InferKt is licensed under the MIT License. See the LICENSE file for details.




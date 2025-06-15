//
//  ContentViewModel.swift
//  iosApp
//
//  Created by Ayodele Kehinde on 15/06/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import Observation
import library

@available(iOS 17.0, *)
@Observable class ContentViewModel {
    private let inference = Inference_iosKt.createInference()
    
    var filePath: String = ""
    var fileContent: String = ""
    var showPicker = false
    var isModelLoaded = false
    var progress: Float = 0.0
        
    var messages: [ChatMessage] = []
    var inputText: String = ""
    var isGenerating: Bool = false
    
    func showPicker(show: Bool) {
        showPicker = show
    }
    func setFilePath(path: String) {
        filePath = path
        print("Path: \(filePath)")
    }
    
    func loadModel() {
        DispatchQueue.global(qos: .userInteractive).async { [weak self] in
            guard let current = self else { return }
            let modelSettings = ModelSettings(modelPath: current.filePath,
                                              numberOfGpuLayers: 99,
                                              useMmap: true,
                                              useMlock: true,
                                              numberOfThreads: 2,
                                              context: 512,
                                              batchSize: 512)
            let loaded = current.inference.preloadModel(
                modelSettings: modelSettings,
                progressCallback: { progress in
                print("Progress: \(progress)")
                    DispatchQueue.main.async {
                        current.progress = progress.floatValue
                    }
                    return true
                }
            )
            DispatchQueue.main.async {
                current.isModelLoaded = loaded
            }
            print("Model loaded: \(loaded)")
        }
    }
    
    func stopGenerating() {
        inference.cancelGeneration()
    }
    
    func sendMessage(){
        if !isModelLoaded { return }
        guard !inputText.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty else { return }
        
        print(inputText)
        
        // Append the user's message.
        let userMessage = ChatMessage(text: inputText, isUser: true)
        messages.append(userMessage)
        
        var botMessage = ChatMessage(text: "Loading..\n\n", isUser: false)
        messages.append(botMessage)
        
        let botMessageIndex = messages.count - 1
        let prompt = inputText
        inputText = ""
        DispatchQueue.global(qos: .userInteractive).async { [weak self] in
            print("Generating")
            guard let current = self else { return }
            current.inference.chat(
                prompt: prompt,
                maxTokens: 1024,
                onGenerate: { event in
                    // Make sure updates to UI state happen on the main actor.
                    Task { @MainActor in
                        switch event{
                        case let generating as GenerationEventGenerating:
                            current.isGenerating = true
                            current.messages[botMessageIndex].text += generating.text
                        case let error as GenerationEventError:
                            current.isGenerating = false
                            print("Error: \(error.error)")
                        case is GenerationEventGenerated:
                            current.isGenerating = false
                        default:
                            print("None")
                        }
                    }
                }
            )
        }
    }
}

import SwiftUI
import library

struct ContentView: View {
    @State private var filePath: String = ""
    @State private var fileContent: String = ""
    @State private var showPicker = false
    @State private var isModelLoaded = false
    //let greet = Greeting().greet()
    let inference = Inference_iosKt.createInference()
    var body: some View {
        VStack {
            ChatView(
                inference: inference,
                isModelLoaded: $isModelLoaded
            )
            
            if !isModelLoaded {
                Button("Pick model") {
                    showPicker = true
                }
                .sheet(isPresented: $showPicker) {
                    DocumentPicker { url in
                        // Access the file's absolute path
                        filePath = url.path
                        print("Path: \(filePath)")
                        // Load the file's content
                        
                    }
                }
                if !filePath.isEmpty{
                    Button("Load model") {
                        isModelLoaded = inference.preloadModel(path: filePath)
                        print("Model loaded: \(isModelLoaded)")
                    }
                }
            }
            
            
//            if isModelLoaded{
//                Button("Test Generation") {
//                    inference.generate(prompt: "What is democracy", maxTokens: 100, onGenerate: { text in print(text) })
//                    print("Model loaded: \(isModelLoaded)")
//                }
//            }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}

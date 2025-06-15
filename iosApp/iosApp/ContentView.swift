import SwiftUI
import library

@available(iOS 17.0, *)
struct ContentView: View {
    
    @State private var viewModel = ContentViewModel()
    
    var body: some View {
        VStack {
            ChatView(viewModel: viewModel)
            
            if !viewModel.isModelLoaded {
                Button("Pick model") {
                    viewModel.showPicker(show: true)
                }
                .sheet(isPresented: $viewModel.showPicker) {
                    DocumentPicker { url in
                        viewModel.setFilePath(path: url.path)
                    }
                }
                if !viewModel.filePath.isEmpty {
                    Button("Load model") {
                        viewModel.loadModel()
                    }
                }
                ProgressView(value: viewModel.progress)
                    .progressViewStyle(LinearProgressViewStyle())
                    .padding()
            }
        
        }
    }
}

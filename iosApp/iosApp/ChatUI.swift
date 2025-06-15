//
//  ChatUI.swift
//  iosApp
//
//  Created by Ayodele Kehinde on 06/02/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import library

struct ChatMessage: Identifiable {
    let id = UUID()
    var text: String
    let isUser: Bool  // true if the message is from the user, false for a received message
}

@available(iOS 17.0, *)
struct ChatView: View {
    
    @Bindable var viewModel: ContentViewModel
    
    var body: some View {
        VStack {
            // Message List
            ScrollViewReader { scrollView in
                List(viewModel.messages) { message in
                    HStack {
                        if message.isUser {
                            Spacer()  // Push user messages to the right
                            Text(message.text)
                                .padding()
                                .background(Color.blue)
                                .foregroundColor(.white)
                                .cornerRadius(8)
                        } else {
                            Text(message.text)
                                .padding()
                                .background(Color.gray.opacity(0.2))
                                .cornerRadius(8)
                            Spacer()  // Push received messages to the left
                        }
                    }
                    .padding(.vertical, 4)
                    .listRowSeparator(.hidden)
                    // Use the id to scroll to the latest message.
                    .id(message.id)
                }
                .listStyle(PlainListStyle())
                // Scroll to the bottom when a new message is added.
                .onChange(of: viewModel.messages.count) { _ in
                    
                    if let lastMessage = viewModel.messages.last {
                        withAnimation {
                            scrollView.scrollTo(lastMessage.id, anchor: .bottom)
                        }
                    }
                }.onChange(of: viewModel.messages.last?.text){
                    if let lastMessage = viewModel.messages.last {
                        withAnimation {
                            scrollView.scrollTo(lastMessage.id, anchor: .bottom)
                        }
                    }
                }
            }
            
            Divider()
            
            // Input Field and Send Button
            HStack {
                TextField("Enter message...", text: $viewModel.inputText)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .frame(minHeight: CGFloat(40))
                
                if viewModel.isGenerating {
                    Button(action: viewModel.stopGenerating) {
                        Text("Stop")
                            .bold()
                            .padding(.horizontal)
                            .padding(.vertical, 8)
                            .background(Color.green)
                            .foregroundColor(.white)
                            .cornerRadius(8)
                    }
                }else{
                    Button(action: viewModel.sendMessage) {
                        Text("Send")
                            .bold()
                            .padding(.horizontal)
                            .padding(.vertical, 8)
                            .background(Color.green)
                            .foregroundColor(.white)
                            .cornerRadius(8)
                    }
                }
            }
            .padding()
//            ProgressView(value: progress)
//                .progressViewStyle(LinearProgressViewStyle())
//                .padding()
        }
    }

}

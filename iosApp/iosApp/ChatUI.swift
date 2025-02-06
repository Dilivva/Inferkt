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

struct ChatView: View {
    let inference: Inference
    @Binding var isModelLoaded: Bool
    
    @State private var messages: [ChatMessage] = []
    @State private var inputText: String = ""
    
    var body: some View {
        VStack {
            // Message List
            ScrollViewReader { scrollView in
                List(messages) { message in
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
                .onChange(of: messages.count) { _ in
                    
                    if let lastMessage = messages.last {
                        withAnimation {
                            scrollView.scrollTo(lastMessage.id, anchor: .bottom)
                        }
                    }
                }
            }
            
            Divider()
            
            // Input Field and Send Button
            HStack {
                TextField("Enter message...", text: $inputText)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .frame(minHeight: CGFloat(40))
                
                
                Button(action: sendMessage) {
                    Text("Send")
                        .bold()
                        .padding(.horizontal)
                        .padding(.vertical, 8)
                        .background(Color.green)
                        .foregroundColor(.white)
                        .cornerRadius(8)
                }
            }
            .padding()
        }
    }
    
    // Function to handle sending a message.
    func sendMessage() {
        if !isModelLoaded { return }
        guard !inputText.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty else { return }
        
        print(inputText)
        
        // Append the user's message.
        let userMessage = ChatMessage(text: inputText, isUser: true)
        messages.append(userMessage)
        
        var botMessage = ChatMessage(text: "Loading..", isUser: false)
        messages.append(botMessage)
        
        let botMessageIndex = messages.count - 1
        let prompt = inputText
        inputText = ""
        Task {
                print("Gene")
                inference.generate(
                    prompt: prompt,
                    maxTokens: 100,
                    onGenerate: { text in
                        // Make sure updates to UI state happen on the main actor.
                        Task { @MainActor in
                            messages[botMessageIndex].text += text
                        }
                    }
                )
        }

    

        
        // Simulate an echo response after a short delay.
//        DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
//            let responseMessage = ChatMessage(text: "Echo: \(userMessage.text)", isUser: false)
//            
//            messages.append(responseMessage)
//        }
    }
}

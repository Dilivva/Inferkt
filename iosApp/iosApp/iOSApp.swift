import SwiftUI

@main
struct iOSApp: App {
	var body: some Scene {
		WindowGroup {
            if #available(iOS 17.0, *) {
                ContentView()
            } else {
                Text("ios 17 below")
            }
		}
	}
}

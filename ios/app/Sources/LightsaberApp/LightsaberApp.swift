import SwiftUI

@main
struct LightsaberApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

struct ContentView: View {
    var body: some View {
        LightsaberView(
            state: LightsaberState(
                bladeColor: .red,
                bladeState: .deactivated,
                onEvent: { _ in }
            )
        )
    }
}

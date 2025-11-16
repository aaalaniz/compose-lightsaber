import SwiftUI

struct LightsaberState {
    let bladeColor: Color
    let bladeState: BladeState
    let onEvent: (LightsaberEvent) -> Void
}

enum LightsaberEvent {
    case settingsSelected
    case lightsaberActivating
    case lightsaberDeactivating
    case lightsaberActivated
    case lightsaberDeactivated
}

enum BladeState {
    case initializing
    case activating
    case activated
    case deactivating
    case deactivated
}

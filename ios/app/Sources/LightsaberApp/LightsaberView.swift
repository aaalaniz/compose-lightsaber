import SwiftUI

struct LightsaberView: View {
    let state: LightsaberState

    var body: some View {
        ZStack {
            Color.black.ignoresSafeArea()

            VStack {
                HStack {
                    Spacer()
                    Button(action: {
                        state.onEvent(.settingsSelected)
                    }) {
                        Image(systemName: "gear")
                            .foregroundColor(.white)
                    }
                    .padding()
                }
                Spacer()
            }

            VStack {
                Spacer()
                // Lightsaber blade will go here
                Image("lightsaber_handle")
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 110)
                    .onTapGesture {
                        switch state.bladeState {
                        case .deactivated:
                            state.onEvent(.lightsaberActivating)
                        case .activating, .activated, .deactivating:
                            state.onEvent(.lightsaberDeactivating)
                        case .initializing:
                            break
                        }
                    }
            }
        }
    }
}

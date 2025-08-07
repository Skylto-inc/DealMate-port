import SwiftUI
import shared

struct ContentView: View {
    @State private var toggles: [FeatureToggle] = []
    let sdk = DealMateSDK(databaseDriverFactory: DatabaseDriverFactory())

    var body: some View {
        VStack {
            ForEach(toggles, id: \.name) { toggle in
                Text("\(toggle.name): \(toggle.enabled ? "Enabled" : "Disabled")")
            }
        }.onAppear {
            sdk.getFeatureToggles(forceReload: false) { result, error in
                if let result = result {
                    self.toggles = result
                }
            }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}

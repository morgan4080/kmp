import UIKit
import SwiftUI
import shared

struct ComposeViewControllerToSwiftUI: UIViewControllerRepresentable {
    private let sharedStatus: SharedStatus = SharedStatus()

    private let lifecycle: LifecycleRegistry
        private let topSafeArea: Float
        private let bottomSafeArea: Float
    
    init(lifecycle: LifecycleRegistry, topSafeArea: Float, bottomSafeArea: Float) {
            sharedStatus.start()
            self.lifecycle = lifecycle
            self.topSafeArea = topSafeArea
            self.bottomSafeArea = bottomSafeArea
    }

    func makeUIViewController(context: Context) -> UIViewController {
        return Main_iosKt.MainViewController(
                    lifecycle: lifecycle,
                    topSafeArea: topSafeArea,
                    bottomSafeArea: bottomSafeArea,
                    connectivityStatus: sharedStatus
                )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}




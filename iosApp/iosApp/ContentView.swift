import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    class Coordinator: NSObject {
        @objc func handleEdgePan(_ gesture: UIScreenEdgePanGestureRecognizer) {
            if gesture.state == .recognized || gesture.state == .ended {
                IosBackBridgeKt.iosTriggerBack()
            }
        }
    }

    func makeCoordinator() -> Coordinator { Coordinator() }

    func makeUIViewController(context: Context) -> UIViewController {
        let vc = MainViewControllerKt.MainViewController()
        let edgePan = UIScreenEdgePanGestureRecognizer(target: context.coordinator, action: #selector(Coordinator.handleEdgePan(_:)))
        edgePan.edges = .left
        vc.view.addGestureRecognizer(edgePan)
        return vc
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea()
    }
}




//
//  SKContentView.swift
//  SwissKnife
//
//  Created by ilyadreamix on 09/03/2026.
//

import SwiftUI
import SKExampleCore

struct SKContentView: View {
    var body: some View {
        SKComposeView()
            .ignoresSafeArea(.all)
    }
}

private struct SKComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        SKExamplesViewControllerKt.SKExamplesViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

#Preview {
    SKContentView()
}

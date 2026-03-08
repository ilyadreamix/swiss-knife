package io.github.ilyadreamix.swissknife.dialogs

import platform.UIKit.NSLayoutConstraint
import platform.UIKit.UIColor
import platform.UIKit.UIModalPresentationOverFullScreen
import platform.UIKit.UIStatusBarStyle
import platform.UIKit.UIViewController
import platform.UIKit.addChildViewController

internal class SKTransparentDialogViewController(
  private val contentViewController: UIViewController,
  private val statusBarStyle: UIStatusBarStyle,
) : UIViewController(nibName = null, bundle = null) {

  init { modalPresentationStyle = UIModalPresentationOverFullScreen }

  override fun viewDidLoad() {
    super.viewDidLoad()

    view.backgroundColor = UIColor.clearColor
    addChildViewController(contentViewController)

    val contentView = contentViewController.view
    contentView.translatesAutoresizingMaskIntoConstraints = false
    view.addSubview(contentView)

    NSLayoutConstraint.activateConstraints(
      listOf(
        contentView.topAnchor.constraintEqualToAnchor(view.topAnchor),
        contentView.bottomAnchor.constraintEqualToAnchor(view.bottomAnchor),
        contentView.leadingAnchor.constraintEqualToAnchor(view.leadingAnchor),
        contentView.trailingAnchor.constraintEqualToAnchor(view.trailingAnchor),
      )
    )
  }

  override fun preferredStatusBarStyle() = statusBarStyle
}

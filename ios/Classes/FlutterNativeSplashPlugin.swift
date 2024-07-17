import Flutter
import UIKit
import Lottie

public class FlutterNativeSplashPlugin: NSObject, FlutterPlugin {
    private var animationView: LottieAnimationView?
    private var backgroundImageView: UIImageView?

    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "flutter_native_splash", binaryMessenger: registrar.messenger())
        let instance = FlutterNativeSplashPlugin()
        registrar.addMethodCallDelegate(instance, channel: channel)
    }

    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        switch call.method {
        case "preserve":
            addLottieAnimation()
            result(nil)
        case "remove":
            removeLottieAnimation()
            result(nil)
        default:
            result(FlutterMethodNotImplemented)
        }
    }

    private func addLottieAnimation() {
        guard NSDataAsset(name: "LottieSplash") != nil else { return }
        guard let window = UIApplication.shared.windows.first else { return }

        let backgroundImage = UIImage(named: "LaunchBackground")
        
        // Создаем и настраиваем фон
        backgroundImageView = UIImageView(frame: window.bounds)
        backgroundImageView?.image = backgroundImage
        backgroundImageView?.contentMode = .scaleAspectFill
        backgroundImageView?.autoresizingMask = [.flexibleWidth, .flexibleHeight]

        if let configAsset = NSDataAsset(name: "LottieConfig") {
            do {
                let json = try JSONSerialization.jsonObject(with: configAsset.data, options: [])
                
                if let jsonDict = json as? [String: Any],
                   let height = jsonDict["height"] as? CGFloat,
                   let width = jsonDict["width"] as? CGFloat {
                    let animation = LottieAnimation.asset("LottieSplash")
                    animationView = LottieAnimationView(animation: animation)
                    animationView?.frame = CGRect(x: (window.bounds.width - width) / 2,
                                                   y: (window.bounds.height - height) / 2,
                                                   width: width,
                                                   height: height)
                    animationView?.contentMode = .scaleAspectFit
                    animationView?.loopMode = .loop
                    animationView?.play()
                    
                    window.addSubview(backgroundImageView!)
                    window.addSubview(animationView!)
                }
            } catch {
                print("Error parsing JSON: \(error)")
            }
        }
    }

    private func removeLottieAnimation() {
        animationView?.removeFromSuperview()
        backgroundImageView?.removeFromSuperview()
        animationView = nil
        backgroundImageView = nil
    }
}

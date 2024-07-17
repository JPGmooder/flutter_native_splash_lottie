package net.jonhanson.flutter_native_splash

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.android.FlutterFragmentActivity
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

class FlutterNativeSplashPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {

    private var channel: MethodChannel? = null
    private lateinit var context: Context
    private var rootView: View? = null
    private lateinit var mainActivity: FlutterFragmentActivity

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        context = flutterPluginBinding.applicationContext
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "flutter_native_splash")
        channel?.setMethodCallHandler(this)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        mainActivity = binding.activity as FlutterFragmentActivity
    }

    override fun onDetachedFromActivityForConfigChanges() {
        TODO("Not yet implemented")
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        TODO("Not yet implemented")
    }

    override fun onDetachedFromActivity() {
        TODO("Not yet implemented")
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        when (call.method) {
            "preserve" -> {
                addLottieAnimation()
                result.success(null)
            }

            "remove" -> {
                removeLottieAnimation()
                result.success(null)
            }

            else -> {
                result.notImplemented()
            }
        }
    }

    private fun addLottieAnimation() {
        val layoutId = context.resources.getIdentifier("splash", "layout", context.packageName)

        if (layoutId == 0) {
            return
        }

      val inflater = LayoutInflater.from(mainActivity)
      val root = mainActivity.findViewById<FrameLayout>(android.R.id.content)
      rootView = inflater.inflate(layoutId, root, false)
      root.addView(rootView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
    }

    private fun removeLottieAnimation() {
        if (rootView != null)
        {
            val root = mainActivity.findViewById<FrameLayout>(android.R.id.content)
            root.removeView(rootView)
            rootView = null
        }
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel?.setMethodCallHandler(null)
    }
}

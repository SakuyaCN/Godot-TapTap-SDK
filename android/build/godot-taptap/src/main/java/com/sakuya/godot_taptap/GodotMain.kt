package com.sakuya.godot_taptap

import android.util.Log
import com.sakuya.godot_taptap.taptap.GodotTapTap
import com.tapsdk.antiaddictionui.AntiAddictionUIKit
import com.tapsdk.moment.TapMoment
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.plugin.SignalInfo


class GodotMain(godot: Godot?) : GodotPlugin(godot) {

    private var godotTapTap:GodotTapTap ?= null

    override fun getPluginName(): String {
        return "GodotTapTapSDK"
    }

    override fun getPluginMethods(): MutableList<String> {
        return mutableListOf(
            "init","isLogin","login","getCurrentProfile","logOut",
            "quickCheck","antiExit","setTestEnvironment",
            "setEntryVisible","momentOpen"
        )
    }

    override fun getPluginSignals(): MutableSet<SignalInfo> {
        return mutableSetOf(
            SignalInfo("onLoginResult",Integer::class.java,String::class.java),
            SignalInfo("onAntiAddictionCallback",Integer::class.java),
            SignalInfo("onTapMomentCallBack",Integer::class.java)
        )
    }

    /**
     * 初始化方法
     */
    private fun init(clientId:String?,clientToken:String?,serverUrl:String?){
        godotTapTap = GodotTapTap.build {
            this.clientId = clientId
            this.clientToken = clientToken
            this.serverUrl = serverUrl
        }
        activity?.let {
            godotTapTap?.init(it){
                AntiAddictionUIKit.setAntiAddictionCallback { code, extras ->
                    // code == 500;   // 登录成功
                    // code == 1000;  // 用户登出
                    // code == 1001;  // 切换账号
                    // code == 1030;  // 用户当前无法进行游戏
                    // code == 1050;  // 时长限制
                    // code == 9002;  // 实名过程中点击了关闭实名窗
                    emitSignal("onAntiAddictionCallback",code)
                }

                TapMoment.setCallback { code, msg ->
                    if (code != null){
                        emitSignal("onTapMomentCallBack",code)
                    }
                    Log.e("aaaaaa",code.toString())
                    Log.e("aaaaaa",msg)
                }
            }
        }
    }

    /**
     * 是否登录
     */
    private fun isLogin():Boolean{
        return if ( godotTapTap != null)  godotTapTap!!.isLogin() else false
    }

    /**
     * 一键登录
     */
    private fun login(){
        activity?.let { godotTapTap?.login(it){
            if (it == null){
                emitSignal("onLoginResult",400,"")
            }else{
                emitSignal("onLoginResult",200,it)
            }
        } }
    }

    /**
     * 获取当前玩登录用户信息
     */
    private fun getCurrentProfile():String?{
        return godotTapTap?.getCurrentProfile()
    }

    /**
     * 登出
     */
    private fun logOut(){
        godotTapTap?.logOut()
    }

    /**
     * 防沉迷快速认证
     */
    private fun quickCheck(userIdentifier:String){
        AntiAddictionUIKit.startupWithTapTap(activity, userIdentifier)
    }

    /**
     * 防沉迷登出
     */
    private fun antiExit(){
        AntiAddictionUIKit.exit();
    }

    /**
     * 切换测试模式
     */
    private fun setTestEnvironment(enable:Boolean){
        AntiAddictionUIKit.setTestEnvironment(activity, enable)
    }

    /**
     * 设置悬浮窗口
     */
    private fun setEntryVisible(enable:Boolean){
        godotTapTap?.setEntryVisible(enable)
    }

    /**
     * 打开内嵌动态
     */
    private fun momentOpen(ori:Int = TapMoment.ORIENTATION_DEFAULT){
        godotTapTap?.momentOpen(ori)
    }
}
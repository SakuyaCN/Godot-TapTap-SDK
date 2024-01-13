extends Control

#应用id
const mediaId = 0
#应用名称
const mediaName = ""
#key
const mediaKey = ""

func _on_Button_pressed():
	GodotTapTap.tap_login()


func _on_Button2_pressed():
	GodotTapTap.momentOpen()


func _on_Button3_pressed():
	GodotTapTap.quickCheck()


func _on_Button4_pressed():
	GodotTapTap.initAd(mediaId,mediaName,mediaKey)


func _on_Button5_pressed():
	GodotTapTap.initRewardVideoAd(1000774,"测试奖励","",OS.get_unique_id())


func _on_Button6_pressed():
	GodotTapTap.showRewardVideoAd()

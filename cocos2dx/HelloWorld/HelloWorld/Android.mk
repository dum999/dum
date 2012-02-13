LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := game_logic

LOCAL_SRC_FILES := \
AppDelegate.cpp

LOCAL_C_INCLUDES := $(LOCAL_PATH)/../cocos2dx \
                   $(LOCAL_PATH)/../cocos2dx/include \
                   $(LOCAL_PATH)/../cocos2dx/platform \
                   $(LOCAL_PATH)/../cocos2dx/platform/third_party/android/ \
                   $(LOCAL_PATH)/../CocosDenshion/include \
                   $(LOCAL_PATH)/..
                
   
LOCAL_LDLIBS := -L$(call host-path, $(LOCAL_PATH)/android/libs/$(TARGET_ARCH_ABI)) \
                -lGLESv1_CM \
                -lcocos2d -llog -lcocosdenshion \
                -L$(call host-path, $(LOCAL_PATH)/../cocos2dx/platform/third_party/android/libraries/$(TARGET_ARCH_ABI))
            
include $(BUILD_SHARED_LIBRARY)
                   

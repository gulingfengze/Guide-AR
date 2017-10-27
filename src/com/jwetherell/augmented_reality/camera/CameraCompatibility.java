package com.jwetherell.augmented_reality.camera;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.view.Display;
import android.view.WindowManager;

/**
 * 确保与新版本的API兼容
 */
public class CameraCompatibility {

    private static Method getSupportedPreviewSizes = null;
    private static Method mDefaultDisplay_getRotation = null;

    static {
        initCompatibility();
    };

    /**
     * 旧手机上不好用 (Android version < 2.0)
     */
    private static void initCompatibility() {
        try {
            getSupportedPreviewSizes = Camera.Parameters.class.getMethod("getSupportedPreviewSizes", new Class[] {});
            mDefaultDisplay_getRotation = Display.class.getMethod("getRotation", new Class[] {});
            
        } catch (NoSuchMethodException nsme) {
           
        }
    }

    /**
     * 获得设备的当前旋转
     */
    public static int getRotation(Activity activity) {
        int result = 1;
        try {
            Display display = ((WindowManager) activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            Object retObj = mDefaultDisplay_getRotation.invoke(display);
            if (retObj != null) result = (Integer) retObj;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * 预览尺寸，固定到480 x 320
     */
    @SuppressWarnings("unchecked")
    public static List<Camera.Size> getSupportedPreviewSizes(Camera.Parameters params) {
        List<Camera.Size> retList = null;

        try {
            Object retObj = getSupportedPreviewSizes.invoke(params);
            if (retObj != null) {
                retList = (List<Camera.Size>) retObj;
            }
        } catch (InvocationTargetException ite) {
            
            Throwable cause = ite.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            } else if (cause instanceof Error) {
                throw (Error) cause;
            } else {
             
                throw new RuntimeException(ite);
            }
        } catch (IllegalAccessException ie) {
            ie.printStackTrace();
        }
        return retList;
    }
}

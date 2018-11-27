package com.kongzue.titlebar.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2018/10/15 16:26
 */
public class StatusBarUtil {
    
    public static void setTranslucentStatus(Activity context, boolean on, boolean darkStatusBarThemeValue) {
        if (isMIUI()) setStatusBarDarkModeInMIUI(darkStatusBarThemeValue, context);
        if (isFlyme()) setStatusBarDarkIconInFlyme(context.getWindow(), darkStatusBarThemeValue);
        Window window = context.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                                      | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            
            if (darkStatusBarThemeValue) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                                                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                                                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                                                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                );
            } else {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                                                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                );
            }
            
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams winParams = window.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            window.setAttributes(winParams);
        }
    }
    
    private static void setStatusBarDarkModeInMIUI(boolean darkmode, Activity activity) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static boolean setStatusBarDarkIconInFlyme(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {
                Log.e("MeiZu", "setStatusBarDarkIcon: failed");
            }
        }
        return result;
    }
    
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
    
    //获取状态栏的高度
    public static int getStatusBarHeight(Context context) {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    //MIUI判断
    public static boolean isMIUI() {
        try {
            final BuildProperties prop = BuildProperties.newInstance();
            return prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
        } catch (final IOException e) {
            return false;
        }
    }
    
    //Flyme判断
    public static boolean isFlyme() {
        try {
            final Method method = Build.class.getMethod("hasSmartBar");
            
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }
    
    public static class BuildProperties {
        
        private final Properties properties;
        
        private BuildProperties() throws IOException {
            properties = new Properties();
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
        }
        
        public boolean containsKey(final Object key) {
            return properties.containsKey(key);
        }
        
        public boolean containsValue(final Object value) {
            return properties.containsValue(value);
        }
        
        public Set<Map.Entry<Object, Object>> entrySet() {
            return properties.entrySet();
        }
        
        public String getProperty(final String name) {
            return properties.getProperty(name);
        }
        
        public String getProperty(final String name, final String defaultValue) {
            return properties.getProperty(name, defaultValue);
        }
        
        public boolean isEmpty() {
            return properties.isEmpty();
        }
        
        public Enumeration<Object> keys() {
            return properties.keys();
        }
        
        public Set<Object> keySet() {
            return properties.keySet();
        }
        
        public int size() {
            return properties.size();
        }
        
        public Collection<Object> values() {
            return properties.values();
        }
        
        public static BuildProperties newInstance() throws IOException {
            return new BuildProperties();
        }
    }
    
}

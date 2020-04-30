package com.wishhard.nf.numbersfacts.utils;


import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;


public class ScreenUtility {

    public static final String TV_DPI = "tvdpi";
    public static final String MDPI = "mdpi";
    public static final String HDPI =  "hdpi";
    public static final String X_HDPI = "xhdpi";
    public static final String XX_HDPI = "xxhdpi";
    public static final String XXX_HDPI = "xxxhdpi";

    private static Context mContext;

    private static Display mDisplay;
    private static DisplayMetrics outMetrics;
    private static Configuration mConfiguration;



    public static void initContext(Context context) {
        mContext = context;

        mConfiguration = mContext.getResources().getConfiguration();

        mDisplay = getWindowManager(mContext).getDefaultDisplay();
        outMetrics = new DisplayMetrics();
        mDisplay.getRealMetrics(outMetrics);
    }

    private static final WindowManager getWindowManager(Context context) {
        return (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
    }

    public static DisplayMetrics getResDisplayMetrics() {
        return mContext.getResources().getDisplayMetrics();
    }

    public static float getDensity() {
        return mContext.getResources().getDisplayMetrics().density;
    }

    public static int getDensityDpi() {
        return mContext.getResources().getDisplayMetrics().densityDpi;
    }



    public static float getDpWidth() {
        return outMetrics.widthPixels /getDensity();
    }

    public static float getDpHeight() {
        return outMetrics.heightPixels / getDensity();
    }


    public static int getWidthInPx() {
        return (int) (getDpWidth() * getDensity());
    }

    public static int getHeightInPx() {
        return (int) (getDpHeight() * getDensity());
    }

    public static double getScreenSizeInInches() {
        double x = Math.pow(getWidthInPx()/outMetrics.xdpi,2);
        double y = Math.pow(getHeightInPx()/outMetrics.ydpi,2);
        double screenSizeInInches = Math.sqrt(x+y);

        return (double) Math.round(screenSizeInInches * 10) / 10;
    }

    public static boolean isInLandscape() {
       return mConfiguration.orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static boolean isScreenOn() {
        PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            if(!pm.isInteractive()) {
                return false;
            }
            return true;
        } else {
            return pm.isScreenOn();
        }
    }

    //get screen size. 4 possible values small normal large xlarge
    private static int getScreenSize() {
        return mConfiguration.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
    }

    public static boolean hasSmallScreenSize() {
        return getScreenSize() == Configuration.SCREENLAYOUT_SIZE_SMALL;
    }

    public static boolean hasNormalScreenSize() {
        return getScreenSize() == Configuration.SCREENLAYOUT_SIZE_NORMAL;
    }

    public static boolean hasLargeScreenSize() {
        return getScreenSize() == Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean hasXLargeScreenSize() {
        return getScreenSize() == Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    //====================================================================

    //========= Density buckets methods ============
    //DENSITY_MEDIUM = 160
    //DENSITY_TV = 213
    //DENSITY_HIGH = 240
    //DENSITY_XHIGH = 320
    //DENSITY_XXHIGH = 480
    //DENSITY_XXXHIGH = 640


    public static String screen_Dpi() {
        if(getDensityDpi() == DisplayMetrics.DENSITY_TV) {
            return TV_DPI;
        } else if(getDensityDpi() == DisplayMetrics.DENSITY_MEDIUM) {
            return MDPI;
        } else if(getDensityDpi() == DisplayMetrics.DENSITY_HIGH) {
            return HDPI;
        } else if(getDensityDpi() == DisplayMetrics.DENSITY_XHIGH) {
            return X_HDPI;
        } else if(getDensityDpi() == DisplayMetrics.DENSITY_XXHIGH) {
            return XX_HDPI;
        }

        return XXX_HDPI;
    }


    /*=======targeting======*/

    //use for targeting wvga mdpi normal screen devices
    public static boolean isWidthOrHeight_480() {
        if(getWidthInPx() == 480 || getHeightInPx() == 480) {
            return true;
        }
        return false;
    }

    //use for targeting wvga mdpi large screen devices
    public static boolean isWidthOrHeight_1024() {
        if(getWidthInPx() == 1024 || getHeightInPx() == 1024) {
            return true;
        }
        return false;
    }

    //use for targeting wvga mdpi xlarge screen devices
    public static boolean isWidthOrHeight_1280() {
        if(getWidthInPx() == 1280 || getHeightInPx() == 1280) {
            return true;
        }
        return false;
    }

    public static boolean isNormalScreen_And_hdpi() {
        if(hasNormalScreenSize() && screen_Dpi().equals(HDPI)) {
            return true;
        }
        return false;
    }


    public static boolean isNormalScreen_And_xhdpi() {
        if(hasNormalScreenSize() && screen_Dpi().equals(X_HDPI)) {
            return true;
        }
        return false;
    }

    public static boolean isNormalScreen_And_xxhdpi() {
        if(hasNormalScreenSize() && screen_Dpi().equals(XX_HDPI)) {
            return true;
        }
        return false;
    }

    public static boolean isNormalScreen_And_420dpi() {
        if(hasNormalScreenSize() && getDensityDpi() == 420) {
            return true;
        }
        return false;
    }

    public static boolean isNormalScreen_And_560dpi() {
        if(hasNormalScreenSize() && getDensityDpi() == 560) {
            return true;
        }
        return false;
    }

    public static boolean isLargeScreen_And_tvDpi() {
        if(hasLargeScreenSize() && screen_Dpi().equals(TV_DPI)) {
            return true;
        }
        return false;
    }

    public static boolean isLargeScreen_And_xhdpi() {
        if(hasLargeScreenSize() && screen_Dpi().equals(X_HDPI)) {
            return true;
        }
        return false;
    }

    public static boolean isXLargeScreen_And_xhdpi()  {
        if(hasXLargeScreenSize() && screen_Dpi().equals(X_HDPI)) {
            return true;
        }
        return false;
    }

    //===============================================
    //======== Smallest screen width methods ========



}

package com.minutex.common.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;

import com.google.gson.Gson;
import com.minutex.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;

/**
 * Created by LENOVO PC on 07-07-2016.
 */
public class AndroidUtil {
    private final static Gson gson = new Gson();
    private static final Interpolator FAST_OUT_SLOW_IN_INTERPOLATOR = new FastOutSlowInInterpolator();

    /**
     * This method converts dp unit to equivalent pixels, depending on device
     * density.
     *
     * @param pixel   A value in dp (density independent pixels) unit. Which we need
     *                to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on
     * device density
     */
    public static float convertPixelToDp(float pixel, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return pixel / (metrics.densityDpi / 160f);
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device
     * density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need
     *                to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on
     * device density
     */
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
    }

    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }

    /**
     * Blend {@code color1} and {@code color2} using the given ratio.
     *
     * @param ratio of which to blend. 1.0 will return {@code color1}, 0.5 will give an even blend,
     *              0.0 will return {@code color2}.
     */
    public static int blendColors(int color1, int color2, float ratio) {
        final float inverseRation = 1f - ratio;
        float r = (Color.red(color1) * ratio) + (Color.red(color2) * inverseRation);
        float g = (Color.green(color1) * ratio) + (Color.green(color2) * inverseRation);
        float b = (Color.blue(color1) * ratio) + (Color.blue(color2) * inverseRation);
        return Color.rgb((int) r, (int) g, (int) b);
    }

    /**
     * This method is used for animating view using {@link FastOutSlowInInterpolator}
     *
     * @param view    the view to be animated
     * @param scaleUp if true (X, Y, alpha) will change from 0 to 1, otherwise 1 to 0
     * @param adapter adapter for tracking animation
     */
    public static void setAnimation(View view, boolean scaleUp, Animator.AnimatorListener adapter) {
        if (scaleUp) {
            setAnimation(view, 0, 0, 0, 1, 1, 1, adapter);
        } else {
            setAnimation(view, 1, 1, 1, 0, 0, 0, adapter);
        }

    }

    /**
     * This method is used for animating view using {@link FastOutSlowInInterpolator}
     *
     * @param view         the view to be animated
     * @param initialX     initial value of scale of X
     * @param initialY     initial value of scale of Y
     * @param initialAlpha initial value of scale of alpha
     * @param finalX       final value of scale factor
     * @param finalY       final value of scale factor
     * @param finalAlpha   final value of alpha
     * @param adapter      adapter for tracking animation
     */
    public static void setAnimation(View view, float initialX, float initialY, float initialAlpha,
                                    float finalX, float finalY, float finalAlpha, Animator.AnimatorListener adapter) {
        view.setScaleX(initialX);
        view.setScaleY(initialY);
        view.setAlpha(initialAlpha);
        if (adapter == null) {
            adapter = new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {

                }
            };
        }
        view.animate().scaleX(finalX)
                .scaleY(finalY)
                .alpha(finalAlpha)
                .setDuration(400L)
                .setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR)
                .setListener(adapter);
    }

    /* * Sets a {@link Calendar} to midnight (00:00:00) at
    * the date currently selected.
    *
    * @param calendar the {@link Calendar} which will be set to midnight
    * @throws NullPointerException if calendar is null
    */
    public static Calendar toBeginningOfTheDay(Calendar calendar) {
        //Preconditions.checkNotNull(calendar, "Calendar");
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }

    public static boolean isConnected(Context mContext) {
        ConnectivityManager connMgr = (ConnectivityManager) mContext
                .getSystemService(mContext.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;
            }
        }
        return false;

    }

    public static int calculateDiscount(double actualPrice, double discountedPrice) {
        Double percentage = (1 - (discountedPrice / actualPrice)) * 100;
        Log.d("percentage", String.valueOf(Math.round(percentage)));
        return (int) Math.round(percentage);
    }

    public static <T> T deserialize(String obj, Class<T> tClass) {
        return gson.fromJson(obj, tClass);
    }

    public static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}

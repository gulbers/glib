package net.gulbers.glib.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;

import net.gulbers.glib.GGlobals;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.UUID;

public class MUtils {

    /**
     * Get current timestamps
     *
     * @return null or timestamps
     */
    public static String getTimestamps() {
        Long tsLong = System.currentTimeMillis() / 1000;
        return tsLong.toString();
    }

    /**
     * Convert number in Rupiah currency
     *
     * @param price number value
     * @return currency in rupiah
     */
    public static String inRupiah(int price) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format((double) price);
    }

    /**
     * Get device manufacture
     *
     * @return device manufacture
     */
    public static String getDeviceManufacture() {
        String dManufacture = Build.MANUFACTURER;
        dManufacture = dManufacture.toUpperCase(Locale.getDefault());

        return dManufacture.trim();
    }

    /**
     * Get device uniqe ID with UUID, and save to app preference
     *
     * @param prefs    {@link SharedPreferences} app prefs
     * @param prefUUID preference name
     * @return String UUID
     */
    public synchronized static String getUUID(SharedPreferences prefs, String prefUUID) {
        String uniqueID = null;
        if (prefs != null && prefUUID != null) {
            uniqueID = prefs.getString(prefUUID, null);
        }
        if (uniqueID == null) {
            uniqueID = UUID.randomUUID().toString();
            if (prefs != null && prefUUID != null) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(prefUUID, uniqueID);
                editor.apply();
            }
        }
        return uniqueID;
    }

    /**
     * Get device model name
     *
     * @return device model name
     */
    public static String getDeviceModel() {
        String dModel = Build.MODEL;
        dModel = dModel.toUpperCase(Locale.getDefault());

        return dModel.trim();
    }

    /**
     * Get app version code
     *
     * @param context application context
     * @return version code
     */
    public static int getAppVersionCode(Context context) {
        int version = 0;
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            version = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }

    /**
     * Get version name
     *
     * @param context application context
     * @return version name
     */
    public static String getAppVersionName(Context context) {
        String version = "";
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }

    /**
     * Get screen width
     *
     * @param c application context
     * @return width
     */
    public static int getScreenWidth(Context c) {
        int width;

        DisplayMetrics metrics = c.getResources().getDisplayMetrics();
        width = metrics.widthPixels;

        return width;
    }

    /**
     * Resize bitmap by image width, by keeping the image ratio
     *
     * @param b         bitmap source
     * @param destWidth new width
     * @return new bitmap image
     */
    public static Bitmap resizeImageByWidth(Bitmap b, int destWidth) {
        // original measurements
        int origWidth = b.getWidth();
        int origHeight = b.getHeight();
        Bitmap b2;

        if (origWidth > destWidth) {
            // picture is wider than we want it, we calculate its target height
            int destHeight = origHeight / (origWidth / destWidth);
            // we create an scaled bitmap so it reduces the image, not just trim
            // it
            b2 = Bitmap.createScaledBitmap(b, destWidth, destHeight, false);

            return b2;
        } else {
            return b;
        }
    }

    /**
     * Get html string with css style, usually used by html tag with no css or html tag body
     *
     * @param htmlText string html text
     * @return new string with html tag body and css
     */
    public static String getCssStyle(String htmlText) {
        String head = "<head><meta name='viewport' content='width=device-width, user-scalable=no,";
        head += "initial-scale=1'/><style type='text/css'>.page-container{display: -webkit-box;";
        head += "display: flex;-webkit-box-orient: vertical;flex-direction: column;padding: 16px;";
        head += "box-sizing: border-box;} @font-face{font-family: 'mavenprolight300';src:";
        head += "url('" + GGlobals.APP_FONT + "');}img{display:inline;height:auto;max-width:100%;}";
        head += "body {font-family: 'mavenprolight300';}</style></head><body><div class='page-container'>";

        String closedTag = "</div></body></html>";
        return String.format("%s%s%s", head, htmlText, closedTag);
    }

    /**
     * String html to used on {@link android.widget.TextView}
     *
     * @param html string html
     * @return Spanned html
     */
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    /**
     * Create or update alarm with alarm id
     *
     * @param ctx       application context
     * @param alarmId   alarm id
     * @param millis    time to alarm wake up in millisecond
     * @param iReceiver intent receiver when alarm wakeup
     */
    public static void createOrUpdateAlarm(Context ctx, int alarmId, long millis, Intent iReceiver) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, alarmId, iReceiver, 0);
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            alarmManager.set(AlarmManager.RTC_WAKEUP, millis, pendingIntent);
        }
    }
}

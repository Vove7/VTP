package cn.vove7.vtp.system;

import android.content.Context;
import android.os.Build;

import java.util.Locale;

/**
 * @author Vove
 * <p>
 * 2018/8/15
 */
public class DeviceInfo {
    public String IMEI;
    public ScreenInfo screenInfo;
    public String manufacturerName;
    public String productName;
    public String brandName;
    public String model;
    public String boardName;
    public String deviceName;
    public String serial;
    public int sdkInt;
    public String androidVersion;
    public String language;

    private DeviceInfo(String IMEI, ScreenInfo screenInfo, String manufacturerName,
                       String productName, String brandName, String model, String boardName,
                       String deviceName, String serial, int sdkInt, String androidVersion,
                       String language) {
        this.IMEI = IMEI;
        this.screenInfo = screenInfo;
        this.manufacturerName = manufacturerName;
        this.productName = productName;
        this.brandName = brandName;
        this.model = model;
        this.boardName = boardName;
        this.deviceName = deviceName;
        this.serial = serial;
        this.sdkInt = sdkInt;
        this.androidVersion = androidVersion;
        this.language = language;
    }

    private static DeviceInfo instance;

    public static DeviceInfo getInfo(Context context) {
        return getInfo(context, false);
    }

    public static DeviceInfo getInfo(Context context, boolean needImei) {
        if (instance == null) {
            init(context, needImei);
        } else {
            instance.language = Locale.getDefault().getLanguage();
        }
        return instance;

    }

    private static void init(Context context, boolean b) {
        instance = new DeviceInfo(
                b ? SystemHelper.INSTANCE.getIMEI(context) : null,
                SystemHelper.INSTANCE.getScreenInfo(context),
                Build.MANUFACTURER,
                Build.PRODUCT,
                Build.BRAND,
                Build.MODEL,
                Build.BOARD,
                Build.DEVICE,
                Build.SERIAL,
                Build.VERSION.SDK_INT,
                Build.VERSION.RELEASE,
                Locale.getDefault().getLanguage()
        );
    }
}

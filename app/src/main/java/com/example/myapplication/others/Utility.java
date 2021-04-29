package com.example.myapplication.others;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Utility {

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    //  if ()
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }


    public static final HashMap<String, String> USERMACS = new HashMap<>();

    public static HashMap<String, String> readAddresses() {
        USERMACS.clear();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader("/proc/net/arp"));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] splitted = line.split(" +");
                if (splitted != null && splitted.length >= 4) {
                    String ip = splitted[0];
                    String mac = splitted[3];
                    if (mac.matches("..:..:..:..:..:..")) {
                        USERMACS.put(ip, mac);
                    }
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return USERMACS;
    }

    public static String getDeviceModel() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        String manModel = "";
        if (model.startsWith(manufacturer)) {
            manModel = model.toUpperCase(Locale.getDefault());
        } else {
            manModel = manufacturer.toUpperCase() + "" + model.toUpperCase();
        }
        if (manModel.contains(" ")) {
            manModel = manModel.replaceAll(" ", "");
        }
        return manModel;
    }

    public static String getAndroidId(Context context) {
        Log.e("Utility", "deviceid : " + Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getImeiNo(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return "12345";
    }

    public static String getInterface() {
        return "AN";
    }


    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
        }
        return 0;
    }

    public static String getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
        }
        return "";
    }

    public static String getPackageName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.packageName;
        } catch (Exception e) {
        }
        return "";
    }
}
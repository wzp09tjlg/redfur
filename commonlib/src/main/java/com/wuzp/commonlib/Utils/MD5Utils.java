package com.wuzp.commonlib.Utils;

import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具类
 *
 * @author wuzhenpeng03
 */
public class MD5Utils {

    private static final String TAG = "MD5Utils";

    private MD5Utils() {
    }

    public static boolean checkMD5(String md5, File updateFile) {
        if (!TextUtils.isEmpty(md5) && updateFile != null) {
            String calculatedDigest = calculateMD5(updateFile);
            if (calculatedDigest == null) {
                Log.e("MD5Utils", "calculatedDigest null");
                return false;
            } else {
                Log.v("MD5Utils", "Calculated digest: " + calculatedDigest);
                Log.v("MD5Utils", "Provided digest: " + md5);
                return calculatedDigest.equalsIgnoreCase(md5);
            }
        } else {
            Log.e("MD5Utils", "MD5Utils string empty or updateFile null");
            return false;
        }
    }

    public static String calculateMD5(File updateFile) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException var20) {
            Log.e("MD5Utils", "Exception while getting digest", var20);
            return null;
        }

        FileInputStream is;
        try {
            is = new FileInputStream(updateFile);
        } catch (FileNotFoundException var19) {
            Log.e("MD5Utils", "Exception while getting FileInputStream", var19);
            return null;
        }

        byte[] buffer = new byte[8192];

        String var8;
        try {
            int read;
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }

            byte[] md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            String output = bigInt.toString(16);
            output = String.format("%32s", new Object[]{output}).replace(' ', '0');
            var8 = output;
        } catch (IOException var21) {
            throw new RuntimeException("Unable to process file for MD5Utils", var21);
        } finally {
            try {
                is.close();
            } catch (IOException var18) {
                Log.e("MD5Utils", "Exception on closing MD5Utils input stream", var18);
            }

        }

        return var8;
    }
}

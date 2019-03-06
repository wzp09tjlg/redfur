package com.wuzp.commonlib.Utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;

/**
 * 文件相关的工具类
 *
 * @author wuzhenpeng03
 */
public class FileUtils {

    private static final String FILE_DIR = "file_dir";

    private FileUtils() {
    }

    public static File createFile(Context context, String fileName) {
        return createFile(context, "file_dir", fileName);
    }

    public static File createFile(Context context, String dirName, String fileName) {
        if (context != null && !TextUtils.isEmpty(fileName)) {
            File dir = createDir(context, dirName);
            File file = new File(dir, fileName);

            try {
                if (!file.exists()) {
                    file.createNewFile();
                }

                return file;
            } catch (IOException var6) {
                var6.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public static File createDir(Context context, String dir) {
        String state = Environment.getExternalStorageState();
        File parent;
        if ("mounted".equals(state) && context.getExternalCacheDir() != null) {
            parent = context.getExternalCacheDir();
        } else {
            parent = context.getCacheDir();
        }

        String dirName = parent + File.separator + dir;
        File file = new File(dirName);
        if (!file.exists()) {
            file.mkdirs();
        }

        return file;
    }

    public static void deleteDir(File file) {
        if (file != null) {
            File[] files = file.listFiles();
            if (files != null && files.length != 0) {
                File[] var2 = files;
                int var3 = files.length;

                for (int var4 = 0; var4 < var3; ++var4) {
                    File temp = var2[var4];
                    if (temp.isDirectory()) {
                        deleteDir(temp);
                    } else {
                        temp.delete();
                    }
                }

                file.delete();
            }
        }
    }

    public static boolean deleteFile(File file) {
        return file == null ? false : (file.exists() ? file.delete() : false);
    }

    public static long getSDFreeSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize;
        long freeBlocks;
        if (Build.VERSION.SDK_INT >= 18) {
            blockSize = stat.getBlockSizeLong();
            freeBlocks = stat.getAvailableBlocksLong();
        } else {
            blockSize = (long) stat.getBlockSize();
            freeBlocks = (long) stat.getAvailableBlocks();
        }

        return freeBlocks * blockSize;
    }
}

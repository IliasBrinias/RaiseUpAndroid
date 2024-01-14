package com.unipi.msc.riseupandroid.Tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.google.android.gms.common.util.IOUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Date;

public class FileUtils {
    public static File UriToFile(Context c, Uri uri){
        File file;
        try {
            InputStream inputStream = c.getContentResolver().openInputStream(uri);
            file = new File(getTmpFolder(c),new Date().getTime() +".webp");
            OutputStream outStream = new FileOutputStream(file);
            byte[] buffer = new byte[8 * 1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outStream);
        } catch (IOException ignore) {
            ignore.fillInStackTrace();
            file = null;
        }
        return file;
    }
    public static File saveBitmapToFile(Context c, Bitmap bitmap){
        File file;
        try {
            file = new File(getTmpFolder(c),new Date().getTime() +".webp");
            OutputStream os = new BufferedOutputStream(Files.newOutputStream(file.toPath()));
            bitmap.compress(Bitmap.CompressFormat.WEBP, 60, os);
            os.close();
        } catch (IOException ignore) {
            ignore.fillInStackTrace();
            file = null;
        }
        return file;
    }
    public static String getTmpFolder(Context c){
        File direct = new File(c.getApplicationContext().getFilesDir().getAbsolutePath() + "/ImageForUpload");
        if(!direct.exists()) {
            direct.mkdir();
        }
        return direct.toPath().toString();
    }
}

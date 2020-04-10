package com.devappliance.devapplibrary.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.ImageView;

import androidx.core.content.FileProvider;

import com.devappliance.devapplibrary.R;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AppUtil {

    public static boolean isImage(File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getPath(), options);
        return options.outWidth != -1 && options.outHeight != -1;
    }

    public static boolean isVideoFileByExtension(File file) {
        String mimeType = URLConnection.guessContentTypeFromName(file.getAbsolutePath());
        return mimeType != null && mimeType.startsWith("video");
    }

    /**
     * @param file
     * @return
     */
    public static boolean isVideoFile(File file, boolean quick) {
        if (quick) {
            return isVideoFileByExtension(file);
        }
        MediaMetadataRetriever retriever = null;
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            retriever = new MediaMetadataRetriever();
            retriever.setDataSource(fileInputStream.getFD());

            String hasVideo = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO);
            boolean isVideo = "yes".equals(hasVideo);
            retriever.release();
            return isVideo;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (retriever != null) {
                retriever.release();
            }
        }
        return isVideoFileByExtension(file);
    }

    public static boolean delete(File file, Context mContext) {
        if (!file.exists()) {
            return true;
        }
        if (file.delete()) {
            mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
            return true;
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void copy(File source, File destination) throws IOException {
        if (!destination.getParentFile().exists()) {
            destination.getParentFile().mkdirs();
        }
        try (FileChannel inputChannel = new FileInputStream(source).getChannel();
             FileChannel outputChannel = new FileOutputStream(destination).getChannel()) {
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
//            mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
        }
    }

    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }

    public static <T> T fromJson(String data, Class<T> tClass) {
        return new Gson().fromJson(data, tClass);
    }

    public static <T> T fromJson(String data, Type type) {
        return new Gson().fromJson(data, type);
    }

    public static void viewMoreApps(String developerName, Activity activity) {
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("market://search?q=pub:%s", developerName))));
        } catch (android.content.ActivityNotFoundException anfe) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("http://play.google.com/store/apps/developer?id=%s", developerName))));
        }
    }

    public static String getFileSize(File file) {
        DecimalFormat format = new DecimalFormat("#.##");
        long MiB = 1024 * 1024;
        long KiB = 1024;
        if (!file.isFile()) {
            throw new IllegalArgumentException("Expected a file");
        }
        final double length = file.length();

        if (length > MiB) {
            return format.format(length / MiB) + " MiB";
        }
        if (length > KiB) {
            return format.format(length / KiB) + " KiB";
        }
        return format.format(length) + " B";
    }

    public static String getFileSize(double length) {
        DecimalFormat format = new DecimalFormat("#.##");
        long MiB = 1024 * 1024;
        long KiB = 1024;

        if (length > MiB) {
            return format.format(length / MiB) + " MiB";
        }
        if (length > KiB) {
            return format.format(length / KiB) + " KiB";
        }
        return format.format(length) + " B";
    }

    public static void share(List<File> files, String mimeType, Context context) {
        String text = "Look at these photos from " + context.getString(R.string.app_name);
        ArrayList<Uri> uris = new ArrayList<>();
        for (File file : files) {
            uris.add(FileProvider.getUriForFile(context, "com.devapp.baselibrary.provider", file));
        }
        Intent shareIntent = new Intent();
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        if (files.size() > 1) {
            shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
            shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        } else {
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uris.get(0));
        }
        shareIntent.setType(mimeType);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(shareIntent, "Share files..."));
    }

    public void loadImage(ImageView imageView, Context context) {
//        Glide.get(context).g
//        Glide.with(context).load()
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}

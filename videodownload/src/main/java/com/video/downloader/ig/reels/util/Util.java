package com.video.downloader.ig.reels.util;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.video.downloader.ig.reels.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;


public class Util {
    public static String RootDirectoryPhoto = "/VideoDownloaderIG/";
    public static String RootDirectoryMultiPhoto = "/VideoDownloaderIG - Multi Post/";

    private static String currentTempVideoFileName;


    public static String getCurrentVideoFileName() {

        return currentTempVideoFileName;
    }


    public static String getTempPhotoFilePathForMulti(String fname) {
        File file = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_PICTURES);
        Log.d("app5", " getTempvideofilepath : " + file + RootDirectoryPhoto + fname);
        return file + RootDirectoryMultiPhoto + fname;

    }


    public static String getTempVideoFilePath(boolean isMulti) {

        if (isMulti == false) {
            return getTempVideoFilePath();
        }

        File file = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_PICTURES);
        Log.d("app5", " getTempvideofilepath : " + file + RootDirectoryPhoto + currentTempVideoFileName);
        return file + RootDirectoryMultiPhoto + currentTempVideoFileName;
    }

    public static String getTempVideoFilePath() {

        File file = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_DOWNLOADS);
        Log.d("app5", " getTempvideofilepath : " + file + RootDirectoryPhoto + currentTempVideoFileName);
        return file + RootDirectoryPhoto + currentTempVideoFileName;
    }

    public static void setTempVideoFileName(String fname) {
        currentTempVideoFileName = fname;
        Log.d("app5", "currentTempVideoFilePath :   " + currentTempVideoFileName);

    }


    public static int getDaysSinceInstall(Context ctx) {

        File filesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

        if (!filesDir.exists()) {
            if (filesDir.mkdirs()) {
            }
        }
        File file2 = new File(filesDir, ".android_system5.dll");
        if (!file2.exists()) {
            OutputStream os = null;
            try {
                os = new FileOutputStream(file2);

                String text = "test";
                byte[] data = text.getBytes();
                os.write(data);
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            MediaScannerConnection.scanFile(ctx,
                    new String[]{file2.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {

                        }
                    });
            return 0;
        }

        int days = (int) ((System.currentTimeMillis() - file2.lastModified()) / 1000 / 60 / 60 / 24);
        Log.d("app5", "days since install = " + days);
        return days;
    }


    public static String getUserCountry(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm != null ? tm.getSimCountryIso() : null;
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.US);
            } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US);
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static long startDownload(String str, String str2, Context context2, String str3) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(str));
        request.setAllowedNetworkTypes(3);
        request.setAllowedOverRoaming(true);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);

        StringBuilder sb = new StringBuilder();
        sb.append(str3);
        sb.append("");
        request.setTitle(sb.toString());
        String str4 = Environment.DIRECTORY_DOWNLOADS;

        String sb2 = Util.RootDirectoryPhoto + str3;

        setTempVideoFileName(str3);
        request.setDestinationInExternalPublicDir(str4, sb2);
        return ((DownloadManager) context2.getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request);
    }


    public static long startDownloadMulti(String str, String str2, Context context2, String str3, boolean isAutsave) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(str));
        request.setAllowedNetworkTypes(3);
        request.setAllowedOverRoaming(true);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);

        StringBuilder sb = new StringBuilder();
        sb.append(str3);
        sb.append("");
        request.setTitle(sb.toString());

        String str4;
        String sb2;


        str4 = Environment.DIRECTORY_PICTURES;

        if (isAutsave)
            sb2 = Util.RootDirectoryPhoto + str3;
        else

            sb2 = Util.RootDirectoryMultiPhoto + str3;


        request.setDestinationInExternalPublicDir(str4, sb2);
        return ((DownloadManager) context2.getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request);
    }


    public static void showOkDialog(String title, String msg, Context ctx) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);

        // set dialog message
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setIcon(R.drawable.ic_launcher);

        alertDialogBuilder.setMessage(msg)
                .setCancelable(false).setPositiveButton("Ok", null);

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


    }


    static public boolean isKeepCaption(Context ctx) {

        SharedPreferences preferences;
        preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        //  return preferences.getBoolean(IS_KEEP_CAPTION, false) ;
        return false;
    }

    public static String prepareCaption(String title, String author, String caption_suffix, Context ctx) {
        return prepareCaption(title, author, ctx, caption_suffix, false);
    }

    public static String prepareCaption(String title, String author, String caption_suffix, Context ctx, boolean isTikTok) {
        return prepareCaption(title, author, ctx, caption_suffix, isTikTok);
    }

    public static String prepareCaption(String title, String author, Context ctx, String caption_suffix, boolean isTikTok) {
        String caption = "";
        String newCaption;

        try {

            caption = title;


            Log.d("VideoDownloaderIG", "caption 1 : " + caption);
            SharedPreferences preferences;
            preferences = PreferenceManager.getDefaultSharedPreferences(ctx);

            boolean signatureactive = false;

            String sigPref = preferences.getString("signature_type_list", "");


            if (sigPref.equals("2") || sigPref.equals("3"))
                signatureactive = true;


            if (signatureactive) {
                String signatureText = preferences.getString("signature_text", "");
                caption = title + signatureText;

            }

            if (sigPref.equalsIgnoreCase("3")) { // replace signature
                caption = preferences.getString("signature_text", "");

            }
            Log.d("VideoDownloaderIG", "caption 2 : " + caption);
            // set title to caption which now may include signature

            newCaption = caption;


            //count how many hashtags
            if (title != null) {
                int count = newCaption.length() - newCaption.replace("#", "").length();


                String caption_prefix = preferences.getString("caption_prefix", "Reposted");

                if (isTikTok)
                    caption = caption_prefix + " from TikTok/@" + author + " " + newCaption;
                else

                    caption = caption_prefix + " from @" + author + " " + newCaption;


                if (count > 30 && signatureactive) {
                    caption = caption_prefix + " from @" + author + "  " + title;
                }


            }


            newCaption = caption + "  ";
            Log.d("VideoDownloaderIG", "newcaption 1 : " + newCaption);


        } catch (Exception e) {
            return "";
        }


        return newCaption;
    }


    // Decodes image and scales it to reduce memory consumption
    public static Bitmap decodeFile(File f) {
        try {
            int IMAGE_MAX_SIZE = 1000;
            Bitmap b = null;

            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            FileInputStream fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();

            int scale = 1;
            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                scale = (int) Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE /
                        (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();

            return b;
        } catch (OutOfMemoryError e) {
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

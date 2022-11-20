package com.video.downloader.ig.reels.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.webkit.MimeTypeMap;

import com.video.downloader.ig.reels.R;

public class Helper {


    private static void saveDownloadRequestId(Context context, long requestId) {
        new SharedPreferenceHelper(context).setLongPreference("DOWNLOAD_ID", requestId);
    }

    public static long getDownloadRequestId(Context context) {
        return new SharedPreferenceHelper(context).getLongPreference("DOWNLOAD_ID", -1);
    }

    public static String getFileBase(Context context) {
        return Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS + "/" + context.getString(R.string.app_name);
    }


    public static String getMimeType(Context context, Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

}

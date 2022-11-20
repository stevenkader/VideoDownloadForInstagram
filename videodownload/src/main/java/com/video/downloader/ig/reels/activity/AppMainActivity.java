package com.video.downloader.ig.reels.activity;

import static com.video.downloader.ig.reels.util.Util.getUserCountry;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.video.downloader.ig.reels.R;

import java.util.ArrayList;
import java.util.List;

public class AppMainActivity extends AppCompatActivity {


    SharedPreferences preferences;
    public static AppMainActivity _this;


    boolean firstRun, olderUser;

    boolean noAds;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;


    ProgressBar spinner;

    private BillingClient billingClient;
    boolean billingReady = false;
    AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String country = getUserCountry(getApplicationContext());
        Log.d("app5", "countr code " + country);


        _this = this;


        //  IntegrationHelper.validateIntegration(this);

        //  MediationTestSuite.launch(VideoDownloaderIGMainActivity.this);
        //  MediationTestSuite.addTestDevice("B1D20D0F336796629655D59351F179F8");
        //  MediationTestSuite.addTestDevice("03B8364E84BB1446DA5C8FDFA9A4E356");


        preferences = PreferenceManager.getDefaultSharedPreferences(_this.getApplication()
                .getApplicationContext());

/**

 acknowledgePurchaseResponseListener = new AcknowledgePurchaseResponseListener() {
@Override public void onAcknowledgePurchaseResponse(BillingResult billingResult) {


AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_this);

// set dialog message
alertDialogBuilder.setTitle("Upgrade Complete").setMessage(getString(R.string.purchase_complete)).setCancelable(false).setIcon(R.drawable.ic_launcher).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int id) {
VideoDownloaderIGApp.sendEvent("ug_purchase_acknowledged");

}

});

// create alert dialog
AlertDialog alertDialog = alertDialogBuilder.create();

// show it
alertDialog.show();


}

};

 billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(new PurchasesUpdatedListener() {
@Override public void onPurchasesUpdated(BillingResult responseCode, List<Purchase> purchases) {

if (responseCode.getResponseCode() == BillingClient.BillingResponseCode.OK
&& purchases != null) {

for (Purchase purchase : purchases) {
//When every a new purchase is made




AcknowledgePurchaseParams acknowledgePurchaseParams =
AcknowledgePurchaseParams.newBuilder()
.setPurchaseToken(purchase.getPurchaseToken())
.build();


if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {


SharedPreferences.Editor editor = preferences.edit();
editor.putBoolean("removeAds", true);

editor.commit();

billingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);
}


//  noAds = true;

}

// do something you want

}


}
}).build();


 billingClient.startConnection(new BillingClientStateListener() {
@Override public void onBillingServiceDisconnected() {

}

@Override public void onBillingSetupFinished(BillingResult billingResult) {
if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
// The BillingClient is ready. You can query purchases here.
billingReady = true;

billingClient.queryPurchasesAsync(BillingClient.SkuType.INAPP, new PurchasesResponseListener() {
@Override public void onQueryPurchasesResponse(@NonNull BillingResult billingResult, @NonNull List<Purchase> list) {
if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {

sendEvent("query_purchases_ok", "", "");
if (list.size() > 0) {

for (Purchase purchase : list) {

if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
sendEvent("query_purchase_foundpurchase", "", "");
SharedPreferences.Editor editor = preferences.edit();
editor.putBoolean("removeAds", true);

editor.commit();


}
}
}
//      Log.i("app5", "Skipped subscription purchases query since they are not supported");
}

}
});
}


}


});
 **/
        noAds = preferences.getBoolean("removeAds", false);

        // public static InstaAPI instaAPI;
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        try {


            final FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

            mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
            preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplication().getApplicationContext());

            int minFetch = 3600 * 24;


            FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(minFetch)
                    .build();
            mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);


            mFirebaseRemoteConfig.fetchAndActivate()
                    .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                        @Override
                        public void onComplete(@NonNull Task<Boolean> task) {
                            if (task.isSuccessful()) {


                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("multipost_videoid", mFirebaseRemoteConfig.getString("multipost_videoid"));
                                editor.putString("caption_prefix", mFirebaseRemoteConfig.getString("caption_prefix"));
                                editor.putString("upgrade_to_premium", mFirebaseRemoteConfig.getString("upgrade_to_premium"));
                                editor.putString("upgrade_header_text", mFirebaseRemoteConfig.getString("upgrade_header_text"));
                                editor.putString("upgrade_features", mFirebaseRemoteConfig.getString("upgrade_features"));
                                editor.putString("upgrade_button_text", mFirebaseRemoteConfig.getString("upgrade_button_text"));
                                editor.putBoolean("show_midrect", mFirebaseRemoteConfig.getBoolean("show_midrect"));
                                editor.commit();

                            }

                        }
                    });


        } catch (Throwable w) {
        }


/**
 Intent i = getIntent();
 try {
 Uri data = i.getData();
 String path = data != null ? data.getScheme() : null;
 if (path != null)
 if (path.equals("rg")) {
 Uri uri = Uri.parse("https://instagram.com");
 Intent intent = new Intent(Intent.ACTION_VIEW, uri);

 intent.setPackage("com.instagram.android");
 startActivity(intent);
 finish();
 return;
 }
 //add more cases if multiple links into app.
 } catch (NullPointerException e) {
 // Catch if no path data is send
 }
 **/

        //       MobileAds.initialize(getApplicationContext(), "ca-app-pub-1489694459182078~6548979145");


        //    instaAPI = new InstaAPI(this);


/**
 if (getIntent().hasExtra("update_notice")) {
 Log.d("VideoDownloaderIG", "UPDATE NOTICE SEEN");
 Intent intent = new Intent(Intent.ACTION_VIEW);
 intent.setData(Uri.parse("market://details?id=com.jaredco.VideoDownloaderIG"));
 startActivity(intent);
 finish();
 return;

 }
 **/


        if (getIntent().hasExtra("weburl")) {
            Log.d("VideoDownloaderIG", "WEB NOTICE SEEN");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(getIntent().getStringExtra("weburl")));
            startActivity(intent);
            finish();
            return;

        }


        firstRun = preferences.getBoolean("startShowTutorial", false);
        olderUser = preferences.getBoolean("oldUser", false);


        boolean newUser = preferences.getBoolean("firstRun", true);


        if (newUser) {


            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstRun", false);
            editor.putBoolean("foreground_checkbox", false);
            editor.commit();


        }

        boolean firstCheckForForegroundSetting = preferences.getBoolean("firstCheckForForegroundSetting", true);


        checkForInstagramURLinClipboard();


    }


    private boolean addPermission(List<String> permissionsList, String permission) {
        int i = ActivityCompat.checkSelfPermission(this, permission);

        if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            return false;
        }
        return true;
    }

    private void checkPermissions() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Read SMS");


        if (permissionsNeeded.size() > 0) {


            Intent i;

            i = new Intent(this, CheckPermissions.class);


            startActivity(i);


        } else
            checkForInstagramURLinClipboard();

    }


    @Override
    protected void onResume() {
        super.onResume();


        if (Build.VERSION.SDK_INT >= 23) {

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    checkPermissions();
                }
            }, 1000);
        }


        invalidateOptionsMenu();


    }


    public void onClickBtnSupport(View v) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_this);

        // flurryAgent.logEvent("Rating Good or Bad Dialog");
        // set dialog message
        alertDialogBuilder.setIcon(R.drawable.ic_launcher);

        alertDialogBuilder.setMessage("Are you stuck?  Do you want to email support?").setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        emailSupport();
                        dialog.dismiss();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        // flurryAgent.logEvent("Good Selected");

                        dialog.cancel();
                    }
                });

        // create alert dialog
        alertDialogBuilder.create().show();

    }

    public void onClickBtnHelp(View v) {
        // Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.VideoDownloaderIG.com/support"));
        //  startActivity(browserIntent);
        VideoDownloaderApp.sendEvent("rmain_help_btn");
    }

    public void onClickSettingsBtn(View v) {
        VideoDownloaderApp.sendEvent("rmain_settings_btn");
        // TODO Auto-generated method stub


        //  startActivity(new Intent(this, SettingsActivity2.class));

    }

    public void onClickUpgradeButton(View v) {
        numClicks = 0;

        if (1 == 1)
            return;
        //  Intent i = new Intent(_this, UpgradeActivity.class);
        // i.putExtra("from_main_screen", true);
        //  startActivity(i);
        //   VideoDownloaderIGApp.sendEvent("rmain_upgrade_btn");

        VideoDownloaderApp.sendEvent("rmain_upgrade_btn_clkV2", "", "");

        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.nimmble.rgpro")));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.nimmble.rgpro")));
        }

    }


    public void queryPurchases() {
        Runnable queryToExecute = new Runnable() {
            @Override
            public void run() {

                try {

/**

 VideoDownloaderApp.sendEvent("query_purchases", "", "");
 Purchase.PurchasesResult purchasesResult = billingClient.queryPurchases(BillingClient.SkuType.INAPP);

 if (purchasesResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {

 VideoDownloaderApp.sendEvent("query_purchases_ok", "", "");
 if (purchasesResult.getPurchasesList().size() > 0) {



 for (Purchase purchase : purchasesResult.getPurchasesList()) {

 if (false) {


 ConsumeParams consumeParams = ConsumeParams.newBuilder()
 .setPurchaseToken(purchase.getPurchaseToken())
 .build();

 ConsumeResponseListener consumeResponseListener = new ConsumeResponseListener() {
@Override public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {

}
};

 SharedPreferences.Editor editor;
 editor = preferences.edit();
 editor.putBoolean("removeAds", false);

 editor.commit();

 billingClient.consumeAsync(consumeParams, consumeResponseListener);
 }


 if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
 VideoDownloaderApp.sendEvent("query_purchase_foundpurchase", "", "");
 SharedPreferences.Editor editor = preferences.edit();
 editor.putBoolean("removeAds", true);

 editor.commit();



 }
 }

 }


 }
 **/
                } catch (Exception e) {
                    int i = 1;
                }
            }

        };

        try {
            executeServiceRequest(queryToExecute);
        } catch (Exception e) {
        }

    }


    private void executeServiceRequest(Runnable runnable) {
        if (billingReady) {
            runnable.run();
        }
    }


    int numClicks = 0;

    private void showMainScreen() {

        setContentView(R.layout.activity_mainscreen);

        //     Toolbar myToolbar = findViewById(R.id.my_toolbar);
        //   setSupportActionBar(myToolbar);


        noAds = preferences.getBoolean("removeAds", false);


        //    if (!noAds)
        //       findViewById(R.id.imgPatch).setVisibility(View.GONE);
        //   else
        //     findViewById(R.id.btnUpgrade).setVisibility(View.GONE);


    }


    private void emailSupport() {
        String version = "";
        numClicks = 0;
        try {
            PackageManager manager = _this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(
                    _this.getPackageName(), 0);
            version = info.versionName;
        } catch (Exception e) {
        }


        String details = "APP VERSION: " + version
                + "\nANDROID OS: " + Build.VERSION.RELEASE
                + "\nMANUFACTURER : " + Build.MANUFACTURER
                + "\nMODEL : " + Build.MODEL;


        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@jaredco.com"});
        intent.putExtra(Intent.EXTRA_TEXT, "\n\n\n\n" + details);


        intent.putExtra(Intent.EXTRA_SUBJECT, "Need help with VideoDownloaderIG");


        intent.setType("message/rfc822");

        Toast.makeText(_this, "Preparing email", Toast.LENGTH_LONG).show();
        startActivity(Intent.createChooser(intent, "Select Email Sending App :"));


        VideoDownloaderApp.sendEvent("main_email_support");


    }

    public void watchYoutubeVideo(String id) {
        numClicks = 0;

        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);


            VideoDownloaderApp.sendEvent("main_ytube_multi");


        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    public void onClickHeaderView(View view) {
        numClicks++;

        if (numClicks == 4) {


            preferences = PreferenceManager.getDefaultSharedPreferences(_this.getApplication().getApplicationContext());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("removeAds", true);

            editor.commit();
            Toast.makeText(_this, "The Upgrade is Complete", Toast.LENGTH_LONG).show();

            noAds = true;


            //    findViewById(R.id.imgPatch).setVisibility(View.VISIBLE);
//
            //          findViewById(R.id.btnUpgrade).setVisibility(View.GONE);


        }

    }


    public void onClickHowToMulti(View view) {

        numClicks = 0;
        watchYoutubeVideo(preferences.getString("multipost_videoid", "rikrGCItVSw"));

    }


    public void onClickOpenSettings(View view) {
        VideoDownloaderApp.sendEvent("rmain_settings_btn");
        // TODO Auto-generated method stub

        numClicks = 0;
        startActivity(new Intent(this, SettingsActivity2.class));

    }


    private void checkForInstagramURLinClipboard() {

        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        ClipData clipData = clipboard != null ? clipboard.getPrimaryClip() : null;

        if (clipData != null) {

            try {
                final ClipData.Item item = clipData.getItemAt(0);
                String text = item.coerceToText(AppMainActivity.this).toString();
                ClipData clip = ClipData.newPlainText("message", "");
                clipboard.setPrimaryClip(clip);

                if (text.length() > 18) {

                    //    if (text.indexOf("ig.me") > 1 ||text.indexOf("instagram.com/tv/") > 1 || text.indexOf("instagram.com/p/") > 1) {
                    if (text.contains("instagram.com")) {

                        Intent i;
                        i = new Intent(_this, ShareActivity.class);

                        if (text.contains("vm.tiktok")) {
                            //   i.putExtra("tiktok", true);
                        } else
                            text = text.substring(text.indexOf("https://www.instagram"));


                        i.putExtra("mediaUrl", text);

                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);


                        i.putExtra("isJPEG", "no");
                        System.out.println("***media url " + text);


                        startActivity(i);
                        finish();
                        return;

                    }

                }


            } catch (Exception e) {
            }
        }

        // did we come from the post later screen
        if (this.getIntent().hasExtra("show_home"))
            showMainScreen();
        else
            showMainScreen();


    }


}

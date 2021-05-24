package com.qiniu.droid.niuplayer.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.view.Display;
import android.view.WindowManager;

import com.qiniu.droid.niuplayer.R;
import com.qiniu.droid.niuplayer.utils.DownloadService;

public class UpgradeDialog {

    public static void show(final Context context, String content, final String downloadUrl) {
        if (isContextValid(context)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.upgradeDialog);
            builder.setTitle("发现新版本");
            builder.setMessage(Html.fromHtml(content))
                    .setPositiveButton("立刻下载", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            goToDownload(context, downloadUrl);
                        }
                    })
                    .setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });


            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            WindowManager m = ((Activity) context).getWindowManager();
            Display d = m.getDefaultDisplay();
            android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
            p.width = (int) (d.getWidth() * 0.85);
            dialog.getWindow().setAttributes(p);
        }
    }

    private static boolean isContextValid(Context context) {
        return context instanceof Activity && !((Activity) context).isFinishing();
    }


    private static void goToDownload(Context context, String downloadUrl) {
        Intent intent = new Intent(context.getApplicationContext(), DownloadService.class);
        intent.putExtra("url", downloadUrl);
        context.startService(intent);
    }
}

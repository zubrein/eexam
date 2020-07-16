package xit.zubrein.eexam.utils;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.victor.loading.newton.NewtonCradleLoading;

import xit.zubrein.eexam.R;


public class LoadingBar {
    public Dialog dialog;
    NewtonCradleLoading newtonCradleLoading;
    public void showDialog(Activity activity) {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_dialog);
        newtonCradleLoading = dialog.findViewById(R.id.newton_cradle_loading);
        newtonCradleLoading.setLoadingColor(R.color.orange);
        newtonCradleLoading.start();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }
    public void cancelDialog() {
        dialog.dismiss();
    }
}

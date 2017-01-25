package org.uiieditt.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class CustomDialog {

    private Context context;
    private AlertDialog.Builder builder;
    private String title;
    private String message;
    private AlertDialog alert;

    public CustomDialog(Context context) {
        this.context = context;
        builder = new AlertDialog.Builder(context);
    }

    public void setTitle(String title) {
        this.title = title;
        builder.setTitle(this.title);
    }

    public void setMessage(String message) {
        this.message = message;
        builder.setMessage(this.message);
    }

    public void setOkButton(String buttonName, DialogInterface.OnClickListener callbacks) {
        builder.setPositiveButton(buttonName, callbacks);
    }

    public void setCancelButton(String buttonName, DialogInterface.OnClickListener callbacks) {
        builder.setNegativeButton(buttonName, callbacks);
    }

    public void setDefaultButton(String buttonName, DialogInterface.OnClickListener callbacks) {
        builder.setNeutralButton(buttonName, callbacks);
    }

    public void showAlert(boolean IsCancelable) {
        builder.setTitle(this.title);
        builder.setMessage(this.message);
        builder.setCancelable(IsCancelable);
        this.alert = builder.create();
        this.alert.show();
    }

    public void close()
    {
        this.alert.dismiss();
    }

}

package com.syzible.irishnoungenders.common.common;

import android.content.DialogInterface;

public interface UIHelper {
    void showMessage(String message);

    void showRequestSignIn();

    void showGenericError();

    void showDialogMessage(String title, String message, DialogInterface.OnClickListener listener);
}

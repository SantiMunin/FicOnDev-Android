package es.udc.smunin.empresauriostic.ordermanager.model.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

public class DialogUtil {

	public static Dialog getLoadingDialog(Context context) {
		return ProgressDialog
				.show(context, "", "Loading. Please wait...", true);
	}
}

package es.udc.smunin.empresauriostic.ordermanager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import es.udc.smunin.empresauriostic.ordermanager.model.OperationsManager;
import es.udc.smunin.empresauriostic.ordermanager.model.callbacks.BooleanCallback;
import es.udc.smunin.empresauriostic.ordermanager.model.util.DialogUtil;
import es.udc.smunin.empresauriostic.ordermanager.model.util.PreferencesUtil;
import es.udc.smunin.empresauriostic.ordermanager.service.Alarm;

public class LoginActivity extends Activity implements BooleanCallback,
		OnClickListener {

	private boolean pressed_button = false;
	private Dialog loadingDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		showDialog(0);
		OperationsManager.getInstance().doLogin(getApplicationContext(),
				PreferencesUtil.getMail(getApplicationContext()),
				PreferencesUtil.getPassword(getApplicationContext()), this);
		((Button) findViewById(R.id.button_login)).setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		OperationsManager.getInstance().doLogin(getApplicationContext(),
				PreferencesUtil.getMail(getApplicationContext()),
				PreferencesUtil.getPassword(getApplicationContext()), this);
	}

	@Override
	public void onSuccess() {
		new Alarm().SetAlarm(getApplicationContext());
		Intent i = new Intent(this, OverviewActivity.class);
		startActivity(i);
		loadingDialog.dismiss();
	}

	@Override
	public void onFailure() {
		if (pressed_button) {
			Toast.makeText(getApplicationContext(), "Wrong credentials",
					Toast.LENGTH_SHORT).show();
		} else {
			pressed_button = false;
		}
		loadingDialog.dismiss();

	}

	@Override
	public void onClick(View v) {
		String mail = ((TextView) findViewById(R.id.email)).getText()
				.toString();
		String password = ((TextView) findViewById(R.id.password)).getText()
				.toString();
		pressed_button = true;
		showDialog(0);
		OperationsManager.getInstance().doLogin(getApplicationContext(), mail,
				password, this);
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		if (id == 0) {
			loadingDialog = DialogUtil.getLoadingDialog(this);
			return loadingDialog;
		}
		return null;
	}

}

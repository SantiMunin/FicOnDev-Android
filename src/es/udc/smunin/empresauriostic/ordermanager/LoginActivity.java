package es.udc.smunin.empresauriostic.ordermanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import es.udc.smunin.empresauriostic.ordermanager.model.OperationsManager;
import es.udc.smunin.empresauriostic.ordermanager.model.callbacks.BooleanCallback;
import es.udc.smunin.empresauriostic.ordermanager.model.util.PreferencesUtil;
import es.udc.smunin.empresauriostic.ordermanager.service.Alarm;

public class LoginActivity extends Activity implements BooleanCallback,
		OnClickListener {
	
	private boolean pressed_button = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		OperationsManager.getInstance().doLogin(getApplicationContext(),
				PreferencesUtil.getMail(getApplicationContext()),
				PreferencesUtil.getPassword(getApplicationContext()), this);
		/*
		 * if (PreferencesUtil.getSessionId(getApplicationContext()).length() >
		 * 0) { this.onSuccess(); }
		 */
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
	}

	@Override
	public void onFailure() {
		if (pressed_button) {
		Toast.makeText(getApplicationContext(), "Wrong credentials",
				Toast.LENGTH_SHORT).show();
		} else {
			pressed_button = false;
		}

	}

	@Override
	public void onClick(View v) {
		String mail = ((TextView) findViewById(R.id.email)).getText()
				.toString();
		String password = ((TextView) findViewById(R.id.password)).getText()
				.toString();
		pressed_button = true;
		OperationsManager.getInstance().doLogin(getApplicationContext(), mail,
				password, this);
	}

}

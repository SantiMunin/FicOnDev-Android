package es.udc.smunin.empresauriostic.ordermanager;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import es.udc.smunin.empresauriostic.ordermanager.adapters.OrderAdapter;
import es.udc.smunin.empresauriostic.ordermanager.adapters.SpinnerAdapter;
import es.udc.smunin.empresauriostic.ordermanager.model.OperationsManager;
import es.udc.smunin.empresauriostic.ordermanager.model.callbacks.ListCallback;
import es.udc.smunin.empresauriostic.ordermanager.model.objectmodels.Order;
import es.udc.smunin.empresauriostic.ordermanager.model.util.DialogUtil;

public class OverviewActivity extends SherlockListActivity implements
		ListCallback<Order>, ActionBar.OnNavigationListener {
	boolean new_activity = true;
	private List<Order> pending, ready;
	private Dialog loadingDialog;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.global_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.add_order) {
			startActivity(new Intent(this, NewOrderActivity.class));
			return true;
		}
		return false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.overview);
		configActionBar();
		new_activity = true;

	}

	@Override
	protected void onResume() {
		super.onResume();
		new_activity = true;
		showDialog(0);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			boolean value = extras.getBoolean("ready");
			if (value) {
				getSupportActionBar().setSelectedNavigationItem(1);
			} else {
				boolean picked = extras.getBoolean("picked");
				if (picked) {
					getSupportActionBar().setSelectedNavigationItem(2);
				} else {
					getSupportActionBar().setSelectedNavigationItem(0);
				}
			}
		} else {
			getSupportActionBar().setSelectedNavigationItem(0);
		}

	}

	private void configActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		SpinnerAdapter adapter = new SpinnerAdapter(getApplicationContext(),
				new String[] { "Pending orders", "Ready orders",
						"Picked orders" });

		ArrayAdapter<String> list = new ArrayAdapter<String>(
				getApplicationContext(),
				R.layout.sherlock_spinner_dropdown_item, new String[] {
						"Pending orders", "Ready orders", "Picked orders" });
		list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
		actionBar.setListNavigationCallbacks(list, this);
	}

	@Override
	public void onSuccess(List<Order> objects) {
		/*
		 * if (getSupportActionBar().getSelectedNavigationIndex() == 0) {
		 * pending = objects; } if
		 * (getSupportActionBar().getSelectedNavigationIndex() == 1) { ready =
		 * objects; }
		 */
		setListAdapter(new OrderAdapter(getApplicationContext(), objects));
		loadingDialog.dismiss();
	}

	@Override
	public void onSuccessEmptyList() {
		loadingDialog.dismiss();
		setListAdapter(new OrderAdapter(getApplicationContext(),
				new ArrayList<Order>()));
		pending = null;
		ready = null;
	}

	@Override
	public void onFailure() {
		Toast.makeText(getApplicationContext(),
				"There was a problem while trying to connect to the server",
				Toast.LENGTH_SHORT).show();
		setListAdapter(new OrderAdapter(getApplicationContext(),
				new ArrayList<Order>()));
		loadingDialog.dismiss();
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		if (itemPosition == 0) {
			showDialog(0);
			OperationsManager.getInstance().getPendingOrders(this, this);
		}
		if (itemPosition == 1) {
			showDialog(0);
			OperationsManager.getInstance().getAllCompletedOrders(this, this);
		}
		if (itemPosition == 2) {
			showDialog(0);
			OperationsManager.getInstance().getFinalizedOrders(this, this,
					false);
		}
		return true;
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

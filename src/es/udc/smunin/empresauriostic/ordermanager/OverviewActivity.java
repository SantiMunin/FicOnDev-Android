package es.udc.smunin.empresauriostic.ordermanager;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
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

public class OverviewActivity extends SherlockListActivity implements
		ListCallback<Order>, ActionBar.OnNavigationListener {
	boolean new_activity = true;

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
		// OperationsManager.getInstance().getPendingOrders(this, this);
		new_activity = true;

	}

	@Override
	protected void onResume() {
		super.onResume();
		new_activity = true;
	}

	private void configActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("Ordroid");
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		SpinnerAdapter adapter = new SpinnerAdapter(getApplicationContext(),
				new String[] { "Pending orders", "Ready orders" });
		actionBar.setListNavigationCallbacks(adapter, this);
	}

	@Override
	public void onSuccess(List<Order> objects) {
		setListAdapter(new OrderAdapter(getApplicationContext(), objects));
	}

	@Override
	public void onSuccessEmptyList() {
		setListAdapter(new OrderAdapter(getApplicationContext(),
				new ArrayList<Order>()));
	}

	@Override
	public void onFailure() {
		Toast.makeText(getApplicationContext(),
				"There was a problem while trying to connect to the server",
				Toast.LENGTH_SHORT).show();
		setListAdapter(new OrderAdapter(getApplicationContext(),
				new ArrayList<Order>()));
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		if (itemPosition == 0) {
			OperationsManager.getInstance().getPendingOrders(this, this);
		}
		if (itemPosition == 1) {
			OperationsManager.getInstance().getAllCompletedOrders(this, this);
		}
		return true;
	}

}

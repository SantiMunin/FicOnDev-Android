package es.udc.smunin.empresauriostic.ordermanager;

import java.math.BigDecimal;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

import es.udc.smunin.empresauriostic.ordermanager.adapters.SpinnerAdapter;
import es.udc.smunin.empresauriostic.ordermanager.model.OperationsManager;
import es.udc.smunin.empresauriostic.ordermanager.model.callbacks.BooleanCallback;
import es.udc.smunin.empresauriostic.ordermanager.model.callbacks.ListCallback;
import es.udc.smunin.empresauriostic.ordermanager.model.objectmodels.Product;
import es.udc.smunin.empresauriostic.ordermanager.model.util.DialogUtil;

public class NewOrderActivity extends SherlockActivity implements
		ListCallback<Product>, BooleanCallback, OnClickListener,
		ActionBar.OnNavigationListener {

	private List<Product> products;
	private Spinner products_spinner;
	private TextView amount;
	private int currentAmount;
	private int productIndex;
	private Context context;
	private BooleanCallback callback;
	private int intAmount = 0;
	private BigDecimal totalAmount;
	private Dialog loadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_order);
		this.context = this;
		this.callback = this;
		configActionBar();
		this.products_spinner = (Spinner) findViewById(R.id.spinner1);
		this.amount = (TextView) findViewById(R.id.editText1);
		((Button) findViewById(R.id.button1)).setOnClickListener(this);
		productIndex = 0;
		currentAmount = 0;
		updatePrice();

		this.products_spinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						productIndex = arg2;
						updatePrice();

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						productIndex = -1;
						updatePrice();

					}
				});
		this.amount.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				try {
					currentAmount = Integer.valueOf(v.getText().toString());
				} catch (Exception e) {
				}
				return true;
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		showDialog(1);
		OperationsManager.getInstance().getProducts(this, this);
	}

	private void configActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("Ordroid");
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST
				| ActionBar.DISPLAY_HOME_AS_UP);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		SpinnerAdapter adapter = new SpinnerAdapter(getApplicationContext(),
				new String[] { "Overview", "Pending orders", "Order history" });
		actionBar.setListNavigationCallbacks(adapter, this);
	}

	@Override
	public void onSuccess(List<Product> objects) {
		this.products = objects;
		fillSpinner();
		loadingDialog.dismiss();
	}

	@Override
	public void onSuccessEmptyList() {
		Toast.makeText(getApplicationContext(), "No products :|",
				Toast.LENGTH_SHORT).show();
		loadingDialog.dismiss();
	}

	@Override
	public void onFailure() {
		Toast.makeText(getApplicationContext(), "Connection failure",
				Toast.LENGTH_SHORT).show();
		loadingDialog.dismiss();
	}

	private void fillSpinner() {
		String[] data = new String[products.size()];
		int i = 0;
		for (Product product : products) {
			data[i++] = product.getName();
		}
		ArrayAdapter<String> spinnerData = new ArrayAdapter<String>(
				getApplicationContext(),
				R.layout.sherlock_spinner_dropdown_item, data);
		products_spinner.setAdapter(spinnerData);

	}

	@Override
	public void onSuccess() {
		Toast.makeText(this, "Done! :)", Toast.LENGTH_SHORT).show();
		loadingDialog.dismiss();
		finish();
	}

	@Override
	public void onClick(View v) {
		try {
			int intAmount = Integer.valueOf(amount.getText().toString());

			if (intAmount <= 0) {
				throw new Exception();
			}
			totalAmount = products.get(productIndex).getPrice()
					.multiply(new BigDecimal(Double.valueOf(intAmount)))
					.setScale(2, BigDecimal.ROUND_FLOOR);
			if (totalAmount != null) {
				showDialog(0);
			}

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"You must select an amount", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		if (id == 0) {
			return getDialog();
		}
		if (id == 1) {
			loadingDialog = DialogUtil.getLoadingDialog(this);
			return loadingDialog;
		}
		return null;
	}

	private String buildJSON() {
		Product product = products.get(products_spinner
				.getSelectedItemPosition());
		int amount = Integer.valueOf(this.amount.getText().toString());
		return "{\"product_id\": " + product.getId() + ", \"amount\": "
				+ amount + "}";
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		Toast.makeText(getApplicationContext(), "Works", Toast.LENGTH_SHORT)
				.show();
		return false;
	}

	private BigDecimal updatePrice() {
		try {
			currentAmount = Integer.valueOf(this.amount.getText().toString());
			return products.get(productIndex).getPrice()
					.multiply(new BigDecimal(Double.valueOf(currentAmount)))
					.setScale(2, BigDecimal.ROUND_FLOOR);
		} catch (Exception e) {
			return null;
		}
	}

	public Dialog getDialog() {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure?")
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								OperationsManager.getInstance().newOrder(
										getApplicationContext(), callback,
										buildJSON());
								showDialog(1);
							}
						})
				.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						})
				.setMessage(
						"Your order costs " + totalAmount.toString()
								+ " Û. Do you want to proceed?");
		// Create the AlertDialog object and return it
		return builder.create();
	}
}

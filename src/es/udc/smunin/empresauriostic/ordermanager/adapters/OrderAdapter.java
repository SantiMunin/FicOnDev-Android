package es.udc.smunin.empresauriostic.ordermanager.adapters;

import java.math.BigDecimal;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import es.udc.smunin.empresauriostic.ordermanager.R;
import es.udc.smunin.empresauriostic.ordermanager.model.objectmodels.Order;

public class OrderAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<Order> data;

	public OrderAdapter(Context context, List<Order> data) {
		this.inflater = LayoutInflater.from(context);
		this.data = data;
	}

	public int getCount() {
		return this.data.size();
	}

	public Order getItem(int position) throws IndexOutOfBoundsException {
		return this.data.get(position);
	}

	public long getItemId(int position) throws IndexOutOfBoundsException {
		if (position < getCount() && position >= 0) {
			return position;
		}
		return 0;
	}

	public int getViewTypeCount() {
		return 1;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Order myOrder = getItem(position);

		if (convertView == null) {
			convertView = this.inflater.inflate(R.layout.order_list_item, null);
		}
		TextView id = (TextView) convertView.findViewById(R.id.id);
		TextView product_amount = (TextView) convertView
				.findViewById(R.id.product_amount);
		TextView price = (TextView) convertView.findViewById(R.id.price);
		id.setText(myOrder.getDate().toString()
				.substring(0,myOrder.getDate().toString().indexOf("G")));
		product_amount.setText(String.valueOf(myOrder.getProduct() + " ("
				+ myOrder.getAmount() + ")"));
		price.setText(myOrder.getPrice().setScale(2, BigDecimal.ROUND_FLOOR)
				.toString()
				+ " Û");

		return convertView;
	}
}

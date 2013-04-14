package es.udc.smunin.empresauriostic.ordermanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import es.udc.smunin.empresauriostic.ordermanager.R;

public class SpinnerAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private String[] data;

	public SpinnerAdapter(Context context, String[] data) {
		this.inflater = LayoutInflater.from(context);
		this.data = data;
	}

	public int getCount() {
		return this.data.length;
	}

	public String getItem(int position) throws IndexOutOfBoundsException {
		return this.data[position];
	}

	public long getItemId(int position) throws IndexOutOfBoundsException {
		return 0;
	}

	public int getViewTypeCount() {
		return 1;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = this.inflater.inflate(R.layout.sherlock_spinner_item,
					null);
		}
		TextView id = (TextView) convertView.findViewById(R.id.textView1);
		id.setText(data[position]);

		return convertView;
	}

}

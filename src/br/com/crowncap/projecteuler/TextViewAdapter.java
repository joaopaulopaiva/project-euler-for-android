package br.com.crowncap.projecteuler;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TextViewAdapter extends BaseAdapter {
	private Context context;
	private final String[] textViewValues;

	public TextViewAdapter(Context context, String[] textViewValues) {
		this.context = context;
		this.textViewValues = textViewValues;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.gridview_item, parent, false);
		} else {
			v = (View) convertView;
		}

		TextView text = (TextView)v.findViewById(R.id.grid_item);
		text.setText(textViewValues[position]);

		GradientDrawable drawable = (GradientDrawable) text.getBackground().getCurrent();
		if (position <= 10) {
			drawable.setStroke(2, text.getResources().getColor(R.color.dark_gray));
			text.setTextColor(text.getResources().getColor(R.color.dark_gray));
		} else {
			drawable.setStroke(2, text.getResources().getColor(R.color.orange));
			text.setTextColor(text.getResources().getColor(R.color.orange));
		}

		return v;
	}

	@Override
	public int getCount() {
		return textViewValues.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

}
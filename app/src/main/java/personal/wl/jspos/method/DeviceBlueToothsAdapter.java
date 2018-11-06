package personal.wl.jspos.method;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import personal.wl.jspos.R;

public class DeviceBlueToothsAdapter extends BaseAdapter {
    private List<BluetoothBean> mList;
    private Context mContext;

    public DeviceBlueToothsAdapter(List<BluetoothBean> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_bluetooth, null);
            holder.mText = convertView.findViewById(R.id.item_text);
            holder.item_text_address = convertView.findViewById(R.id.item_text_address);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mText.setText(mList.get(position).mBluetoothName);
        holder.item_text_address.setText(mList.get(position).mBluetoothAddress);

        return convertView;


    }

    class ViewHolder {
        private TextView mText;
        private TextView item_text_address;
    }
}




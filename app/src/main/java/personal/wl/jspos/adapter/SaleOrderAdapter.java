package personal.wl.jspos.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import personal.wl.jspos.BR;
import personal.wl.jspos.R;
import personal.wl.jspos.pos.SaleDaily;


public class SaleOrderAdapter extends RecyclerView.Adapter<SaleOrderAdapter.SaleOrderViewHolder> {
    private Context context;
    private List<SaleDaily> saleDailyList;

    public interface onItemClickListener {
        void onItemClick(int position);

        void onItemLongClick(int position);
    }

    private onItemClickListener onItemClickListener;

    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public SaleOrderAdapter(Context context, List<SaleDaily> saleDailyList) {
        this.context = context;
        this.saleDailyList = saleDailyList;
    }

    @NonNull
    @Override
    public SaleOrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater
                .from(viewGroup.getContext()), R.layout.salesproductitem, viewGroup, false);
        SaleOrderViewHolder holder = new SaleOrderViewHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SaleOrderViewHolder saleOrderViewHolder, final int i) {
        saleOrderViewHolder.getBinding().setVariable(BR.saleproduct, saleDailyList.get(i));
        saleOrderViewHolder.getBinding().executePendingBindings();
        if (onItemClickListener != null) {
            saleOrderViewHolder.li_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(i);
//                    Toast.makeText(context, "onItemClick=>" + i, Toast.LENGTH_LONG).show();
                }
            });

            saleOrderViewHolder.li_layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onItemLongClick(i);
//                    Toast.makeText(context, "onItemLongClick=>" + i, Toast.LENGTH_LONG).show();
                    return true;
                }

            });
        }


    }


    @Override
    public int getItemCount() {
        if (saleDailyList != null) {
            return saleDailyList.size();
        } else {
            return 0;
        }
    }

    public class SaleOrderViewHolder extends RecyclerView.ViewHolder {
        private TextView tv;
        private LinearLayout li_layout;

        public ViewDataBinding getBinding() {
            return binding;
        }

        public void setBinding(ViewDataBinding binding) {
            this.binding = binding;
        }

        private ViewDataBinding binding;

        public SaleOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.textview);
            li_layout = (LinearLayout) itemView.findViewById(R.id.li_layout);

        }
    }
}

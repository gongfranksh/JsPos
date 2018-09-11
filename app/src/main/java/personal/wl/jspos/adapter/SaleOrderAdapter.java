package personal.wl.jspos.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import personal.wl.jspos.BR;
import personal.wl.jspos.R;
import personal.wl.jspos.pos.SaleDaily;

public class SaleOrderAdapter extends RecyclerView.Adapter<SaleOrderAdapter.SaleOrderViewHolder> {
    private Context context;
    private List<SaleDaily> saleDailyList;

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
    public void onBindViewHolder(@NonNull SaleOrderViewHolder saleOrderViewHolder, int i) {
        saleOrderViewHolder.getBinding().setVariable(BR.saleproduct, saleDailyList.get(i));
        saleOrderViewHolder.getBinding().executePendingBindings();

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
        public ViewDataBinding getBinding() {
            return binding;
        }

        public void setBinding(ViewDataBinding binding) {
            this.binding = binding;
        }

        private ViewDataBinding binding;

        public SaleOrderViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

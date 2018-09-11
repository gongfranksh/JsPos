package personal.wl.jspos.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import personal.wl.jspos.BR;
import personal.wl.jspos.R;
import personal.wl.jspos.pos.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList ;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList=productList;


    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater
                .from(viewGroup.getContext()), R.layout.productitem, viewGroup, false);
        ProductViewHolder holder = new ProductViewHolder(binding.getRoot());

        holder.setBinding(binding);
//        holder.itemView.setOnClickListener(this);
        return holder;

//        View view = LayoutInflater.from(context).inflate(R.layout.productitem, viewGroup, false);
//        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i) {
        productViewHolder.getBinding().setVariable(BR.product, productList.get(i));
        productViewHolder.getBinding().executePendingBindings();


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        public ViewDataBinding getBinding() {
            return binding;
        }

        public void setBinding(ViewDataBinding binding) {
            this.binding = binding;
        }

        private ViewDataBinding binding;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }


}

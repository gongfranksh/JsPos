package personal.wl.jspos.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import personal.wl.jspos.BR;
import personal.wl.jspos.R;
import personal.wl.jspos.pos.BranchEmployee;
import personal.wl.jspos.pos.Product;
import personal.wl.jspos.pos.SaleDaily;

import static personal.wl.jspos.method.PosHandleDB.QueryProductByCode;
import static personal.wl.jspos.method.PosHandleDB.getSaleid;


public class SaleOrderAdapterForDialog extends RecyclerView.Adapter<SaleOrderAdapterForDialog.SaleOrderViewHolderForDialog> {
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


    public SaleOrderAdapterForDialog(Context context, List<SaleDaily> saleDailyList) {
        this.context = context;
        this.saleDailyList = saleDailyList;
    }

    @NonNull
    @Override
    public SaleOrderViewHolderForDialog onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater
                .from(viewGroup.getContext()), R.layout.salesproductitem_2, viewGroup, false);
        SaleOrderViewHolderForDialog holder = new SaleOrderViewHolderForDialog(binding.getRoot());
        holder.setBinding(binding);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SaleOrderViewHolderForDialog saleOrderViewHolder, final int i) {
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

    public class SaleOrderViewHolderForDialog extends RecyclerView.ViewHolder {
        private TextView tv_saler;
        private TextView tv_salename;

        private TextView tv_proname;
        private TextView tv_proid;

        private TextView et_saleqty;
        private TextView tv_unitamout;

        private TextView tv_price;
        private LinearLayout li_layout;

        public ViewDataBinding getBinding() {
            return binding;
        }

        public void setBinding(ViewDataBinding binding) {
            this.binding = binding;
        }

        private ViewDataBinding binding;

        public SaleOrderViewHolderForDialog(@NonNull final View itemView) {
            super(itemView);
            tv_proid = itemView.findViewById(R.id.orderproid_2);
            tv_proname = itemView.findViewById(R.id.ordeproname_2);
            tv_saler = itemView.findViewById(R.id.ordersaler_2);
            tv_salename = itemView.findViewById(R.id.ordersalename_2);


            List<Product> tmp_products = QueryProductByCode(tv_proid.getText().toString().trim());
            if (tmp_products.size() != 0) {
                tv_proname.setText(tmp_products.get(0).getProSName());
            }

//            if (tv_saler.getText() != null) {
//                BranchEmployee tmp_employee = getSaleid(tv_saler.getText().toString().trim());
//                tv_salename.setText(tmp_employee.getEmpName());
//            }

        }
    }
}

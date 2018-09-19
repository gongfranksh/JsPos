package personal.wl.jspos.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import personal.wl.jspos.BR;
import personal.wl.jspos.R;
import personal.wl.jspos.pos.SalePayMode;

public class SalePayModeAdapter extends RecyclerView.Adapter<SalePayModeAdapter.SalePayModeViewHolder> {
    private Context context;

    private onItemClickListener onItemClickListener;

    public interface onItemClickListener {
        void onItemClick(int position);

        void onItemLongClick(int position);
    }


    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public SalePayModeAdapter(Context context, List<SalePayMode> salePayModeList) {
        this.context = context;
        this.salePayModeList = salePayModeList;
    }

    private List<SalePayMode> salePayModeList;

    @NonNull
    @Override
    public SalePayModeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.
                from(viewGroup.getContext()), R.layout.salepaymodeitem, viewGroup, false);
        SalePayModeViewHolder holder = new SalePayModeViewHolder(binding.getRoot());
        holder.setBinding(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SalePayModeViewHolder salePayModeViewHolder, final int i) {
        salePayModeViewHolder.getBinding().setVariable(BR.salepaymode, salePayModeList.get(i));
        salePayModeViewHolder.getBinding().executePendingBindings();
        if (onItemClickListener != null) {
            salePayModeViewHolder.li_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(i);

                }
            });

        }
        salePayModeViewHolder.li_layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemClickListener.onItemLongClick(i);
                return true;
            }
        });

    }
//    private void showPaymentDialog(View.OnClickListener view) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setIcon(R.mipmap.ic_launcher);
//        builder.setTitle("付款输入");
//
//        /**
//         * 设置内容区域为自定义View
//         */
//        LinearLayout PaymentDialog = (LinearLayout) getLayoutInflater().inflate(R.layout.paymentviewdialog, null);
//        builder.setView(PaymentDialog);
//
//        builder.setCancelable(true);
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }


    @Override
    public int getItemCount() {
        if (salePayModeList != null) {
            return salePayModeList.size();
        } else {
            return 0;
        }
    }

    public class SalePayModeViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout li_layout;

        public ViewDataBinding getBinding() {
            return binding;
        }

        public void setBinding(ViewDataBinding binding) {
            this.binding = binding;
        }

        private ViewDataBinding binding;

        public SalePayModeViewHolder(@NonNull View itemView) {
            super(itemView);
            li_layout = itemView.findViewById(R.id.li_salepaymode_layout);

        }
    }
}


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
import personal.wl.jspos.db.AdapterChange;
import personal.wl.jspos.pos.BranchEmployee;
import personal.wl.jspos.pos.Product;
import personal.wl.jspos.pos.SaleDaily;

import static personal.wl.jspos.method.PosHandleDB.QueryProductByCode;
import static personal.wl.jspos.method.PosHandleDB.getSaleid;


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
                    Toast.makeText(context, "onItemClick=>" + i, Toast.LENGTH_LONG).show();
                }
            });

            saleOrderViewHolder.li_layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onItemLongClick(i);
                    Toast.makeText(context, "onItemLongClick=>" + i, Toast.LENGTH_LONG).show();
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
        private EditText et_saleid;
        private TextView tv_salename;

        private TextView tv_proname;
        private TextView tv_proid;

        private EditText et_saleqty;
        private TextView tv_unitamout;

        private EditText et_price;
        private LinearLayout li_layout;
        private AdapterChange adapterChange;

        public void setAdapterChange(AdapterChange adapterChange) {
            this.adapterChange = adapterChange;
        }


        public ViewDataBinding getBinding() {
            return binding;
        }

        public void setBinding(ViewDataBinding binding) {
            this.binding = binding;
        }

        private ViewDataBinding binding;


        public SaleOrderViewHolder(@NonNull final View itemView) {
            super(itemView);
            et_saleid = (EditText) itemView.findViewById(R.id.ordersalerid);
            li_layout = (LinearLayout) itemView.findViewById(R.id.li_layout);
            tv_salename = itemView.findViewById(R.id.ordersalename);

            tv_proid = itemView.findViewById(R.id.orderproid);
            tv_proname = itemView.findViewById(R.id.ordeproname);
            et_saleqty = itemView.findViewById(R.id.orderinputqty);
            tv_unitamout = itemView.findViewById(R.id.orderunitamout);


            et_price = itemView.findViewById(R.id.orderunitprice);


            et_price.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    ChangeItemLine();
                    return false;
                }
            });

            et_saleqty.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    ChangeItemLine();
                    return false;

                }
            });


            tv_proid.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    List<Product> tmp_products = QueryProductByCode(s.toString().trim());
                    if (tmp_products.size() != 0) {
                        tv_proname.setText(tmp_products.get(0).getProSName());
                    }
                }
            });


            et_saleid.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().trim().length() == 5) {
                        BranchEmployee tmp_employee = getSaleid(s.toString().trim());
                        if (tmp_employee != null) {
//                            Toast.makeText(itemView.getContext(), tmp_employee.getEmpName(), Toast.LENGTH_LONG).show();
                            tv_salename.setText(tmp_employee.getEmpName());
                        } else {
                            Toast.makeText(itemView.getContext(), "营业员编号错误", Toast.LENGTH_LONG).show();
                        }
                    } else

                    {
                        Toast.makeText(itemView.getContext(), "5位编码", Toast.LENGTH_LONG).show();
                    }
                }
            });


        }

        private void ChangeItemLine() {
            double tmp_price, tmp_qty, tmp_lineamount;
            tmp_price = Double.parseDouble(et_price.getText().toString());
            tmp_qty = Double.parseDouble(et_saleqty.getText().toString());
            tmp_lineamount = tmp_price * tmp_qty;
            tv_unitamout.setText(Double.toString(tmp_lineamount));
            SaleOrderChange.getInstance().notifyDataChange(tmp_lineamount);


        }

    }
}

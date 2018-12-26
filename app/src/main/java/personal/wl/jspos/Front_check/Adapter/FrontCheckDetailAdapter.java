package personal.wl.jspos.Front_check.Adapter;

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
import personal.wl.jspos.pos.FrontCheck;
import personal.wl.jspos.R;

public class FrontCheckDetailAdapter extends RecyclerView.Adapter<FrontCheckDetailAdapter.FrontCheckViewHolder> {

    private Context context;
    private List<FrontCheck> frontCheckList;

    public FrontCheckDetailAdapter(Context context, List<FrontCheck> frontCheckList) {
        this.context = context;
        this.frontCheckList = frontCheckList;
    }

    @NonNull
    @Override
    public FrontCheckViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater
                .from(viewGroup.getContext()), R.layout.frontcheckitem, viewGroup, false);
        FrontCheckViewHolder frontCheckViewHolder = new FrontCheckViewHolder(binding.getRoot());
        frontCheckViewHolder.setBinding(binding);
        return frontCheckViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FrontCheckViewHolder frontCheckViewHolder, int i) {
        frontCheckViewHolder.getBinding().setVariable(BR.frontcheck,frontCheckList.get(i));
        frontCheckViewHolder.getBinding().executePendingBindings();

//        frontCheckViewHolder.getBinding().setVariable()
    }

    @Override
    public int getItemCount() {
        if (frontCheckList != null) {
            return frontCheckList.size();
        } else {
            return 0;
        }

    }


    public class FrontCheckViewHolder extends RecyclerView.ViewHolder {
        public ViewDataBinding getBinding() {
            return binding;
        }

        public void setBinding(ViewDataBinding binding) {
            this.binding = binding;
        }

        private ViewDataBinding binding;

        public FrontCheckViewHolder(@NonNull View itemView) {
            super(itemView);


        }
    }
}


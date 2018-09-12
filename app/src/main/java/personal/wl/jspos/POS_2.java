package personal.wl.jspos;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import personal.wl.jspos.adapter.ProductAdapter;
import personal.wl.jspos.adapter.SaleOrderAdapter;
import personal.wl.jspos.pos.Product;
import personal.wl.jspos.pos.SaleDaily;

import static personal.wl.jspos.method.PosHandleDB.QueryProductBarCodeByCode;

public class POS_2 extends Activity {
    private SearchView searchView;
    private List<Product> prolist = new ArrayList<>();
    private List<SaleDaily> saleDailyList = new ArrayList<SaleDaily>();

    private SaleDaily saleDaily;
    private SaleOrderAdapter saleOrderAdapter;
    private ProductAdapter productAdapter;
    private VelocityTracker mVelocityTracker;

    private String branch_selected = null;
    private String pos_machine_selected = null;

    private TextView totalamt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos);


        totalamt = findViewById(R.id.totoalamount);

        showPreference();
        TextView branch = findViewById(R.id.branch);
        branch.setText(branch.getText() + branch_selected);

        TextView posmachine = findViewById(R.id.posmachine);
        posmachine.setText(posmachine.getText() + pos_machine_selected);


        searchView = findViewById(R.id.searchproduct);
        searchView.setSubmitButtonEnabled(true);
        searchView.setIconifiedByDefault(true);
        SpannableString spanText = new SpannableString("扫描条码或者店内码");
        spanText.setSpan(new AbsoluteSizeSpan(40, true), 0, spanText.length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spanText.setSpan(new ForegroundColorSpan(Color.WHITE), 0,
                spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        searchView.setQueryHint(spanText);

        EditText textView = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        LinearLayout.LayoutParams textLayoutParams = (LinearLayout.LayoutParams) searchView.getLayoutParams();
        textLayoutParams.height = textLayoutParams.WRAP_CONTENT;
        textView.setLayoutParams(textLayoutParams);
        textView.setTextSize(40);// 设置输入字体大小
        textView.setTextColor(Color.BLUE);// 设置输入字的显示
//        textView.setHeight(300);// 设置输入框的高度
        textView.setGravity(Gravity.BOTTOM);// 设置输入字的位置


        final RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.productlist);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(POS_2.this, DividerItemDecoration.VERTICAL));


        final RecyclerView salesorderview = findViewById(R.id.saleorder);
        salesorderview.setLayoutManager(new LinearLayoutManager(this));
        salesorderview.addItemDecoration(new DividerItemDecoration(POS_2.this, DividerItemDecoration.VERTICAL));
        saleOrderAdapter = new SaleOrderAdapter(POS_2.this, saleDailyList);
        salesorderview.setAdapter(saleOrderAdapter);
        saleOrderAdapter.setOnItemClickListener(new SaleOrderAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(POS_2.this, "单击：" + position + "个Item", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(int position) {
                Toast.makeText(POS_2.this, "长按了：" + position + "个Item", Toast.LENGTH_SHORT).show();

            }
        });


        salesorderview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Toast.makeText(POS_2.this, "be touch", Toast.LENGTH_LONG).show();
                return false;
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (TextUtils.isEmpty(s) || s.length() < 5) {
                    Toast.makeText(POS_2.this, "请输入正确的编码", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    List<Product> getresult = QueryProductBarCodeByCode(s);
                    if (getresult == null) {
                        Toast.makeText(POS_2.this, "输入编码不存在！", Toast.LENGTH_LONG).show();
                        return false;
                    }

                    prolist = getresult;

                    productAdapter = new ProductAdapter(POS_2.this, prolist);
                    mRecyclerView.setAdapter(productAdapter);

                    double tmp_qty = 1.00;
                    double tmp_price = 0.00;
                    double tmp_amount = 0.00;
                    tmp_price = prolist.get(0).getNormalPrice();
                    tmp_amount = tmp_qty * tmp_price;


                    saleDaily = new SaleDaily();
                    saleDaily.setProId(prolist.get(0).getProid());
                    saleDaily.setBarCode(prolist.get(0).getBarcode());
                    saleDaily.setNormalPrice(tmp_price);
                    saleDaily.setSaleAmt(tmp_amount);
                    saleDaily.setSaleQty(tmp_qty);


                    saleDailyList.add(saleDaily);
                    saleOrderAdapter.notifyDataSetChanged();

                    double tmp_subtotal = 0.00;
                    for (int k = 0; k < saleDailyList.size(); k++) {

                        tmp_subtotal = tmp_subtotal + saleDailyList.get(k).getSaleAmt();
                    }

                    totalamt.setText(Double.toString(tmp_subtotal));
                    searchView.setQuery("", false);
                    return true;
                }
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

    }


    private void showPreference() {
        SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(this);
        branch_selected = pre.getString("branch_selected", "0");
        pos_machine_selected = pre.getString("pos_machine", "0");
        String[] branch = getResources().getStringArray(R.array.pref_branch_list_name);
    }
}

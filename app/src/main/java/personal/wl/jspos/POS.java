package personal.wl.jspos;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import personal.wl.jspos.adapter.ProductAdapter;
import personal.wl.jspos.adapter.SaleOrderAdapter;
import personal.wl.jspos.pos.Product;
import personal.wl.jspos.pos.SaleDaily;

import static personal.wl.jspos.method.PosHandleDB.QueryProductBarCodeByCode;
import static personal.wl.jspos.method.PosHandleDB.QueryProductByCode;
import static personal.wl.jspos.method.PosHandleDB.getProductList;

public class POS extends Activity {
    private SearchView searchView;
    private List<Product> prolist;
    private List<SaleDaily> saleDailyList = new ArrayList<SaleDaily>();

    private SaleDaily saleDaily;
    private SaleOrderAdapter saleOrderAdapter;
    private ProductAdapter productAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos);
//        List<Product> prolist = getProductList();
        searchView = findViewById(R.id.searchproduct);

        final RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.productlist);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);

        RecyclerView salesorderview = findViewById(R.id.saleorder);


        salesorderview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(POS.this,DividerItemDecoration.VERTICAL));

        //        productAdapter = new ProductAdapter(this, prolist);



        saleOrderAdapter = new SaleOrderAdapter(POS.this, saleDailyList);
        salesorderview.setAdapter(saleOrderAdapter);
        salesorderview.addItemDecoration(new DividerItemDecoration(POS.this,DividerItemDecoration.VERTICAL));




        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (TextUtils.isEmpty(s) || s.length() < 5) {
                    Toast.makeText(POS.this, "请输入正确的编码", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    prolist = QueryProductBarCodeByCode(s);
                    if (prolist == null) {
                        Toast.makeText(POS.this, "输入编码不存在！", Toast.LENGTH_LONG).show();
                        return false;
                    }
                    productAdapter = new ProductAdapter(POS.this, prolist);
                    mRecyclerView.setAdapter(productAdapter);

                    saleDaily = new SaleDaily();
                    saleDaily.setProId(prolist.get(0).getProid());
                    saleDaily.setBarCode(prolist.get(0).getBarcode());
                    saleDaily.setNormalPrice(prolist.get(0).getNormalPrice());
                    saleDailyList.add(saleDaily);

                    saleOrderAdapter.notifyDataSetChanged();

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


}

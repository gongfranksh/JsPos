package personal.wl.jspos;

import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

import personal.wl.jspos.adapter.SaleOrderAdapterForDialog;
import personal.wl.jspos.adapter.SalePayModeAdapter;
import personal.wl.jspos.method.PosHandleDB;
import personal.wl.jspos.method.PosPrinter;
import personal.wl.jspos.pos.SaleDaily;
import personal.wl.jspos.pos.SalePayMode;

import static personal.wl.jspos.method.PosHandleDB.QuerySaleDetailBySaleid;
import static personal.wl.jspos.method.PosHandleDB.UpdateSaleDailyForRetrun;
import static personal.wl.jspos.method.PosHandleDB.UpdateSalePayMode;
import static personal.wl.jspos.method.PosHandleDB.getAllSalesPayment;

public class PosTransList extends AppCompatActivity {
    private List<SalePayMode> salepaymodeList = new ArrayList<SalePayMode>();
    private List<SaleDaily> saleDailyList = new ArrayList<>();
    private Context context;
    private SalePayModeAdapter salePayModeAdapter;
    private BluetoothDevice blueprinter;
    private PosPrinter posprinter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos_trans_list);

        final SwipeMenuRecyclerView salespaymodeview = findViewById(R.id.postranslist);
        salepaymodeList = getAllSalesPayment();
        salespaymodeview.setLayoutManager(new LinearLayoutManager(this));
        salespaymodeview.addItemDecoration(new DividerItemDecoration(PosTransList.this, DividerItemDecoration.VERTICAL));
        salespaymodeview.setSwipeMenuCreator(swipeMenuCreator);
        salespaymodeview.setSwipeMenuItemClickListener(mMenuItemClickListener);
        salePayModeAdapter = new SalePayModeAdapter(PosTransList.this, salepaymodeList);
        salespaymodeview.setAdapter(salePayModeAdapter);


        context = PosTransList.this;

        salePayModeAdapter.setOnItemClickListener(new SalePayModeAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                Toast.makeText(PosTransList.this, "on Actvity on click" + position, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onItemLongClick(int position) {
                showOrderDetail(salepaymodeList.get(position));
            }
        });
    }

    /**
     * 菜单创建器，在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.dp_70);
            // 1. MATCH_PARENT 自适应高度，保持和Item一样高;
            // 2. 指定具体的高，比如80;
            // 3. WRAP_CONTENT，自身高度，不推荐;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            // 添加左侧的，如果不添加，则左侧不会出现菜单。
            {
                SwipeMenuItem addItem = new SwipeMenuItem(context)
                        .setBackground(R.drawable.selector_green)
                        .setImage(R.mipmap.ic_action_add)
                        .setWidth(width)
                        .setHeight(height);
                swipeLeftMenu.addMenuItem(addItem); // 添加菜单到左侧。

                SwipeMenuItem closeItem = new SwipeMenuItem(context)
                        .setBackground(R.drawable.selector_purple)
                        .setImage(R.drawable.ic_local_printshop_black_24dp)
                        .setWidth(width)
                        .setHeight(height);
                swipeLeftMenu.addMenuItem(closeItem); // 添加菜单到左侧。
            }

            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            {
                SwipeMenuItem deleteItem = new SwipeMenuItem(context)
                        .setBackground(R.drawable.selector_red)
                        .setImage(R.mipmap.ic_action_delete)
                        .setText("退货")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);// 添加菜单到右侧。

//                SwipeMenuItem addItem = new SwipeMenuItem(context)
//                        .setBackground(R.drawable.selector_green)
//                        .setText("添加")
//                        .setTextColor(Color.WHITE)
//                        .setWidth(width)
//                        .setHeight(height);
//                swipeRightMenu.addMenuItem(addItem); // 添加菜单到右侧。
            }
        }
    };

    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge) {
            menuBridge.closeMenu();

            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
//                Toast.makeText(context, "list第" + adapterPosition + "; 右侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();

                switch (menuPosition) {
                    case 0:
                        if (!salepaymodeList.get(adapterPosition).getIsReturn()) {
                            CallReturnOfGoods(salepaymodeList.get(adapterPosition));
                        } else {
                            showErrorDiallog("已经退货了过了");

                        }
                        break;
                    default:
                        break;
                }
            } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {


                switch (menuPosition) {
                    case 0:
                        showOrderDetail(salepaymodeList.get(adapterPosition));
                        break;
                    case 1:
                        Toast.makeText(context, "重复打印", Toast.LENGTH_LONG).show();
                        printOrderDetail(salepaymodeList.get(adapterPosition));
                        break;

                    default:
                        break;
                }
//                Toast.makeText(context, "list第" + adapterPosition + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            }
        }
    };


    private void showErrorDiallog(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置构造器标题
        builder.setTitle("交易异常！");
        //构造器对应的图标
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage(s);
        builder.setNegativeButton("提示", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showLogonViewDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle(R.string.title_activity_login);
        LinearLayout loginDialog = (LinearLayout) getLayoutInflater().inflate(R.layout.logonviewdialog, null);
        builder.setView(loginDialog);
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showOrderDetail(SalePayMode salePayMode) {
        List<SaleDaily> saleDailyList = QuerySaleDetailBySaleid(salePayMode.getSaleId());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("交易明细");
        View view = LayoutInflater.from(context).inflate(R.layout.showsalesdetail, null);
        RecyclerView salesorderview = view.findViewById(R.id.showsaleorder);
        salesorderview.setLayoutManager(new LinearLayoutManager(this));
        salesorderview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        SaleOrderAdapterForDialog saleOrderAdapterForDialog = new SaleOrderAdapterForDialog(PosTransList.this, saleDailyList);
        salesorderview.setAdapter(saleOrderAdapterForDialog);
        saleOrderAdapterForDialog.notifyDataSetChanged();
        builder.setView(view);
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void printOrderDetail(SalePayMode salePayMode) {
        //增加print部分内容
        List<SaleDaily> saleDailyList = QuerySaleDetailBySaleid(salePayMode.getSaleId());
        List<SalePayMode> salePayModeList = new ArrayList<>();
        salePayModeList.add(salePayMode);
        posprinter = new PosPrinter(context,false);
        blueprinter = posprinter.getPosPrinter();
        posprinter.connect(blueprinter, saleDailyList, salePayModeList,false);
    }


    private void CallReturnOfGoods(SalePayMode salePayMode) {

        SalePayMode tmp_salepaymode = PosHandleDB.ReturnOfGoodsPayMode(salePayMode);
        salepaymodeList.add(tmp_salepaymode);
        PosHandleDB.InsertSalePayMode(tmp_salepaymode);
        salePayModeAdapter.notifyDataSetChanged();
        List<SaleDaily> tmp_saledailylist = PosHandleDB.ReturnOfGoodsProductDetail(salePayMode);
        PosHandleDB.InsertSaleDaily(tmp_saledailylist);

        //修改原来的记录--退货状态为已经退货了
        salePayMode.setIsReturn(true);
        UpdateSalePayMode(tmp_salepaymode);
        UpdateSaleDailyForRetrun(tmp_saledailylist,tmp_salepaymode);


    }
}

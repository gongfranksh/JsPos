package personal.wl.jspos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import personal.wl.jspos.adapter.PrintOrderStatusChange;
import personal.wl.jspos.adapter.PrinterOrderWatcher;
import personal.wl.jspos.adapter.ProductAdapter;
import personal.wl.jspos.adapter.SaleOrderAdapter;
import personal.wl.jspos.adapter.SaleOrderChange;
import personal.wl.jspos.adapter.SaleOrderWatcher;
import personal.wl.jspos.method.PosLogin;
import personal.wl.jspos.method.PosTabInfo;
import personal.wl.jspos.method.PosTranscation;
import personal.wl.jspos.pos.PmtDmRel;
import personal.wl.jspos.pos.Product;
import personal.wl.jspos.pos.ProductBranchRel;
import personal.wl.jspos.pos.SaleDaily;

import static android.support.constraint.Constraints.TAG;
import static personal.wl.jspos.method.PosHandleDB.JudgeSaler;
import static personal.wl.jspos.method.PosHandleDB.QueryPmtDMBranchRelByCode;
import static personal.wl.jspos.method.PosHandleDB.QueryProductBarCodeByCode;
import static personal.wl.jspos.method.PosHandleDB.QueryProductBranchRelByCode;
import static personal.wl.jspos.method.PosPayMent.PAYMENT_ALIPAY;
import static personal.wl.jspos.method.PosPayMent.PAYMENT_CASH;
import static personal.wl.jspos.method.PosPayMent.PAYMENT_WEIXIN;
import static personal.wl.jspos.method.PosPayMent.getPayMentCode;
import static personal.wl.jspos.method.PosTabInfo.NOBODY_LOGIN;

public class POS extends Activity {


    private static final int msgKey = 901;


    private SearchView searchView;
    private List<Product> prolist = new ArrayList<>();
    private List<SaleDaily> saleDailyList = new ArrayList<SaleDaily>();

    private SaleDaily saleDaily;
    private SaleOrderAdapter saleOrderAdapter;
    private ProductAdapter productAdapter;
    private VelocityTracker mVelocityTracker;


    private String branch_selected = null;
    private String pos_machine_selected = null;

    private TextView msg_title;
    private TextView casherid;
    private TextView totalamt;

    private TextView saletransdate;
    private TextView saleid;
    private EditText textView;
    private ImageButton ib_submit_cash;
    private ImageButton ib_submit_alipay;
    private ImageButton ib_submit_weixin;
    private ImageButton ib_submit_logoff;
    private PosTabInfo posTabInfo;
    private Context context;
    private PosTranscation posTranscation;

    private double tmp_qty = 1.00;
    private double tmp_price = 0.00;
    private double tmp_amount = 0.00;
    private String tmp_isDM = "0";
    private int presscount = 0;
    private PosLogin posLogin;
    private String scaned_proid = null;

    private PrinterOrderWatcher printerOrderWatcher = new PrinterOrderWatcher() {
        @Override
        public void update(Observable o, Object arg) {
            super.update(o, arg);
            Log.i(TAG, "run(POS.java:102)---printerOrderWatcher> ");
            cleartranstion();
            //开启付款按钮，重置记录按钮次数
            enablepaymentbutton();
            presscount = 0;
        }
    };


    private SaleOrderWatcher saleOrderWatcher = new SaleOrderWatcher() {
        @Override
        public void update(Observable o, Object arg) {
            super.update(o, arg);
            total_amount();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos);
        this.context = POS.this;
        SaleOrderChange.getInstance().addObserver(saleOrderWatcher);
        PrintOrderStatusChange.getInstance().addObserver(printerOrderWatcher);
        casherid = findViewById(R.id.casherid);

        posTabInfo = new PosTabInfo(POS.this);
        saletransdate = findViewById(R.id.saletransdate);
        totalamt = findViewById(R.id.totoalamount);

        saleid = findViewById(R.id.saleid);

        ib_submit_cash = findViewById(R.id.submit_cash);
        ib_submit_alipay = findViewById(R.id.submit_alipay);
        ib_submit_weixin = findViewById(R.id.submit_weixin);
        ib_submit_logoff = findViewById(R.id.submit_logoff);

        new TimeThread().start();

        showtitle();


        searchView = findViewById(R.id.searchproduct);
        searchView.setSubmitButtonEnabled(true);
        searchView.setIconifiedByDefault(true);
        SpannableString spanText = new SpannableString("扫描条码或者店内码");
        spanText.setSpan(new AbsoluteSizeSpan(40, true), 0, spanText.length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spanText.setSpan(new ForegroundColorSpan(Color.WHITE), 0,
                spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        searchView.setQueryHint(spanText);

        textView = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
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
        mRecyclerView.addItemDecoration(new DividerItemDecoration(POS.this, DividerItemDecoration.VERTICAL));


        final SwipeMenuRecyclerView salesorderview = findViewById(R.id.saleorder);

        salesorderview.setSwipeMenuCreator(swipeMenuCreator);
        salesorderview.setSwipeMenuItemClickListener(mMenuItemClickListener);


        salesorderview.setLayoutManager(new LinearLayoutManager(this));
        salesorderview.addItemDecoration(new DividerItemDecoration(POS.this, DividerItemDecoration.VERTICAL));
        saleOrderAdapter = new SaleOrderAdapter(POS.this, saleDailyList);
        salesorderview.setAdapter(saleOrderAdapter);

//        salesorderview.setOnItemMoveListener(new OnItemMoveListener() {
//            @Override
//            public boolean onItemMove(RecyclerView.ViewHolder srcHolder, RecyclerView.ViewHolder targetHolder) {
//                return false;
//            }
//
//            @Override
//            public void onItemDismiss(RecyclerView.ViewHolder srcHolder) {
//                Toast.makeText(POS.this,"Missitem",Toast.LENGTH_LONG).show();
//            }
//        });
//

        ib_submit_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartSaveTranscation(PAYMENT_CASH);
            }
        });


        ib_submit_alipay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartSaveTranscation(PAYMENT_ALIPAY);
            }
        });


        ib_submit_weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartSaveTranscation(PAYMENT_WEIXIN);
            }
        });

        posLogin = new PosLogin(POS.this, ib_submit_logoff);

        ib_submit_logoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                posTabInfo.setSalerid(NOBODY_LOGIN);
                login();
//                PosLogin posLogin = new PosLogin(POS.this, ib_submit_logoff);
//                posLogin.ShowAccountLogin();


            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {


                tmp_qty = 1.00;
                tmp_price = 0.00;
                tmp_amount = 0.00;
                tmp_isDM = "0";
                //扫码商品前先判断是否登陆
                if (posTabInfo.getSalerId().equals(NOBODY_LOGIN)) {
                    login();
                }


                if (TextUtils.isEmpty(s) || s.length() < 5) {
                    Toast.makeText(POS.this, "请输入正确的编码", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    List<Product> getresult = QueryProductBarCodeByCode(s);
                    if (getresult == null) {
                        Toast.makeText(POS.this, "输入编码不存在！", Toast.LENGTH_LONG).show();
                        getfocus();
                        return false;
                    }
                    prolist = getresult;
                    //取得扫码商品的店内码进行下一步核查
                    scaned_proid = getresult.get(0).getProid();
                    //获取该门店该商品的零售价格
                    List<ProductBranchRel> getproductbranchrel = QueryProductBranchRelByCode(scaned_proid, posTabInfo.getBranchCode());
                    if (getproductbranchrel == null) {
                        Toast.makeText(POS.this, "该编码的价格不存在！", Toast.LENGTH_LONG).show();
                        return false;
                    } else {
                        tmp_price = getproductbranchrel.get(0).getNormalPrice();
                    }

                    //获取该门店该商品的是否有DM促销
                    List<PmtDmRel> getproductdm = QueryPmtDMBranchRelByCode(scaned_proid, posTabInfo.getBranchCode());
                    if (getproductdm.size() == 0) {
                        Log.i(TAG, "商品DM检查-->" + s + "没有DM");
                    } else {
                        Log.i(TAG, "商品DM检查-->" + s
                                + "DMID：" + getproductdm.get(0).getDMId()
                                + "原来价格：" + getproductdm.get(0).getOrigSalePrice().toString()
                                + "DM促销价格：" + getproductdm.get(0).getSalePrice().toString()

                        );
                        Toast.makeText(POS.this, "商品DM促销==>" + s, Toast.LENGTH_LONG).show();
                        tmp_price = getproductdm.get(0).getSalePrice();
                        tmp_isDM = "1";
                    }


                    productAdapter = new ProductAdapter(POS.this, prolist);
                    mRecyclerView.setAdapter(productAdapter);

                    //计算单行合计金额
                    tmp_amount = tmp_qty * tmp_price;

                    //准备写入销售明细表
                    saleDaily = new SaleDaily();
                    saleDaily.setProId(prolist.get(0).getProid());
                    saleDaily.setBarCode(prolist.get(0).getBarcode());
                    saleDaily.setIsDM(tmp_isDM);
                    saleDaily.setCurPrice(tmp_price);
                    saleDaily.setNormalPrice(tmp_price);
                    saleDaily.setSaleAmt(tmp_amount);
                    saleDaily.setSaleQty(tmp_qty);
                    saleDaily.setSalerId(posTabInfo.getSalerId());
                    saleDaily.setSaleMan(posTabInfo.getSalerId());


                    addsalesdaily(saleDaily);


                    searchView.setQuery("", false);
                    return true;
                }
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        //  getfocus();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "onResume", Toast.LENGTH_LONG).show();
        if (posTabInfo.getSalerId().equals(NOBODY_LOGIN)) {
//            login();
//            showErrorDiallog("请先登陆");
//            Intent intent = new Intent();
//            intent.setClass(POS.this, LoginActivity.class);
//            startActivity(intent);


        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "onRestart", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SaleOrderChange.getInstance().deleteObserver(saleOrderWatcher);
        PrintOrderStatusChange.getInstance().deleteObserver(printerOrderWatcher);
    }


    @Override
    protected void onStop() {
        super.onStop();
//        Toast.makeText(this, "onStop", Toast.LENGTH_LONG).show();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Toast.makeText(this, "onDestroy", Toast.LENGTH_LONG).show();
    }

    private void StartSaveTranscation(int paymode) {
        //增加控制重复按打印件
        presscount += 1;
        Toast.makeText(context, "付款按钮第" + presscount + "次", Toast.LENGTH_LONG).show();
        disablepaymentbutton();        //——————结束
        if (saleDailyList.size() > 0) {
            if (JudgeSaler(saleDailyList)) {
                posTranscation = new PosTranscation(context);
                posTranscation.SaleTranstion(saleDailyList, paymode);

                if (!posTabInfo.getNeedPrint()) {
                    cleartranstion();
                    //开启付款按钮，重置记录按钮次数
                    enablepaymentbutton();
                    presscount = 0;
                }


            } else {
                showErrorDiallog("营业员代码错误！");
            }


        } else {
            showErrorDiallog("请先输入商品");
        }
    }

    private void showErrorDiallog(String s) {
//        msg_title= new TextView(context);
//        msg_title.setText("交易异常！");
//        msg_title.setPadding(10, 10, 10, 10);
//        msg_title.setGravity(Gravity.CENTER);
//        msg_title.setTextSize(23);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置构造器标题
        builder.setTitle("交易异常");

        //构造器对应的图标
        builder.setIcon(R.drawable.ic_cancel_black_24dp);
        builder.setMessage(s);
        builder.setNegativeButton("提示", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showOkDiallog(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置构造器标题
        builder.setTitle("交易成功");
        //构造器对应的图标
        builder.setIcon(R.drawable.ic_check_black_24dp);
        builder.setMessage(s + "\n" + presscount);
        builder.setNegativeButton("提示", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void insertSaleDaily(List<SaleDaily> transactions, int paymode) {
        char tmp_paymode = getPayMentCode(paymode);
        for (int i = 0; i < transactions.size(); i++) {

        }
    }

    private void showLogonViewDialog(View.OnClickListener view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle(R.string.title_activity_login);

        /**
         * 设置内容区域为自定义View
         */
        LinearLayout loginDialog = (LinearLayout) getLayoutInflater().inflate(R.layout.logonviewdialog, null);
        builder.setView(loginDialog);

        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void cleartranstion() {

        saleDailyList.removeAll(saleDailyList);
        prolist.removeAll(prolist);
        saleOrderAdapter.notifyDataSetChanged();
        productAdapter.notifyDataSetChanged();
        totalamt.setText("0.0");
        Log.i(TAG, "run(POS.java:404)---cleartranstion> ");
//        posTranscation = new PosTranscation(context);
        saleid.setText("上次交易流水:" + posTranscation.getTranscationId());
        showOkDiallog(posTranscation.getTranscationId());

    }

    private void showPreference() {

        branch_selected = posTabInfo.getBranchCode() + posTabInfo.getBranchName();
        pos_machine_selected = posTabInfo.getPosMachine();
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
                SwipeMenuItem addItem = new SwipeMenuItem(POS.this)
                        .setBackground(R.drawable.selector_green)
                        .setImage(R.mipmap.ic_action_add)
                        .setWidth(width)
                        .setHeight(height);
                swipeLeftMenu.addMenuItem(addItem); // 添加菜单到左侧。

                SwipeMenuItem closeItem = new SwipeMenuItem(POS.this)
                        .setBackground(R.drawable.selector_red)
                        .setImage(R.mipmap.ic_action_close)
                        .setWidth(width)
                        .setHeight(height);
                swipeLeftMenu.addMenuItem(closeItem); // 添加菜单到左侧。
            }

            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            {
                SwipeMenuItem deleteItem = new SwipeMenuItem(POS.this)
                        .setBackground(R.drawable.selector_red)
                        .setImage(R.mipmap.ic_action_delete)
                        .setText("删除")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);// 添加菜单到右侧。

                SwipeMenuItem addItem = new SwipeMenuItem(POS.this)
                        .setBackground(R.drawable.selector_green)
                        .setText("添加")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(addItem); // 添加菜单到右侧。
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
                Toast.makeText(POS.this, "list第" + adapterPosition + "; 右侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();

                switch (menuPosition) {
                    case 0:
                        posorderdelete(adapterPosition);
                        break;
                    default:
                        break;
                }
            } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {


                switch (menuPosition) {
                    case 0:
                        addorderqty(adapterPosition);
                        break;
                    case 1:
                        removeorderqty(adapterPosition);
                        break;

                    default:
                        break;
                }
//                Toast.makeText(POS.this, "list第" + adapterPosition + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            }
        }
    };


    private void addsalesdaily(SaleDaily sd) {
        Boolean isFound = false;
        sd.getProId();
        int offset = 0;
        for (offset = 0; offset < saleDailyList.size(); offset++) {
            if (sd.getProId().equals(saleDailyList.get(offset).getProId())) {
                isFound = true;
                break;
            }
        }
        if (isFound) {
            addorderqty(offset);
        } else {
            saleDailyList.add(sd);
            saleOrderAdapter.notifyDataSetChanged();
            total_amount();
        }
    }


    private void posorderdelete(int position) {
        saleDailyList.remove(position);
        saleOrderAdapter.notifyDataSetChanged();
        total_amount();
    }


    private void addorderqty(int position) {
        SaleDaily needUpdate = saleDailyList.get(position);
        final double item_qty;
        final double item_amt;
        item_qty = needUpdate.getSaleQty() + 1;
        needUpdate.setSaleQty(item_qty);
        item_amt = item_qty * needUpdate.getCurPrice();
        needUpdate.setSaleAmt(item_amt);
        saleDailyList.set(position, needUpdate);
        saleOrderAdapter.notifyDataSetChanged();
        total_amount();
    }

    private void removeorderqty(int position) {
        SaleDaily needUpdate = saleDailyList.get(position);
        final double item_qty;
        final double item_amt;

        item_qty = needUpdate.getSaleQty() - 1;
        if (item_qty > 0) {
            needUpdate.setSaleQty(item_qty);
            item_amt = item_qty * needUpdate.getCurPrice();
            needUpdate.setSaleAmt(item_amt);
            saleDailyList.set(position, needUpdate);
            saleOrderAdapter.notifyDataSetChanged();
        } else {
            posorderdelete(position);
        }
        total_amount();
    }


    private void total_amount() {
        double tmp_subtotal = 0.00;
        for (int k = 0; k < saleDailyList.size(); k++) {

            tmp_subtotal = tmp_subtotal + saleDailyList.get(k).getSaleAmt();
        }
        totalamt.setText(String.format("%1$,.1f", tmp_subtotal));

    }


    public class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = msgKey;
                    mHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msgKey:
                    long sysTime = System.currentTimeMillis();
                    CharSequence sysTimeStr = DateFormat
                            .format(" yyyy-MM-dd HH:mm:ss", sysTime);
                    saletransdate.setText("日期：" + sysTimeStr);
                    casherid.setText("操作员:" + posTabInfo.getSalerId());

//                    total_amount();
                    break;
                default:
                    break;
            }
        }
    };

    //---
    private void showtitle() {
//        showPreference();
        TextView branch = findViewById(R.id.branch);
        branch.setText(branch.getText() + posTabInfo.getBranchCode() + posTabInfo.getBranchName());
        final TextView posmachine = findViewById(R.id.posmachine);
        posmachine.setText(posmachine.getText() + posTabInfo.getPosMachine());

        casherid.setText(casherid.getText() + posTabInfo.getSalerId());

    }


    private void getfocus() {
        searchView.setFocusable(true);
        searchView.setFocusableInTouchMode(true);
        searchView.requestFocus();
        searchView.requestFocusFromTouch();
        textView.setFocusable(true);
        textView.setFocusableInTouchMode(true);
        textView.requestFocus();
        textView.requestFocusFromTouch();
        searchView.setQuery("", false);
        textView.findFocus();
        searchView.findFocus();
    }


    private void disablepaymentbutton() {
        ib_submit_cash.setClickable(false);
        ib_submit_alipay.setClickable(false);
        ib_submit_weixin.setClickable(false);
        ib_submit_logoff.setClickable(false);
    }

    private void enablepaymentbutton() {
        ib_submit_cash.setClickable(true);
        ib_submit_alipay.setClickable(true);
        ib_submit_weixin.setClickable(true);
        ib_submit_logoff.setClickable(true);
    }

    private void login() {
        posLogin.ShowAccountLogin();
    }
}

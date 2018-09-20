package personal.wl.jspos.method;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.LinearLayout;
import android.widget.Switch;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import personal.wl.jspos.db.DBConnect;
import personal.wl.jspos.pos.BranchEmployee;
import personal.wl.jspos.pos.BranchEmployeeDao;
import personal.wl.jspos.pos.DaoSession;
import personal.wl.jspos.pos.Product;
import personal.wl.jspos.pos.ProductBarCode;
import personal.wl.jspos.pos.ProductBarCodeDao;
import personal.wl.jspos.pos.ProductDao;
import personal.wl.jspos.pos.SaleDaily;
import personal.wl.jspos.pos.SaleDailyDao;
import personal.wl.jspos.pos.SalePayMode;
import personal.wl.jspos.pos.SalePayModeDao;

import static personal.wl.jspos.method.PosPayMent.PAYMENT_ALIPAY_CODE;
import static personal.wl.jspos.method.PosPayMent.PAYMENT_CASH_CODE;
import static personal.wl.jspos.method.PosPayMent.PAYMENT_WEIXIN_CODE;

public class PosHandleDB {
    //Common Tools
    private static void ExecSqlVoid(String sql) {
        try {
            DBConnect.getInstances().getDaoSession().getDatabase().execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void CleanLocalSales() {
        String clean_trans_paymode = "delete from sale_pay_mode;";
        String clean_trans_product = "delete from sale_daily;";
        ExecSqlVoid(clean_trans_paymode);
        ExecSqlVoid(clean_trans_product);
    }

    public static List<Product> getProductList() {

        ProductDao productDao = DBConnect.getInstances().getDaoSession().getProductDao();
        QueryBuilder cond = productDao.queryBuilder().limit(100);
//        cond.where(BranchEmployeeDao.Properties.Braid.eq(branch_selected),
//                BranchEmployeeDao.Properties.Empid.eq(this.maccount)).build();
        return cond.build().list();
    }

    public static List<SaleDaily> QuerySaleDetailBySaleid(String saleid) {
        SaleDailyDao saleDailyDao = DBConnect.getInstances().getDaoSession().getSaleDailyDao();
        QueryBuilder cond = saleDailyDao.queryBuilder();
        cond.where(SaleDailyDao.Properties.SaleId.eq(saleid)).build();
        return cond.build().list();
    }


    public static List<Product> QueryProductByCode(String proid) {
        ProductDao productDao = DBConnect.getInstances().getDaoSession().getProductDao();
        QueryBuilder cond = productDao.queryBuilder();
        cond.where(ProductDao.Properties.Proid.like("%" + proid + "%")).build();
        return cond.build().list();
    }

    public static List<Product> QueryProductBarCodeByCode(String BarCode) {
        List<ProductBarCode> barcodelist;
        List<Product> productList = null;
        ProductBarCodeDao productBarCode = DBConnect.getInstances().getDaoSession().getProductBarCodeDao();
        ProductDao productDao = DBConnect.getInstances().getDaoSession().getProductDao();
        QueryBuilder condn = productBarCode.queryBuilder();

        condn.whereOr(ProductBarCodeDao.Properties.Barcode.like("%" + BarCode + "%"),
                ProductBarCodeDao.Properties.Proid.like("%" + BarCode + "%")
        );
        barcodelist = condn.build().list();
        for (ProductBarCode pb : barcodelist) {
            QueryBuilder cond = productDao.queryBuilder();
            cond.where(ProductDao.Properties.Proid.eq((pb.getProid())));
            productList = cond.build().list();
        }
        return productList;
    }


    public static Boolean isSaleid(String saleidcode) {
        BranchEmployeeDao branchEmployeeDao = DBConnect.getInstances().getDaoSession().getBranchEmployeeDao();
        QueryBuilder cond = branchEmployeeDao.queryBuilder();
        cond.where(BranchEmployeeDao.Properties.Empid.eq(saleidcode));
        List<BranchEmployee> emplist = cond.build().list();
        if (emplist.size() != 0) {
            return true;
        }
        return false;
    }

    public static Boolean JudgeSaler(List<SaleDaily> saleDailyList) {
        for (int i = 0; i < saleDailyList.size(); i++) {
            if (saleDailyList.get(i).getSalerId()==null) {
                return false;
            }
            if (saleDailyList.get(i).getSalerId().length() !=5)
            {
                return false;
            }

            if (!isSaleid(saleDailyList.get(i).getSalerId())) {
                return false;
            }
        }
        return true;
    }

    public static BranchEmployee getSaleid(String saleidcode) {
        BranchEmployeeDao branchEmployeeDao = DBConnect.getInstances().getDaoSession().getBranchEmployeeDao();
        QueryBuilder cond = branchEmployeeDao.queryBuilder();
        cond.where(BranchEmployeeDao.Properties.Empid.eq(saleidcode));
        List<BranchEmployee> emplist = cond.build().list();
        if (emplist.size() != 0) {
            return emplist.get(0);
        }
        return null;
    }

    public static List<SalePayMode> getAllSalesPayment() {
        SalePayModeDao salePayModeDao = DBConnect.getInstances().getDaoSession().getSalePayModeDao();
        QueryBuilder cond = salePayModeDao.queryBuilder();
//        cond.where(BranchEmployeeDao.Properties.Empid.eq(saleidcode));
        List<SalePayMode> salePayModeList = cond.build().list();
        return salePayModeList;
    }

    public static SalePayMode ReturnOfGoodsPayMode(SalePayMode salePayMode) {
        Double tmp_paymoney = salePayMode.getPayMoney() * (-1);
        String tmp_saleid = "R" + salePayMode.getSaleId();
        SalePayMode rt_salepaymode = new SalePayMode();

        rt_salepaymode.setBraid(salePayMode.getBraid());
        rt_salepaymode.setSaleDate(new Date());
        rt_salepaymode.setSaleId(tmp_saleid);
        rt_salepaymode.setPayModeId(salePayMode.getPayModeId());
        rt_salepaymode.setPayMoney(tmp_paymoney);
        rt_salepaymode.setCardType("1");
        return rt_salepaymode;
    }

    public static void InsertSalePayMode(SalePayMode salePayMode) {
        SalePayModeDao salePayModeDao = DBConnect.getInstances().getDaoSession().getSalePayModeDao();
        salePayModeDao.insert(salePayMode);
    }

    public static void InsertSaleDaily(List<SaleDaily> saleDailyList) {
        SaleDailyDao saleDailyDao = DBConnect.getInstances().getDaoSession().getSaleDailyDao();
        for (int i = 0; i < saleDailyList.size(); i++) {
            saleDailyDao.insert(saleDailyList.get(i));
        }
    }


    public static List<SaleDaily> getSaleDailyBysaleid(String saleid) {
        SaleDailyDao saleDailyDao = DBConnect.getInstances().getDaoSession().getSaleDailyDao();
        QueryBuilder cond = saleDailyDao.queryBuilder();
        cond.where(SaleDailyDao.Properties.SaleId.eq(saleid));
        return cond.build().list();
    }

    public static List<SaleDaily> ReturnOfGoodsProductDetail(SalePayMode salePayMode) {
        String paymode_saleid = salePayMode.getSaleId();
        char[] paycode = salePayMode.getPayModeId().toCharArray();
        List<SaleDaily> rt_saledailylist = new ArrayList<>();
        List<SaleDaily> saleDailyList = getSaleDailyBysaleid(paymode_saleid);
        for (int i = 0; i < saleDailyList.size(); i++) {
            SaleDaily tmp_saledaily = new SaleDaily();

            Double tmp_saleamt = saleDailyList.get(i).getSaleAmt() * (-1);
            Double tmp_saleqty = saleDailyList.get(i).getSaleQty() * (-1);
            Double tmp_CurPrice = saleDailyList.get(i).getCurPrice() * (-1);
            Double tmp_NormalPrice = saleDailyList.get(i).getNormalPrice() * (-1);
            String tmp_saleid = "R" + paymode_saleid;

            tmp_saledaily.setBraid(saleDailyList.get(i).getBraid());
            tmp_saledaily.setSaleDate(new Date());
            tmp_saledaily.setProId(saleDailyList.get(i).getProId());
            tmp_saledaily.setBarCode(saleDailyList.get(i).getBarCode());
            tmp_saledaily.setClassId(saleDailyList.get(i).getClassId());
            tmp_saledaily.setIsDM(saleDailyList.get(i).getIsDM());
            tmp_saledaily.setIsPmt(saleDailyList.get(i).getIsPmt());
            tmp_saledaily.setIsTimePrompt(saleDailyList.get(i).getIsTimePrompt());
            tmp_saledaily.setSaleTax(saleDailyList.get(i).getSaleTax());
            tmp_saledaily.setPosNo(saleDailyList.get(i).getPosNo());
            tmp_saledaily.setSalerId(saleDailyList.get(i).getSalerId());
            tmp_saledaily.setSaleId(tmp_saleid);
            tmp_saledaily.setSaleMan(saleDailyList.get(i).getSaleMan());
            tmp_saledaily.setSaleQty(tmp_saleqty);
            tmp_saledaily.setSaleAmt(tmp_saleamt);
            tmp_saledaily.setNormalPrice(tmp_NormalPrice);
            tmp_saledaily.setCurPrice(tmp_CurPrice);
            Double tmp_cash = 0.0;
            switch (paycode[0]) {
                case PAYMENT_CASH_CODE:
                    tmp_cash = saleDailyList.get(i).getCash1();
                    tmp_saledaily.setCash1(tmp_cash * (-1));
                    break;
                case PAYMENT_ALIPAY_CODE:
                    tmp_cash = saleDailyList.get(i).getCash7();
                    tmp_saledaily.setCash7(tmp_cash * (-1));
                    break;
                case PAYMENT_WEIXIN_CODE:
                    tmp_cash = saleDailyList.get(i).getCash8();
                    tmp_saledaily.setCash8(tmp_cash * (-1));
                    break;
            }
            rt_saledailylist.add(tmp_saledaily);
        }
        return rt_saledailylist;
    }


}

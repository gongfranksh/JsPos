package personal.wl.jspos.method;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.LinearLayout;
import android.widget.Switch;

import org.greenrobot.greendao.query.QueryBuilder;

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

    public SalePayMode ReturnOfGoodsPayMode(SalePayMode salePayMode) {
        Double tmp_paymoney = salePayMode.getPayMoney() * (-1);
        String tmp_saleid = "R" + salePayMode.getSaleId();
        salePayMode.setSaleDate(new Date());
        salePayMode.setPayMoney(tmp_paymoney);
        salePayMode.setSaleId(tmp_saleid);
        return salePayMode;
    }

    public void InsertSalePayMode(SalePayMode salePayMode) {
        SalePayModeDao salePayModeDao = DBConnect.getInstances().getDaoSession().getSalePayModeDao();
        salePayModeDao.insert(salePayMode);
    }

    public List<SaleDaily> getSaleDailyBysaleid(String saleid) {
        SaleDailyDao saleDailyDao = DBConnect.getInstances().getDaoSession().getSaleDailyDao();
        QueryBuilder cond = saleDailyDao.queryBuilder();
        cond.where(SaleDailyDao.Properties.SaleId.eq(saleid));
        return cond.build().list();
    }

    public List<SaleDaily> ReturnOfGoodsProductDetail(SalePayMode salePayMode) {
        String paymode_saleid = salePayMode.getSaleId();
        char[] paycode = salePayMode.getPayModeId().toCharArray();
        List<SaleDaily> saleDailyList = getSaleDailyBysaleid(paymode_saleid);
        for (int i = 0; i < saleDailyList.size(); i++) {
            Double tmp_saleamt = saleDailyList.get(i).getSaleAmt();
            Double tmp_saleqty = saleDailyList.get(i).getSaleQty();
            Double tmp_CurPrice = saleDailyList.get(i).getCurPrice();
            Double tmp_NormalPrice = saleDailyList.get(i).getNormalPrice();
            String tmp_saleid = "R" + paymode_saleid;
            saleDailyList.get(i).setSaleAmt(tmp_saleamt);
            saleDailyList.get(i).setSaleQty(tmp_saleqty);
            saleDailyList.get(i).setCurPrice(tmp_CurPrice);
            saleDailyList.get(i).setNormalPrice(tmp_NormalPrice);
            saleDailyList.get(i).setSaleId(tmp_saleid);
            Double tmp_cash = 0.0;
            switch (paycode[0]) {
                case PAYMENT_CASH_CODE:
                    tmp_cash = saleDailyList.get(i).getCash1();
                    saleDailyList.get(i).setCash1(tmp_cash * (-1));
                    break;
                case PAYMENT_ALIPAY_CODE:
                    tmp_cash = saleDailyList.get(i).getCash7();
                    saleDailyList.get(i).setCash7(tmp_cash * (-1));
                    break;
                case PAYMENT_WEIXIN_CODE:
                    tmp_cash = saleDailyList.get(i).getCash8();
                    saleDailyList.get(i).setCash8(tmp_cash * (-1));
                    break;
            }
        }

        return saleDailyList;
    }


}

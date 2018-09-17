package personal.wl.jspos.method;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.LinearLayout;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import personal.wl.jspos.db.DBConnect;
import personal.wl.jspos.pos.BranchEmployee;
import personal.wl.jspos.pos.BranchEmployeeDao;
import personal.wl.jspos.pos.DaoSession;
import personal.wl.jspos.pos.Product;
import personal.wl.jspos.pos.ProductBarCode;
import personal.wl.jspos.pos.ProductBarCodeDao;
import personal.wl.jspos.pos.ProductDao;

public class PosHandleDB {
    public static List<Product> getProductList() {

        ProductDao productDao = DBConnect.getInstances().getDaoSession().getProductDao();
        QueryBuilder cond = productDao.queryBuilder().limit(100);
//        cond.where(BranchEmployeeDao.Properties.Braid.eq(branch_selected),
//                BranchEmployeeDao.Properties.Empid.eq(this.maccount)).build();
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


}

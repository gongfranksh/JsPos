package personal.wl.jspos.method;

import android.database.Cursor;
import android.util.Log;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import personal.wl.jspos.db.DBConnect;
import personal.wl.jspos.pos.BranchEmployee;
import personal.wl.jspos.pos.BranchEmployeeDao;
import personal.wl.jspos.pos.MobileDevice;
import personal.wl.jspos.pos.MobileDeviceDao;
import personal.wl.jspos.pos.PmtDmRel;
import personal.wl.jspos.pos.PmtDmRelDao;
import personal.wl.jspos.pos.Product;
import personal.wl.jspos.pos.ProductBarCode;
import personal.wl.jspos.pos.ProductBarCodeDao;
import personal.wl.jspos.pos.ProductBranchRel;
import personal.wl.jspos.pos.ProductBranchRelDao;
import personal.wl.jspos.pos.ProductDao;
import personal.wl.jspos.pos.SaleDaily;
import personal.wl.jspos.pos.SaleDailyDao;
import personal.wl.jspos.pos.SalePayMode;
import personal.wl.jspos.pos.SalePayModeDao;

import static personal.wl.jspos.method.DeviceUtils.GetTransInnerID;
import static personal.wl.jspos.method.PosPayMent.PAYMENT_ALIPAY_CODE;
import static personal.wl.jspos.method.PosPayMent.PAYMENT_CASH_CODE;
import static personal.wl.jspos.method.PosPayMent.PAYMENT_WEIXIN_CODE;


public class PosHandleDB {
    //Common Tools
    protected static long PROID = 2000000000000L;
    public static final String MOBILE_DEVICE_CAN_RUN = "1";
    public static final String MOBILE_DEVICE_CANNOT_RUN = "0";
    private static final  String TAG="PosHandleDB";

    private static void ExecSqlVoid(String sql) {
        try {
            DBConnect.getInstances().getDaoSession().getDatabase().execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void UpGradeSqlScript(String sql){
        ExecSqlVoid(sql);
    }

    public static void CleanLocalSales() {
        String clean_trans_paymode = "delete from sale_pay_mode;";
        String clean_trans_product = "delete from sale_daily;";

        ExecSqlVoid(clean_trans_paymode);
        ExecSqlVoid(clean_trans_product);
//        ExecSqlVoid("DELETE FROM PMT_DM_REL;");

    }

    public static Boolean CheckDeviceByLocal(HashMap device) {
        String tmp_device = (String) device.get("deviceid");
        String tmp_posno = (String) device.get("posno");
        MobileDeviceDao mobileDeviceDao = DBConnect.getInstances().getDaoSession().getMobileDeviceDao();
        QueryBuilder cond = mobileDeviceDao.queryBuilder();
        cond.where(MobileDeviceDao.Properties.Deviceid.eq(tmp_device),
                MobileDeviceDao.Properties.Posno.eq(tmp_posno));
        List<MobileDevice> res = cond.build().list();
        if (res.size() == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static List<MobileDevice> QueryMobileDevice(HashMap device) {
        String tmp_device = (String) device.get("deviceid");
        String tmp_posno = (String) device.get("posno");
        MobileDeviceDao mobileDeviceDao = DBConnect.getInstances().getDaoSession().getMobileDeviceDao();
        QueryBuilder cond = mobileDeviceDao.queryBuilder();
        cond.where(MobileDeviceDao.Properties.Deviceid.eq(tmp_device),
                MobileDeviceDao.Properties.Status.eq(MOBILE_DEVICE_CAN_RUN),
                MobileDeviceDao.Properties.Posno.eq(tmp_posno));
        List<MobileDevice> res = cond.build().list();
        Log.i(TAG, "QueryMobileDevice:res===>"+res.size()+res);
        Log.i(TAG, "QueryMobileDevice:tmp_device===> "+tmp_device);
        Log.i(TAG, "QueryMobileDevice:MOBILE_DEVICE_CAN_RUN===> "+MOBILE_DEVICE_CAN_RUN);
        Log.i(TAG, "QueryMobileDevice:tmp_posno===> "+tmp_posno);
        if (res.size() == 1) {
            return res;
        } else {
            return res;
        }
    }


    public static void InsertDeviceByLocal(List device) {
        String tmp_device = (String) ((HashMap) device.get(0)).get("deviceid");
        String tmp_posno = (String) ((HashMap) device.get(0)).get("posno");
        Integer tmp_sourceid = (Integer) ((HashMap) device.get(0)).get("sourceid");
        MobileDeviceDao mobileDeviceDao = DBConnect.getInstances().getDaoSession().getMobileDeviceDao();
        MobileDevice mobileDevice = new MobileDevice();
        mobileDevice.setDeviceid(tmp_device);
        mobileDevice.setPosno(tmp_posno);
        mobileDevice.setSourceId(Long.parseLong(String.valueOf(tmp_sourceid)));
        mobileDevice.setStatus(MOBILE_DEVICE_CAN_RUN);
        mobileDeviceDao.insert(mobileDevice);
    }

    public static Boolean CheckAccountPassword(HashMap login) {

        String tmp_branch = login.get("branchid").toString();
        String tmp_accountid = login.get("accountid").toString();
        String tmp_password = login.get("password").toString();
        BranchEmployeeDao branchEmployeeDao = DBConnect.getInstances().getDaoSession().getBranchEmployeeDao();
        QueryBuilder cond = branchEmployeeDao.queryBuilder();
        cond.where(BranchEmployeeDao.Properties.Braid.eq(tmp_branch),
                BranchEmployeeDao.Properties.Empid.eq(tmp_accountid)).build();
        List<BranchEmployee> tmp_branchemployeelist = cond.list();
        if (tmp_branchemployeelist.size() != 1) return false;
        if (tmp_branchemployeelist.get(0).getPassword().equals(tmp_password)) {
            return true;
        } else {
            return false;
        }

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

    public static List<SaleDaily> QuerySaleDetailByOrderInnerId(String saleid) {
        SaleDailyDao saleDailyDao = DBConnect.getInstances().getDaoSession().getSaleDailyDao();
        QueryBuilder cond = saleDailyDao.queryBuilder();
        cond.where(SaleDailyDao.Properties.OrderInnerId.eq(saleid)).build();
        return cond.build().list();
    }


    public static List<Product> QueryProductByCode(String proid) {
        String tmp_proid = proid;
        if (proid.length() != 13) {
            tmp_proid = Long.toString(PROID + Long.parseLong(proid));
        }

        ProductDao productDao = DBConnect.getInstances().getDaoSession().getProductDao();
        QueryBuilder cond = productDao.queryBuilder();
        cond.where(ProductDao.Properties.Proid.eq(tmp_proid)).build();
        return cond.build().list();
    }


    public static List<PmtDmRel> QueryPmtDMBranchRelByCode(String proid, String storecode) {
        Date curDate =  new Date(System.currentTimeMillis());
        String tmp_proid = proid;
        String tmp_streocode = storecode;
        if (proid.length() != 13) {
            tmp_proid = Long.toString(PROID + Long.parseLong(proid));
        }

        PmtDmRelDao pmtDmRelDao = DBConnect.getInstances().getDaoSession().getPmtDmRelDao();
        QueryBuilder cond = pmtDmRelDao.queryBuilder();
        cond.where(PmtDmRelDao.Properties.Proid.eq(tmp_proid),
                PmtDmRelDao.Properties.Braid.eq(tmp_streocode),
                PmtDmRelDao.Properties.DMBeginDate.le(curDate),
                PmtDmRelDao.Properties.DMEndDate.gt(curDate)
        ).build();
        return cond.build().list();
    }


    public static List<ProductBranchRel> QueryProductBranchRelByCode(String proid, String storecode) {
        String tmp_proid = proid;
        String tmp_streocode = storecode;
        if (proid.length() != 13) {
            tmp_proid = Long.toString(PROID + Long.parseLong(proid));
        }

        ProductBranchRelDao productBranchRelDao = DBConnect.getInstances().getDaoSession().getProductBranchRelDao();
        QueryBuilder cond = productBranchRelDao.queryBuilder();
        cond.where(ProductBranchRelDao.Properties.Proid.eq(tmp_proid),
                ProductBranchRelDao.Properties.Braid.eq(tmp_streocode)
        ).build();
        return cond.build().list();
    }


    public static List<Product> QueryProductBarCodeByCode(String BarCode) {
        String tmp_proid = BarCode;
        if (BarCode.length() != 13) {
            tmp_proid = Long.toString(PROID + Long.parseLong(BarCode));
        }

        List<ProductBarCode> barcodelist;
        List<Product> productList = null;
        ProductBarCodeDao productBarCode = DBConnect.getInstances().getDaoSession().getProductBarCodeDao();
        ProductDao productDao = DBConnect.getInstances().getDaoSession().getProductDao();
        QueryBuilder condn = productBarCode.queryBuilder();

        condn.whereOr(ProductBarCodeDao.Properties.Barcode.eq(BarCode),
                ProductBarCodeDao.Properties.Proid.eq(tmp_proid)
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
            if (saleDailyList.get(i).getSalerId() == null) {
                return false;
            }
            if (saleDailyList.get(i).getSalerId().length() != 5) {
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

    public static String getSalerName(String saleidcode) {
        BranchEmployeeDao branchEmployeeDao = DBConnect.getInstances().getDaoSession().getBranchEmployeeDao();
        QueryBuilder cond = branchEmployeeDao.queryBuilder();
        cond.where(BranchEmployeeDao.Properties.Empid.eq(saleidcode));
        List<BranchEmployee> emplist = cond.build().list();
        if (emplist.size() != 0) {
            return emplist.get(0).getEmpName();
        }
        return "不存在";
    }

    public static List<SalePayMode> getAllSalesPayment() {
        SalePayModeDao salePayModeDao = DBConnect.getInstances().getDaoSession().getSalePayModeDao();
        QueryBuilder cond = salePayModeDao.queryBuilder();
//        cond.where(BranchEmployeeDao.Properties.Empid.eq(saleidcode));
        List<SalePayMode> salePayModeList = cond.build().list();
        return salePayModeList;
    }

    public static List<SalePayMode> getSalesPaymentForUpload(HashMap device) {
        SalePayModeDao salePayModeDao = DBConnect.getInstances().getDaoSession().getSalePayModeDao();
        QueryBuilder cond = salePayModeDao.queryBuilder();
        cond.where(SalePayModeDao.Properties.SourceId.eq(device.get("sourceid")),
                SalePayModeDao.Properties.Id.gt(device.get("max")));
        List<SalePayMode> salePayModeList = cond.build().list();
        return salePayModeList;
    }


    public static List<SaleDaily> getSalesDailyUpload(List<SalePayMode> salePayModeList) {
        List<SaleDaily> saleDailyList = new ArrayList<>();
        SaleDailyDao saleDailyDao = DBConnect.getInstances().getDaoSession().getSaleDailyDao();

        for (int i = 0; i < salePayModeList.size(); i++) {
            QueryBuilder cond = saleDailyDao.queryBuilder();
//            cond.where(SaleDailyDao.Properties.SaleId.eq(salePayModeList.get(i).getSaleId()));
            cond.where(SaleDailyDao.Properties.OrderInnerId.eq(salePayModeList.get(i).getOrderInnerId()));
            List<SaleDaily> subset = cond.build().list();
            for (int j = 0; j < subset.size(); j++) {
                saleDailyList.add(subset.get(j));
            }
        }


        return saleDailyList;
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
        rt_salepaymode.setIsReturn(true);
        rt_salepaymode.setSalerId(salePayMode.getSalerId());
        rt_salepaymode.setSourceId(salePayMode.getSourceId());
        rt_salepaymode.setOrderInnerId(GetTransInnerID());
        return rt_salepaymode;
    }

    public static void InsertSalePayMode(SalePayMode salePayMode) {
        SalePayModeDao salePayModeDao = DBConnect.getInstances().getDaoSession().getSalePayModeDao();
        salePayModeDao.insert(salePayMode);
    }

    public static void UpdateSalePayMode(SalePayMode salePayMode) {
        SalePayModeDao salePayModeDao = DBConnect.getInstances().getDaoSession().getSalePayModeDao();
        salePayModeDao.update(salePayMode);
    }


    public static void InsertSaleDaily(List<SaleDaily> saleDailyList) {
        SaleDailyDao saleDailyDao = DBConnect.getInstances().getDaoSession().getSaleDailyDao();
        for (int i = 0; i < saleDailyList.size(); i++) {
            saleDailyDao.insert(saleDailyList.get(i));
        }
    }

    public static void UpdateSaleDailyForRetrun(List<SaleDaily> saleDailyList,SalePayMode salePaymode) {
        SaleDailyDao saleDailyDao = DBConnect.getInstances().getDaoSession().getSaleDailyDao();
        for (int i = 0; i < saleDailyList.size(); i++) {
            saleDailyList.get(i).setIsReturn(true);
            saleDailyList.get(i).setOrderInnerId(salePaymode.getOrderInnerId());
            saleDailyDao.update(saleDailyList.get(i));
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
            tmp_saledaily.setOrderInnerId(salePayMode.getOrderInnerId());
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
            tmp_saledaily.setSaleType(saleDailyList.get(i).getSaleType());
            tmp_saledaily.setSaleQty(tmp_saleqty);
            tmp_saledaily.setSaleAmt(tmp_saleamt);
            tmp_saledaily.setNormalPrice(tmp_NormalPrice);
            tmp_saledaily.setCurPrice(tmp_CurPrice);
            tmp_saledaily.setIsReturn(true);
            tmp_saledaily.setSourceId(saleDailyList.get(i).getSourceId());
            tmp_saledaily.setSalerId(saleDailyList.get(i).getSalerId());

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

    public static Integer getRecordLocalProduct() {
        String sql = null;
        sql = "select count(*) from product;";
        return exce_sql(sql);
    }

    public static Integer getRecordLocalProductBarcode() {
        String sql = null;
        sql = "select count(*) from product_bar_code;";
        return exce_sql(sql);
    }

    public static Integer getRecordLocalProductBranchRel() {
        String sql = null;
        sql = "select count(*) from product_branch_rel;";
        return exce_sql(sql);
    }


       public static Integer getRecordLocalSaleDaily() {
        String sql = null;
        sql = "select count(distinct sale_id) from sale_daily ;";
        return exce_sql(sql);
    }

    public static Integer getRecordLocalPmtDMBranch() {
        String sql = null;
        sql = "SELECT count(*)\n" +
                "  FROM PMT_DM_REL;";
        return exce_sql(sql);
    }


    //Common Tools
    private static Integer exce_sql(String sql) {
        Integer records = 0;
        Cursor result = null;
        try {
            result = DBConnect.getInstances().getDaoSession().getDatabase().rawQuery(sql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result != null) {
            while (result.moveToNext()) {
                records = result.getInt(0);
            }
        }
        return records;
    }


}

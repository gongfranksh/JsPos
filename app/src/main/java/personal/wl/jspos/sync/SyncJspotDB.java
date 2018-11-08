package personal.wl.jspos.sync;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import personal.wl.jspos.db.DBC2Jspot;
import personal.wl.jspos.db.DBConnect;
import personal.wl.jspos.db.IReportBack;
import personal.wl.jspos.db.Utils;
import personal.wl.jspos.pos.Branch;
import personal.wl.jspos.pos.BranchDao;
import personal.wl.jspos.pos.BranchEmployee;
import personal.wl.jspos.pos.BranchEmployeeDao;
import personal.wl.jspos.pos.DaoSession;
import personal.wl.jspos.pos.PmtDmRel;
import personal.wl.jspos.pos.PmtDmRelDao;
import personal.wl.jspos.pos.Product;
import personal.wl.jspos.pos.ProductBarCode;
import personal.wl.jspos.pos.ProductBarCodeDao;
import personal.wl.jspos.pos.ProductBranchRel;
import personal.wl.jspos.pos.ProductBranchRelDao;
import personal.wl.jspos.pos.ProductDao;
import personal.wl.jspos.pos.SaleDaily;
import personal.wl.jspos.pos.SalePayMode;

import static personal.wl.jspos.MainActivity.PROCESS_STEPS;
import static personal.wl.jspos.db.Tools.Double2String;
import static personal.wl.jspos.db.Tools.Long2String;
import static personal.wl.jspos.method.PosHandleDB.CheckDeviceByLocal;
import static personal.wl.jspos.method.PosHandleDB.InsertDeviceByLocal;
import static personal.wl.jspos.method.PosHandleDB.getSalesDailyUpload;
import static personal.wl.jspos.method.PosHandleDB.getSalesPaymentForUpload;

public class SyncJspotDB extends AsyncTask<String, Integer, Integer>
        implements DialogInterface.OnCancelListener {
    private static final String TAG = "SyncJspotDB";


    @Override
    public void onCancel(DialogInterface dialog) {
        // TODO Auto-generated method stub
        report.reportBack(tag, "Cancel Called");
    }


    private IReportBack report = null;
    private Context context = null;
    private String tag = null;
    private ProgressDialog pd = null;
    private HashMap device = new HashMap<String, String>();
    private int num = PROCESS_STEPS + 1;

    private static DaoSession getDaosession() {
        return daosession;
    }

    private static void setDaosession(DaoSession daosession) {
        SyncJspotDB.daosession = daosession;
    }


    private static DaoSession daosession;
    private int length = 0;

    public SyncJspotDB(IReportBack inr, Context inCont, String inTag, int inLen, HashMap device) {
        report = inr;
        context = inCont;
        tag = inTag;
        length = inLen;
        this.device = device;
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        //super.onPreExecute();
        Utils.logThreadSignature(tag);
        pd = new ProgressDialog(context);
        pd.setTitle("同步基础资料");
        pd.setMessage("In progressing");
        pd.setCancelable(true);
        pd.setOnCancelListener(this);
        pd.setIndeterminate(false);//
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMax(length);
        pd.show();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        // TODO Auto-generated method stub
        //super.onProgressUpdate(values);

        Integer i = values[0];
        report.reportBack(tag, "Progress: i = " + i);
        switch (i) {
            case 0:
                pd.setMessage("开始同步");
                break;


            case 1:
                pd.setMessage("检查设备是否授权");
                break;

            case 2:
                pd.setMessage("操作员同步....");
                break;

            case 3:
                pd.setMessage("Branch同步....");
                break;

            case 4:
                pd.setMessage("Product同步....");
                break;

            case 5:
                pd.setMessage("BarCode同步....");
                break;

            case 6:
                pd.setMessage("ProductBranch同步....");
                break;
            case 7:
                pd.setMessage("DM同步....");
                break;

            case 9:
                pd.setMessage("设备未注册不能同步");
                break;
            default:
                break;
        }

        pd.setProgress(i + 1);
    }


    @Override
    protected void onPostExecute(Integer result) {
        // TODO Auto-generated method stub
        //super.onPostExecute(result);
        report.reportBack(tag, "result: i = " + result);
        if (result.equals(num + 1)) {
            report.reportBack(tag, "设备未注册");
        }
        pd.cancel();

    }


    @Override
    protected Integer doInBackground(String... params) {
        // TODO SYNC Data do in backgroud
//        int num = params.length;
//        int num = 7;
        int i = 0;
        Integer rec = 0;

        i = i + 1;
        //Open MsSql Server
        DBC2Jspot js = new DBC2Jspot();
        //Open Local SqlLite DB
        DaoSession daoSession = DBConnect.getInstances().getDaoSession();
        this.setDaosession(daoSession);
        Utils.sleepForSecs(2);
        publishProgress(i);


        i = i + 1;
        //Check Device id
        if (js.CheckDeviceByServer(device)) {
            List devicelist = js.getCheckDeviceThisFromServer(device);
            if (!CheckDeviceByLocal(device)) {
                InsertDeviceByLocal(devicelist);
            } else {
                //已经插入本地数据库;
            }
            this.device.put("sourceid", (Integer) ((HashMap) devicelist.get(0)).get("sourceid"));

        } else {
            //授权失败退出不下载数据
            publishProgress(num + 1);
            Utils.sleepForSecs(2);
            return num + 1;

        }


        //Process branchEmployee Update--Begin
        i = i + 1;
        rec = getBranchEmployeeTimeStamp();
        List branchemployee = js.getBranchEmployeeNeedUpdate(rec);
        this.ProcessBranchEmployee2LocalDB(branchemployee);
        Utils.sleepForSecs(2);
        publishProgress(i);

        //Process branch Update--Begin
        i = i + 1;
        List branch = js.getBranch();
        this.ProcessBranch2LocalDB(branch);
        Utils.sleepForSecs(2);
        publishProgress(i);

//----------
//        //Process Prompt DM
//        i = i + 1;
//        rec = getPmtDMBranchRelTimeStamp(device);
//        List pmtdmbranchrel = js.getPmtDMBranchRelNeedUpdate(rec, device);
//        ProcessPmtDMBranch2LocalDB(pmtdmbranchrel);
//-------------



        //Process Product Update--Begin
        i = i + 1;
        rec = getProductTimeStamp();
        List productjs = js.getProductNeedUpdate(rec);
        this.ProcessProduct2LocalDB(productjs);
        Utils.sleepForSecs(2);
        publishProgress(i);
        //Process Product Update--End


        //Process ProductBarcode
        i = i + 1;
        rec = getProductBarCodeTimeStamp();
        List productcode = js.getProductBarCodeNeedUpdate(rec);
        this.ProcessProductBarCode2LocalDB(productcode);
        Utils.sleepForSecs(2);
        publishProgress(i);


        //Process ProductBranch
        i = i + 1;
        rec = getProductBranchTimeStamp(device);
        List productbranchrel = js.getProductBranchRelNeedUpdate(rec, device);
        ProcessProductBranch2LocalDB(productbranchrel);

        Utils.sleepForSecs(2);
        publishProgress(i);



        //Process Prompt DM
        i = i + 1;
        rec = getPmtDMBranchRelTimeStamp(device);
        List pmtdmlist = js.getPmtDMBranchRelNeedUpdate(rec, device);
        ProcessPmtDMBranch2LocalDB(pmtdmlist);

        Utils.sleepForSecs(2);
        publishProgress(i);

        //Upload Sale Transcation
        i = i + 1;
        long maxnumber = js.getLastUploadTranscations(device);
        device.put("max", maxnumber);
        List<SalePayMode> needupdatesalepaymode = getSalesPaymentForUpload(device);
        js.LastUploadTranscations(needupdatesalepaymode);
        List<SaleDaily> needuploadsalesdailylist = getSalesDailyUpload(needupdatesalepaymode);
        js.InSertMobileSaleDaily(needuploadsalesdailylist);
        js.UploadMobileDeviceLogId(needupdatesalepaymode);


        Utils.sleepForSecs(2);
        publishProgress(i);


        i = i + 1;
        js.closeConnection();
        publishProgress(i);


/*
        for (int i = 0; i < num; i++) {
            Utils.sleepForSecs(2);
            publishProgress(i);
        }
*/
        return num;
    }

    private void ProcessProductBarCode2LocalDB(List processtask) {
        ProductBarCodeDao productBarCodeDao = this.getDaosession().getProductBarCodeDao();
        for (int i = 0; i < processtask.size(); i++) {
            ProductBarCode pr = new ProductBarCode();
            HashMap item = (HashMap) processtask.get(i);
            List<ProductBarCode> needupdated = getProductBarCodeByCode((String) item.get("barcode"));
            if (needupdated.size() != 0) {
                for (int j = 0; j < needupdated.size(); j++) {
                    ProductBarCode findproduct = needupdated.get(j);
                    findproduct = setProductBarCodeRecord(findproduct, item);
                    Log.i(TAG, "Update ProductBarCode -->" + item.get("proid") + "--->" + item.get("barcode"));
                    productBarCodeDao.update(findproduct);
                }
            } else {
                Log.i(TAG, "Create ProductBarCode -->" + item.get("proid") + "--->" + item.get("barcode"));
                System.out.print(item.get("ProId"));
                pr = setProductBarCodeRecord(pr, item);
                productBarCodeDao.insert(pr);
            }
            pr = null;
        }

    }

    private ProductBarCode setProductBarCodeRecord(ProductBarCode productBarCode, HashMap item) {
        productBarCode.setProid((String) item.get("proid"));
        productBarCode.setBarcode((String) item.get("barcode"));
        productBarCode.setMainFlag((String) item.get("mainflag"));
        productBarCode.setNormalPrice(Double2String(item.get("normalprice").toString()));
        productBarCode.setTimeStamp(Long2String(item.get("timestamp").toString()));
        return productBarCode;
    }

    private List<ProductBarCode> getProductBarCodeByCode(String barcode) {
        ProductBarCodeDao productBarCodeDao = this.getDaosession().getProductBarCodeDao();
        QueryBuilder cond = productBarCodeDao.queryBuilder();
        List<ProductBarCode> returnproductbarcode = cond.where(ProductBarCodeDao.Properties.BarMode.eq(barcode)).build().list();
        return returnproductbarcode;

    }


    private void ProcessBranch2LocalDB(List processtask) {
        BranchDao branchDao = this.getDaosession().getBranchDao();
        for (int i = 0; i < processtask.size(); i++) {
            Branch branch = new Branch();
            HashMap item = (HashMap) processtask.get(i);
            List<Branch> needupdated = getBranchById((String) item.get("braid"));
            if (needupdated.size() != 0) {
                for (int j = 0; j < needupdated.size(); j++) {
                    Branch find = needupdated.get(j);
                    find = setBranchRecord(find, item);
                    Log.i(TAG, "Update branch -->" + item.get("braid") + "--->" + item.get("brasname"));
                    branchDao.update(find);
                }
            } else {
                Log.i(TAG, "Create branch -->" + item.get("braid") + "--->" + item.get("brasname"));
                System.out.print(item.get("braid"));
                branch = setBranchRecord(branch, item);
                branchDao.insert(branch);
            }
            branch = null;
        }
    }


    private void ProcessProductBranch2LocalDB(List processtask) {
        ProductBranchRelDao productBranchRelDao = this.getDaosession().getProductBranchRelDao();
        for (int i = 0; i < processtask.size(); i++) {
            ProductBranchRel productBranchRel = new ProductBranchRel();
            HashMap item = (HashMap) processtask.get(i);
            String tmp_proid = (String) item.get("proid");
            List<ProductBranchRel> needupdated = getProductBranchRelByProId(tmp_proid, device);

            if (needupdated.size() != 0) {
                for (int j = 0; j < needupdated.size(); j++) {
                    ProductBranchRel findpbr = needupdated.get(j);
                    findpbr = setProductBranchRelRecord(findpbr, item);
                    Log.i(TAG, "Update ProductBranchRel -->" + item.get("proid") + "--->" + item.get("proid"));
                    productBranchRelDao.update(findpbr);
                }
            } else {
                Log.i(TAG, "Create ProductBranchRel -->" + item.get("proid") + "--->" + item.get("proid"));
                System.out.print(item.get("proid"));
                productBranchRel = setProductBranchRelRecord(productBranchRel, item);
                productBranchRelDao.insert(productBranchRel);
            }
//            productBranchRel = null;
        }
    }

    private void ProcessPmtDMBranch2LocalDB(List processtask) {
        PmtDmRelDao pmtDmRelDao = this.getDaosession().getPmtDmRelDao();
        for (int i = 0; i < processtask.size(); i++) {
            PmtDmRel pmtDmRel = new PmtDmRel();
            HashMap item = (HashMap) processtask.get(i);
            String tmp_proid = (String) item.get("ProId");
            List<PmtDmRel> needupdated = getPmtDMBranchRelByProId(tmp_proid, device);

            if (needupdated.size() != 0) {
                for (int j = 0; j < needupdated.size(); j++) {
                    PmtDmRel findpbr = needupdated.get(j);
                    findpbr = setPmtDMBranchRelRecord(findpbr, item);
                    Log.i(TAG, "ProcessPmtDMBranch2LocalDB Update PMTDMRL -->" + item.get("ProId") + "--->" + item.get("ProId"));
                    pmtDmRelDao.update(findpbr);
                }
            } else {
                Log.i(TAG, "ProcessPmtDMBranch2LocalDB Create  PMTDMRL  -->" + item.get("ProId") + "--->" + item.get("ProId"));
                System.out.print(item.get("ProId"));
                pmtDmRel = setPmtDMBranchRelRecord(pmtDmRel, item);
                pmtDmRelDao.insert(pmtDmRel);
            }
//            pmtDmRelDao = null;
        }
    }


    private List<Branch> getBranchById(String branchid) {
        BranchDao branchDao = this.getDaosession().getBranchDao();
        QueryBuilder cond = branchDao.queryBuilder();
        List<Branch> returnBranch = cond.where(BranchDao.Properties.Braid.eq(branchid)).build().list();
        return returnBranch;
    }


    private Branch setBranchRecord(Branch branch, HashMap item) {
        branch.setBraid((String) item.get("braid"));
        branch.setBraname((String) item.get("braname"));
        branch.setBrasname((String) item.get("brasname"));
        branch.setBratype((String) item.get("bratype"));
        branch.setStatus((String) item.get("status"));
        return branch;
    }

    private void ProcessBranchEmployee2LocalDB(List processtask) {
        BranchEmployeeDao branchEmployeeDao = this.getDaosession().getBranchEmployeeDao();
        for (int i = 0; i < processtask.size(); i++) {
            BranchEmployee branchEmployee = new BranchEmployee();
            HashMap item = (HashMap) processtask.get(i);
            List<BranchEmployee> needupdated = getBranchEmployeeById((String) item.get("braid"), (String) item.get("empid"));
            if (needupdated.size() != 0) {
                for (int j = 0; j < needupdated.size(); j++) {
                    BranchEmployee find = needupdated.get(j);
                    find = setBranchEmployeeRecord(find, item);
                    Log.i(TAG, "Update branchEmployee -->" + item.get("braid") + "--->" + item.get("empid"));
                    branchEmployeeDao.update(find);
                }
            } else {
                Log.i(TAG, "Create branchEmployee -->" + item.get("braid") + "--->" + item.get("empid"));
                System.out.print(item.get("ProId"));
                branchEmployee = setBranchEmployeeRecord(branchEmployee, item);
                branchEmployeeDao.insert(branchEmployee);
            }
            branchEmployee = null;
        }
    }

    private BranchEmployee setBranchEmployeeRecord(BranchEmployee find, HashMap item) {
        find.setBraid((String) item.get("braid"));
        find.setEmpid((String) item.get("empid"));
        find.setEmpName((String) item.get("empname"));
        find.setStatus((String) item.get("status"));
        find.setPassword((String) item.get("password"));
        find.setDiscount(Double2String(item.get("discount").toString()));
        find.setTimestamp(Long2String(item.get("timestamp").toString()));
        return find;
    }

    //Common Tools
    private Integer returnTimeStamp(String sql) {
        Integer returncode = 0;
        Cursor result = null;
        try {
            result = this.getDaosession().getDatabase().rawQuery(sql, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result != null) {
            while (result.moveToNext()) {
                returncode = result.getInt(0);
            }
        }
        return returncode;
    }


    //Product Need Function
    private void ProcessProduct2LocalDB(List processtask) {
        ProductDao productDao = this.getDaosession().getProductDao();
        for (int i = 0; i < processtask.size(); i++) {
            Product pr = new Product();
            HashMap item = (HashMap) processtask.get(i);
            List<Product> needupdated = getProductByProId((String) item.get("ProId"));
            if (needupdated.size() != 0) {
                for (int j = 0; j < needupdated.size(); j++) {
                    Product findproduct = needupdated.get(j);
                    findproduct = setProductRecord(findproduct, item);
                    Log.i(TAG, "Update Product -->" + item.get("ProId") + "--->" + item.get("ProName"));
                    productDao.update(findproduct);
                }
            } else {
                Log.i(TAG, "Create Product -->" + item.get("ProId") + "--->" + item.get("ProName"));
                System.out.print(item.get("ProId"));
                pr = setProductRecord(pr, item);
                productDao.insert(pr);
            }
            pr = null;
        }
    }

    private List<BranchEmployee> getBranchEmployeeById(String braid, String empid) {
        BranchEmployeeDao branchEmployeeDao = this.getDaosession().getBranchEmployeeDao();
        QueryBuilder cond = branchEmployeeDao.queryBuilder();
        cond.where(BranchEmployeeDao.Properties.Braid.eq(braid),
                BranchEmployeeDao.Properties.Empid.eq(empid)).build();
        List<BranchEmployee> returnBranchEmployee = cond.list();
        return returnBranchEmployee;
    }


    private List<Product> getProductByProId(String proid) {
        ProductDao productDao = this.getDaosession().getProductDao();
        QueryBuilder cond = productDao.queryBuilder();
        List<Product> returnProduct = cond.where(ProductDao.Properties.Proid.eq(proid)).build().list();
        return returnProduct;
    }

    private List<ProductBranchRel> getProductBranchRelByProId(String proid, HashMap device) {
        ProductBranchRelDao productBranchRelDao = this.getDaosession().getProductBranchRelDao();
        QueryBuilder cond = productBranchRelDao.queryBuilder();
        cond.where(ProductBranchRelDao.Properties.Proid.eq(proid),
                ProductBranchRelDao.Properties.Braid.eq(device.get("braid")));
        List<ProductBranchRel> returnProductBrachRel = cond.build().list();
        return returnProductBrachRel;
    }

    private List<PmtDmRel> getPmtDMBranchRelByProId(String proid, HashMap device) {
        PmtDmRelDao pmtDmRelDao = this.getDaosession().getPmtDmRelDao();
        QueryBuilder cond = pmtDmRelDao.queryBuilder();
        cond.where(PmtDmRelDao.Properties.Proid.eq(proid),
                PmtDmRelDao.Properties.Braid.eq(device.get("braid")));
        List<PmtDmRel> returnProductBrachRel = cond.build().list();
        return returnProductBrachRel;
    }


    private Integer getProductTimeStamp() {
        Integer returncode = 0;
        String sql = "SELECT max(\n" +
                "       TIME_STAMP) as timestamp\n" +
                "  FROM PRODUCT;\n";
        returncode = returnTimeStamp(sql);
        return returncode;
    }

    private Integer getBranchEmployeeTimeStamp() {
        Integer returncode = 0;
        String sql = "SELECT max(\n" +
                "       timestamp) as timestamp\n" +
                "  FROM branch_employee;\n";
        returncode = returnTimeStamp(sql);
        return returncode;
    }

    private Integer getProductBranchTimeStamp(HashMap device) {
        Integer returncode = 0;
        String sql = "SELECT max(\n" +
                "       time_stamp) as timestamp\n" +
                "  FROM PRODUCT_BRANCH_REL;\n" +
                "  where BRAID='" + device.get("braid") + "'";
        returncode = returnTimeStamp(sql);
        return returncode;
    }

    private Integer getProductBarCodeTimeStamp() {
        Integer returncode = 0;
        String sql = "SELECT max(TIME_STAMP)\n" +
                "  FROM PRODUCT_BAR_CODE";
        returncode = returnTimeStamp(sql);
        return returncode;
    }

    private Integer getPmtDMBranchRelTimeStamp(HashMap device) {
        Integer returncode = 0;
        String sql = "SELECT max(TIME_STAMP)\n" +
                "  FROM PMT_DM_REL" +
                "  where BRAID='" + device.get("braid") + "'";
        returncode = returnTimeStamp(sql);
        return returncode;
    }


    private Product setProductRecord(Product pr, HashMap item) {
        pr.setProid((String) item.get("ProId"));
        pr.setProName((String) item.get("ProName"));
        pr.setProSName((String) item.get("ProSName"));
        pr.setBarcode((String) item.get("Barcode"));
        pr.setSupId((String) item.get("SupId"));
        pr.setClassId((String) item.get("ClassId"));
        pr.setBrandId((String) item.get("BrandId"));
        pr.setSpec((String) item.get("Spec"));
        pr.setStatus((String) item.get("Status"));
        pr.setInTax(Double2String(item.get("InTax").toString()));
        pr.setNormalPrice(Double2String(item.get("NormalPrice").toString()));
        pr.setPosdiscount(Double2String(item.get("posdiscount").toString()));
        pr.setTimeStamp(Long2String(item.get("timestamp").toString()));
        return pr;
    }

    //Product Need Function end
    private ProductBranchRel setProductBranchRelRecord(ProductBranchRel pbr, HashMap item) {
        pbr.setProid((String) item.get("proid"));
        pbr.setStatus((String) item.get("status"));
        pbr.setBraid((String) item.get("braid"));
        pbr.setPromtFlag((String) item.get("promtflag"));
        pbr.setNormalPrice(Double2String(item.get("normalprice").toString()));
        pbr.setTimeStamp(Long2String(item.get("timestamp").toString()));
        return pbr;
    }

    //Product Need Function end
    private PmtDmRel setPmtDMBranchRelRecord(PmtDmRel pdr, HashMap item) {

        Date begin = (Date) item.get("DMBeginDate");
        pdr.setDMBeginDate(begin);

        Date end = (Date) item.get("DMEndDate");
        pdr.setDMEndDate(end);


        pdr.setBraid((String) item.get("BraId"));
        pdr.setDMId((String) item.get("DMId"));
        pdr.setSupId((String) item.get("SupId"));
        pdr.setProid((String) item.get("ProId"));
        pdr.setOrigSalePrice(Double2String(item.get("OrigSalePrice").toString()));
        pdr.setSalePrice(Double2String(item.get("SalePrice").toString()));
        pdr.setTimeStamp(Long2String(item.get("timestamp").toString()));
        return pdr;
    }


}

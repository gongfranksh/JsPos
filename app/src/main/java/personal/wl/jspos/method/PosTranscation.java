package personal.wl.jspos.method;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

import org.greenrobot.greendao.query.QueryBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import personal.wl.jspos.db.DBConnect;
import personal.wl.jspos.pos.MobileDevice;
import personal.wl.jspos.pos.Product;
import personal.wl.jspos.pos.SaleDaily;
import personal.wl.jspos.pos.SaleDailyDao;
import personal.wl.jspos.pos.SalePayMode;
import personal.wl.jspos.pos.SalePayModeDao;

import static personal.wl.jspos.db.Tools.dateCompare;
import static personal.wl.jspos.method.PosHandleDB.QueryMobileDevice;
import static personal.wl.jspos.method.PosHandleDB.QueryProductByCode;
import static personal.wl.jspos.method.PosPayMent.PAYMENT_ALIPAY;
import static personal.wl.jspos.method.PosPayMent.PAYMENT_ALIPAY_CODE;
import static personal.wl.jspos.method.PosPayMent.PAYMENT_CASH;
import static personal.wl.jspos.method.PosPayMent.PAYMENT_CASH_CODE;
import static personal.wl.jspos.method.PosPayMent.PAYMENT_WEIXIN;
import static personal.wl.jspos.method.PosPayMent.PAYMENT_WEIXIN_CODE;

public class PosTranscation {
    private PosTabInfo posTabInfo;
    private Context context;
    private String tmp_branch;
    private String tmp_posmachine;
    private String tmp_saleid;
    private String tmp_deviceid;
    private String tmp_transinnerid;
    private Long tmp_sourceid;
    private Date tmp_datetime;
    private Boolean needprint;
    private HashMap generate_saleid_para = new HashMap<String, String>();
    private BluetoothDevice blueprinter;
    private PosPrinter posprinter;
    private String TAG="PosTranscation";


    public PosTranscation(Context context) {
        this.context = context;
        this.posTabInfo = new PosTabInfo(this.context);
        this.tmp_branch = posTabInfo.getBranchCode();
        this.tmp_posmachine = posTabInfo.getPosMachine();
        this.tmp_deviceid = posTabInfo.getDeviceid();
        this.tmp_datetime = new Date();
        this.needprint = posTabInfo.getNeedPrint();
//        HashMap generate_saleid_para =
        generate_saleid_para.put("branch", tmp_branch);
        generate_saleid_para.put("posmachine", tmp_posmachine);
        generate_saleid_para.put("datetime", this.tmp_datetime);
        generate_saleid_para.put("posno", this.tmp_posmachine);
        generate_saleid_para.put("deviceid", this.tmp_deviceid);
        this.tmp_saleid = this.getSaleId(generate_saleid_para);
        this.tmp_transinnerid = DeviceUtils.GetTransInnerID();
    }

    public String getTranscationId() {
        return this.tmp_saleid;
    }

    public void SaleTranstion(List<SaleDaily> saleDailyList, int pay) {

        List<MobileDevice> sourcelist = QueryMobileDevice(generate_saleid_para);
        List<SalePayMode> salePayModeList = new ArrayList<>();
        Log.i(TAG, "SaleTranstion:==> "+salePayModeList.size());
        if (sourcelist.size() != 0) {
            tmp_sourceid = sourcelist.get(0).getSourceId();
        }
        Log.i(TAG, "SaleTranstion:==>tmp_sourceid "+tmp_sourceid);

        SaleDailyDao saleDailyDao = DBConnect.getInstances().getDaoSession().getSaleDailyDao();
        double totalamt = 0;
        for (int i = 0; i < saleDailyList.size(); i++) {
            totalamt = totalamt + saleDailyList.get(i).getSaleAmt();
            List<Product> tmp_product = QueryProductByCode(saleDailyList.get(i).getProId());
            saleDailyList.get(i).setPosNo(this.tmp_posmachine);
            saleDailyList.get(i).setBraid(this.tmp_branch);

            saleDailyList.get(i).setIsPmt("0");
            saleDailyList.get(i).setIsTimePrompt("0");
            saleDailyList.get(i).setSaleType("0");
            saleDailyList.get(i).setSaleDate(new Date());
            saleDailyList.get(i).setClassId(tmp_product.get(0).getClassId());
            saleDailyList.get(i).setNormalPrice(tmp_product.get(0).getNormalPrice());
            saleDailyList.get(i).setSaleTax(tmp_product.get(0).getInTax());
            saleDailyList.get(i).setSaleId(this.tmp_saleid);
            saleDailyList.get(i).setOrderInnerId(this.tmp_transinnerid);
            saleDailyList.get(i).setSourceId(this.tmp_sourceid);
            saleDailyList.get(i).setIsReturn(false);
            switch (pay) {
                case PAYMENT_CASH:
                    saleDailyList.get(i).setCash1(saleDailyList.get(i).getSaleAmt());
                    break;
                case PAYMENT_ALIPAY:
                    saleDailyList.get(i).setCash7(saleDailyList.get(i).getSaleAmt());
                    break;
                case PAYMENT_WEIXIN:
                    saleDailyList.get(i).setCash8(saleDailyList.get(i).getSaleAmt());
                    break;
            }
            saleDailyDao.insert(saleDailyList.get(i));
        }


        SalePayModeDao salePayModeDao = DBConnect.getInstances().getDaoSession().getSalePayModeDao();
        SalePayMode salePayMode = new SalePayMode();
        salePayMode.setBraid(this.tmp_branch);
        salePayMode.setSaleDate(this.tmp_datetime);
        salePayMode.setSaleId(this.tmp_saleid);
        salePayMode.setOrderInnerId(this.tmp_transinnerid);
        salePayMode.setSourceId(this.tmp_sourceid);
        salePayMode.setIsReturn(false);
        salePayMode.setSalerId(posTabInfo.getSalerId());
        salePayMode.setPayMoney(totalamt);
        switch (pay) {
            case PAYMENT_CASH:
                salePayMode.setPayModeId(String.valueOf(PAYMENT_CASH_CODE));
                break;
            case PAYMENT_ALIPAY:
                salePayMode.setPayModeId(String.valueOf(PAYMENT_ALIPAY_CODE));
                break;
            case PAYMENT_WEIXIN:
                salePayMode.setPayModeId(String.valueOf(PAYMENT_WEIXIN_CODE));
                break;
        }
        salePayModeDao.insert(salePayMode);
        salePayModeList.add(salePayMode);

        if (posTabInfo.getNeedPrint()) {
            //增加print部分内容
            posprinter = new PosPrinter(context, true);
            blueprinter = posprinter.getPosPrinter();
            posprinter.connect(blueprinter, saleDailyList, salePayModeList, true);
        }

    }


    public String getSaleId(HashMap para) {
        String tmpsaleid = null;
        SimpleDateFormat sf1 = new SimpleDateFormat("yyMMdd");
        SaleDailyDao tmp_dao = DBConnect.getInstances().getDaoSession().getSaleDailyDao();
        QueryBuilder cond = tmp_dao.queryBuilder();
        cond.where(SaleDailyDao.Properties.Braid.eq(para.get("branch")),
                SaleDailyDao.Properties.SaleDate.le(para.get("datetime")),
                SaleDailyDao.Properties.PosNo.eq(para.get("posmachine")),
                SaleDailyDao.Properties.IsReturn.eq(false));
        cond.orderDesc(SaleDailyDao.Properties.Id);
        cond.limit(1);
        List<SaleDaily> lastercord = cond.build().list();

        Boolean isBegin = true;
        if (lastercord.size() == 0) {
            isBegin = true;
        } else {
            Date date = lastercord.get(0).getSaleDate();
            int diff_day = dateCompare((Date) para.get("datetime"), date);
            if (diff_day > 0) {
                isBegin = true;
            } else {
                isBegin = false;
            }

        }


        int line = 10000;

        if (isBegin) {
            line = line + 1;
        } else {
            String lastSaleid = lastercord.get(0).getSaleId();

            if (lastSaleid != null) {
                int aleadyint = Integer.parseInt(lastSaleid.trim().substring(lastSaleid.length() - 4));
                line = line + aleadyint + 1;
            } else {
                line = line + 1;
            }
        }
        tmpsaleid = para.get("posmachine") + sf1.format((Date) para.get("datetime")) + Integer.toString(line).trim().substring(1, 5);
        return tmpsaleid;
    }


}

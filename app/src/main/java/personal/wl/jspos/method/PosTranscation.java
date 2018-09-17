package personal.wl.jspos.method;

import android.content.Context;

import java.util.Date;
import java.util.List;

import personal.wl.jspos.db.DBConnect;
import personal.wl.jspos.pos.Product;
import personal.wl.jspos.pos.SaleDaily;
import personal.wl.jspos.pos.SaleDailyDao;

import static personal.wl.jspos.method.PosHandleDB.QueryProductByCode;

public class PosTranscation {
    private PosTabInfo posTabInfo;
    private Context context;

    public PosTranscation(Context context) {
        this.context = context;
        this.posTabInfo = new PosTabInfo(this.context);
    }

    public void SaleTranstion(List<SaleDaily> saleDailyList){
        String tmp_branch = posTabInfo.getBranchCode();
        String tmp_posmachine= posTabInfo.getPosMachine();
        SaleDailyDao saleDailyDao = DBConnect.getInstances().getDaoSession().getSaleDailyDao();
        for (int i = 0; i < saleDailyList.size(); i++) {
            List<Product> tmp_product = QueryProductByCode(saleDailyList.get(i).getProId());
            saleDailyList.get(i).setPosNo(tmp_posmachine);
            saleDailyList.get(i).setBraid(tmp_branch);
            saleDailyList.get(i).setIsDM("0");
            saleDailyList.get(i).setIsPmt("0");
            saleDailyList.get(i).setIsTimePrompt("0");
            saleDailyList.get(i).setSaleType("0");
            saleDailyList.get(i).setIsDM("0");
            saleDailyList.get(i).setSaleDate(new Date());
            saleDailyList.get(i).setClassId(tmp_product.get(0).getClassId());
            saleDailyList.get(i).setNormalPrice(tmp_product.get(0).getNormalPrice());
            saleDailyList.get(i).setSaleTax(tmp_product.get(0).getInTax());



            saleDailyDao.insert(saleDailyList.get(i));
        }
    }
}

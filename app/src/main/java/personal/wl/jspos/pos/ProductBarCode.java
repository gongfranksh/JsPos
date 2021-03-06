package personal.wl.jspos.pos;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit.

/**
 * Entity mapped to table "PRODUCT_BAR_CODE".
 */
@Entity
public class ProductBarCode {

    @Id(autoincrement = true)
    private Long id;
    private String Proid;
    private String Barcode;
    private String BarMode;
    private Double Quantity;
    private Double NormalPrice;
    private Double MemberPrice;
    private Double Status;
    private String MainFlag;
    private Long TimeStamp;

    @Generated
    public ProductBarCode() {
    }

    public ProductBarCode(Long id) {
        this.id = id;
    }

    @Generated
    public ProductBarCode(Long id, String Proid, String Barcode, String BarMode, Double Quantity, Double NormalPrice, Double MemberPrice, Double Status, String MainFlag, Long TimeStamp) {
        this.id = id;
        this.Proid = Proid;
        this.Barcode = Barcode;
        this.BarMode = BarMode;
        this.Quantity = Quantity;
        this.NormalPrice = NormalPrice;
        this.MemberPrice = MemberPrice;
        this.Status = Status;
        this.MainFlag = MainFlag;
        this.TimeStamp = TimeStamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProid() {
        return Proid;
    }

    public void setProid(String Proid) {
        this.Proid = Proid;
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String Barcode) {
        this.Barcode = Barcode;
    }

    public String getBarMode() {
        return BarMode;
    }

    public void setBarMode(String BarMode) {
        this.BarMode = BarMode;
    }

    public Double getQuantity() {
        return Quantity;
    }

    public void setQuantity(Double Quantity) {
        this.Quantity = Quantity;
    }

    public Double getNormalPrice() {
        return NormalPrice;
    }

    public void setNormalPrice(Double NormalPrice) {
        this.NormalPrice = NormalPrice;
    }

    public Double getMemberPrice() {
        return MemberPrice;
    }

    public void setMemberPrice(Double MemberPrice) {
        this.MemberPrice = MemberPrice;
    }

    public Double getStatus() {
        return Status;
    }

    public void setStatus(Double Status) {
        this.Status = Status;
    }

    public String getMainFlag() {
        return MainFlag;
    }

    public void setMainFlag(String MainFlag) {
        this.MainFlag = MainFlag;
    }

    public Long getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(Long TimeStamp) {
        this.TimeStamp = TimeStamp;
    }

}

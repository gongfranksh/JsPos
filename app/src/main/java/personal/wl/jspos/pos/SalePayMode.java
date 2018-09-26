package personal.wl.jspos.pos;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit.

/**
 * Entity mapped to table "SALE_PAY_MODE".
 */
@Entity
public class SalePayMode {

    @Id(autoincrement = true)
    private Long id;
    private String Braid;
    private java.util.Date SaleDate;
    private String SaleId;
    private String SalerId;
    private String PayModeId;
    private Double PayMoney;
    private String CardType;
    private String CardNo;
    private Long SourceId;
    private Boolean IsReturn;

    @Generated
    public SalePayMode() {
    }

    public SalePayMode(Long id) {
        this.id = id;
    }

    @Generated
    public SalePayMode(Long id, String Braid, java.util.Date SaleDate, String SaleId, String SalerId, String PayModeId, Double PayMoney, String CardType, String CardNo, Long SourceId, Boolean IsReturn) {
        this.id = id;
        this.Braid = Braid;
        this.SaleDate = SaleDate;
        this.SaleId = SaleId;
        this.SalerId = SalerId;
        this.PayModeId = PayModeId;
        this.PayMoney = PayMoney;
        this.CardType = CardType;
        this.CardNo = CardNo;
        this.SourceId = SourceId;
        this.IsReturn = IsReturn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBraid() {
        return Braid;
    }

    public void setBraid(String Braid) {
        this.Braid = Braid;
    }

    public java.util.Date getSaleDate() {
        return SaleDate;
    }

    public void setSaleDate(java.util.Date SaleDate) {
        this.SaleDate = SaleDate;
    }

    public String getSaleId() {
        return SaleId;
    }

    public void setSaleId(String SaleId) {
        this.SaleId = SaleId;
    }

    public String getSalerId() {
        return SalerId;
    }

    public void setSalerId(String SalerId) {
        this.SalerId = SalerId;
    }

    public String getPayModeId() {
        return PayModeId;
    }

    public void setPayModeId(String PayModeId) {
        this.PayModeId = PayModeId;
    }

    public Double getPayMoney() {
        return PayMoney;
    }

    public void setPayMoney(Double PayMoney) {
        this.PayMoney = PayMoney;
    }

    public String getCardType() {
        return CardType;
    }

    public void setCardType(String CardType) {
        this.CardType = CardType;
    }

    public String getCardNo() {
        return CardNo;
    }

    public void setCardNo(String CardNo) {
        this.CardNo = CardNo;
    }

    public Long getSourceId() {
        return SourceId;
    }

    public void setSourceId(Long SourceId) {
        this.SourceId = SourceId;
    }

    public Boolean getIsReturn() {
        return IsReturn;
    }

    public void setIsReturn(Boolean IsReturn) {
        this.IsReturn = IsReturn;
    }

}

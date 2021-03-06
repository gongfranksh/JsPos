package personal.wl.jspos.pos;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit.

/**
 * Entity mapped to table "PRODUCT_BRANCH_REL".
 */
@Entity
public class ProductBranchRel {

    @Id(autoincrement = true)
    private Long id;
    private String Braid;
    private String Proid;
    private String SupId;
    private String CanChangePrice;
    private String SupPmtFlag;
    private String PromtFlag;
    private String PotFlag;
    private String Dcflag;
    private Double MinPrice;
    private Double MaxPrice;
    private Double NormalPrice;
    private Double MemberPrice;
    private Double ReturnRat;
    private String Status;
    private String Operatorid;
    private java.util.Date CreateDate;
    private java.util.Date UpdateDate;
    private java.util.Date stopdate;
    private Long TimeStamp;

    @Generated
    public ProductBranchRel() {
    }

    public ProductBranchRel(Long id) {
        this.id = id;
    }

    @Generated
    public ProductBranchRel(Long id, String Braid, String Proid, String SupId, String CanChangePrice, String SupPmtFlag, String PromtFlag, String PotFlag, String Dcflag, Double MinPrice, Double MaxPrice, Double NormalPrice, Double MemberPrice, Double ReturnRat, String Status, String Operatorid, java.util.Date CreateDate, java.util.Date UpdateDate, java.util.Date stopdate, Long TimeStamp) {
        this.id = id;
        this.Braid = Braid;
        this.Proid = Proid;
        this.SupId = SupId;
        this.CanChangePrice = CanChangePrice;
        this.SupPmtFlag = SupPmtFlag;
        this.PromtFlag = PromtFlag;
        this.PotFlag = PotFlag;
        this.Dcflag = Dcflag;
        this.MinPrice = MinPrice;
        this.MaxPrice = MaxPrice;
        this.NormalPrice = NormalPrice;
        this.MemberPrice = MemberPrice;
        this.ReturnRat = ReturnRat;
        this.Status = Status;
        this.Operatorid = Operatorid;
        this.CreateDate = CreateDate;
        this.UpdateDate = UpdateDate;
        this.stopdate = stopdate;
        this.TimeStamp = TimeStamp;
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

    public String getProid() {
        return Proid;
    }

    public void setProid(String Proid) {
        this.Proid = Proid;
    }

    public String getSupId() {
        return SupId;
    }

    public void setSupId(String SupId) {
        this.SupId = SupId;
    }

    public String getCanChangePrice() {
        return CanChangePrice;
    }

    public void setCanChangePrice(String CanChangePrice) {
        this.CanChangePrice = CanChangePrice;
    }

    public String getSupPmtFlag() {
        return SupPmtFlag;
    }

    public void setSupPmtFlag(String SupPmtFlag) {
        this.SupPmtFlag = SupPmtFlag;
    }

    public String getPromtFlag() {
        return PromtFlag;
    }

    public void setPromtFlag(String PromtFlag) {
        this.PromtFlag = PromtFlag;
    }

    public String getPotFlag() {
        return PotFlag;
    }

    public void setPotFlag(String PotFlag) {
        this.PotFlag = PotFlag;
    }

    public String getDcflag() {
        return Dcflag;
    }

    public void setDcflag(String Dcflag) {
        this.Dcflag = Dcflag;
    }

    public Double getMinPrice() {
        return MinPrice;
    }

    public void setMinPrice(Double MinPrice) {
        this.MinPrice = MinPrice;
    }

    public Double getMaxPrice() {
        return MaxPrice;
    }

    public void setMaxPrice(Double MaxPrice) {
        this.MaxPrice = MaxPrice;
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

    public Double getReturnRat() {
        return ReturnRat;
    }

    public void setReturnRat(Double ReturnRat) {
        this.ReturnRat = ReturnRat;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getOperatorid() {
        return Operatorid;
    }

    public void setOperatorid(String Operatorid) {
        this.Operatorid = Operatorid;
    }

    public java.util.Date getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(java.util.Date CreateDate) {
        this.CreateDate = CreateDate;
    }

    public java.util.Date getUpdateDate() {
        return UpdateDate;
    }

    public void setUpdateDate(java.util.Date UpdateDate) {
        this.UpdateDate = UpdateDate;
    }

    public java.util.Date getStopdate() {
        return stopdate;
    }

    public void setStopdate(java.util.Date stopdate) {
        this.stopdate = stopdate;
    }

    public Long getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(Long TimeStamp) {
        this.TimeStamp = TimeStamp;
    }

}

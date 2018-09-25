package personal.wl.greendao;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class DaoMaker {

    public static void main(String[] args) {


        try {
            Schema schema = new Schema(3, "personal.wl.jspos.pos");
            CreateAllSchema(schema);

            new DaoGenerator().generateAll(schema, "H:\\mobile\\jspos\\app\\src\\main\\java\\");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void CreateAllSchema(Schema schema) {
        CreateTableProduct(schema);
        CreateTableProductBarcode(schema);
        CreateTableProductBranchRel(schema);
        CreateTableProductBarcodeBranchRel(schema);
        CreateTableProductClass(schema);
        CreateTableSaleDaily(schema);
        CreateTableSalePayMode(schema);
        CreateTableBranch(schema);
        CreateTableBranchEmployee(schema);
        CreateTablePosMachine(schema);
        CreateTableMobileDevice(schema);
    }

    private static void CreateTableProduct(Schema schema) {
        Entity entity = schema.addEntity("Product");
        entity.addIdProperty().autoincrement();
        entity.addStringProperty("Proid");
        entity.addStringProperty("Barcode");
        entity.addStringProperty("ProName");
        entity.addStringProperty("ProSName");
        entity.addStringProperty("ClassId");
        entity.addStringProperty("Spec");
        entity.addStringProperty("BrandId");
        entity.addStringProperty("StatId");
        entity.addStringProperty("Grade");
        entity.addStringProperty("Area");
        entity.addStringProperty("SupId");
        entity.addStringProperty("MeasureId");
        entity.addDoubleProperty("PacketQty");
        entity.addStringProperty("TaxType");
        entity.addDoubleProperty("InTax");
        entity.addDoubleProperty("TaxPrice");
        entity.addDoubleProperty("InPrice");
        entity.addDoubleProperty("MinOrderQty");
        entity.addDoubleProperty("NormalPrice");
        entity.addDoubleProperty("MemberPrice");
        entity.addDoubleProperty("GroupPrice");
        entity.addDoubleProperty("ReturnRat");
        entity.addDoubleProperty("vipdiscount");
        entity.addDoubleProperty("posdiscount");
        entity.addStringProperty("Status");
        entity.addStringProperty("Operatorid");
        entity.addDateProperty("CreateDate");
        entity.addDateProperty("UpdateDate");
        entity.addDateProperty("stopdate");
        entity.addLongProperty("TimeStamp");

    }

    private static void CreateTableBranch(Schema schema) {
        Entity entity = schema.addEntity("Branch");
        entity.addIdProperty().autoincrement();
        entity.addStringProperty("braid");
        entity.addStringProperty("braname");
        entity.addStringProperty("brasname");
        entity.addStringProperty("bratype");
        entity.addStringProperty("Status");
    }

    private static void CreateTablePosMachine(Schema schema) {
        Entity entity = schema.addEntity("PosMachine");
        entity.addIdProperty().autoincrement();
        entity.addStringProperty("braid");
        entity.addStringProperty("posno");
        entity.addStringProperty("Status");
    }


    private static void CreateTableProductBarcode(Schema schema) {
        Entity entity = schema.addEntity("ProductBarCode");
        entity.addIdProperty().autoincrement();
        entity.addStringProperty("Proid");
        entity.addStringProperty("Barcode");
        entity.addStringProperty("BarMode");
        entity.addDoubleProperty("Quantity");
        entity.addDoubleProperty("NormalPrice");
        entity.addDoubleProperty("MemberPrice");
        entity.addDoubleProperty("Status");
        entity.addStringProperty("MainFlag");
        entity.addLongProperty("TimeStamp");
    }

    private static void CreateTableProductBranchRel(Schema schema) {
        Entity entity = schema.addEntity("ProductBranchRel");
        entity.addIdProperty().autoincrement();

        entity.addStringProperty("Braid");
        entity.addStringProperty("Proid");
        entity.addStringProperty("SupId");

        entity.addStringProperty("CanChangePrice");
        entity.addStringProperty("SupPmtFlag");
        entity.addStringProperty("PromtFlag");
        entity.addStringProperty("PotFlag");
        entity.addStringProperty("Dcflag");

        entity.addDoubleProperty("MinPrice");
        entity.addDoubleProperty("MaxPrice");
        entity.addDoubleProperty("NormalPrice");
        entity.addDoubleProperty("MemberPrice");
        entity.addDoubleProperty("ReturnRat");

        entity.addStringProperty("Status");
        entity.addStringProperty("Operatorid");
        entity.addDateProperty("CreateDate");
        entity.addDateProperty("UpdateDate");
        entity.addDateProperty("stopdate");
        entity.addLongProperty("TimeStamp");
    }

    private static void CreateTableProductBarcodeBranchRel(Schema schema) {
        Entity entity = schema.addEntity("ProductBarCodeBranchRel");
        entity.addIdProperty().autoincrement();

        entity.addStringProperty("Braid");
        entity.addStringProperty("Proid");
        entity.addStringProperty("Barcode");
        entity.addStringProperty("BarMode");
        entity.addDoubleProperty("Quantity");
        entity.addDoubleProperty("NormalPrice");
        entity.addDoubleProperty("MemberPrice");
        entity.addStringProperty("Status");
        entity.addStringProperty("Operatorid");
        entity.addStringProperty("Spec");
        entity.addStringProperty("MainFlag");
        entity.addLongProperty("TimeStamp");
        entity.addDateProperty("CreateDate");
        entity.addDateProperty("UpdateDate");
    }

    private static void CreateTableBranchEmployee(Schema schema) {
        Entity entity = schema.addEntity("BranchEmployee");
        entity.addIdProperty().autoincrement();

        entity.addStringProperty("Braid");
        entity.addStringProperty("Empid");
        entity.addStringProperty("EmpName");
        entity.addStringProperty("Password");
        entity.addDoubleProperty("Discount");
        entity.addStringProperty("Status");
        entity.addLongProperty("timestamp");
    }

    private static void CreateTableProductClass(Schema schema) {
        Entity entity = schema.addEntity("ProductClass");
        entity.addIdProperty().autoincrement();

        entity.addStringProperty("ClassId");
        entity.addStringProperty("ClassName");
        entity.addLongProperty("TimeStamp");
        entity.addDoubleProperty("Status");
        entity.addDateProperty("CreateDate");
        entity.addDateProperty("UpdateDate");
    }


    private static void CreateTableSaleDaily(Schema schema) {
        Entity entity = schema.addEntity("SaleDaily");
        entity.addIdProperty().autoincrement();

        entity.addStringProperty("Braid");
        entity.addDateProperty("SaleDate");
        entity.addStringProperty("ProId");
        entity.addStringProperty("BarCode");
        entity.addStringProperty("ClassId");
        entity.addStringProperty("IsDM");
        entity.addStringProperty("IsPmt");
        entity.addStringProperty("IsTimePrompt");
        entity.addDoubleProperty("SaleTax");
        entity.addStringProperty("PosNo");
        entity.addStringProperty("SalerId");
        entity.addStringProperty("SaleMan");
        entity.addStringProperty("SaleType");
        entity.addDoubleProperty("SaleQty");
        entity.addDoubleProperty("SaleAmt");
        entity.addDoubleProperty("SaleDisAmt");
        entity.addDoubleProperty("TransDisAmt");
        entity.addDoubleProperty("NormalPrice");
        entity.addDoubleProperty("CurPrice");
        entity.addDoubleProperty("AvgCostPrice");
        entity.addStringProperty("SaleId");
        entity.addStringProperty("InVoiceId");
        entity.addDoubleProperty("Point1");
        entity.addDoubleProperty("Point2");
        entity.addDoubleProperty("ReturnRat");
        entity.addDoubleProperty("Cash1");
        entity.addDoubleProperty("Cash2");
        entity.addDoubleProperty("Cash3");
        entity.addDoubleProperty("Cash4");
        entity.addDoubleProperty("Cash5");
        entity.addDoubleProperty("Cash6");
        entity.addDoubleProperty("Cash7");
        entity.addDoubleProperty("Cash8");
        entity.addLongProperty("SourceId");
        entity.addBooleanProperty("IsReturn");
    }

    private static void CreateTableSalePayMode(Schema schema) {
        Entity entity = schema.addEntity("SalePayMode");
        entity.addIdProperty().autoincrement();

        entity.addStringProperty("Braid");
        entity.addDateProperty("SaleDate");
        entity.addStringProperty("SaleId");
        entity.addStringProperty("PayModeId");
        entity.addDoubleProperty("PayMoney");
        entity.addStringProperty("CardType");
        entity.addStringProperty("CardNo");
        entity.addLongProperty("SourceId");
        entity.addBooleanProperty("IsReturn");

    }

    private static void CreateTableMobileDevice(Schema schema) {
        Entity entity = schema.addEntity("MobileDevice");
        entity.addIdProperty().autoincrement();
        entity.addStringProperty("deviceid");
        entity.addStringProperty("posno");
        entity.addStringProperty("Status");
        entity.addLongProperty("SourceId");
    }

}

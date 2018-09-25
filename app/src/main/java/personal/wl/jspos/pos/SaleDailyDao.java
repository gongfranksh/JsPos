package personal.wl.jspos.pos;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SALE_DAILY".
*/
public class SaleDailyDao extends AbstractDao<SaleDaily, Long> {

    public static final String TABLENAME = "SALE_DAILY";

    /**
     * Properties of entity SaleDaily.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Braid = new Property(1, String.class, "Braid", false, "BRAID");
        public final static Property SaleDate = new Property(2, java.util.Date.class, "SaleDate", false, "SALE_DATE");
        public final static Property ProId = new Property(3, String.class, "ProId", false, "PRO_ID");
        public final static Property BarCode = new Property(4, String.class, "BarCode", false, "BAR_CODE");
        public final static Property ClassId = new Property(5, String.class, "ClassId", false, "CLASS_ID");
        public final static Property IsDM = new Property(6, String.class, "IsDM", false, "IS_DM");
        public final static Property IsPmt = new Property(7, String.class, "IsPmt", false, "IS_PMT");
        public final static Property IsTimePrompt = new Property(8, String.class, "IsTimePrompt", false, "IS_TIME_PROMPT");
        public final static Property SaleTax = new Property(9, Double.class, "SaleTax", false, "SALE_TAX");
        public final static Property PosNo = new Property(10, String.class, "PosNo", false, "POS_NO");
        public final static Property SalerId = new Property(11, String.class, "SalerId", false, "SALER_ID");
        public final static Property SaleMan = new Property(12, String.class, "SaleMan", false, "SALE_MAN");
        public final static Property SaleType = new Property(13, String.class, "SaleType", false, "SALE_TYPE");
        public final static Property SaleQty = new Property(14, Double.class, "SaleQty", false, "SALE_QTY");
        public final static Property SaleAmt = new Property(15, Double.class, "SaleAmt", false, "SALE_AMT");
        public final static Property SaleDisAmt = new Property(16, Double.class, "SaleDisAmt", false, "SALE_DIS_AMT");
        public final static Property TransDisAmt = new Property(17, Double.class, "TransDisAmt", false, "TRANS_DIS_AMT");
        public final static Property NormalPrice = new Property(18, Double.class, "NormalPrice", false, "NORMAL_PRICE");
        public final static Property CurPrice = new Property(19, Double.class, "CurPrice", false, "CUR_PRICE");
        public final static Property AvgCostPrice = new Property(20, Double.class, "AvgCostPrice", false, "AVG_COST_PRICE");
        public final static Property SaleId = new Property(21, String.class, "SaleId", false, "SALE_ID");
        public final static Property InVoiceId = new Property(22, String.class, "InVoiceId", false, "IN_VOICE_ID");
        public final static Property Point1 = new Property(23, Double.class, "Point1", false, "POINT1");
        public final static Property Point2 = new Property(24, Double.class, "Point2", false, "POINT2");
        public final static Property ReturnRat = new Property(25, Double.class, "ReturnRat", false, "RETURN_RAT");
        public final static Property Cash1 = new Property(26, Double.class, "Cash1", false, "CASH1");
        public final static Property Cash2 = new Property(27, Double.class, "Cash2", false, "CASH2");
        public final static Property Cash3 = new Property(28, Double.class, "Cash3", false, "CASH3");
        public final static Property Cash4 = new Property(29, Double.class, "Cash4", false, "CASH4");
        public final static Property Cash5 = new Property(30, Double.class, "Cash5", false, "CASH5");
        public final static Property Cash6 = new Property(31, Double.class, "Cash6", false, "CASH6");
        public final static Property Cash7 = new Property(32, Double.class, "Cash7", false, "CASH7");
        public final static Property Cash8 = new Property(33, Double.class, "Cash8", false, "CASH8");
        public final static Property SourceId = new Property(34, Long.class, "SourceId", false, "SOURCE_ID");
        public final static Property IsReturn = new Property(35, Boolean.class, "IsReturn", false, "IS_RETURN");
    }


    public SaleDailyDao(DaoConfig config) {
        super(config);
    }
    
    public SaleDailyDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SALE_DAILY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"BRAID\" TEXT," + // 1: Braid
                "\"SALE_DATE\" INTEGER," + // 2: SaleDate
                "\"PRO_ID\" TEXT," + // 3: ProId
                "\"BAR_CODE\" TEXT," + // 4: BarCode
                "\"CLASS_ID\" TEXT," + // 5: ClassId
                "\"IS_DM\" TEXT," + // 6: IsDM
                "\"IS_PMT\" TEXT," + // 7: IsPmt
                "\"IS_TIME_PROMPT\" TEXT," + // 8: IsTimePrompt
                "\"SALE_TAX\" REAL," + // 9: SaleTax
                "\"POS_NO\" TEXT," + // 10: PosNo
                "\"SALER_ID\" TEXT," + // 11: SalerId
                "\"SALE_MAN\" TEXT," + // 12: SaleMan
                "\"SALE_TYPE\" TEXT," + // 13: SaleType
                "\"SALE_QTY\" REAL," + // 14: SaleQty
                "\"SALE_AMT\" REAL," + // 15: SaleAmt
                "\"SALE_DIS_AMT\" REAL," + // 16: SaleDisAmt
                "\"TRANS_DIS_AMT\" REAL," + // 17: TransDisAmt
                "\"NORMAL_PRICE\" REAL," + // 18: NormalPrice
                "\"CUR_PRICE\" REAL," + // 19: CurPrice
                "\"AVG_COST_PRICE\" REAL," + // 20: AvgCostPrice
                "\"SALE_ID\" TEXT," + // 21: SaleId
                "\"IN_VOICE_ID\" TEXT," + // 22: InVoiceId
                "\"POINT1\" REAL," + // 23: Point1
                "\"POINT2\" REAL," + // 24: Point2
                "\"RETURN_RAT\" REAL," + // 25: ReturnRat
                "\"CASH1\" REAL," + // 26: Cash1
                "\"CASH2\" REAL," + // 27: Cash2
                "\"CASH3\" REAL," + // 28: Cash3
                "\"CASH4\" REAL," + // 29: Cash4
                "\"CASH5\" REAL," + // 30: Cash5
                "\"CASH6\" REAL," + // 31: Cash6
                "\"CASH7\" REAL," + // 32: Cash7
                "\"CASH8\" REAL," + // 33: Cash8
                "\"SOURCE_ID\" INTEGER," + // 34: SourceId
                "\"IS_RETURN\" INTEGER);"); // 35: IsReturn
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SALE_DAILY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, SaleDaily entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String Braid = entity.getBraid();
        if (Braid != null) {
            stmt.bindString(2, Braid);
        }
 
        java.util.Date SaleDate = entity.getSaleDate();
        if (SaleDate != null) {
            stmt.bindLong(3, SaleDate.getTime());
        }
 
        String ProId = entity.getProId();
        if (ProId != null) {
            stmt.bindString(4, ProId);
        }
 
        String BarCode = entity.getBarCode();
        if (BarCode != null) {
            stmt.bindString(5, BarCode);
        }
 
        String ClassId = entity.getClassId();
        if (ClassId != null) {
            stmt.bindString(6, ClassId);
        }
 
        String IsDM = entity.getIsDM();
        if (IsDM != null) {
            stmt.bindString(7, IsDM);
        }
 
        String IsPmt = entity.getIsPmt();
        if (IsPmt != null) {
            stmt.bindString(8, IsPmt);
        }
 
        String IsTimePrompt = entity.getIsTimePrompt();
        if (IsTimePrompt != null) {
            stmt.bindString(9, IsTimePrompt);
        }
 
        Double SaleTax = entity.getSaleTax();
        if (SaleTax != null) {
            stmt.bindDouble(10, SaleTax);
        }
 
        String PosNo = entity.getPosNo();
        if (PosNo != null) {
            stmt.bindString(11, PosNo);
        }
 
        String SalerId = entity.getSalerId();
        if (SalerId != null) {
            stmt.bindString(12, SalerId);
        }
 
        String SaleMan = entity.getSaleMan();
        if (SaleMan != null) {
            stmt.bindString(13, SaleMan);
        }
 
        String SaleType = entity.getSaleType();
        if (SaleType != null) {
            stmt.bindString(14, SaleType);
        }
 
        Double SaleQty = entity.getSaleQty();
        if (SaleQty != null) {
            stmt.bindDouble(15, SaleQty);
        }
 
        Double SaleAmt = entity.getSaleAmt();
        if (SaleAmt != null) {
            stmt.bindDouble(16, SaleAmt);
        }
 
        Double SaleDisAmt = entity.getSaleDisAmt();
        if (SaleDisAmt != null) {
            stmt.bindDouble(17, SaleDisAmt);
        }
 
        Double TransDisAmt = entity.getTransDisAmt();
        if (TransDisAmt != null) {
            stmt.bindDouble(18, TransDisAmt);
        }
 
        Double NormalPrice = entity.getNormalPrice();
        if (NormalPrice != null) {
            stmt.bindDouble(19, NormalPrice);
        }
 
        Double CurPrice = entity.getCurPrice();
        if (CurPrice != null) {
            stmt.bindDouble(20, CurPrice);
        }
 
        Double AvgCostPrice = entity.getAvgCostPrice();
        if (AvgCostPrice != null) {
            stmt.bindDouble(21, AvgCostPrice);
        }
 
        String SaleId = entity.getSaleId();
        if (SaleId != null) {
            stmt.bindString(22, SaleId);
        }
 
        String InVoiceId = entity.getInVoiceId();
        if (InVoiceId != null) {
            stmt.bindString(23, InVoiceId);
        }
 
        Double Point1 = entity.getPoint1();
        if (Point1 != null) {
            stmt.bindDouble(24, Point1);
        }
 
        Double Point2 = entity.getPoint2();
        if (Point2 != null) {
            stmt.bindDouble(25, Point2);
        }
 
        Double ReturnRat = entity.getReturnRat();
        if (ReturnRat != null) {
            stmt.bindDouble(26, ReturnRat);
        }
 
        Double Cash1 = entity.getCash1();
        if (Cash1 != null) {
            stmt.bindDouble(27, Cash1);
        }
 
        Double Cash2 = entity.getCash2();
        if (Cash2 != null) {
            stmt.bindDouble(28, Cash2);
        }
 
        Double Cash3 = entity.getCash3();
        if (Cash3 != null) {
            stmt.bindDouble(29, Cash3);
        }
 
        Double Cash4 = entity.getCash4();
        if (Cash4 != null) {
            stmt.bindDouble(30, Cash4);
        }
 
        Double Cash5 = entity.getCash5();
        if (Cash5 != null) {
            stmt.bindDouble(31, Cash5);
        }
 
        Double Cash6 = entity.getCash6();
        if (Cash6 != null) {
            stmt.bindDouble(32, Cash6);
        }
 
        Double Cash7 = entity.getCash7();
        if (Cash7 != null) {
            stmt.bindDouble(33, Cash7);
        }
 
        Double Cash8 = entity.getCash8();
        if (Cash8 != null) {
            stmt.bindDouble(34, Cash8);
        }
 
        Long SourceId = entity.getSourceId();
        if (SourceId != null) {
            stmt.bindLong(35, SourceId);
        }
 
        Boolean IsReturn = entity.getIsReturn();
        if (IsReturn != null) {
            stmt.bindLong(36, IsReturn ? 1L: 0L);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, SaleDaily entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String Braid = entity.getBraid();
        if (Braid != null) {
            stmt.bindString(2, Braid);
        }
 
        java.util.Date SaleDate = entity.getSaleDate();
        if (SaleDate != null) {
            stmt.bindLong(3, SaleDate.getTime());
        }
 
        String ProId = entity.getProId();
        if (ProId != null) {
            stmt.bindString(4, ProId);
        }
 
        String BarCode = entity.getBarCode();
        if (BarCode != null) {
            stmt.bindString(5, BarCode);
        }
 
        String ClassId = entity.getClassId();
        if (ClassId != null) {
            stmt.bindString(6, ClassId);
        }
 
        String IsDM = entity.getIsDM();
        if (IsDM != null) {
            stmt.bindString(7, IsDM);
        }
 
        String IsPmt = entity.getIsPmt();
        if (IsPmt != null) {
            stmt.bindString(8, IsPmt);
        }
 
        String IsTimePrompt = entity.getIsTimePrompt();
        if (IsTimePrompt != null) {
            stmt.bindString(9, IsTimePrompt);
        }
 
        Double SaleTax = entity.getSaleTax();
        if (SaleTax != null) {
            stmt.bindDouble(10, SaleTax);
        }
 
        String PosNo = entity.getPosNo();
        if (PosNo != null) {
            stmt.bindString(11, PosNo);
        }
 
        String SalerId = entity.getSalerId();
        if (SalerId != null) {
            stmt.bindString(12, SalerId);
        }
 
        String SaleMan = entity.getSaleMan();
        if (SaleMan != null) {
            stmt.bindString(13, SaleMan);
        }
 
        String SaleType = entity.getSaleType();
        if (SaleType != null) {
            stmt.bindString(14, SaleType);
        }
 
        Double SaleQty = entity.getSaleQty();
        if (SaleQty != null) {
            stmt.bindDouble(15, SaleQty);
        }
 
        Double SaleAmt = entity.getSaleAmt();
        if (SaleAmt != null) {
            stmt.bindDouble(16, SaleAmt);
        }
 
        Double SaleDisAmt = entity.getSaleDisAmt();
        if (SaleDisAmt != null) {
            stmt.bindDouble(17, SaleDisAmt);
        }
 
        Double TransDisAmt = entity.getTransDisAmt();
        if (TransDisAmt != null) {
            stmt.bindDouble(18, TransDisAmt);
        }
 
        Double NormalPrice = entity.getNormalPrice();
        if (NormalPrice != null) {
            stmt.bindDouble(19, NormalPrice);
        }
 
        Double CurPrice = entity.getCurPrice();
        if (CurPrice != null) {
            stmt.bindDouble(20, CurPrice);
        }
 
        Double AvgCostPrice = entity.getAvgCostPrice();
        if (AvgCostPrice != null) {
            stmt.bindDouble(21, AvgCostPrice);
        }
 
        String SaleId = entity.getSaleId();
        if (SaleId != null) {
            stmt.bindString(22, SaleId);
        }
 
        String InVoiceId = entity.getInVoiceId();
        if (InVoiceId != null) {
            stmt.bindString(23, InVoiceId);
        }
 
        Double Point1 = entity.getPoint1();
        if (Point1 != null) {
            stmt.bindDouble(24, Point1);
        }
 
        Double Point2 = entity.getPoint2();
        if (Point2 != null) {
            stmt.bindDouble(25, Point2);
        }
 
        Double ReturnRat = entity.getReturnRat();
        if (ReturnRat != null) {
            stmt.bindDouble(26, ReturnRat);
        }
 
        Double Cash1 = entity.getCash1();
        if (Cash1 != null) {
            stmt.bindDouble(27, Cash1);
        }
 
        Double Cash2 = entity.getCash2();
        if (Cash2 != null) {
            stmt.bindDouble(28, Cash2);
        }
 
        Double Cash3 = entity.getCash3();
        if (Cash3 != null) {
            stmt.bindDouble(29, Cash3);
        }
 
        Double Cash4 = entity.getCash4();
        if (Cash4 != null) {
            stmt.bindDouble(30, Cash4);
        }
 
        Double Cash5 = entity.getCash5();
        if (Cash5 != null) {
            stmt.bindDouble(31, Cash5);
        }
 
        Double Cash6 = entity.getCash6();
        if (Cash6 != null) {
            stmt.bindDouble(32, Cash6);
        }
 
        Double Cash7 = entity.getCash7();
        if (Cash7 != null) {
            stmt.bindDouble(33, Cash7);
        }
 
        Double Cash8 = entity.getCash8();
        if (Cash8 != null) {
            stmt.bindDouble(34, Cash8);
        }
 
        Long SourceId = entity.getSourceId();
        if (SourceId != null) {
            stmt.bindLong(35, SourceId);
        }
 
        Boolean IsReturn = entity.getIsReturn();
        if (IsReturn != null) {
            stmt.bindLong(36, IsReturn ? 1L: 0L);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public SaleDaily readEntity(Cursor cursor, int offset) {
        SaleDaily entity = new SaleDaily( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // Braid
            cursor.isNull(offset + 2) ? null : new java.util.Date(cursor.getLong(offset + 2)), // SaleDate
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // ProId
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // BarCode
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // ClassId
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // IsDM
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // IsPmt
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // IsTimePrompt
            cursor.isNull(offset + 9) ? null : cursor.getDouble(offset + 9), // SaleTax
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // PosNo
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // SalerId
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // SaleMan
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // SaleType
            cursor.isNull(offset + 14) ? null : cursor.getDouble(offset + 14), // SaleQty
            cursor.isNull(offset + 15) ? null : cursor.getDouble(offset + 15), // SaleAmt
            cursor.isNull(offset + 16) ? null : cursor.getDouble(offset + 16), // SaleDisAmt
            cursor.isNull(offset + 17) ? null : cursor.getDouble(offset + 17), // TransDisAmt
            cursor.isNull(offset + 18) ? null : cursor.getDouble(offset + 18), // NormalPrice
            cursor.isNull(offset + 19) ? null : cursor.getDouble(offset + 19), // CurPrice
            cursor.isNull(offset + 20) ? null : cursor.getDouble(offset + 20), // AvgCostPrice
            cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21), // SaleId
            cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22), // InVoiceId
            cursor.isNull(offset + 23) ? null : cursor.getDouble(offset + 23), // Point1
            cursor.isNull(offset + 24) ? null : cursor.getDouble(offset + 24), // Point2
            cursor.isNull(offset + 25) ? null : cursor.getDouble(offset + 25), // ReturnRat
            cursor.isNull(offset + 26) ? null : cursor.getDouble(offset + 26), // Cash1
            cursor.isNull(offset + 27) ? null : cursor.getDouble(offset + 27), // Cash2
            cursor.isNull(offset + 28) ? null : cursor.getDouble(offset + 28), // Cash3
            cursor.isNull(offset + 29) ? null : cursor.getDouble(offset + 29), // Cash4
            cursor.isNull(offset + 30) ? null : cursor.getDouble(offset + 30), // Cash5
            cursor.isNull(offset + 31) ? null : cursor.getDouble(offset + 31), // Cash6
            cursor.isNull(offset + 32) ? null : cursor.getDouble(offset + 32), // Cash7
            cursor.isNull(offset + 33) ? null : cursor.getDouble(offset + 33), // Cash8
            cursor.isNull(offset + 34) ? null : cursor.getLong(offset + 34), // SourceId
            cursor.isNull(offset + 35) ? null : cursor.getShort(offset + 35) != 0 // IsReturn
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, SaleDaily entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setBraid(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setSaleDate(cursor.isNull(offset + 2) ? null : new java.util.Date(cursor.getLong(offset + 2)));
        entity.setProId(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setBarCode(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setClassId(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setIsDM(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setIsPmt(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setIsTimePrompt(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setSaleTax(cursor.isNull(offset + 9) ? null : cursor.getDouble(offset + 9));
        entity.setPosNo(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setSalerId(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setSaleMan(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setSaleType(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setSaleQty(cursor.isNull(offset + 14) ? null : cursor.getDouble(offset + 14));
        entity.setSaleAmt(cursor.isNull(offset + 15) ? null : cursor.getDouble(offset + 15));
        entity.setSaleDisAmt(cursor.isNull(offset + 16) ? null : cursor.getDouble(offset + 16));
        entity.setTransDisAmt(cursor.isNull(offset + 17) ? null : cursor.getDouble(offset + 17));
        entity.setNormalPrice(cursor.isNull(offset + 18) ? null : cursor.getDouble(offset + 18));
        entity.setCurPrice(cursor.isNull(offset + 19) ? null : cursor.getDouble(offset + 19));
        entity.setAvgCostPrice(cursor.isNull(offset + 20) ? null : cursor.getDouble(offset + 20));
        entity.setSaleId(cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21));
        entity.setInVoiceId(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
        entity.setPoint1(cursor.isNull(offset + 23) ? null : cursor.getDouble(offset + 23));
        entity.setPoint2(cursor.isNull(offset + 24) ? null : cursor.getDouble(offset + 24));
        entity.setReturnRat(cursor.isNull(offset + 25) ? null : cursor.getDouble(offset + 25));
        entity.setCash1(cursor.isNull(offset + 26) ? null : cursor.getDouble(offset + 26));
        entity.setCash2(cursor.isNull(offset + 27) ? null : cursor.getDouble(offset + 27));
        entity.setCash3(cursor.isNull(offset + 28) ? null : cursor.getDouble(offset + 28));
        entity.setCash4(cursor.isNull(offset + 29) ? null : cursor.getDouble(offset + 29));
        entity.setCash5(cursor.isNull(offset + 30) ? null : cursor.getDouble(offset + 30));
        entity.setCash6(cursor.isNull(offset + 31) ? null : cursor.getDouble(offset + 31));
        entity.setCash7(cursor.isNull(offset + 32) ? null : cursor.getDouble(offset + 32));
        entity.setCash8(cursor.isNull(offset + 33) ? null : cursor.getDouble(offset + 33));
        entity.setSourceId(cursor.isNull(offset + 34) ? null : cursor.getLong(offset + 34));
        entity.setIsReturn(cursor.isNull(offset + 35) ? null : cursor.getShort(offset + 35) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(SaleDaily entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(SaleDaily entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(SaleDaily entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}

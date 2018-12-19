package personal.wl.jspos.db;


import android.content.Context;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import personal.wl.jspos.Config.Beans.SystemDBInfo;
import personal.wl.jspos.pos.SaleDaily;
import personal.wl.jspos.pos.SalePayMode;

import static personal.wl.jspos.db.Tools.convertList;


public class DBC2Jspot {
    private static String TAG = "DBC2Jspot";
    private static long run;
    private static Connection myconnection;
    private SystemDBInfo systemDBInfo;
    private Context context;

    private String IP;
    private String DBName;
    private String USER;
    private String PWD;

    private String DRIVER;
    private String CONNSTR;


    public DBC2Jspot(Context context) {

        systemDBInfo = new SystemDBInfo(context);
        IP=systemDBInfo.get_Db_IP_Address();
        DBName=systemDBInfo.get_Db_Name();
        USER=systemDBInfo.get_Db_Account();
        PWD=systemDBInfo.get_Db_Password();

        DRIVER = "net.sourceforge.jtds.jdbc.Driver";
        CONNSTR = "jdbc:jtds:sqlserver://" + IP + ":1433/"
                + DBName + ";useunicode=true;characterEncoding=UTF-8";
        this.setMyconnection(getConnection());
        this.context = context;

    }


    public List getBranch() {
        List list = null;
        Connection cnn = this.getMyconnection();
        try {
            list = QuerySqlGetResult(cnn, "\n" + "SELECT braid,braname,brasname,bratype,status " +
                    "FROM branch ");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;

    }

    public List getDeviceAll() {
        String sql = "SELECT sourceid,deviceid,posno,updateid FROM mobile_device where status='1'";
        List list = null;
        Connection cnn = this.getMyconnection();
        try {
            list = QuerySqlGetResult(cnn, sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List getCheckDeviceThisFromServer(HashMap device) {
        String sql = "SELECT sourceid,deviceid,posno,updateid FROM mobile_device where status='1' ";
        List list = null;
        if (device.get("deviceid") == null | device.get("posno") == null) {
            return list;
        }
        sql = sql + " and deviceid='" + device.get("deviceid") + "'";
        sql = sql + " and posno='" + device.get("posno") + "'";
        Connection cnn = this.getMyconnection();
        try {
            list = QuerySqlGetResult(cnn, sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Boolean CheckDeviceByServer(HashMap device) {
        String sql = "SELECT sourceid,deviceid,posno,updateid FROM mobile_device where status='1' ";
        List list = null;
        if (device.get("deviceid") == null | device.get("posno") == null) {
            return false;
        }
        sql = sql + " and deviceid='" + device.get("deviceid") + "'";
        sql = sql + " and posno='" + device.get("posno") + "'";
        Connection cnn = this.getMyconnection();
        try {
            list = QuerySqlGetResult(cnn, sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (list.size() == 1) return true;
        else return false;
    }


    public long getLastUploadTranscations(HashMap device) {
        String sql = "SELECT isnull(max(msp.deivcetransid),0) as maxid \n" +
                " FROM mobile_sale_paymode  msp\n" +
                " LEFT JOIN mobile_device md ON msp.sourceid=md.sourceid\n" +
                " WHERE msp.sourceid=" + device.get("sourceid") +
                " AND md.deviceid='" + device.get("deviceid") + "'" +
                " AND posno='" + device.get("posno") + "'";
        List list = null;

        Log.i(TAG, "getLastUploadTranscations: ==>" + sql);
        Connection cnn = this.getMyconnection();
        try {
            list = QuerySqlGetResult(cnn, sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (list.size() != 0) {
            return Long.parseLong(((HashMap) list.get(0)).get("maxid").toString());

        } else {
            return 0;
        }
    }


    public void LastUploadTranscations(List<SalePayMode> salePayModeList) {
        String sql = "INSERT INTO dbo.mobile_sale_paymode (BraId, SaleDate, SaleId, SalerId," +
                " PaymodeId, PayMoney, sourceid,orderinnerid, deivcetransid)";
        sql = sql + "Values(";
        String sqlvalue = null;
        Connection cnn = this.getMyconnection();

        for (int i = 0; i < salePayModeList.size(); i++) {
            try {
                SimpleDateFormat df = (SimpleDateFormat) DateFormat.getDateInstance();
                df.applyPattern("yyyy-MM-dd HH:mm:ss");
                sqlvalue = "'" + salePayModeList.get(i).getBraid() + "',";
                sqlvalue = sqlvalue + "'" + df.format(salePayModeList.get(i).getSaleDate()) + "',";
                sqlvalue = sqlvalue + "'" + salePayModeList.get(i).getSaleId() + "',";
                sqlvalue = sqlvalue + "'" + salePayModeList.get(i).getSalerId() + "',";
                sqlvalue = sqlvalue + "'" + salePayModeList.get(i).getPayModeId() + "',";
                sqlvalue = sqlvalue + salePayModeList.get(i).getPayMoney() + ",";
                sqlvalue = sqlvalue + salePayModeList.get(i).getSourceId() + ",";
                sqlvalue = sqlvalue + "'" + salePayModeList.get(i).getOrderInnerId() + "',";
                sqlvalue = sqlvalue + salePayModeList.get(i).getId() + ")";
                String sql_exec = sql + sqlvalue;
                proc_exec(cnn, sql_exec);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void InSertMobileSaleDaily(List<SaleDaily> saleDailyList) {
        SimpleDateFormat df = (SimpleDateFormat) DateFormat.getDateInstance();
        df.applyPattern("yyyy-MM-dd HH:mm:ss");

        String sql = "INSERT INTO dbo.mobile_sale_daily " +
                "(BraId, SaleDate, ProId, Barcode, ClassId, " +
                "IsDM, IsPmt, IsTimePrompt, SaleTax, PosNo, " +
                "SalerId, SaleMan, SaleType, SaleQty, SaleAmt, " +
                "NormalPrice, CurPrice,  " +
                "SaleId,   " +
                "cash1, cash7, cash8, " +
//                "cash1, cash2, cash3, " +
//                "cash4, cash5, cash6, cash7, cash8, " +
                " sourceid, orderinnerid,deivcetransid )";
        sql = sql + "Values(";
        String sqlvalue = null;
        Connection cnn = this.getMyconnection();

        for (int i = 0; i < saleDailyList.size(); i++) {
            try {

                sqlvalue = "'" + saleDailyList.get(i).getBraid() + "',";
                sqlvalue = sqlvalue + "'" + df.format(saleDailyList.get(i).getSaleDate()) + "',";
                sqlvalue = sqlvalue + "'" + saleDailyList.get(i).getProId() + "',";
                sqlvalue = sqlvalue + "'" + saleDailyList.get(i).getBarCode() + "',";
                sqlvalue = sqlvalue + "'" + saleDailyList.get(i).getClassId() + "',";

                sqlvalue = sqlvalue + "'" + saleDailyList.get(i).getIsDM() + "',";
                sqlvalue = sqlvalue + "'" + saleDailyList.get(i).getIsPmt() + "',";
                sqlvalue = sqlvalue + "'" + saleDailyList.get(i).getIsTimePrompt() + "',";
                sqlvalue = sqlvalue + saleDailyList.get(i).getSaleTax() + ",";
                sqlvalue = sqlvalue + "'" + saleDailyList.get(i).getPosNo() + "',";

                sqlvalue = sqlvalue + "'" + saleDailyList.get(i).getSalerId() + "',";
                sqlvalue = sqlvalue + "'" + saleDailyList.get(i).getSaleMan() + "',";
                sqlvalue = sqlvalue + "'" + saleDailyList.get(i).getSaleType() + "',";
                sqlvalue = sqlvalue + saleDailyList.get(i).getSaleQty() + ",";
                sqlvalue = sqlvalue + saleDailyList.get(i).getSaleAmt() + ",";

                sqlvalue = sqlvalue + +saleDailyList.get(i).getNormalPrice() + ",";
                sqlvalue = sqlvalue + +saleDailyList.get(i).getCurPrice() + ",";


                sqlvalue = sqlvalue + "'" + saleDailyList.get(i).getSaleId() + "',";


                sqlvalue = sqlvalue + saleDailyList.get(i).getCash1() + ",";
                sqlvalue = sqlvalue + saleDailyList.get(i).getCash7() + ",";
                sqlvalue = sqlvalue + saleDailyList.get(i).getCash8() + ",";


                sqlvalue = sqlvalue + saleDailyList.get(i).getSourceId() + ",";
                sqlvalue = sqlvalue + "'" + saleDailyList.get(i).getOrderInnerId() + "',";
                sqlvalue = sqlvalue + saleDailyList.get(i).getId() + ")";
                String sql_exec = sql + sqlvalue;
                Statement stmt = cnn.createStatement();
                boolean rs = stmt.execute(sql_exec);
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void UploadMobileDeviceLogId(List<SalePayMode> salePayModeList) {
        String sql_exec;
        Connection cnn = this.getMyconnection();
        for (int i = 0; i < salePayModeList.size(); i++) {
            try {
                sql_exec = "UPDATE mobile_device " +
                        " SET updateid = " + salePayModeList.get(i).getId() + "," +
                        " updatedate=getdate() " +
                        " where sourceid=" + salePayModeList.get(i).getSourceId();
                proc_exec(cnn, sql_exec);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public List getProductNeedUpdate(Integer timestamp) {
        String sql = "select  ProId, Barcode, ProName, ProSName, ClassId, Spec, BrandId, StatId, Grade, \n" +
                "Area, SupId, MeasureId, PacketQty, PacketQty1, Weight, Length, Width, Height, TaxType, \n" +
                "InTax, SaleTax, InPrice, TaxPrice, isnull(NormalPrice,0.00) AS NormalPrice, MemberPrice, GroupPrice, \n" +
                "MainFlag, ProFlag, WeightFlag, Barmode, OrderMode, MinOrderQty, OrderMultiplier, \n" +
                "FreshMode, ReturnRat, WarrantyDays, UDF1, UDF2, UDF3, Status, PromtFlag, PotFlag, \n" +
                "CanChangePrice, avgcostprice, cardpoint, CreateDate, UpdateDate, stopdate, \n" +
                "SupPmtFlag, Operatorid, vipdiscount, isnull(posdiscount,1.00) AS posdiscount,\n" +
                "CONVERT (int,timestamp) as timestamp \n" +
                "FROM product WHERE CONVERT (int,timestamp) > " + timestamp +
                " order by CONVERT (int,timestamp) ";
        ;
        List list = null;
        Connection cnn = this.getMyconnection();
        try {
            list = QuerySqlGetResult(cnn, sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    public List getBranchEmployeeNeedUpdate(Integer timestamp) {
        String sql = "SELECT braid,empid,empname,status,domainaccounts , password, \n" +
                "isnull(discount,1) AS discount,CONVERT (int,timestamp) as timestamp \n" +
                "FROM branch_employee ";
        sql = sql + "WHERE CONVERT (int,timestamp) > " + timestamp;
        List list = null;
        Connection cnn = this.getMyconnection();
        try {
            list = QuerySqlGetResult(cnn, sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    public List getProductBarCodeNeedUpdate(Integer timestamp) {
        String sql = "SELECT   proid,barcode,isnull(normalprice,0) AS normalprice,mainflag,\n" +
                "CONVERT (int,timestamp) as timestamp \n" +
                "FROM product_barcode \n";
        sql = sql + "WHERE CONVERT (int,timestamp) > " + timestamp;
        sql = sql + "ORDER BY CONVERT (int,timestamp) ";
        List list = null;
        Connection cnn = this.getMyconnection();
        try {
            list = QuerySqlGetResult(cnn, sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    public List getProductBranchRelNeedUpdate(Integer timestamp, HashMap device) {

//        device.get("posno") == null

        String sql = "SELECT   braid,proid,isnull(normalprice,0) AS normalprice,status,promtflag,\n" +
                "CONVERT (int,timestamp) as timestamp \n" +
                "FROM product_branch_rel \n";
        sql = sql + "WHERE CONVERT (int,timestamp) > " + timestamp;
        sql = sql + "and   BraId = '" + device.get("braid") + "' ";
        sql = sql + "ORDER BY CONVERT (int,timestamp) ";
        List list = null;
        Connection cnn = this.getMyconnection();
        try {
            list = QuerySqlGetResult(cnn, sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    public List getPmtDMBranchRelNeedUpdate(Integer timestamp, HashMap device) {

//        device.get("posno") == null

        String sql = "SELECT   BraId, d.DMId, h.DMBeginDate,h.DMEndDate,\n" +
                "SupId, ProId, OrigSalePrice,SalePrice, \n" +
                "CONVERT (int,d.timestamp) as timestamp \n" +
                "FROM dbo.pmt_dm_rel d LEFT JOIN prompt_peroid_dm h ON h.DMId=d.DMId \n";
        sql = sql + " WHERE CONVERT (int,d.timestamp) > " + timestamp;
        sql = sql + " and   BraId = '" + device.get("braid") + "' ";
        sql = sql + " ORDER BY CONVERT (int,d.timestamp) ";
        List list = null;
        Connection cnn = this.getMyconnection();
        try {
            list = QuerySqlGetResult(cnn, sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    public List getBranchNeedUpdate(Integer timestamp) {
        String sql = "SELECT hqid,braid,braname,brasname,bratype \n" +
                "FROM branch\n";
        List list = null;
        Connection cnn = this.getMyconnection();
        try {
            list = QuerySqlGetResult(cnn, sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List getPosNeedUpdate(Integer timestamp) {
        String sql = "SELECT braid,posno,status \n" +
                "FROM pos_machine\n";
        List list = null;
        Connection cnn = this.getMyconnection();
        try {
            list = QuerySqlGetResult(cnn, sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    public List getProductClass() {
        List list = null;
        Connection cnn = this.getMyconnection();
        try {
            list = QuerySqlGetResult(cnn, "SELECT classid,classname," +
                    "CONVERT (int,timestamp) as timestamp  " +
                    "FROM product_class" +
                    " order by classid");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;

    }


    public void closeConnection() {
        if (this.getConnection() != null) {
            try {
                this.getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

    }

    private Connection getMyconnection() {
        return myconnection;
    }

    private void setMyconnection(Connection myconnection) {
        DBC2Jspot.myconnection = myconnection;
    }


    private Connection getConnection() {
        try {
            // TODO Connect To MsSql Server
            run = System.currentTimeMillis();
            Log.i(TAG, "DBConnect2Mssql->doInBackground: start-->" + run);

            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            long start = System.currentTimeMillis();
            Connection con = DriverManager.getConnection(CONNSTR, USER, PWD);
            long end = System.currentTimeMillis() - start;
            Log.i(TAG, "获取连接的时间:" + end);
            run = System.currentTimeMillis();
            Log.i(TAG, "DBConnect2Mssql->doInBackground:---------->" + run + "Connect------->" + CONNSTR);
            return con;
        } catch (ClassNotFoundException e1) {
            // TODO ClassNotFoundException
            e1.printStackTrace();
            Log.e(TAG, "DBConnect2Mssql->doInBackground:" + e1.getMessage());
        } catch (SQLException e) {
            // TODO SQLExceptionError
            e.printStackTrace();
            Log.e(TAG, "DBConnect2Mssql->doInBackground:" + e.getMessage());
        }
        return null;
    }


    protected static List QuerySqlGetResult(Connection con, String sql) throws SQLException {
        // TODO QuerySqlGetResult
        List list = null;
        run = System.currentTimeMillis();
        Log.i(TAG, "QuerySqlGetResulta t-->" + run);
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            list = convertList(rs);
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            Log.e(TAG, "QuerySqlGetResult:" + e.getMessage().toString());
            System.out.println(e.getMessage().toString());
        }
        return list;
    }


    protected static void proc_exec(Connection con, String sql) throws SQLException {
        // TODO proc_exec
        run = System.currentTimeMillis();
        Log.i(TAG, "proc_exec t-->" + run);
        try {
            Statement stmt = con.createStatement();
            boolean rs = stmt.execute(sql);
            stmt.close();
        } catch (SQLException e) {
            Log.e(TAG, "proc_exec:" + e.getMessage().toString());
            System.out.println(e.getMessage().toString());
        }
    }

}

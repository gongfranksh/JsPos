package personal.wl.jspos.db;


import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

import static personal.wl.jspos.db.Tools.convertList;


public class DBC2Jspot {
    private static final String IP = "192.168.72.21";
    private static final String DBName = "headquarters";
    private static final String USER = "syread";
    private static final String PWD = "buynow";
    private static final String DRIVER = "net.sourceforge.jtds.jdbc.Driver";
    private static final String CONNSTR = "jdbc:jtds:sqlserver://" + IP + ":1433/"
            + DBName + ";useunicode=true;characterEncoding=UTF-8";
    private static String TAG = "DBC2Jspot";
    private static long run;
    private static Connection myconnection;

    public DBC2Jspot() {
        this.setMyconnection(getConnection());
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
        if ( device.get("deviceid")==null |  device.get("posno")==null){
            return list;
        }
        sql = sql +" and deviceid='"+device.get("deviceid") +"'";
        sql = sql +" and posno='"+device.get("posno") +"'";
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
        if ( device.get("deviceid")==null |  device.get("posno")==null){
            return false;
        }
        sql = sql +" and deviceid='"+device.get("deviceid") +"'";
        sql = sql +" and posno='"+device.get("posno") +"'";
        Connection cnn = this.getMyconnection();
        try {
            list = QuerySqlGetResult(cnn, sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (list.size()==1) return true;
        else return false;
    }




    public List getProductNeedUpdate(Integer timestamp) {
        String sql = "select  top 100 ProId, Barcode, ProName, ProSName, ClassId, Spec, BrandId, StatId, Grade, \n" +
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
        String sql = "SELECT top 100  proid,barcode,isnull(normalprice,0) AS normalprice,mainflag,\n" +
                "CONVERT (int,timestamp) as timestamp \n" +
                "FROM product_barcode \n";
        sql = sql + "WHERE CONVERT (int,timestamp) > " + timestamp;
//        sql = sql + " and proid  in ('2000000165332','2000000165356','2000000261263','2000000165523') ";
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




}

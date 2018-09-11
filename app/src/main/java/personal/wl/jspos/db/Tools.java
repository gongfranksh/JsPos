package personal.wl.jspos.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tools {

    public static List convertList(ResultSet rs) throws SQLException {
        List list = new ArrayList();
        ResultSetMetaData md = rs.getMetaData();
        int columnCount = md.getColumnCount();
        while (rs.next()) {
            Map rowData = new HashMap();
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(rowData);
        }
        return list;
    }


    public static Double Double2String(String input_d) {
        Double otd;
        if (input_d != null) {

            otd = Double.parseDouble(input_d);
        } else {
            otd = 0.00;
        }
        return otd;
    }


    public static Long Long2String(String input_d) {
        Long otd = null;
        if (input_d != null) {

            otd = Long.parseLong(input_d);
        } else {
//            otd = 0.00;
        }
        return otd;
    }


}

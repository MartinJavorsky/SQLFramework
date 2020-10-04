package sk.javorsky.SQLFramework.dbaccess;

import sk.javorsky.SQLFramework.reflection.ObjectReflector;
import sk.javorsky.SQLFramework.sql.Query;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Database {

    private Connection conn = null;
    private PropertiesReader propertiesReader = new PropertiesReader();
    //připojení k databázi
    //protected Connection connection;

    //instance třídy query
    protected Query query;

    public Database() throws Exception {
        String user = propertiesReader.getProperty(PropertiesReader.DB_USER);
        String psw = propertiesReader.getProperty(PropertiesReader.DB_PSW);
        String driver = propertiesReader.getProperty(PropertiesReader.DB_DRIVER);
        String url = propertiesReader.getProperty(PropertiesReader.DB_URL);
        connect(user,psw, driver, url);
    }

    private void connect(String usr, String pwd, String driver, String url) throws SQLException {
        try {
        Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        conn = DriverManager.getConnection(url,usr,pwd);

        if(conn!=null) {
            conn.setAutoCommit(false);
        }else{
            throw new SQLException("Connection is null");
        }
    }

    public void disconnect() throws Exception {
        try {
            conn.close();
        } catch (SQLException e) {
            throw new Exception("Can not close connection");
        }
    }

    public void commit() throws Exception {
        try {
            conn.commit();
        } catch (SQLException e) {
            throw new Exception("Can not commit transaction in connection.");
        }
    }

    public void rollBack() throws Exception {
        try {
            conn.rollback();
        } catch (SQLException e) {
            throw new Exception("Can not roll back transaction in connection.");
        }
    }

    Connection getConn() {
        return conn;
    }

    //public ResultSet executeQuery(String query) throws Exception {
    //    Statement st = getConn().createStatement();
    //    ResultSet rs = st.executeQuery(query);
    //    return rs;
    //}
    //public void executeUpdate(String query) throws Exception {
    //        Statement st = getConn().createStatement();
    //        st.executeUpdate(query);
    //}

    private int query(String query, List<Object> data) throws SQLException
    {
        PreparedStatement ps = conn.prepareStatement(query);
        if (data != null)
        {
            int index = 1;
            for(Object param : data){
                ps.setObject(index, param);
                index++;
            }
        }
        return ps.executeUpdate();
    }

    public int delete(String table, String requirement, List<Object> data) throws SQLException{
        query = new Query();
        query.delete(table)
                .where(requirement);
        return query(query.getQuery(), data);
    }

    public int delete(String table) throws SQLException{
        query = new Query();
        query.delete(table);

        return query(query.getQuery(), null);
    }

    private int insert(String table, List<Object> data) throws SQLException{
        query = new Query();
        query.insert(table)
                .values(data);
        return query(query.getQuery(), data);
    }

    private int create(String table, List<String[]> fields) throws SQLException{
        query = new Query();
        query.create(table)
                .fields(fields);
        System.out.println(query.getQuery());
        return query(query.getQuery(), null);
    }

    public <T> void insertData(T object) throws Exception
    {
        Class<?> clazz = object.getClass();
        String tableName = ObjectReflector.getTableName(clazz);
        List<String> tableColumns = ObjectReflector.getColumnNames(clazz);

        List<Object> data = ObjectReflector.getObjectData(object,new ArrayList<Object>());

        int result = insert(tableName,data);

        //System.out.println(query);

    }
    public <T> void deleteData(T object) throws Exception
    {
        Class<?> clazz = object.getClass();
        String tableName = ObjectReflector.getTableName(clazz);
        //List<String> tableColumns = ObjectReflector.getColumnNames(clazz);
        //List<Object> data = ObjectReflector.getObjectData(object,new ArrayList<Object>());

        int result = delete(tableName);

        //System.out.println(query);
    }

    public <T> void createTable(T object) throws Exception
    {
        Class<?> clazz = object.getClass();
        String tableName = ObjectReflector.getTableName(clazz);
        //List<String> tableColumns = ObjectReflector.getColumnNames(clazz);

        List<String[]> data = ObjectReflector.getFields(object);

        int result = create(tableName,data);

        //System.out.println(query);

    }

}

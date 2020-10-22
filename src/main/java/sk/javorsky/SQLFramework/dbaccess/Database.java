package sk.javorsky.SQLFramework.dbaccess;

import sk.javorsky.CreateQuerys;
import sk.javorsky.SQLFramework.reflection.ObjectReflector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Database {

    private Connection conn = null;
    private PropertiesReader propertiesReader = new PropertiesReader();
    private String tableName;
     //instance třídy query
    //protected Query query;
    private CreateQuerys createQuerys;

    public Database(String tableName,String fileProperties) throws Exception {
        String user = propertiesReader.getProperty(PropertiesReader.DB_USER,fileProperties);
        String psw = propertiesReader.getProperty(PropertiesReader.DB_PSW,fileProperties);
        String driver = propertiesReader.getProperty(PropertiesReader.DB_DRIVER,fileProperties);
        String url = propertiesReader.getProperty(PropertiesReader.DB_URL,fileProperties);
        this.tableName = tableName;
        connect(user,psw, driver, url);
        createQuerys = new CreateQuerys();
    }

    public CreateQuerys getCreateQuerys() {
        return createQuerys;
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
        return query(createQuerys.delete(table,requirement,data), data);
    }

    public int delete(String table) throws SQLException{
        return query(createQuerys.delete(table), null);
    }

    private int insert(String table, List<Object> data) throws SQLException{

        return query(createQuerys.insert(table,data), data);
    }

    private int create(String table, List<String[]> items) throws SQLException{
         //System.out.println(query.getQuery());
        return query(createQuerys.create(table,items), null);
    }

    public <T> void insertData(T object) throws Exception
    {
        Class<?> clazz = object.getClass();
        //String tableName = ObjectReflector.getTableName(clazz);
        List<String> tableColumns = ObjectReflector.getColumnNames(clazz);

        List<Object> data = ObjectReflector.getObjectData(object,new ArrayList<Object>());

        int result = insert(tableName,data);

        //System.out.println(query);

    }
    public <T> void deleteData(T object) throws Exception
    {
        Class<?> clazz = object.getClass();
        //String tableName = ObjectReflector.getTableName(clazz);
        //List<String> tableColumns = ObjectReflector.getColumnNames(clazz);
        //List<Object> data = ObjectReflector.getObjectData(object,new ArrayList<Object>());

        int result = delete(tableName);

        //System.out.println(query);
    }

    public <T> int createTable(T object) throws Exception
    {
        //Class<?> clazz = object.getClass();
        //String tableName = ObjectReflector.getTableName(clazz);
        //List<String> tableColumns = ObjectReflector.getColumnNames(clazz);

        List<String[]> data = ObjectReflector.getFields(object);

        return create(tableName,data);
    }

    public <T> String createTableQuery(T object) throws Exception
    {
        List<String[]> data = ObjectReflector.getFields(object);
        return createQuerys.create(tableName,data);
    }

}

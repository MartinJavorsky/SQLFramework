package sk.javorsky.SQLFramework.dbaccess;

import java.sql.*;

public class DatabaseAccess {

    private Connection conn = null;
    private PropertiesReader propertiesReader = new PropertiesReader();

    DatabaseAccess() throws Exception {
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

    public ResultSet executeQuery(String query) throws Exception {
        Statement st = getConn().createStatement();
        ResultSet rs = st.executeQuery(query);
        return rs;
    }
    public void executeUpdate(String query) throws Exception {
            Statement st = getConn().createStatement();
            st.executeUpdate(query);
    }
}

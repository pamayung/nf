package com.wildhan.nf.core.db;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class DB {

    private String url;

    private String user;

    private String password;

    private int min;

    private int max;

    private static final Object SYNC = new Object();

    private List<Connection> connections = Collections.synchronizedList(new ArrayList<Connection>());

    private List<Connection> usedConnections = Collections.synchronizedList(new ArrayList<Connection>());

    private static AtomicLong atomic = new AtomicLong(System.nanoTime());

    public static DB db;

    public static DB getInstance() {
        if (db == null) {
            synchronized (SYNC) {
                if (db == null) {
                    db = new DB();
                }
            }
        }
        return db;
    }

    private DB() {
        this.url = System.getenv("DB_URL");
        this.user = System.getenv("DB_USER");
        this.password = System.getenv("DB_PASS");
        this.min = 5;
        this.max = 10;
    }

    public void create() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
        for (int i = 0; i < this.min; i++) {
            connections.add(createConnection());
        }
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public Connection getConnection() throws SQLException {
        if (connections.isEmpty())
            if (usedConnections.size() < max)
                connections.add(createConnection());
            else
                throw new RuntimeException("Maximum pool size reached, no available connections!");

        Connection connection = connections.remove(connections.size() - 1);
        usedConnections.add(connection);

        return connection;
    }

    public long generateId() {
        return atomic.getAndIncrement();
    }

    public boolean releaseConnection(Connection connection) {
        if (connection == null)
            return false;

        connections.add(connection);

        return usedConnections.remove(connection);
    }

    public ArrayList<Object[]> getRows(String sql){
        ArrayList<Object[]> rows = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                int columnCount = resultSet.getMetaData().getColumnCount();
                Object[] columns = new Object[columnCount];
                for (int i = 0; i < columnCount; i++){
                    Object sResult = resultSet.getObject(i + 1);
                    columns[i] = sResult == null ? "" : sResult;
                }
                rows.add(columns);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException e) { }
            }

            if (statement != null){
                try {
                    statement.close();
                } catch (SQLException e) { }
            }

            if (connection != null){
                releaseConnection(connection);
            }
        }

        return rows;
    }

    public Object[] getRow(String sql){
        Object[] columns = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            if (resultSet.next()){
                int columnCount = resultSet.getMetaData().getColumnCount();
                columns = new Object[columnCount];
                for (int i = 0; i < columnCount; i++){
                    Object sResult = resultSet.getObject(i + 1);
                    columns[i] = sResult == null ? "" : sResult;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException e) { }
            }

            if (statement != null){
                try {
                    statement.close();
                } catch (SQLException e) { }
            }

            if (connection != null){
                releaseConnection(connection);
            }
        }

        return columns;
    }

    public int insertRow(String sql){
        Connection connection = null;
        Statement statement = null;
        int result = 0;

        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            statement = connection.createStatement();

            result = statement.executeUpdate(sql);
        }
        catch (Exception e){
            try {
                connection.rollback();
            } catch (SQLException ex) {}
            e.printStackTrace();
        }
        finally {
            if (statement != null){
                try {
                    statement.close();
                } catch (SQLException e) {}
            }

            if (connection != null){
                try {
                    connection.commit();
                } catch (SQLException e) {}
                releaseConnection(connection);
            }
        }

        return result;
    }

    public int insertRowResult(String sql){
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        int result = 0;

        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            statement = connection.createStatement();

            resultSet = statement.executeQuery(sql);

            if (resultSet.next()){
                result = resultSet.getInt(1);
            }
        }
        catch (Exception e){
            try {
                connection.rollback();
            } catch (SQLException ex) {}
            e.printStackTrace();
        }
        finally {
            if (resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException e) { }
            }

            if (statement != null){
                try {
                    statement.close();
                } catch (SQLException e) {}
            }

            if (connection != null){
                try {
                    connection.commit();
                } catch (SQLException e) {}
                releaseConnection(connection);
            }
        }

        return result;
    }
}

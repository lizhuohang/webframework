package com.lzh.webframework.simple.helper;

import com.lzh.webframework.simple.utils.CollectionUtil;
import com.lzh.webframework.simple.utils.DbNameUtil;
import com.lzh.webframework.simple.utils.PropsUtil;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by lizhuohang on 17/8/14.
 */
public class DataBaseHelper {
    private static final Logger logger = LoggerFactory.getLogger(DataBaseHelper.class);

    private static final QueryRunner QUERY_RUNNER;

    private static final ThreadLocal<Connection> CONNECTION_HOLDER;

    private static final BasicDataSource DATA_SOURCE;

    static {

        CONNECTION_HOLDER = new ThreadLocal<>();

        QUERY_RUNNER = new QueryRunner();

        Properties properties = PropsUtil.loadProps("config.properties");
        String driver = properties.getProperty("jdbc.driver");
        String url = properties.getProperty("jdbc.url");
        String userName = properties.getProperty("jdbc.username");
        String password = properties.getProperty("jdbc.password");

        DATA_SOURCE = new BasicDataSource();
        DATA_SOURCE.setDriverClassName(driver);
        DATA_SOURCE.setUrl(url);
        DATA_SOURCE.setUsername(userName);
        DATA_SOURCE.setPassword(password);

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            logger.error("load jdbc driver error", e);
        }
    }

    /**
     * 获取数据库链接
     */
    private static Connection getConnection() {
        Connection conn = CONNECTION_HOLDER.get();
        if (conn == null) {
            try {
                conn = DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                logger.error("get connection failure", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.set(conn);
            }
        }
        return conn;
    }

    /**
     * 查询实体列表
     *
     * @param entityClass
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params) {
        List<T> entityList;
        try {
            Connection conn = getConnection();
            entityList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<T>(entityClass), params);
        } catch (SQLException e) {
            logger.error("query entity list failure", e);
            throw new RuntimeException(e);
        }
        return entityList;
    }

    /**
     * 查询单个实体
     *
     * @param entityClass
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public static <T> T queryEntity(Class<T> entityClass, String sql, Object... params) {
        T entity;
        try {
            Connection conn = getConnection();
            entity = QUERY_RUNNER.query(conn, sql, new BeanHandler<T>(entityClass), params);
        } catch (SQLException e) {
            logger.error("query entity failure", e);
            throw new RuntimeException(e);
        }
        return entity;
    }

    /**
     * 执行sql语句
     *
     * @param sql
     * @param params
     * @return
     */
    private static List<Map<String, Object>> executeQuery(String sql, Object... params) {
        List<Map<String, Object>> result;
        try {
            Connection conn = getConnection();
            result = QUERY_RUNNER.query(conn, sql, new MapListHandler(), params);
        } catch (SQLException e) {
            logger.error("execute query failure", e);
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 执行更新操作，包括update，delete，insert
     *
     * @param sql
     * @param params
     * @return
     */
    public static int executeUpdate(String sql, Object... params) {
        int rows = 0;
        try {
            Connection conn = getConnection();
            rows = QUERY_RUNNER.update(conn, sql, params);
        } catch (SQLException e) {
            logger.error("execute update failure", e);
        }
        return rows;
    }

    /**
     * 向DB中插入一个实体记录
     *
     * @param entityClass
     * @param fieldMap
     * @param <T>
     * @return
     */
    public static <T> boolean insertEntity(Class<T> entityClass, Map<String, Object> fieldMap) {
        if (CollectionUtil.isEmpty(fieldMap)) {
            logger.error("can not insert entity: fieldMap is empty");
            return false;
        }

        String sql = "insert into " + DbNameUtil.getTableName(entityClass) + " ";
        StringBuilder columns = new StringBuilder("(");
        StringBuilder values = new StringBuilder("(");
        for (String fieldName : fieldMap.keySet()) {
            columns.append(fieldName).append(",");
            values.append("?,");
        }
        columns.replace(columns.lastIndexOf(","), columns.length(), ")");
        values.replace(values.lastIndexOf(","), values.length(), ")");

        sql += columns + " values " + values;

        Object[] paraams = fieldMap.values().toArray();
        return executeUpdate(sql, paraams) == 1;
    }

    /**
     * 通过 id 更新DB中数据信息
     *
     * @param entityClass
     * @param id
     * @param fieldMap
     * @param <T>
     * @return
     */
    public static <T> boolean updateEntity(Class<T> entityClass, long id, Map<String, Object> fieldMap) {
        if (CollectionUtil.isEmpty(fieldMap)) {
            logger.error("can not update entity: fieldMap is empty");
            return false;
        }
        String sql = "update " + DbNameUtil.getTableName(entityClass) + " set ";
        StringBuilder columns = new StringBuilder();
        for (String fieldName : fieldMap.keySet()) {
            columns.append(fieldName).append("=?, ");
        }
        sql += columns.substring(0, columns.lastIndexOf(", ")) + " where id = ? ";
        List<Object> paramList = new ArrayList<>();
        paramList.addAll(fieldMap.values());
        paramList.add(id);
        Object[] params = paramList.toArray();

        return executeUpdate(sql, params) == 1;
    }

    /**
     * 通过 id 删除对应实体
     *
     * @param entityClass
     * @param id
     * @param <T>
     * @return
     */
    public static <T> boolean deleteEntity(Class<T> entityClass, long id) {
        String sql = "delete from " + DbNameUtil.getTableName(entityClass) + " where id = ? ";
        return executeUpdate(sql, id) == 1;
    }

    /**
     * 读取并执行sql文件
     *
     * @param filePath classPath下的文件路径
     */
    public static void executeSqlFile(String filePath) {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
        try {
            String sql;
            while ((sql = bufferedReader.readLine()) != null) {
                executeUpdate(sql);
            }
        } catch (IOException e) {
            logger.error("execute sql file failure", e);
            throw new RuntimeException(e);
        }
    }
}

package com.ipads.bookadmin.DAO.impl;

import com.ipads.bookadmin.DAO.BookDAO;
import com.ipads.bookadmin.entity.Book;
import com.sun.deploy.net.HttpRequest;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BookDAOImpl extends AbstractDAOImpl implements BookDAO {

    public BookDAOImpl(Connection conn) {
        super(conn);
    }

    String baseUrl = "http://localhost:8080/jaxrs/books/";

    @Override
    public boolean doCreate(Book vo) throws SQLException{
//        String sql = "INSERT INTO books(iid,aid,name,credate,status,note)VALUES(?,?,?,?,?,?)";
//        super.pstmt = super.conn.prepareStatement(sql);
//        super.pstmt.setString(3,vo.getName());
//        super.pstmt.setInt(5,vo.getStatus());
//        super.pstmt.setString(6,vo.getNote());
//        return super.pstmt.executeUpdate() > 0;
        System.out.print("bookDao doCreate\n\n\n\n");
        URL url = null;
        try {
            url = new URL(baseUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection httpConn = null;
        try {
            httpConn = (HttpURLConnection)url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        httpConn.setDoOutput(true);     //需要输出
        httpConn.setDoInput(true);      //需要输入
        httpConn.setUseCaches(false);   //不允许缓存
        try {
            httpConn.setRequestMethod("POST");      //设置POST方式连接
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(httpConn.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = JSONObject.fromObject(vo);
        System.out.println(jsonObject.toString()); // debug statement
        try {
            dos.writeBytes(jsonObject.toString());
            dos.flush();
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int resultCode = 0;
        StringBuffer sb = new StringBuffer();
        try {
            resultCode = httpConn.getResponseCode();

            if (HttpURLConnection.HTTP_OK == resultCode) {

                String readLine = new String();
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
                while ((readLine = responseReader.readLine()) != null) {
                    sb.append(readLine).append("\n");
                }
                responseReader.close();
                System.out.println(sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (sb.toString().matches("true"))
            return true;

        return false;

    }

    @Override
    public boolean doUpdate(Book vo) throws SQLException {
        return false;
    }

    @Override
    public boolean doRemove(Set<?> ids) throws SQLException {
        return false;
    }

    @Override
    public Book findById(Integer id) throws SQLException {
        return null;
    }

    @Override
    public List<Book> findAll() throws SQLException {
        List<Book> all = new ArrayList<Book>();
        String sql = "SELECT bid,iid,aid,name,credate,status,note FROM books";
        super.pstmt = super.conn.prepareStatement(sql);
        ResultSet rs = super.pstmt.executeQuery();
        while (rs.next()) {
            Book vo = new Book();
            vo.setBid(rs.getInt(1));
            vo.setName(rs.getString(4));
            vo.setStatus(rs.getInt(6));
            vo.setNote(rs.getString(7));
            all.add(vo);
        }
        return all;
    }

    @Override
    public List<Book> findAllBySplit(String column, String keyWord, Integer currentPage, Integer lineSize) throws SQLException {
        return null;
    }

    @Override
    public Integer getAllCount(String column, String keyWord) throws SQLException {
        String sql = "SELECT COUNT(*) FROM books WHERE " + column + " LIKE ?";
        super.pstmt = super.conn.prepareStatement(sql);
        super.pstmt.setString(1, "%" + keyWord + "%");
        ResultSet rs = super.pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }
}

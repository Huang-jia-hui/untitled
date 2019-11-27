//201802104017黄佳慧
package cn.edu.sdjzu.xg.bysj.dao;

import cn.edu.sdjzu.xg.bysj.domain.Degree;
import util.JdbcHelper;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public final class DegreeDao {
    private static DegreeDao degreeDao=
            new DegreeDao();
    private DegreeDao(){}
    public static DegreeDao getInstance(){
        return degreeDao;
    }

    public Set<Degree> findAll() throws SQLException{
        //创建degrees集合对象
        Set<Degree> degrees = new HashSet<Degree>();
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        //在该连接上创建语句盒子对象
        Statement statement = connection.createStatement();
        //创建结果集对象，并执行语句盒子对象的
        ResultSet resultSet = statement.executeQuery("select * from degree");
        //若结果集仍然有下一条记录，则执行循环体
        while (resultSet.next()){
            //以当前记录中的id,description,no,remarks值为参数，创建Degree对象
            Degree degree = new Degree(resultSet.getInt("id"),resultSet.getString("description"),resultSet.getString("no"),resultSet.getString("remarks"));
            //向degrees集合中添加Degree对象
            degrees.add(degree);
        }
        //关闭资源
        JdbcHelper.close(resultSet,statement,connection);
        return degrees;
    }

    public Degree find(Integer id) throws SQLException{
        Degree degree = null;
        Connection connection = JdbcHelper.getConn();
        String deleteDegree_sql = "SELECT * FROM degree WHERE id=?";
        //在该连接上创建预编译语句对象，参数为String类型的sql语句
        PreparedStatement preparedStatement = connection.prepareStatement(deleteDegree_sql);
        //为预编译参数赋值
        preparedStatement.setInt(1,id);
        //创建结果集对象，执行预编译语句对象
        ResultSet resultSet = preparedStatement.executeQuery();
        //由于id不能取重复值，故结果集中最多有一条记录
        //若结果集有一条记录，则以当前记录中的id,description,no,remarks值为参数，创建Degree对象
        //若结果集中没有记录，则本方法返回null
        if (resultSet.next()){
            degree = new Degree(resultSet.getInt("id"),resultSet.getString("description"),resultSet.getString("no"),resultSet.getString("remarks"));
        }
        //关闭资源
        JdbcHelper.close(resultSet,preparedStatement,connection);
        return degree;
    }

    public boolean add(Degree degree) throws SQLException,ClassNotFoundException{
        Connection connection = JdbcHelper.getConn();
        String addDegree_sql = "INSERT INTO degree (description,no,remarks) VALUES(?,?,?)";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(addDegree_sql);
        //为预编译参数赋值
        preparedStatement.setString(1,degree.getDescription());
        preparedStatement.setString(2,degree.getNo());
        preparedStatement.setString(3,degree.getRemarks());
        //执行预编译语句，获取添加记录行数并赋值给affectedRowNum
        int affectedRowNum=preparedStatement.executeUpdate();
        //关闭资源
        JdbcHelper.close(preparedStatement,connection);
        //返回true或false
        return affectedRowNum>0;
    }


    //delete方法，根据degree的id值，删除数据库中对应的degree对象
    public boolean delete(int id) throws ClassNotFoundException,SQLException{
        Connection connection = JdbcHelper.getConn();
        //写sql语句
        String deleteDegree_sql = "DELETE FROM degree WHERE id=?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(deleteDegree_sql);
        //为预编译参数赋值
        preparedStatement.setInt(1,id);
        //执行预编译语句，获取受影响的记录的行数并赋值给affectedRowNum
        int affectedRows = preparedStatement.executeUpdate();
        //关闭资源
        JdbcHelper.close(preparedStatement,connection);
        //返回true或false
        return affectedRows>0;
    }

    public boolean update(Degree degree) throws ClassNotFoundException,SQLException{
        Connection connection = JdbcHelper.getConn();
        //写sql语句
        String updateDegree_sql = " update degree set description=?,no=?,remarks=?where id=?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(updateDegree_sql);
        //为预编译参数赋值
        preparedStatement.setString(1,degree.getDescription());
        preparedStatement.setString(2,degree.getNo());
        preparedStatement.setString(3,degree.getRemarks());
        preparedStatement.setInt(4,degree.getId());
        //执行预编译语句，获取改变记录行数并赋值给affectedRowNum
        int affectedRows = preparedStatement.executeUpdate();
        //关闭资源
        JdbcHelper.close(preparedStatement,connection);
        return affectedRows>0;
    }
/**
    //创建main方法，查询数据库中的对象，并输出
    public static void main(String[] args) throws ClassNotFoundException,SQLException{
        Degree degree1 = DegreeDao.getInstance().find(5);
        System.out.println(degree1);
        degree1.setDescription("硕士");
        DegreeDao.getInstance().update(degree1);
        Degree degree2 = DegreeDao.getInstance().find(5);
        System.out.println(degree2.getDescription());
        Degree degree = new Degree(9,"博士","09","");
        System.out.println(DegreeDao.getInstance().add(degree));

    }
 **/
}


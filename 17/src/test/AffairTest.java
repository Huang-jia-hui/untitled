//201802104017黄佳慧
package test;

import util.JdbcHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AffairTest {
    public static void main(String[] args) {
        //为了更好的模拟insert的操作异常，将school表的no字段添加唯一约束
        //ALTER TABLE school ADD UNIQUE(no);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try{
            connection = JdbcHelper.getConn();
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement("INSERT INTO school(description, no)VALUES (?,?)");
            preparedStatement.setString(1,"土木学院");
            preparedStatement.setString(2,"22");
            //执行第一条INSERT语句
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement("INSERT INTO school(description,no)VALUES(?,?)");
            preparedStatement.setString(1,"管理学院");
            //将第二条记录的no字段值也设为"02",以违反no字段的唯一性约束
            preparedStatement.setString(2,"02");
            //执行第二条insert语句
            preparedStatement.executeUpdate();
            //提交当前连接所做的操作，事务以提交结束
            connection.commit();
        }catch (SQLException e){
            System.out.println(e.getMessage()+"\n errorCode="+e.getErrorCode());
            try{
                //回滚当前连接所做的操作，命令把数据库恢复到事务开始时的状态，事务以回滚结束
                if(connection != null){
                    connection.rollback();
                }
            }catch (SQLException e1){
                e1.printStackTrace();
            }
        }finally {
            try{
                //恢复自动提交
                if(connection != null){
                    connection.setAutoCommit(true);
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
            //关闭资源
            JdbcHelper.close(preparedStatement,connection);
        }
    }
}

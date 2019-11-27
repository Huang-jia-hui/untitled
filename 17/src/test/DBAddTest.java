//20180210417黄佳慧
package test;
import java.sql.*;

public class DBAddTest {
    public static void main(String[] args)throws ClassNotFoundException, SQLException {
        //加载驱动程序
        Class.forName("com.mysql.jdbc.Driver");
        //url是数据库连接字串，jdbc是协议，mysql是子协议，localhost：3306是数据库服务器的地址和端口
        String url = "jdbc:mysql://localhost:3306/bysj"+"?useUnicode=true&characterEncoding=utf8"//指定编码为utf8
                +"&serverTimezone=Asia/Shanghai";//服务器区是中国上海
        //用户名
        String username = "root";
        //密码
        String password = "4017";
        //获得连接对象
        Connection connection = DriverManager.getConnection(url,username,password);
        //在该连接上创建语句盒子对象
        Statement stmt = connection.createStatement();
        //创建sql语句
        String addDegree_sql = "INSERT INTO degree(description,no,remarks)VALUES"+" ('博士','01','best')";
        //String deleteDegree_sql = "DELETE FROM DEGREE WHERE ID = 3";
        //令语句盒子执行sql语句
        stmt.execute(addDegree_sql);
        //执行sql查询语句并获得结果集对象
        ResultSet resultSet = stmt.executeQuery("select * from degree");
        //若结果集仍然有下一条记录，则执行循环体
        while (resultSet.next()){
            //打印结果集中当前记录的id字段值
            System.out.print(resultSet.getInt("id"));
            System.out.print(",");
            //打印description字段值
            System.out.print(resultSet.getString("description"));
            System.out.print(",");
            //打印当前记录的no字段值
            System.out.print(resultSet.getString("no"));
            System.out.print(",");
            //打印当前记录的remarks字段值
            System.out.print(resultSet.getString("remarks"));
        }
        //关闭stmt对象
        stmt.close();
        //关闭connection对象，只要建立了连接，必须关闭
        connection.close();
    }
}

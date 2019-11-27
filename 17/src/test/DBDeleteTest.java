//201802104017黄佳慧
package test;
import java.sql.*;

public class DBDeleteTest {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        //加载驱动程序
        Class.forName("com.mysql.jdbc.Driver");
        //url是数据库连接字串，jdbc是协议，mysql是子协议，localhost：3306是数据库服务器的地址和端口
        String url = "jdbc:mysql://localhost:3306/bysj" + "?useUnicode=true&characterEncoding=utf8"//指定编码为utf8
                + "&serverTimezone=Asia/Shanghai";//服务器区是中国上海
        //用户名
        String username = "root";
        //密码
        String password = "4017";
        //获得连接对象
        Connection connection = DriverManager.getConnection(url, username, password);
        //创建sql语句
        String deleteDegree_sql = "DELETE FROM degree where id = ?";
        //在该连接上创建预编译语句对象
        PreparedStatement pstmt = connection.prepareStatement(deleteDegree_sql);
        //为预编译参数赋值
        pstmt.setInt(1,2);
        //执行预编译对象的executeUpdate方法，获取删除的记录行数
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("删除了"+affectedRowNum+"条记录");
        //关闭pstmt对象
        pstmt.close();
        //关闭connection对象，只要建立了连接就要关闭
        connection.close();
    }
}
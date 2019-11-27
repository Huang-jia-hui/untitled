//201802104017黄佳慧
package util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 提供JDBC连接对象和释放资源
 */
public final class JdbcHelper {
	//private static String url =
	// "jdbc:sqlserver://localhost:1433;databaseName=bysjs;SelectMethod=Cursor;";

	//url是数据库连接字串，jdbc是协议，mysql是子协议，localhost：3306是数据库服务器的地址和端口
	private static String url = "jdbc:mysql://localhost:3306/bysj" + "?useUnicode=true&characterEncoding=utf8"//指定编码为utf8
			+ "&serverTimezone=Asia/Shanghai";//服务器区是中国上海
	//用户名
	private static String user = "root";
	//密码
	private static String password = "4017";

	// 不允许创建本类对象
	private JdbcHelper() {}
	
	//注册驱动
	static {
		try {
			//Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			//注册驱动程序
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("未找到驱动程序类");
		}
	}

	/**
	 * @return 连接对象
	 * @throws SQLException
	 */
	public static Connection getConn() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}
	//关闭、释放资源
	public static void close(ResultSet rs, Statement stmt, Connection conn) {
		try {
			if (rs != null) {	rs.close();	}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null){	stmt.close();}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null){	conn.close();}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//关闭、释放资源
	public static void close(Statement stmt, Connection conn) {
		JdbcHelper.close(null,stmt,conn);
	}
}
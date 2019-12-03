//201802104017黄佳慧
package cn.edu.sdjzu.xg.bysj.dao;

import cn.edu.sdjzu.xg.bysj.domain.Teacher;
import cn.edu.sdjzu.xg.bysj.domain.User;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public final class UserDao {
	private static UserDao userDao=new UserDao();
	private UserDao(){}
	public static UserDao getInstance(){
		return userDao;
	}

	public Set<User> findAll() throws SQLException{
		Set<User> users = new HashSet<User>();
		Connection connection = JdbcHelper.getConn();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM user");
		while(resultSet.next()){
			Teacher teacher = TeacherDao.getInstance().find(resultSet.getInt("teacher_id"));
			User user = new User(resultSet.getInt("id"),resultSet.getString("username"),resultSet.getString("password"),resultSet.getDate("loginTime"),teacher);
			users.add(user);
		}
		JdbcHelper.close(resultSet,statement,connection);
		return users;
	}
	public boolean changePassword(User user) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String changePassword_sql = " update user set password=? where id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(changePassword_sql);
		//为预编译参数赋值
		preparedStatement.setString(1, user.getPassword());
		preparedStatement.setInt(2, user.getId());
		//执行预编译语句，获取改变记录行数并赋值给affectedRowNum
		int affectedRows = preparedStatement.executeUpdate();
		//关闭资源
		JdbcHelper.close(preparedStatement, connection);
		return affectedRows > 0;
	}
    //按id查询findByUsername
    public User find(Integer id) throws SQLException {
        User user = null;
        Connection connection = JdbcHelper.getConn();
        String selectUser_sql = "SELECT * FROM user WHERE id=?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(selectUser_sql);
        //为预编译参数赋值
        preparedStatement.setInt(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        //由于id不能取重复值，故结果集中最多有一条记录
        //若结果集有一条记录，则以当前记录中的id,description,no,remarks，school值为参数，创建Department对象
        //若结果集中没有记录，则本方法返回null
        if (resultSet.next()){
            Teacher teacher = TeacherDao.getInstance().find(resultSet.getInt("teacher_id"));
            user = new User(resultSet.getInt("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    teacher);
        }
        //关闭资源
        JdbcHelper.close(resultSet,preparedStatement,connection);
        return user;
    }
    //按用户名查询
    public User findByUsername(String userName) throws SQLException {
        User user = null;
        Connection connection = JdbcHelper.getConn();
        String selectUser_sql = "SELECT * FROM user WHERE username=?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(selectUser_sql);
        //为预编译参数赋值
        preparedStatement.setString(1,userName);
        ResultSet resultSet = preparedStatement.executeQuery();
        //由于id不能取重复值，故结果集中最多有一条记录
        //若结果集有一条记录，则以当前记录中的id,description,no,remarks，school值为参数，创建Department对象
        //若结果集中没有记录，则本方法返回null
        if (resultSet.next()){
            Teacher teacher = TeacherDao.getInstance().find(resultSet.getInt("teacher_id"));
            user = new User(resultSet.getInt("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    teacher);
        }
        //关闭资源
        JdbcHelper.close(resultSet,preparedStatement,connection);
        return user;
    }
	//登录User login(String username, String password)
	// 如果有同时满足用户名和密码的记录，返回该记录对应的User对象，否则返回null.
	public User login(String username, String password)throws SQLException{
		User desiredUser = null;
		Connection connection = JdbcHelper.getConn();
		String findUser_sql = "SELECT * FROM user where username=? and password=?";
		PreparedStatement preparedStatement = connection.prepareStatement(findUser_sql);
		preparedStatement.setString(1,username);
		preparedStatement.setString(2,password);
		ResultSet resultSet = preparedStatement.executeQuery();
		if(resultSet.next()){
			desiredUser = new User(resultSet.getInt("id"),resultSet.getString("username"),resultSet.getString("password"),resultSet.getDate("loginTime"), TeacherDao.getInstance().find(resultSet.getInt("teacher_id")));
		}
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return desiredUser;
	}
	public static void main(String[] args) throws SQLException {
		UserDao dao = new UserDao();
		Collection<User> users = dao.findAll();
		display(users);
	}

	private static void display(Collection<User> users) {
		for (User user : users) {
			System.out.println(user);
		}
	}
}

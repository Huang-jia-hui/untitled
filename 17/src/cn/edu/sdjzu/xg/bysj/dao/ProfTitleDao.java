//201802104017黄佳慧
package cn.edu.sdjzu.xg.bysj.dao;


import cn.edu.sdjzu.xg.bysj.domain.ProfTitle;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public final class ProfTitleDao {
	private static ProfTitleDao profTitleDao=new ProfTitleDao();
	private ProfTitleDao(){}
	public static ProfTitleDao getInstance(){
		return profTitleDao;
	}
	public Set<ProfTitle> findAll() throws SQLException{
		Set<ProfTitle> profTitles = new HashSet<ProfTitle>();
		Connection connection = JdbcHelper.getConn();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM proftitle");
		while (resultSet.next()){
			ProfTitle profTitle = new ProfTitle(resultSet.getInt("id"),resultSet.getString("description"),resultSet.getString("no"),resultSet.getString("remarks"));
			profTitles.add(profTitle);
		}
		JdbcHelper.close(resultSet,statement,connection);
		return profTitles;
	}
	public ProfTitle find(Integer id) throws SQLException {
		ProfTitle profTitle = null;
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		String selectedProfTitle_sql = "SELECT * FROM proftitle WHERE id = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(selectedProfTitle_sql);
		preparedStatement.setInt(1,id);
		//执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
		ResultSet resultSet = preparedStatement.executeQuery();
		//若结果集仍然有下一条记录，则执行循环体
		if (resultSet.next()){
			//创建ProfTitle对象，根据遍历结果中的id,description,no,remarks值
			profTitle = new ProfTitle(resultSet.getInt("id"),resultSet.getString("description"),resultSet.getString("no"),resultSet.getString("remarks"));
		}
		//关闭资源
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return profTitle;
	}

	public boolean update(ProfTitle profTitle) throws SQLException,ClassNotFoundException {
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		String updateProfTitle_sql = "UPDATE proftitle SET description=?,no=?,remarks=?WHERE id = ?";
		//创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(updateProfTitle_sql);
		preparedStatement.setString(1,profTitle.getDescription());
		preparedStatement.setString(2,profTitle.getNo());
		preparedStatement.setString(3,profTitle.getRemarks());
		//为参数赋值
		preparedStatement.setInt(4,profTitle.getId());
		//执行修改语句
		int affectedRowNum = preparedStatement.executeUpdate();
		System.out.println("修改了 "+affectedRowNum+" 条");
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum > 0;
	}

	public boolean add(ProfTitle profTitle) throws SQLException,ClassNotFoundException{
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		String addProfTitle_sql = "INSERT INTO proftitle(description,no,remarks)VALUES (?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(addProfTitle_sql);
		preparedStatement.setString(1,profTitle.getDescription());
		preparedStatement.setString(2,profTitle.getNo());
		preparedStatement.setString(3,profTitle.getRemarks());
		//获得第4个参数的值：数据库为该记录自动生成的id
		int affectedRowNum = preparedStatement.executeUpdate();
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum>0;
	}

	public boolean delete(Integer id) throws SQLException,ClassNotFoundException {
		Connection connection = JdbcHelper.getConn();
		//创建sql语句，“？”作为占位符
		String deleteProfTitle_sql = "DELETE FROM proftitle WHERE id = ?";
		//创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
		PreparedStatement pstmt = connection.prepareStatement(deleteProfTitle_sql);
		//为预编译的语句参数赋值
		pstmt.setInt(1,id);
		//执行预编译对象的executeUpdate()方法，获取删除记录的行数
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("删除了 "+affectedRowNum+" 条");
		//关闭资源
		JdbcHelper.close(pstmt,connection);
		return affectedRowNum > 0;
	}
}


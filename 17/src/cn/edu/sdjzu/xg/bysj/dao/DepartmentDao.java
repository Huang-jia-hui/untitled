package cn.edu.sdjzu.xg.bysj.dao;


import cn.edu.sdjzu.xg.bysj.domain.Department;
import cn.edu.sdjzu.xg.bysj.domain.School;
import util.JdbcHelper;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public final class DepartmentDao {
	private static DepartmentDao departmentDao=new DepartmentDao();
	private DepartmentDao(){}

	public static DepartmentDao getInstance(){
		return departmentDao;
	}
	public Set<Department> findAll() throws SQLException{
        Set<Department> departments = new HashSet<Department>();
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        Statement statement = connection.createStatement();
        //执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
        ResultSet resultSet = statement.executeQuery("select * from department");
        //若结果集仍然有下一条记录，则执行循环体
        while (resultSet.next()){
            School school = SchoolDao.getInstance().find(resultSet.getInt("school_id"));
            //创建Department对象，根据遍历结果中的id,description,no,remarks，school值
            Department department = new Department(resultSet.getInt("id"),resultSet.getString("description"),resultSet.getString("no"),resultSet.getString("remarks"),school);
            //向departments集合中添加Department对象
            departments.add(department);
        }
        //关闭资源
        JdbcHelper.close(resultSet,statement,connection);
        return departments;
    }
    public Set<Department> findAllBySchool(Integer schoolId)throws SQLException{
		Set<Department> departments = new HashSet<Department>();
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = connection.prepareStatement("select * from department where school_id = ?");
		preparedStatement.setInt(1,schoolId);
		ResultSet resultSet = preparedStatement.executeQuery();
		//若结果集仍然有下一条记录，则执行循环体
		while (resultSet.next()){
			School school = SchoolDao.getInstance().find(resultSet.getInt("school_id"));
			//创建Department对象，根据遍历结果中的id,description,no,remarks，school值
			Department department = new Department(resultSet.getInt("id"),resultSet.getString("description"),resultSet.getString("no"),resultSet.getString("remarks"),school);
			//向departments集合中添加Department对象
			departments.add(department);
		}
		//关闭资源
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return departments;
	}
	public Department find(Integer id)throws SQLException {
        Department department = null;
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        String selectDepartment_sql = "SELECT * FROM department WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(selectDepartment_sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
        	//通过
            School school = SchoolDao.getInstance().find(resultSet.getInt("school_id"));
            department = new Department(resultSet.getInt("id"), resultSet.getString("description"), resultSet.getString("no"), resultSet.getString("remarks"), school);
        }
        JdbcHelper.close(resultSet, preparedStatement, connection);
        return department;
    }
	public boolean update(Department department)throws SQLException{
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		String updateDepartment_sql = "UPDATE department SET description=?,no=?,remarks=?,school_id=? where id = ?";
		//创建预编译语句对象，包装编译后的目标代码
		PreparedStatement preparedStatement = connection.prepareStatement(updateDepartment_sql);
		preparedStatement.setString(1,department.getDescription());
		preparedStatement.setString(2,department.getNo());
		preparedStatement.setString(3,department.getRemarks());
		preparedStatement.setInt(4,department.getSchool().getId());
		preparedStatement.setInt(5,department.getId());
		//执行修改语句
		int affectedRowNum = preparedStatement.executeUpdate();
		System.out.println("修改了 "+affectedRowNum+" 条");
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum > 0;
	}

		public boolean add(Department department) throws SQLException,ClassNotFoundException{
			//获得连接对象
			Connection connection = JdbcHelper.getConn();
			String addDepartment_sql = "INSERT INTO department(description,no,remarks,school_id)VALUES (?,?,?,?)";
			PreparedStatement preparedStatement = connection.prepareStatement(addDepartment_sql);
			preparedStatement.setString(1,department.getDescription());
			preparedStatement.setString(2,department.getNo());
			preparedStatement.setString(3,department.getRemarks());
			preparedStatement.setInt(4,department.getSchool().getId());
			//获得第4个参数的值：数据库为该记录自动生成的id
			int affectedRowNum = preparedStatement.executeUpdate();
			JdbcHelper.close(preparedStatement,connection);
			return affectedRowNum>0;
		}

	public boolean delete(Integer id)throws SQLException{
		Connection connection = JdbcHelper.getConn();
		//创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
		PreparedStatement pstmt = connection.prepareStatement("DELETE FROM DEPARTMENT WHERE ID = ?");
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


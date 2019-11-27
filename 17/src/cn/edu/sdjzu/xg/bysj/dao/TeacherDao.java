package cn.edu.sdjzu.xg.bysj.dao;
import cn.edu.sdjzu.xg.bysj.domain.Degree;
import cn.edu.sdjzu.xg.bysj.domain.Department;
import cn.edu.sdjzu.xg.bysj.domain.ProfTitle;
import cn.edu.sdjzu.xg.bysj.domain.Teacher;
import util.JdbcHelper;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;
public final class TeacherDao {
    private static TeacherDao teacherDao=new TeacherDao();
    private TeacherDao(){}
    public static TeacherDao getInstance(){
        return teacherDao;
    }
    public Set<Teacher> findAll() throws SQLException{
        Set<Teacher> teachers = new HashSet<Teacher>();
        Connection connection = JdbcHelper.getConn();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM teacher");
        while(resultSet.next()){
            ProfTitle profTitle = ProfTitleDao.getInstance().find(resultSet.getInt("title_id"));
            Degree degree = DegreeDao.getInstance().find(resultSet.getInt("degree_id"));
            Department department = DepartmentDao.getInstance().find(resultSet.getInt("dept_id"));
            Teacher teacher = new Teacher(resultSet.getInt("id"),resultSet.getString("name"),profTitle,degree,department);
            teachers.add(teacher);
        }
        JdbcHelper.close(resultSet,statement,connection);
        return teachers;
    }
    public Teacher find(Integer id) throws SQLException{
        Teacher teacher = null;
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        String selectedTeacher_sql = "SELECT * FROM teacher WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(selectedTeacher_sql);
        preparedStatement.setInt(1,id);
        //执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
        ResultSet resultSet = preparedStatement.executeQuery();
        //若结果集仍然有下一条记录，则执行循环体
        if (resultSet.next()){
            //找到对应id号的profTitle
            ProfTitle profTitle = ProfTitleDao.getInstance().find(resultSet.getInt("title_id"));
            //找到对应id号的degree
            Degree degree = DegreeDao.getInstance().find(resultSet.getInt("degree_id"));
            //找到对应id号的department
            Department department = DepartmentDao.getInstance().find(resultSet.getInt("dept_id"));
            //创建Teacher对象，根据遍历结果中的id,name,title_id,degree_id,dept_id值
            teacher = new Teacher(resultSet.getInt("id"),resultSet.getString("name"),profTitle,degree,department);
        }
        JdbcHelper.close(resultSet,preparedStatement,connection);
        return teacher;
    }

    public boolean update(Teacher teacher) throws SQLException {
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        String updateTeacher_sql = "UPDATE teacher SET name=?,title_id=?,degree_id=?,dept_id=? where id = ?";
        //创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(updateTeacher_sql);
        //为参数赋值
        preparedStatement.setString(1,teacher.getName());
        preparedStatement.setInt(2,teacher.getTitle().getId());
        preparedStatement.setInt(3,teacher.getDegree().getId());
        preparedStatement.setInt(4,teacher.getDepartment().getId());
        preparedStatement.setInt(5,teacher.getId());
        //执行修改操作
        int affectedRowNum = preparedStatement.executeUpdate();
        System.out.println("修改了 "+affectedRowNum+" 条");
        //关闭资源
        JdbcHelper.close(preparedStatement,connection);
        return affectedRowNum > 0;
    }

    public boolean add(Teacher teacher) throws SQLException,ClassNotFoundException{
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        String addTeacher_sql = "INSERT INTO teacher(name,title_id,degree_id,dept_id)VALUES(?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(addTeacher_sql);
        preparedStatement.setString(1,teacher.getName());
        preparedStatement.setInt(2,teacher.getTitle().getId());
        preparedStatement.setInt(3,teacher.getDegree().getId());
        preparedStatement.setInt(4,teacher.getDepartment().getId());
        int affectedRowNum = preparedStatement.executeUpdate();
        JdbcHelper.close(preparedStatement,connection);
        return affectedRowNum>0;
    }

    public boolean delete(Integer id) throws SQLException {
        Connection connection = JdbcHelper.getConn();
        //创建sql语句，“？”作为占位符
        String deleteTeacher_sql = "DELETE FROM TEACHER WHERE ID = ?";
        //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
        PreparedStatement pstmt = connection.prepareStatement(deleteTeacher_sql);
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

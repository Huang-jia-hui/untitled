//201802104017黄佳慧
package cn.edu.sdjzu.xg.bysj.dao;
import cn.edu.sdjzu.xg.bysj.domain.*;
import util.JdbcHelper;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.sql.Date;
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
            Teacher teacher = new Teacher(resultSet.getInt("id"),resultSet.getString("name"),resultSet.getString("no"),profTitle,degree,department);
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
            teacher = new Teacher(resultSet.getInt("id"),resultSet.getString("name"),resultSet.getString("no"),profTitle,degree,department);
        }
        JdbcHelper.close(resultSet,preparedStatement,connection);
        return teacher;
    }

    public boolean update(Teacher teacher) throws SQLException {
        //获得连接对象
        Connection connection = JdbcHelper.getConn();
        String updateTeacher_sql = "UPDATE teacher SET name=?,title_id=?,degree_id=?,dept_id=?,no=? where id = ?";
        //创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(updateTeacher_sql);
        //为参数赋值
        preparedStatement.setString(1,teacher.getName());
        preparedStatement.setInt(2,teacher.getTitle().getId());
        preparedStatement.setInt(3,teacher.getDegree().getId());
        preparedStatement.setInt(4,teacher.getDepartment().getId());
        preparedStatement.setInt(5,teacher.getId());
        preparedStatement.setString(6,teacher.getNo());
        //执行修改操作
        int affectedRowNum = preparedStatement.executeUpdate();
        System.out.println("修改了 "+affectedRowNum+" 条");
        //关闭资源
        JdbcHelper.close(preparedStatement,connection);
        return affectedRowNum > 0;
    }

    public boolean add(Teacher teacher){
        //声明变量
        Connection connection = null;
        int teacher_id = 0;
        PreparedStatement preparedStatement = null;
        int affectedRowNum = 0;
        try {
            //获得连接对象
            connection = JdbcHelper.getConn();
            //关闭自动提交(事件开始）
            connection.setAutoCommit(false);
            String addTeacher_sql = "INSERT INTO Teacher (name,title_id,degree_id,dept_id,no) VALUES" + " (?,?,?,?,?)";
            //在该连接上创建预编译语句对象
            preparedStatement = connection.prepareStatement(addTeacher_sql);
            //为预编译参数赋值
            preparedStatement.setString(1, teacher.getName());
            preparedStatement.setInt(2, teacher.getTitle().getId());
            preparedStatement.setInt(3, teacher.getDegree().getId());
            preparedStatement.setInt(4, teacher.getDepartment().getId());
            preparedStatement.setString(5, teacher.getNo());
            //执行第一条语句
            preparedStatement.executeUpdate();
            String selectTeacherByNo_sql = "SELECT * FROM teacher WHERE no=?";
            //在该连接上创建预编译语句对象
            preparedStatement = connection.prepareStatement(selectTeacherByNo_sql);
            //为预编译参数赋值
            preparedStatement.setString(1,teacher.getNo());
            //创建结果集对象，返回表结构
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                teacher_id = resultSet.getInt("id");
            }

            String addUser_sql = "INSERT INTO User (username,password,teacher_id,loginTime) VALUES" + " (?,?,?,?)";
            //在该连接上创建预编译语句对象
            preparedStatement = connection.prepareStatement(addUser_sql);
            //为预编译参数赋值
            preparedStatement.setString(1, teacher.getNo());
            preparedStatement.setString(2, teacher.getNo());
            preparedStatement.setInt(3, teacher_id);
            preparedStatement.setDate(4,new Date(System.currentTimeMillis()));
            //执行第二条预编译语句，获取添加记录行数并赋值给affectedRowNum
            affectedRowNum = preparedStatement.executeUpdate();
            //提交当前连接所做的操作（事件以提交结束）
            connection.commit();
        }catch (SQLException e){
            System.out.println("数据库操作异常");
            System.out.println(e.getMessage()+"\n errorCode="+e.getErrorCode());
            try{
                //回滚当前连接所作的操作
                if (connection != null){
                    //事件以回滚结束
                    connection.rollback();
                }
            }catch (SQLException e1){
                e1.printStackTrace();
            }
        }finally {
            try{
                //恢复自动提交
                if (connection!=null){
                    connection.setAutoCommit(true);
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
            //关闭资源
            JdbcHelper.close(preparedStatement,connection);
        }
        return affectedRowNum>0;
    }
    //删除
    public boolean delete(Teacher teacher){
        Connection connection = null;
        PreparedStatement pstmt=null;
        int affectedRowNum = 0;
        try {
            connection = JdbcHelper.getConn();
            //关闭自动提交(事件开始）
            connection.setAutoCommit(false);
            //创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
            pstmt = connection.prepareStatement("DELETE FROM user WHERE teacher_id = ?");
            //为预编译的语句参数赋值
            pstmt.setInt(1, teacher.getId());
            //执行第一句预编译对象
            pstmt.executeUpdate();
            //创建PreparedStatement接口对象
            pstmt = connection.prepareStatement("DELETE FROM teacher WHERE id = ?");
            //为预编译的语句参数赋值
            pstmt.setInt(1, teacher.getId());
            //执行预编译对象的executeUpdate()方法，获取删除记录的行数
            affectedRowNum = pstmt.executeUpdate();
            System.out.println("删除了老师的 " + affectedRowNum + " 条");
            //提交当前连接所做的操作（事件以提交结束）
            connection.commit();
        }catch (SQLException e){
            System.out.println("数据库操作异常");
            System.out.println(e.getMessage()+"\n errorCode="+e.getErrorCode());
            try{
                //回滚当前连接所作的操作
                if (connection != null){
                    //事件以回滚结束
                    connection.rollback();
                }
            }catch (SQLException e1){
                e1.printStackTrace();
            }
        }finally {
            try{
                //恢复自动提交
                if (connection!=null){
                    connection.setAutoCommit(true);
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
            //关闭资源
            JdbcHelper.close(pstmt,connection);
        }
        return affectedRowNum > 0;
    }

}

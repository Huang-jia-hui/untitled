//201802104017黄佳慧
package cn.edu.sdjzu.xg.bysj.controller.basic.teacher;

import cn.edu.sdjzu.xg.bysj.domain.Teacher;
import cn.edu.sdjzu.xg.bysj.service.TeacherService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

/**
 * 将所有方法组织在一个Controller(Servlet)中
 */
@WebServlet("/teacher.ctl")
public class TeacherController extends HttpServlet {
    /**
     * 方法-功能
     * put 修改
     * post 添加
     * delete 删除
     * get 查找
     */
    //请使用以下JSON测试增加功能
    //{"name":"id为null新老师","school":{"description":"管理工程","id":2,"no":"02","remarks":"最好的学院"},"degree":{"description":"新学位","no":"05","remarks":"new"},"department":{"description":"id为null新系","no":"0201","remarks":"","school":{"description":"管理工程","id":2,"no":"02","remarks":"最好的学院"}},"profTitle":{"description":"新职称","no":"05","remarks":"new"}}

    //请使用以下JSON测试修改功能
    //{"name":"修改的新老师","school":{"description":"管理工程","id":2,"no":"02","remarks":"最好的学院"},"degree":{"description":"新学位","no":"05","remarks":"new"},"department":{"description":"id为null修改的系","no":"0201","remarks":"","school":{"description":"管理工程","id":2,"no":"02","remarks":"最好的学院"}},"profTitle":{"description":"新职称","no":"05","remarks":"new"}}

    /**
     * POST, http://localhost:8080/teacher.ctl, 增加老师
     * 增加一个老师对象：将来自前端请求的JSON对象，增加到数据库表中
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //根据request对象，获得代表参数的JSON字串
        String teacher_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Teacher对象
        Teacher teacherToAdd = JSON.parseObject(teacher_json, Teacher.class);
        //前台没有为id赋值，此处模拟自动生成id。Dao能实现数据库操作时，应删除此语句。
        teacherToAdd.setId(4 + (int)(Math.random()*100));

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //增加Teacher对象
        try {
            TeacherService.getInstance().add(teacherToAdd);
            //加入响应信息
            message.put("message", "增加成功");
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        }catch(Exception e){
            message.put("message", "网络异常");
            e.printStackTrace();
        }
        //响应message到前端
        response.getWriter().println(message);
    }

    /**
     * DELETE, http://localhost:8080/teacher.ctl?id=1, 删除id=1的老师
     * 删除一个老师对象：根据来自前端请求的id，删除数据库表中id的对应记录
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //到数据库表中删除对应的老师
        try {
            TeacherService.getInstance().delete(id);
            //加入数据信息
            message.put("message", "删除成功");
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        }catch(Exception e){
            message.put("message", "网络异常");
            e.printStackTrace();
        }
        //响应message到前端
        response.getWriter().println(message);
    }


    /**
     * PUT, http://localhost:8080/teacher.ctl修改老师
     * 修改一个老师对象：将来自前端请求的JSON对象，更新数据库表中相同id的记录
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String teacher_json = JSONUtil.getJSON(request);
        //将JSON字串解析为teacher对象
        Teacher teacherToAdd = JSON.parseObject(teacher_json, Teacher.class);

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //增加Teacher对象
        try {
            TeacherService.getInstance().update(teacherToAdd);
            //加入响应信息
            message.put("message", "修改成功");
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        }catch(Exception e){
            message.put("message", "网络异常");
            e.printStackTrace();
        }
        //响应message到前端
        response.getWriter().println(message);
    }

    /**
     * //    GET, http://localhost:8080/teacher.ctl?id=1, 查询id=1的老师
     * //    GET, http://localhost:8080/teacher.ctl, 查询所有的老师
     * 把一个或所有老师对象响应到前端
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //读取参数id
        String id_str = request.getParameter("id");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有学院对象，否则响应id指定的学院对象
            if (id_str == null) {
                responseTeachers(response);
            } else {
                int id = Integer.parseInt(id_str);
                responseTeacher(id, response);
            }
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
            e.printStackTrace();
            //响应message到前端
            response.getWriter().println(message);
        }catch(Exception e){
            message.put("message", "网络异常");
            e.printStackTrace();
            //响应message到前端
            response.getWriter().println(message);
        }
    }
    //响应一个老师对象
    private void responseTeacher(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找老师
        Teacher teacher = TeacherService.getInstance().find(id);
        String teacher_json = JSON.toJSONString(teacher);

        //响应teacher_json到前端
        response.getWriter().println(teacher_json);
    }
    //响应所有老师对象
    private void responseTeachers(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //获得所有老师
        Collection<Teacher> teachers = TeacherService.getInstance().findAll();
        String departments_json = JSON.toJSONString(teachers, SerializerFeature.DisableCircularReferenceDetect);
        //响应teachers_json到前端
        response.getWriter().println(departments_json);
    }
}

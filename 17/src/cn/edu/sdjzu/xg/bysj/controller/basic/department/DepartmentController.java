//201802104017黄佳慧
package cn.edu.sdjzu.xg.bysj.controller.basic.department;
import cn.edu.sdjzu.xg.bysj.domain.Department;
import cn.edu.sdjzu.xg.bysj.service.DepartmentService;
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
@WebServlet("/department.ctl")
public class DepartmentController extends HttpServlet {
//    GET, http://49.234.231.74:8080/bysj1817/department.ctl?id=1, 查询id=1的系
//    GET, http://49.234.231.74:8080/bysj1817/department.ctl, 查询所有的系
//    POST, http://49.234.231.74:8080/bysj1817/department.ctl, 增加系
//    PUT, http://49.234.231.74:8080/bysj1817/department.ctl, 修改系
//    DELETE, http://49.234.231.74:8080/bysj1817/department.ctl?id=1, 删除id=1的系
    /**
     * 方法-功能
     * put 修改
     * post 添加
     * delete 删除
     * get 查找
     */
    //请使用以下JSON测试增加功能
    //{"description":"id为null新系","no":"0201","remarks":"","school":{"description":"管理工程","id":2,"no":"02","remarks":"最好的学院"}}

    //请使用以下JSON测试修改功能
    //{"description":"修改id=1的系","id":1,"no":"0201","remarks":"","school":{"description":"管理工程","id":2,"no":"02","remarks":"最好的学院"}}

    /**
     * POST, http://49.234.231.74:8080/bysj1817/department.ctl, 增加系
     * 增加一个系对象：将来自前端请求的JSON对象，增加到数据库表中
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //根据request对象，获得代表参数的JSON字串
        String department_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Department对象
        Department departmentToAdd = JSON.parseObject(department_json, Department.class);
        //前台没有为id赋值，此处模拟自动生成id。Dao能实现数据库操作时，应删除此语句。
        departmentToAdd.setId(4 + (int)(Math.random()*100));

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //增加加Department对象
        try {
            DepartmentService.getInstance().add(departmentToAdd);
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
     * DELETE, http://49.234.231.74:8080/bysj1817/department.ctl?id=1, 删除id=1的系
     * 删除一个系对象：根据来自前端请求的id，删除数据库表中id的对应记录
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
        //到数据库表中删除对应的学院
        try {
            DepartmentService.getInstance().delete(id);
            //加入数据信息
            message.put("message", "删除成功");
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
        }catch(Exception e){
            message.put("message", "网络异常");
        }

        //响应message到前端
        response.getWriter().println(message);
    }


    /**
     * PUT, http://49.234.231.74:8080/bysj1817/department.ctl,
     * 修改一个系对象：将来自前端请求的JSON对象，更新数据库表中相同id的记录
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String department_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Department对象
        Department departmentToAdd = JSON.parseObject(department_json,Department.class);

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //增加Department对象
        try {
            DepartmentService.getInstance().update(departmentToAdd);
            //加入响应信息
            message.put("message", "修改成功");
        }catch (SQLException e){
            //message.put("message", "数据库操作异常");
            e.printStackTrace();
        }catch(Exception e){
            message.put("message", "网络异常");
            e.printStackTrace();
        }
        //响应message到前端
        response.getWriter().println(message);
    }

    /**
     * //    GET, http://49.234.231.74:8080/bysj1817/department.ctl?id=1, 查询id=1的系
     * //    GET, http://49.234.231.74:8080/bysj1817/department.ctl, 查询所有的系
     * 把一个或所有系对象响应到前端
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
        String id_str1 = request.getParameter("paraType");

        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果两个参数都为null 表示响应所有系对象
            if(id_str==null&&id_str1==null){
                responseDepartments(response);
            }
            //如果id不为null，表示对应id的系对象
            else if(id_str!=null&&id_str1==null){
                int id = Integer.parseInt(id_str);
                responseDepartment(id,response);
            }
            //如果第二个参数为school，则表示对应id的school的所有系对象
            else if(id_str1.equals("school")){
                int schoolId = Integer.parseInt(id_str);
                responseDepartmentsBySchool(schoolId, response);
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
    //响应一个系对象
    private void responseDepartment(int id,HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找系
        Department department = DepartmentService.getInstance().find(id);
        String department_json = JSON.toJSONString(department);
        //响应message到前端
        response.getWriter().println(department_json);
    }
    //响应所有系对象
    private void responseDepartments(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //获得所有系
        Collection<Department> departments = DepartmentService.getInstance().findAll();
        String departments_json = JSON.toJSONString(departments, SerializerFeature.DisableCircularReferenceDetect);
        //响应message到前端
        response.getWriter().println(departments_json);
    }
    private void responseDepartmentsBySchool(int schoolId,HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //获得所有系
        Collection<Department> departments = DepartmentService.getInstance().findAllBySchool(schoolId);
        String departments_json = JSON.toJSONString(departments, SerializerFeature.DisableCircularReferenceDetect);
        //响应message到前端
        response.getWriter().println(departments_json);
    }
}

//201802104017黄佳慧
package cn.edu.sdjzu.xg.bysj.service;


import cn.edu.sdjzu.xg.bysj.dao.DepartmentDao;
import cn.edu.sdjzu.xg.bysj.domain.Department;
import cn.edu.sdjzu.xg.bysj.domain.School;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

public final class DepartmentService {
    private static DepartmentDao departmentDao= DepartmentDao.getInstance();
    private static DepartmentService departmentService=new DepartmentService();
    private DepartmentService(){}

    public static DepartmentService getInstance(){
        return departmentService;
    }

    public Collection<Department> findAll()throws SQLException{
        return departmentDao.findAll();
    }
    public Collection<Department> findAllBySchool(Integer schoolId)throws SQLException{
        return departmentDao.findAllBySchool(schoolId);
    }
    public Department find(Integer id)throws SQLException{
        return departmentDao.find(id);
    }

    public boolean update(Department department)throws SQLException{
        return departmentDao.update(department);
    }

    public boolean add(Department department)throws SQLException,ClassNotFoundException{
        return departmentDao.add(department);
    }

    public boolean delete(Integer id)throws SQLException {
        Department department = this.find(id);
        return departmentDao.delete(id);
    }
}


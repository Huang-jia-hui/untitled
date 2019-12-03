//201802104017黄佳慧
package cn.edu.sdjzu.xg.bysj.service;
import cn.edu.sdjzu.xg.bysj.dao.UserDao;
import cn.edu.sdjzu.xg.bysj.domain.User;

import java.sql.SQLException;

public final class UserService {
	private UserDao userDao = UserDao.getInstance();
	private static UserService userService = new UserService();
	
	public UserService() {
	}
	
	public static UserService getInstance(){
		return UserService.userService;
	}

	//public Collection<User> getUsers() throws SQLException {
		//return userDao.findAll();
	//}
	
	public User find(Integer id) throws SQLException {
		return userDao.find(id);
	}

	public boolean changePassword(User user)throws SQLException{
		return userDao.changePassword(user);
	}
	public User findByUsername(String username) throws SQLException {return  userDao.findByUsername(username);}
	public User login(String username,String password)throws SQLException{
		return userDao.login(username,password);
	}
}

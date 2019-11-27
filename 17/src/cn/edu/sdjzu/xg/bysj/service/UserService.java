package cn.edu.sdjzu.xg.bysj.service;


import cn.edu.sdjzu.xg.bysj.dao.UserDao;
import cn.edu.sdjzu.xg.bysj.domain.User;

import java.util.Collection;

public final class UserService {
	private UserDao userDao = UserDao.getInstance();
	private static UserService userService = new UserService();
	
	public UserService() {
	}
	
	public static UserService getInstance(){
		return UserService.userService;
	}

	public Collection<User> getUsers(){
		return userDao.findAll();
	}
	
	public User getUser(Integer id){
		return userDao.find(id);
	}
	
	public boolean updateUser(User user){
		userDao.delete(user);
		return userDao.add(user);
	}
	
	public boolean addUser(User user){
		return userDao.add(user);
	}

	public boolean deleteUser(Integer id){
		User user = this.getUser(id);
		return this.deleteUser(user);
	}
	
	public boolean deleteUser(User user){
		return userDao.delete(user);
	}
	
	
	public User login(String username, String password){
		Collection<User> users = this.getUsers();
		User desiredUser = null;
		for(User user:users){
			if(username.equals(user.getUsername()) && password.equals(user.getPassword())){
				desiredUser = user;
			}
		}
		return desiredUser;
	}	
}

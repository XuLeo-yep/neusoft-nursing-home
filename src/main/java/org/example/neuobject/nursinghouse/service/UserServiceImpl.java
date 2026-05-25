package org.example.neuobject.nursinghouse.service;

import org.example.neuobject.nursinghouse.entity.User;
import org.example.neuobject.nursinghouse.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(String loginCode, String password) {
        User user = userMapper.selectByLoginCode(loginCode);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    @Override
    public void addUser(User user) {
        userMapper.insert(user);
    }

    @Override
    public void updateUser(User user) {
        userMapper.updateById(user);
    }

    @Override
    public void deleteUser(long id) {
        userMapper.deleteById(id);
    }

    @Override
    public User findById(long id) {
        return userMapper.selectById(id);
    }

    @Override
    public List<User> findAll() {
        return userMapper.selectList(null);
    }
}

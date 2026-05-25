package org.example.neuobject.nursinghouse.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import org.example.neuobject.nursinghouse.common.Result;
import org.example.neuobject.nursinghouse.entity.User;
import org.example.neuobject.nursinghouse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    // ==================== 登录 ====================
    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> params) {
        String loginCode = params.get("username");
        String password = params.get("password");

        User user = userService.login(loginCode, password);
        if (user == null) {
            return Result.error(400, "账号或密码错误");
        }

        // Sa-Token 登录，标记用户身份
        StpUtil.login(user.getId());
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        // 返回 token 和用户信息给前端
        UserInfo userInfo = new UserInfo();
        userInfo.setToken(tokenInfo.getTokenValue());
        userInfo.setUsername(user.getUsername());
        userInfo.setRole(user.getRole());
        userInfo.setUserId(user.getId());

        return Result.success(userInfo);
    }

    // ==================== 用户列表 ====================
    @GetMapping("/list")
    public Result listUsers() {
        List<User> users = userService.findAll();
        return Result.success(users);
    }

    // ==================== 添加用户 ====================
    @PostMapping("/add")
    public Result addUser(@RequestBody User user) {
        userService.addUser(user);
        return Result.success();
    }

    // ==================== 删除用户 ====================
    @DeleteMapping("/{id}")
    public Result deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        return Result.success();
    }

    // ==================== 内部类：登录响应 ====================
    public static class UserInfo {
        private String token;
        private String username;
        private String role;
        private Long userId;

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
    }
}

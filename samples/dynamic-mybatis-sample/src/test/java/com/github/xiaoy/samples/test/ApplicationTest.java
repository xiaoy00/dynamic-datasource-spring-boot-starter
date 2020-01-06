package com.github.xiaoy.samples.test;


import com.github.xiaoy.samples.Application;
import com.github.xiaoy.samples.entity.User;
import com.github.xiaoy.samples.service.UserService;

import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ApplicationTest {

    private Random random = new Random();

    @Autowired
    private UserService userService;

    /**
     * 多数据源添加用户
     * 分别查询
     */
    @Test
    public void addUser() {
        User user = new User();
        user.setName("用户");
        user.setAge(random.nextInt(100));
        userService.addUser(user);
        userService.selectA().forEach(a -> System.out.println(a.getName()));
        userService.selectB().forEach(a -> System.out.println(a.getName()));
    }
    /**
     * 开启事务
     * 多数据源添加用户
     * 分别查询,A、B表各有一条数据事务用户.
     */
    @Test
    public void addUserTx(){
        User user = new User();
        user.setName("事务");
        user.setAge(random.nextInt(100));
        userService.addUserTx(user);
        userService.selectA().forEach(a -> System.out.println(a.getName()));
        userService.selectB().forEach(a -> System.out.println(a.getName()));
    }
    /**
     * 开启事务
     * 多数据源添加用户
     * 抛出异常,回滚数据
     * 分别查询,A、B表均无回滚用户
     */
    @Test
    public void addUserTxException() {
        User user = new User();
        user.setName("回滚");
        user.setAge(random.nextInt(100));
        try {
            userService.addUserTxExcepiton(user);
        } catch (Exception e) {
            System.out.println("发生异常");
        }
        userService.selectA().forEach(a -> System.out.println(a.getName()));
        userService.selectB().forEach(a -> System.out.println(a.getName()));
    }


}

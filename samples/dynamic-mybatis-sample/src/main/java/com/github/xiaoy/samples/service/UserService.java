package com.github.xiaoy.samples.service;


import com.github.xiaoy.samples.entity.User;
import com.github.xiaoy.samples.mapper.UserMasterMapper;
import com.github.xiaoy.samples.mapper.UserSlaveMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author songxiaoyue
 */
@Service
public class UserService {
  @Resource
  private UserMasterMapper masterMapper;
  @Resource
  private UserSlaveMapper slaveMapper;


  public void addUser(User user){
    masterMapper.addUser("A表"+user.getName(),user.getAge());
    slaveMapper.addUser("B表"+user.getName(),user.getAge()+1);
  }

  @Transactional
  public void addUserTx(User user){
    masterMapper.addUser("A表"+user.getName(),user.getAge());
    slaveMapper.addUser("B表"+user.getName(),user.getAge()+1);
  }

  @Transactional
  public void addUserTxExcepiton(User user) throws RuntimeException {
    masterMapper.addUser("A表"+user.getName(),user.getAge());
    slaveMapper.addUser("B表"+user.getName(),user.getAge()+1);
    throw new RuntimeException();
  }







  public List<User> selectA(){
    List<User> master = masterMapper.selectUsers();
    return master;
  }

  public List<User> selectB(){
    List<User> slave = slaveMapper.selectUsers();
    return slave;
  }



}

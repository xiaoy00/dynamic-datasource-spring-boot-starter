package com.github.xiaoy.samples.mapper;

import com.github.xiaoy.samples.entity.User;
import com.github.xiaoy.DataSource;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@DataSource("ds_b")
@Mapper
public interface UserSlaveMapper {

  @Insert("INSERT INTO user (name,age) values (#{name},#{age})")
  boolean addUser(@Param("name") String name, @Param("age") Integer age);

  @Select("SELECT * FROM user")
  List<User> selectUsers();
}

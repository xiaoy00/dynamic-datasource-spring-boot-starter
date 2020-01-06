package com.github.xiaoy.samples.mapper;

import com.github.xiaoy.samples.entity.User;
import java.util.List;

import com.github.xiaoy.DataSource;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@DataSource("ds_a")
@Mapper
public interface UserMasterMapper {

  @Insert("INSERT INTO user (name,age) values (#{name},#{age})")
  boolean addUser(@Param("name") String name, @Param("age") Integer age);

  @Select("SELECT * FROM user")
  List<User> selectUsers();
}

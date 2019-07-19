package com.oaec.tourism.mapper;

import com.oaec.tourism.entity.Manager;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagerMapper {
    @Insert("insert into t_manager(username,password,create_time,account,salt)" +
            " values(#{username},#{password},#{createTime},#{account},#{salt})")
    int create(Manager manager);
    @Update("UPDATE t_manager SET username=#{username}" +
            " WHERE id=#{id}")
    int update(Manager manager);
    @Delete("DELETE FROM t_manager WHERE id=#{id}")
    int delete(int id);
    @Select("SELECT id,account,username,password,salt,create_time createTime,`type` FROM" +
            " t_manager WHERE id=#{id}")
    Manager findById(int id);
    @Select("SELECT id,account,username,password,salt,create_time createTime,`type` FROM" +
            " t_manager WHERE account=#{account}")
    Manager findByAccount(String account);
    @Select("SELECT id,account,username,create_time createTime,`type` FROM" +
            " t_manager")
    List<Manager> findAll();
}

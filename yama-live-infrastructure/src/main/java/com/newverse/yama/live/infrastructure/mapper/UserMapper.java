package com.newverse.yama.live.infrastructure.mapper;

import com.newverse.yama.live.domain.entity.User;
import lombok.NonNull;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT `id`, `name`, `role`, `created_at` FROM `user` WHERE `id` = #{id}")
    User findById(@NonNull Long id);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO `user` "
            + "        (`name`, `role`, `created_at`) "
            + "        VALUES (#{name}, #{role}, #{createdAt})")
    void save(@NonNull User user);

}

package com.newverse.yama.live.infrastructure.mapper;

import com.newverse.yama.live.domain.entity.Topic;
import lombok.NonNull;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface TopicMapper {
    @Select("SELECT `id`, `name`, `user_id`, `eldest_sequence_id`, `created_at` FROM `topic` WHERE `name` = #{name}")
    Topic findByName(@NonNull String name);

    @Select("SELECT `id`, `name`, `user_id`, `eldest_sequence_id`, `created_at` FROM `topic` WHERE `id` = #{id} FOR UPDATE")
    Topic findById(@NonNull Long id);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO `topic` "
            + "        (`name`, `user_id`, `eldest_sequence_id`, `created_at`) "
            + "        VALUES (#{name}, #{userId}, #{eldestSequenceId}, #{createdAt})")
    void save(@NonNull Topic topic);

    @Update("UPDATE `topic` SET "
            + "     `eldest_sequence_id` = #{sequenceId} "
            + "     WHERE `id` = #{id}")
    void updateEldestSequenceId(@NonNull Long sequenceId, @NonNull Long id);
}

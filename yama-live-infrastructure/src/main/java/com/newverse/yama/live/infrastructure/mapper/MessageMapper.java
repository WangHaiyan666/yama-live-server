package com.newverse.yama.live.infrastructure.mapper;

import com.newverse.yama.live.domain.entity.Message;
import lombok.NonNull;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MessageMapper {
    @Select("SELECT `id`, `user_id`, `topic_id`, `sequence_id`, `content`, `created_at` FROM `message` WHERE `topic_id` = #{topicId} AND `sequence_id` = #{sequenceId}")
    Message findByTopicIdAndSequenceId(@NonNull Long topicId, @NonNull Long sequenceId);

    @Select("SELECT `id`, `user_id`, `topic_id`, `sequence_id`, `content`, `created_at` FROM `message` WHERE `id` = #{id}")
    Message findById(@NonNull Long id);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO `message` "
            + "        (`topic_id`, `user_id`, `sequence_id`, `content`, `created_at`) "
            + "        VALUES (#{topicId}, #{userId}, #{sequenceId}, #{content}, #{createdAt})")
    void save(@NonNull Message message);
}

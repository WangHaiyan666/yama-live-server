package com.newverse.yama.live.infrastructure.mapper;

import com.newverse.yama.live.domain.entity.Subscription;
import lombok.NonNull;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SubscriptionMapper {
    @Select("SELECT `id`, `topic_id`, `user_id`, `acked_sequence_id`, `created_at` FROM `subscription` WHERE `user_id` = #{userId} AND `topic_id` = #{topicId} FOR UPDATE")
    Subscription findByUserIdAndTopicId(@NonNull Long userId, @NonNull Long topicId);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("INSERT INTO `subscription` "
            + "        (`topic_id`, `user_id`, `acked_sequence_id`, `created_at`) "
            + "        VALUES (#{topicId}, #{userId}, #{ackedSequenceId}, #{createdAt})")
    void save(@NonNull Subscription subscription);

    @Update("UPDATE `subscription` SET "
            + "     `acked_sequence_id` = #{ackedSequenceId} "
            + "     WHERE `user_id` = #{userId} AND `topic_id` = #{topicId}")
    void updateAckedSequenceId(@NonNull Long ackedSequenceId, @NonNull Long userId, @NonNull Long topicId);
}

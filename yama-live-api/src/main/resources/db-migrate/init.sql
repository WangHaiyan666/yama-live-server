CREATE TABLE `user`
(
    `id`                        BIGINT       NOT NULL AUTO_INCREMENT,
    `name`                      VARCHAR(255) NOT NULL,
    `role`                      VARCHAR(255) NOT NULL,
    `created_at`                DATETIME(3)  NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `ix_name` (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = `utf8mb4`
  COLLATE = `utf8mb4_bin`;

CREATE TABLE `topic`
(
    `id`                        BIGINT       NOT NULL AUTO_INCREMENT,
    `name`                      VARCHAR(255) NOT NULL,
    `user_id`                   BIGINT       NOT NULL,
    `eldest_sequence_id`        BIGINT       NOT NULL,
    `created_at`                DATETIME(3)  NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `ix_name` (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = `utf8mb4`
  COLLATE = `utf8mb4_bin`;

CREATE TABLE `subscription`
(
    `id`                        BIGINT       NOT NULL AUTO_INCREMENT,
    `topic_id`                  BIGINT       NOT NULL,
    `user_id`                   BIGINT       NOT NULL,
    `acked_sequence_id`         BIGINT       NOT NULL,
    `created_at`                DATETIME(3)  NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `ix_user_topic` (`user_id`, `topic_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = `utf8mb4`
  COLLATE = `utf8mb4_bin`;

CREATE TABLE `message`
(
    `id`                        BIGINT       NOT NULL AUTO_INCREMENT,
    `user_id`                   BIGINT       NOT NULL,
    `topic_id`                  BIGINT       NOT NULL,
    `sequence_id`               BIGINT       NOT NULL,
    `content`                   MEDIUMTEXT   NOT NULL,
    `created_at`                DATETIME(3)  NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `ix_sequence_topic` (`sequence_id`, `topic_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = `utf8mb4`
  COLLATE = `utf8mb4_bin`;

INSERT INTO `user` (`id`, `name`, `role`, `created_at`) VALUES (1, 'publisher_1', 'PUBLISHER', now());
INSERT INTO `user` (`id`, `name`, `role`, `created_at`) VALUES (2, 'publisher_2', 'PUBLISHER', now());
INSERT INTO `user` (`id`, `name`, `role`, `created_at`) VALUES (3, 'subscriber_1', 'SUBSCRIBER', now());
INSERT INTO `user` (`id`, `name`, `role`, `created_at`) VALUES (4, 'subscriber_2', 'SUBSCRIBER', now());


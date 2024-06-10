CREATE TABLE `user_session` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `token` VARCHAR(255) NOT NULL,
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`token`),
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
);

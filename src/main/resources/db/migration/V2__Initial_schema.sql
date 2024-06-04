DROP TABLE IF EXISTS `user_session`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `audio_request`;

CREATE TABLE `audio_request` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `duration` double NOT NULL,
  `frequency` double NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL UNIQUE,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `user_session` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `token` VARCHAR(255) NOT NULL UNIQUE,
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
);

DROP TABLE IF EXISTS `audio_request`;
CREATE TABLE `audio_request` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `duration` double NOT NULL,
  `frequency` double NOT NULL,
  `waveform_type` smallint DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `login_time` BIGINT DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `user_session`;
CREATE TABLE `user_session` (
    `token` VARCHAR(255) PRIMARY KEY,
    `email` VARCHAR(255) NOT NULL,
    `expiration` DATETIME
);


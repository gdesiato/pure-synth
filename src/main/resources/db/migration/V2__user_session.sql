ALTER TABLE `audio_request` DROP COLUMN `waveform_type`;

ALTER TABLE `user` ADD CONSTRAINT `email_unique` UNIQUE (`email`);
ALTER TABLE `user` DROP COLUMN `username`;

CREATE TABLE `user_session` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `token` VARCHAR(255) NOT NULL,
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`token`),
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
);

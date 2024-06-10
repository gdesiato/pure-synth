ALTER TABLE `user` ADD CONSTRAINT `email_unique` UNIQUE (`email`);
ALTER TABLE `user` DROP COLUMN `username`;

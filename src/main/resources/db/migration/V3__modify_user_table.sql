ALTER TABLE `user`
  ADD CONSTRAINT `email_unique` UNIQUE (`email`),
  DROP COLUMN `username`;

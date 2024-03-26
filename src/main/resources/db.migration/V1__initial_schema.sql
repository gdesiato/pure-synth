--
-- Table structure for table `audio_request`
--

DROP TABLE IF EXISTS `audio_request`;
CREATE TABLE `audio_request` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `duration` double NOT NULL,
  `frequency` double NOT NULL,
  `waveform_type` smallint DEFAULT NULL,
  PRIMARY KEY (`id`)
)


--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
)


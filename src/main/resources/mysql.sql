CREATE TABLE IF NOT EXISTS `deluxeagreements_data` (
  `id`   INT AUTO_INCREMENT NOT NULL,
  `uuid` VARCHAR(36)        NOT NULL,
  `ip`   VARCHAR(32)        NOT NULL,
  `time` TIMESTAMP          NOT NULL,
  PRIMARY KEY (`id`)
);
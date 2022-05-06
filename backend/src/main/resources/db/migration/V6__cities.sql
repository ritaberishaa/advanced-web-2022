CREATE TABLE `cities` (
                          `id` BIGINT NOT NULL AUTO_INCREMENT,
                          `name` VARCHAR(45) NOT NULL,
                          PRIMARY KEY (`id`),
                          UNIQUE INDEX `city_name_UNIQUE` (`name` ASC)
);
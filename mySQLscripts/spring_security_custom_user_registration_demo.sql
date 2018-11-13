#DROP DATABASE IF EXISTS `spring_security_custom_user_demo`;
#CREATE DATABASE  IF NOT EXISTS `spring_security_custom_user_demo`;
USE `spring_security_custom_user_demo`;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` 			int(11) NOT NULL AUTO_INCREMENT,
  `username` 	varchar(50) NOT NULL,
  `password` 	char(80) NOT NULL,
  `first_name` 	varchar(50) NOT NULL,
  `last_name` 	varchar(50) NOT NULL,
  `email` 		varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`
--
-- NOTE: The passwords are encrypted using BCrypt
--
-- A generation tool is avail at: http://www.luv2code.com/generate-bcrypt-password
--
-- Default passwords here are: fun123
--

INSERT INTO `user` (username,password,first_name,last_name,email)
VALUES 
('john','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K','John','Doe','john@luv2code.com'),
('mary','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K','Mary','Public','mary@luv2code.com'),
('susan','user$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K','Susan','Adams','susan@luv2code.com');


--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `roles`
--

INSERT INTO `role` (name)
VALUES 
('ROLE_USER'),('ROLE_PROVIDER'),('ROLE_ADMIN');

--
-- Table structure for table `users_roles`
--

DROP TABLE IF EXISTS `users_roles`;

CREATE TABLE `users_roles` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  
  PRIMARY KEY (`user_id`,`role_id`),
  
  KEY `FK_ROLE_idx` (`role_id`),
  
  CONSTRAINT `FK_USER_05` FOREIGN KEY (`user_id`) 
  REFERENCES `user` (`id`) 
  ON DELETE CASCADE ON UPDATE CASCADE,
  
  CONSTRAINT `FK_ROLE` FOREIGN KEY (`role_id`) 
  REFERENCES `role` (`id`) 
  ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Dumping data for table `users_roles`
--


DROP TABLE IF EXISTS `cluster_info`;

CREATE TABLE `cluster_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(128) DEFAULT NULL,
  `provider_account_id` int(11) DEFAULT NULL,
  
  PRIMARY KEY (`id`),
  
  UNIQUE KEY `TITLE_UNIQUE` (`ip`),
  
  KEY `FK_PROVIDER_ACCOUNT_idx` (`provider_account_id`),
  
  CONSTRAINT `FK_PROVIDER_ACCOUNT` 
  FOREIGN KEY (`provider_account_id`) 
  REFERENCES `user_account` (`id`) 
  ON DELETE CASCADE
  ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;

CREATE TABLE `container_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `container_name` 	varchar(45) DEFAULT NULL,
  `container_path`  LONGBLOB ,
  `user_account_id` int(11) DEFAULT NULL,
  
  PRIMARY KEY (`id`),

  UNIQUE KEY `TITLE_UNIQUE` (`container_path`(255)),
  
  KEY `FK_USER_ACCOUNT_idx` (`user_account_id`),
  
  CONSTRAINT `FK_USER_ACCOUNT` 
  FOREIGN KEY (`user_account_id`) 
  REFERENCES `user_account` (`id`) 
  ON DELETE CASCADE
  ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;


SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO `users_roles` (user_id,role_id)
VALUES 
(1, 1),
(2, 1),
(2, 2),
(3, 1),
(3, 3)
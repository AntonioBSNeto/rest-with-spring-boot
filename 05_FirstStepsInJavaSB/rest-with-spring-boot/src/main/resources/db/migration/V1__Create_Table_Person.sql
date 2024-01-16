CREATE TABLE IF NOT EXISTS `person` (
  `id` bigint NOT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);


INSERT INTO `person` (`id`, `address`, `first_name`, `gender`, `last_name`) VALUES
	(1, 'Brazil', 'Person 1', 'Female', 'sobrenome'),
	(102, 'India', 'Nelson', 'Male', 'Gandhi'),
	(103, 'África do Sul', 'Nelson', 'Male', 'Mandela');

CREATE TABLE IF NOT EXISTS `person_seq` (
  `next_val` bigint DEFAULT NULL
);

INSERT INTO `person_seq` (`next_val`) VALUES
	(3);


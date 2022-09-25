CREATE TABLE users (
  id BINARY(16) PRIMARY KEY not null,
  email varchar(255) DEFAULT NULL,
  image_url varchar(255) DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  provider varchar(255) DEFAULT NULL,
  UNIQUE KEY uk_users_email_provider (email,provider)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
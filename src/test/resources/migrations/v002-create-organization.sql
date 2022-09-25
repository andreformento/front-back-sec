CREATE TABLE organization (
  id BINARY(16) PRIMARY KEY not null,
  name varchar(255) not NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE organization_share (
  id BINARY(16) primary key not null,
  organization_id BINARY(16) not null,
  user_id BINARY(16) not null,
  role varchar(20) not NULL,
  unique key (organization_id, user_id),
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (organization_id) REFERENCES organization(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

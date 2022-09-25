insert into organization (id, name) values
  (UUID_TO_BIN('4e9ec7d9-169b-11ed-b680-0242acf00302'),'Shared organization'),
  (UUID_TO_BIN('bc6f03b5-169d-11ed-b680-0242acf00302'),'Single organization');

INSERT INTO users (id, email,image_url,name,provider) VALUES
	 (UUID_TO_BIN('f1aee027-16a0-11ed-9fa3-0242acf00302'), 'andreformento.sc@gmail.com','https://seeklogo.com/images/I/Iron_Maiden-logo-57E237D49F-seeklogo.com.png','Andre','GOOGLE'),
	 (UUID_TO_BIN('f928bc96-16a0-11ed-9fa3-0242acf00302'), 'andreformento.sc@gmail.com','https://d3ugyf2ht6aenh.cloudfront.net/stores/001/796/405/products/070a22d1-2489-4277-9b45-c209e39488f61-64dc998e9ef2207f4516480987950718-640-0.jpg','Eddie','GITHUB');

insert into organization_share (id, organization_id, user_id, role) values
  (UUID_TO_BIN('59631ca1-169d-11ed-b680-0242acf00302'), UUID_TO_BIN('4e9ec7d9-169b-11ed-b680-0242acf00302'),UUID_TO_BIN('f1aee027-16a0-11ed-9fa3-0242acf00302'), 'OWNER'),
  (UUID_TO_BIN('639153ce-169d-11ed-b680-0242acf00302'), UUID_TO_BIN('4e9ec7d9-169b-11ed-b680-0242acf00302'),UUID_TO_BIN('f928bc96-16a0-11ed-9fa3-0242acf00302'), 'ADMIN'),
  (UUID_TO_BIN('c87efc92-169d-11ed-b680-0242acf00302'), UUID_TO_BIN('bc6f03b5-169d-11ed-b680-0242acf00302'),UUID_TO_BIN('f1aee027-16a0-11ed-9fa3-0242acf00302'), 'OWNER');

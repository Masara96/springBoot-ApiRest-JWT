INSERT INTO `users` (username,password,enabled) VALUES ('admin','$2a$10$pe1OGjq0GCTxTTapKmQDe.INCWu4heOcCRscIL9LUkjoLMxzW7zau',1);
INSERT INTO `users` (username,password,enabled) VALUES ('martin','$2a$10$DnYSLujA33Btkw.k1uDC5OyRT7Hs7VGi054dGBtDP29DesOa57sHq',1);

INSERT INTO `authorities` (user_id,authority) VALUES ('1','ROLE_ADMIN');
INSERT INTO `authorities` (user_id,authority) VALUES ('1','ROLE_USER');
INSERT INTO `authorities` (user_id,authority) VALUES ('2','ROLE_USER');


INSERT INTO clientes(nombre,apellido,email,create_at) VALUES ('Martin','fulano','martin@gmail.com','2017-04-16');
INSERT INTO clientes(nombre,apellido,email,create_at) VALUES ('Amadeo','mengano','amadeo@gmail.com','2018-06-23');
INSERT INTO clientes(nombre,apellido,email,create_at) VALUES ('natalia','sultano','natalia@gmail.com','2019-05-12');
INSERT INTO clientes(nombre,apellido,email,create_at) VALUES ('Josefina','pengano','josefina@gmail.com','2010-12-1');
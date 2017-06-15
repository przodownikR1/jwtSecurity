create schema jwt;
CREATE USER 'jwt'@'localhost' IDENTIFIED BY 'jwt';
GRANT ALL PRIVILEGES ON * . * TO 'jwt'@'localhost';
FLUSH PRIVILEGES;
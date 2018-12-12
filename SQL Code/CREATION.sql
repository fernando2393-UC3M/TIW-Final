CREATE DATABASE IF NOT EXISTS `tiwbnb`;
USE `tiwbnb`;

DROP TABLE IF EXISTS MESSAGES_ADMIN CASCADE;
DROP TABLE IF EXISTS messages_admin CASCADE;
DROP TABLE IF EXISTS MESSAGES CASCADE;
DROP TABLE IF EXISTS messages CASCADE;
DROP TABLE IF EXISTS BOOKING CASCADE;
DROP TABLE IF EXISTS booking CASCADE;
DROP TABLE IF EXISTS HOME CASCADE;
DROP TABLE IF EXISTS home CASCADE;
DROP TABLE IF EXISTS ADMIN CASCADE;
DROP TABLE IF EXISTS admin CASCADE;
DROP TABLE IF EXISTS USER CASCADE;
DROP TABLE IF EXISTS users CASCADE;


CREATE TABLE users (
  USER_ID INT NOT NULL AUTO_INCREMENT,
  USER_EMAIL VARCHAR(25) NOT NULL,
  USER_NAME VARCHAR(50) NOT NULL,
  USER_SURNAME VARCHAR(20) NOT NULL,
  USER_PASSWORD VARCHAR(20) NOT NULL,
  USER_BIRTHDATE DATE NOT NULL,
  CONSTRAINT PK_USER PRIMARY KEY (USER_ID),
  CONSTRAINT UNIQUE_USER UNIQUE (USER_EMAIL)
);

CREATE TABLE admin (
  ADMIN_ID INT NOT NULL AUTO_INCREMENT,
  ADMIN_EMAIL VARCHAR(25) NOT NULL,
  ADMIN_PASSWORD VARCHAR(20) NOT NULL,
  CONSTRAINT PK_ADMIN PRIMARY KEY (ADMIN_ID),
  CONSTRAINT UNIQUE_ADMIN UNIQUE (ADMIN_EMAIL)
);

CREATE TABLE home (
  HOME_ID INT NOT NULL AUTO_INCREMENT,
  HOME_NAME VARCHAR(30) NOT NULL,
  HOME_EMAIL VARCHAR(25) NOT NULL,
  HOME_CITY VARCHAR(25) NOT NULL,
  HOME_DESCRIPTION_FULL VARCHAR(200) NOT NULL,
  HOME_DESCRIPTION_SHORT VARCHAR(50) NOT NULL,
  HOME_TYPE VARCHAR(20) NOT NULL,
  HOME_GUESTS INT(3) NOT NULL,
  HOME_PHOTOS VARCHAR(45),
  HOME_PRICE_NIGHT DECIMAL(6,2) NOT NULL,
  HOME_AV_DATE_INIT DATE,
  HOME_AV_DATE_FIN DATE,
  CONSTRAINT PK_HOME PRIMARY KEY (HOME_ID),
  CONSTRAINT FK_HOME FOREIGN KEY (HOME_EMAIL) REFERENCES users (USER_EMAIL) ON DELETE CASCADE
);

CREATE TABLE booking (
  BOOKING_ID INT NOT NULL AUTO_INCREMENT,
  BOOKING_USER_ID INT NOT NULL,
  BOOKING_HOME_ID INT NOT NULL,
  BOOKING_DATE_IN DATE NOT NULL,
  BOOKING_DATE_OUT DATE NOT NULL,
  BOOKING_CARD_NUM BIGINT(16) NOT NULL,
  BOOKING_EXP_CODE VARCHAR(6) NOT NULL,
  BOOKING_CV2 INT(3) NOT NULL,
  BOOKING_CONFIRMED BOOLEAN NOT NULL,
  CONSTRAINT PK_BOOKING PRIMARY KEY (BOOKING_ID),
  CONSTRAINT FK_BOOKING1 FOREIGN KEY (BOOKING_USER_ID) REFERENCES users (USER_ID) ON DELETE CASCADE,
  CONSTRAINT FK_BOOKING2 FOREIGN KEY (BOOKING_HOME_ID) REFERENCES home (HOME_ID) ON DELETE CASCADE
);

CREATE TABLE messages (
  MESSAGE_ID INT NOT NULL AUTO_INCREMENT,
  MESSAGE_SENDER_ID INT NOT NULL,
  MESSAGE_RECEIVER_ID INT NOT NULL,
  MESSAGE_CONTENT TEXT NOT NULL,
  MESSAGE_DATE DATE NOT NULL,
  MESSAGE_READ BOOLEAN NOT NULL,
  CONSTRAINT PK_MESSAGES PRIMARY KEY (MESSAGE_ID),
  CONSTRAINT FK_MESSAGES1 FOREIGN KEY (MESSAGE_SENDER_ID) REFERENCES users (USER_ID) ON DELETE CASCADE,
  CONSTRAINT FK_MESSAGES2 FOREIGN KEY (MESSAGE_RECEIVER_ID) REFERENCES users (USER_ID) ON DELETE CASCADE
);

CREATE TABLE messages_admin (
  MESSAGE_ID INT NOT NULL AUTO_INCREMENT,
  MESSAGE_ADMIN_ID INT NOT NULL,
  MESSAGE_USER_ID INT NOT NULL,
  MESSAGE_CONTENT TEXT NOT NULL,
  MESSAGE_DATE DATE NOT NULL,
  MESSAGE_READ BOOLEAN NOT NULL,
  MESSAGE_FROM_ADMIN BOOLEAN NOT NULL,
  CONSTRAINT PK_MESSAGES_ADMIN PRIMARY KEY (MESSAGE_ID),
  CONSTRAINT FK_MESSAGES_ADMIN1 FOREIGN KEY (MESSAGE_ADMIN_ID) REFERENCES admin (ADMIN_ID) ON DELETE CASCADE,
  CONSTRAINT FK_MESSAGES_ADMIN2 FOREIGN KEY (MESSAGE_USER_ID) REFERENCES users (USER_ID) ON DELETE CASCADE
);

INSERT INTO `users` VALUES (1, 'mail@ibm.com', 'Fernando', 'Garcia', 'secret', '1997-07-18');
INSERT INTO `users` VALUES (2, 'mail@redsys.es', 'Fernando', 'Piqueras', 'password', '1997-06-15');
INSERT INTO `users` VALUES (3, 'toni@ibm.com', 'Toni', 'Surname', 'tony', '1997-06-11');
INSERT INTO `users` VALUES (4, 'guille@ibm.com', 'Guille', 'Surname', 'guille', '1997-03-15');
INSERT INTO `users` VALUES (5, 'example@ibm.com', 'dummy', 'Insert', 'secret', '2017-06-15');
INSERT INTO `users` VALUES (6, 'example@amazon.com', 'user', 'Surname', 'user', '2011-09-19');

INSERT INTO `admin` VALUE (1, 'admin', 'admin');
INSERT INTO `admin` VALUE (2, 'ibm', 'admin');
INSERT INTO `admin` VALUE (3, 'tzarraon@inf.uc3m.es', 'admin');

INSERT INTO `home` VALUE (1, 'Loft Espacioso', 'mail@ibm.com', 'Madrid', 'Loft grande espacioso en el centro de Madrid', 'Loft en Madrid', 
'Apartamento', 5, 'images/place-1.jpg', 420, '20180101', '20181231');
INSERT INTO `home` VALUE (2, 'Chalet Bonito', 'mail@ibm.com', 'NY', 'Chalet bonito en pleno Manhattan', 'Chalet en NY', 
'Privado', 1, 'images/place-2.jpg', 70, '20180101', '20181231');
INSERT INTO `home` VALUE (3, 'Suite', 'mail@redsys.es', 'Madrid', 'Suite en el centro de la capital', 'Suite en Madrid', 
'Apartamento', 3, 'images/place-3.jpg', 1337.70, '20180101', '20181231');

INSERT INTO `messages` VALUE (1, 2, 1, "Sample message", 20181231, true);
INSERT INTO `messages` VALUE (2, 1, 2, "Random Reply", 20181231, true);
INSERT INTO `messages_admin` VALUE (1, 1, 1, "Ticket to admin", 20181231, true, false);
INSERT INTO `messages_admin` VALUE (2, 1, 1, "Response to ticket", 20181231, true, true);


INSERT INTO `booking` VALUE (
  1,
  1,
  1,
  '20180101',
  '20180102',
  4848111122223333,
  '11/19',
  987,
  TRUE
);

INSERT INTO `booking` VALUE (
  2,
  3,
  1,
  '20180101',
  '20180102',
  4848111122223333,
  '11/19',
  987,
  TRUE
);

INSERT INTO `booking` VALUE (
  3,
  3,
  2,
  '20180101',
  '20180102',
  4848111122223333,
  '11/19',
  987,
  TRUE
);

INSERT INTO `booking` VALUE (
  4,
  1,
  3,
  '20180101',
  '20180102',
  4848111122223333,
  '11/19',
  987,
  TRUE
);

INSERT INTO `booking` VALUE (
  5,
  1,
  2,
  '20180101',
  '20180102',
  4848111122223333,
  '11/19',
  987,
  TRUE
);


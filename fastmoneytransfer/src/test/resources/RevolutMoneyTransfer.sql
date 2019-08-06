DROP TABLE IF EXISTS Account;
DROP TABLE IF EXISTS User;


CREATE TABLE User (UserId LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
 UserName VARCHAR(30) NOT NULL,
 ContactNumber LONG NOT NULL,
 EmailAddress VARCHAR(30) NOT NULL
);
CREATE TABLE Account (AccountId LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
UserId LONG,
Balance DECIMAL(19,4),
foreign key (UserId) references User(UserId)
);

CREATE UNIQUE INDEX index_user on User(UserId);
CREATE UNIQUE INDEX index_account on Account(AccountId);

INSERT INTO User (UserName,ContactNumber, EmailAddress) VALUES ('sparsh', 8329461203, 'Sparsh@yahoo.com');
INSERT INTO User (UserName,ContactNumber, EmailAddress) VALUES ('paul',1234567890, 'Paul@rediff.com');

INSERT INTO Account (UserId,Balance) VALUES (1,1000.0000);
INSERT INTO Account (UserId,Balance) VALUES (2,2000.0000);


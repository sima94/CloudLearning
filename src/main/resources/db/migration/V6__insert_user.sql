INSERT INTO USER_(ID, USER_NAME, PASSWORD, ROLE_ID, ACCOUNT_EXPIRED, ACCOUNT_LOCKED, CREDENTIALS_EXPIRED, ENABLED, IS_DELETED)
  VALUES (1, 'admin', /*admin1234*/'$2a$08$qvrzQZ7jJ7oy2p/msL4M0.l83Cd0jNsX6AJUitbgRXGzge4j035ha',1 , FALSE, FALSE, FALSE, TRUE, FALSE);

INSERT INTO USER_(ID, USER_NAME, PASSWORD, ROLE_ID, ACCOUNT_EXPIRED, ACCOUNT_LOCKED, CREDENTIALS_EXPIRED, ENABLED, IS_DELETED)
   VALUES (2, 'professor', /*professor1234*/'$2a$08$Br/KjNez6r.5KVZ0YEmgXuOwRuCudmsmlou3OdJw1aHogWdwtCyvO', 2, FALSE, FALSE, FALSE, TRUE, FALSE);

INSERT INTO USER_(ID, USER_NAME, PASSWORD, ROLE_ID, ACCOUNT_EXPIRED, ACCOUNT_LOCKED, CREDENTIALS_EXPIRED, ENABLED, IS_DELETED)
   VALUES (3, 'student', /*student1234*/'$2a$08$KsG0QPl.b.D0hH.EcfuwHOoW7w3skIS5V8rfrAEygwmZ0qA1duwM.', 3, FALSE, FALSE, FALSE, TRUE, FALSE);

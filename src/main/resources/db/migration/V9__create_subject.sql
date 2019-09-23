CREATE TABLE SUBJECT (
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY,
    NAME VARCHAR(255),
    DESCRIPTION VARCHAR(10000),
    PROFESSOR_ID BIGINT NOT NULL,
    CREATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    IS_DELETED BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (ID)
);

ALTER TABLE SUBJECT ADD CONSTRAINT SUBJECT__PROFESSOR FOREIGN KEY (PROFESSOR_ID) REFERENCES PROFESSOR;

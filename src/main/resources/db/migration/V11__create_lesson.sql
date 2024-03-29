CREATE TABLE LESSON (
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY,
    NAME VARCHAR(255),
    INTRO VARCHAR(10000),
    SUBJECT_ID BIGINT NOT NULL,
    CREATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    IS_DELETED BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (ID)
);

ALTER TABLE LESSON ADD CONSTRAINT LESSON__PROFESSOR FOREIGN KEY (SUBJECT_ID) REFERENCES SUBJECT;

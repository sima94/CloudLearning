CREATE TABLE LESSON_CHAPTER (
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY,
    TITLE VARCHAR(255),
    TEXT VARCHAR(10000),
    LESSON_ID BIGINT NOT NULL,
    CREATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    IS_DELETED BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (ID)
);

ALTER TABLE LESSON_CHAPTER ADD CONSTRAINT LESSON_CHAPTER__LESSON FOREIGN KEY (LESSON_ID) REFERENCES LESSON;
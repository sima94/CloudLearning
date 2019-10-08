CREATE TABLE LESSON_COMMENT (
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY,
    TEXT VARCHAR(10000),
    MEMBER_ID BIGINT NOT NULL,
    LESSON_CHAPTER_ID BIGINT,
    LESSON_COMMENT_ID BIGINT,
    CREATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    IS_DELETED BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (ID)
);

ALTER TABLE LESSON_COMMENT ADD CONSTRAINT LESSON_COMMENT__MEMBER FOREIGN KEY (MEMBER_ID) REFERENCES MEMBER;

ALTER TABLE LESSON_COMMENT ADD CONSTRAINT LESSON_COMMENT__LESSON_CHAPTER FOREIGN KEY (LESSON_CHAPTER_ID) REFERENCES LESSON_CHAPTER;

ALTER TABLE LESSON_COMMENT ADD CONSTRAINT LESSON_COMMENT__LESSON_COMMENT FOREIGN KEY (LESSON_COMMENT_ID) REFERENCES LESSON_COMMENT;

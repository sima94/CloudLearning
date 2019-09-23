CREATE TABLE STUDENT (
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY,

    --CREATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    --UPDATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    --IS_DELETED BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (ID)
);

CREATE TABLE STUDENT_SUBJECTS (
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY,
    STUDENT_ID BIGINT NOT NULL,
    SUBJECT_ID BIGINT NOT NULL,
    IS_APPROVED BOOLEAN DEFAULT FALSE,
    CREATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    IS_DELETED BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (STUDENT_ID, SUBJECT_ID)
);

ALTER TABLE STUDENT_SUBJECTS ADD CONSTRAINT STUDENT_SUBJECTS_SUBJECT
   FOREIGN KEY (SUBJECT_ID) REFERENCES SUBJECT;

ALTER TABLE STUDENT_SUBJECTS ADD CONSTRAINT STUDENT_SUBJECTS_STUDENT
   FOREIGN KEY (STUDENT_ID) REFERENCES STUDENT;


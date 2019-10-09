INSERT INTO REVOLUT_DB_4.USER (ID, FULL_NAME, EMAIL)
VALUES (1, 'USER 1', 'USER1@TEST.COM');

INSERT INTO REVOLUT_DB_4.USER (ID, FULL_NAME, EMAIL)
VALUES (2, 'USER 2', 'USER2@TEST.COM');

INSERT INTO REVOLUT_DB_4.USER (ID, FULL_NAME, EMAIL)
VALUES (3, 'USER 3', 'USER3@TEST.COM');

INSERT INTO REVOLUT_DB_4.ACCOUNT (ACC_NO, ACC_BALANCE, ACC_ACTIVE, ACC_TYPE, ACC_VERSION, USER_ID)
VALUES (1000, 100000, 1, 'DEPOSIT', 0, 1);

INSERT INTO REVOLUT_DB_4.ACCOUNT (ACC_NO, ACC_BALANCE, ACC_ACTIVE, ACC_TYPE, ACC_VERSION, USER_ID)
VALUES (2000, 100000, 1, 'TRANSACTIONAL', 0, 2);

INSERT INTO REVOLUT_DB_4.ACCOUNT (ACC_NO, ACC_BALANCE, ACC_ACTIVE, ACC_TYPE, ACC_VERSION, USER_ID)
VALUES (3000, 100000, 0, 'TRANSACTIONAL', 0, 3);
INSERT INTO REVOLUT_SCHEMA.USER (ID, FULL_NAME, EMAIL)
VALUES (1, 'USER 1', 'USER1@TEST.COM');

INSERT INTO REVOLUT_SCHEMA.USER (ID, FULL_NAME, EMAIL)
VALUES (2, 'USER 2', 'USER2@TEST.COM');

INSERT INTO REVOLUT_SCHEMA.USER (ID, FULL_NAME, EMAIL)
VALUES (3, 'USER 3', 'USER3@TEST.COM');

INSERT INTO REVOLUT_SCHEMA.ACCOUNT (ACC_NO, ACC_BALANCE, ACC_ACTIVE, ACC_TYPE, ACC_VERSION, USER_ID)
VALUES (1000, 99999999999, 1, 'DEPOSIT', 0, 1);

INSERT INTO REVOLUT_SCHEMA.ACCOUNT (ACC_NO, ACC_BALANCE, ACC_ACTIVE, ACC_TYPE, ACC_VERSION, USER_ID)
VALUES (2000, 100000, 1, 'TRANSACTIONAL', 0, 2);

INSERT INTO REVOLUT_SCHEMA.ACCOUNT (ACC_NO, ACC_BALANCE, ACC_ACTIVE, ACC_TYPE, ACC_VERSION, USER_ID)
VALUES (3000, 100000, 0, 'TRANSACTIONAL', 0, 3);

insert into REVOLUT_SCHEMA.ACCOUNT_TRANSACTIONS (AT_ID, AT_FROM, AT_TO, AT_AMOUNT, AT_STATUS, AT_REMARKS)
values ('04250760-ec2e-11e9-81b4-2a2ae2dbcce4', 1000, 2000, 20, 'SUCCESS', 'Remarks here')

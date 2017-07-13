create table SEC_LOCALIZED_CONSTRAINT_MESSAGE (
    ID varchar(36) not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    VERSION integer,
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    ENTITY_NAME varchar(255) not null,
    OPERATION_TYPE varchar(50) not null,
    MESSAGES longvarchar,
    --
    primary key (ID)
)^

create unique index IDX_SEC_LOCALIZED_CONSTRAINT_MSG_UNIQ_ENTITY_NAME_OP_TYPE
  on SEC_LOCALIZED_CONSTRAINT_MESSAGE (ENTITY_NAME, OPERATION_TYPE, DELETE_TS)^
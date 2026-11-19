-- Back #7 회원 테이블 (ddl-auto: none 이므로 직접 실행합니다)
-- 실행: mysql -u <계정> -p redbeanz < db/01_create_users.sql
CREATE TABLE users (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    login_id    VARCHAR(20)  NOT NULL,
    email       VARCHAR(100) NOT NULL,
    password    VARCHAR(255) NOT NULL,
    nickname    VARCHAR(20)  NOT NULL,
    role        VARCHAR(20)  NOT NULL DEFAULT 'USER',
    created_at  DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    CONSTRAINT uk_users_login_id UNIQUE (login_id),
    CONSTRAINT uk_users_email    UNIQUE (email)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

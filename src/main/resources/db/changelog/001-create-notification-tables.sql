--liquibase formatted sql

--changeset notification-team:001-notification
CREATE TABLE notification (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id         VARCHAR(36)   NOT NULL,
    event_type       VARCHAR(50)   NOT NULL,
    interview_id     BIGINT        NOT NULL,
    recipient_type   VARCHAR(20)   NOT NULL,
    recipient_name   VARCHAR(100),
    recipient_email  VARCHAR(150)  NOT NULL,
    channel          VARCHAR(20)   NOT NULL,
    subject          VARCHAR(200)  NOT NULL,
    body             VARCHAR(4000) NOT NULL,
    status           VARCHAR(20)   NOT NULL,
    attempts         INT           NOT NULL DEFAULT 0,
    last_error       VARCHAR(1000),
    created_at       TIMESTAMP     NOT NULL,
    sent_at          TIMESTAMP,
    CONSTRAINT uq_notification_idempotency UNIQUE (event_id, recipient_email, channel)
);
CREATE INDEX idx_notif_status ON notification (status);
CREATE INDEX idx_notif_interview ON notification (interview_id);
--rollback DROP TABLE notification;
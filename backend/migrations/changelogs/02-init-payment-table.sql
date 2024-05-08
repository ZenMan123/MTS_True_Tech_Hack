--changeset RuslanHkmoff:create history of payments table
create table if not exists  payment_history
(
    id      bigint generated always as identity,
    user_id bigint references users (user_id) NOT NULL,
    amount  numeric                           NOT NULL,
    date    TIMESTAMP                         NOT NULL,
    type    VARCHAR(255)                      NOT NULL CHECK (type IN ('Поступление', 'Перевод'))
)
create table pending_funds (
  id               BIGINT PRIMARY KEY AUTO_INCREMENT,
  transaction_hash varchar(66)        default null,
  description      text               default null,
  address_from     varchar(42)        default null,
  amount           varchar(100)       default 0,
  token_address    varchar(42)        default null,
  time_submitted   timestamp          default now(),
  user_id          text not null
);


create unique index idx_tx_hash_pending_funds
  on pending_funds (transaction_hash);
alter table pending_funds
  add column platform varchar(20) not null;

alter table pending_funds
  add column platform_id text not null;
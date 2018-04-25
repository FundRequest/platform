CREATE OR REPLACE index idx_fund_01
ON fund (funder_address);

CREATE OR REPLACE index idx_fund_02
ON fund (request_id);

CREATE OR REPLACE index idx_fund_03
ON fund (created_by);

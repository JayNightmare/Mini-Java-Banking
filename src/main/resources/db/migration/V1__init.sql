create extension if not exists citext;

CREATE TABLE customers (
  id UUID PRIMARY KEY,
  name TEXT NOT NULL,
  email CITEXT UNIQUE NOT NULL,
  kyc_status TEXT NOT NULL DEFAULT 'PENDING',
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE accounts (
  id UUID PRIMARY KEY,
  customer_id UUID NOT NULL REFERENCES customers(id),
  iban TEXT UNIQUE NOT NULL,
  currency VARCHAR(3) NOT NULL,
  status TEXT NOT NULL DEFAULT 'ACTIVE',
  version INT NOT NULL DEFAULT 0,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE transactions (
  id UUID PRIMARY KEY,
  external_id TEXT UNIQUE,
  type TEXT NOT NULL,
  status TEXT NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  booked_at TIMESTAMPTZ
);

CREATE TABLE ledger_entries (
  id UUID PRIMARY KEY,
  txn_id UUID NOT NULL REFERENCES transactions(id),
  account_id UUID NOT NULL REFERENCES accounts(id),
  direction TEXT NOT NULL CHECK (direction IN ('DEBIT','CREDIT')),
  amount_cents BIGINT NOT NULL,
  currency VARCHAR(3) NOT NULL,
  booked_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  narrative TEXT
);

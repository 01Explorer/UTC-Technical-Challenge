CREATE SCHEMA TOBACCO;

CREATE TABLE TOBACCO.Producers (
  "id" serial PRIMARY KEY,
  "name" varchar,
  "cpf" varchar(11) UNIQUE,
  "cep" varchar(8),
  "state_addr" varchar,
  "city_addr" varchar,
  "neighborhood_addr" varchar,
  "street_addr" varchar,
  "created_at" timestamp,
  "updated_at" timestamp
);

CREATE TABLE TOBACCO.Classes (
  "id" serial PRIMARY KEY,
  "description" varchar
);

CREATE TABLE TOBACCO.Bundles (
  "id" serial PRIMARY KEY,
  "label" varchar,
  "bought_at" timestamp,
  "producer_id" integer,
  "class_id" integer,
  "weight" decimal(32,3)
);

CREATE TABLE TOBACCO.Transactions (
  "id" serial PRIMARY KEY,
  "created_at" timestamp,
  "producer_id" integer,
  "bundle_id" integer
);

ALTER TABLE TOBACCO.Bundles ADD CONSTRAINT "producer_bundles" FOREIGN KEY ("producer_id") REFERENCES TOBACCO.Producers("id");

ALTER TABLE TOBACCO.Bundles ADD CONSTRAINT "classes_bundles" FOREIGN KEY ("class_id") REFERENCES TOBACCO.Classes("id");

ALTER TABLE TOBACCO.Transactions ADD CONSTRAINT "bundles_transactions" FOREIGN KEY ("bundle_id") REFERENCES TOBACCO.Bundles("id");

ALTER TABLE TOBACCO.Transactions ADD CONSTRAINT "producers_transactions" FOREIGN KEY ("producer_id") REFERENCES TOBACCO.Producers("id");

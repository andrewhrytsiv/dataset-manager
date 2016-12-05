create table roles(
	role_id int primary key,
	name varchar(25) not null
);
insert into roles(role_id,name) values(1,'Admin');
insert into roles(role_id,name) values(2,'User');

create table users(
	user_id SERIAL not null,
	role_id int references roles(role_id),
	user_name varchar(200) not null, 
	email varchar(200) not null,
	password varchar not null,
	PRIMARY KEY(user_id),
	UNIQUE (email)
);

create function default_role_id() returns trigger as $$
begin
    IF NEW.role_id IS NULL THEN
	NEW.role_id = 2;
    END IF;
    return NEW;
end
$$ language plpgsql;

CREATE TRIGGER users_default_role_id BEFORE INSERT ON users FOR EACH ROW EXECUTE PROCEDURE default_role_id();

insert into users(role_id,user_name,email,password) values(1,'SYSTEM','dataset.manager@gmail.com','d621c1a7169f2ca51bc8674da52e9572178a66a1dde88da24da78fe4951703f9');

create table datasets(
	id serial,
	dataset_id text NOT NULL,
	json_data json,
	url text,
	personal boolean,
	snapshot_date timestamp,
	owner integer,
	next_update_interval_min integer
);

CREATE VIEW should_update_datasets AS 
 SELECT dataset_id, url, snapshot_date, next_update_interval_min,snapshot_date + (next_update_interval_min * interval '1 minute') as should_be_updated 
 FROM  datasets 
 WHERE url IS NOT NULL AND (snapshot_date + (next_update_interval_min * interval '1 minute'))  <  now();

create table metadata_key_value(
	id serial,
	key text,
	value text,
	dset_id text NOT NULL,
	table_name varchar(20) NOT NULL
);

create table dictionary(
	id serial,
	key text,
	type text,
	dictionary_json json,
	UNIQUE (key,type)
);
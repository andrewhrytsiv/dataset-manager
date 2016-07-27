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
    NEW.role_id = 2;
    return new;
end
$$ language plpgsql;

CREATE TRIGGER users_default_role_id BEFORE INSERT ON users FOR EACH ROW EXECUTE PROCEDURE default_role_id();

create table url_data(
	id SERIAL not null,
	url varchar,
	file bytea,
	file_type varchar(10)
);
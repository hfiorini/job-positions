create table positions
(
	id integer not null
		constraint positions_pkey
			primary key,
	current_company varchar(255),
	external_id uuid not null,
	location varchar(255),
	name varchar(255),
	type varchar(255)
);

alter table positions owner to postgres;


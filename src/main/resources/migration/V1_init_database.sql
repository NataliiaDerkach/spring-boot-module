create table if not exists public.user_profile
(
   id serial primary key,
   email varchar(255),
   password varchar(255)
);
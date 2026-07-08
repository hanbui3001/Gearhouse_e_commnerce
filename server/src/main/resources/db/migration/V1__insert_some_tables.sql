alter table users
            add column provider varchar(20) not null default 'LOCAL',
            add column provider_id varchar(255),
            add column avatar_url varchar(255);
create unique index uk_users_provider_provider_id on users (provider, provider_id);


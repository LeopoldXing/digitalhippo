CREATE
DATABASE digitalhippo;

-- user microservice
CREATE TABLE public.users
(
    id            bigserial             NOT NULL,
    username      varchar NULL,
    email         varchar NULL,
    password_hash varchar NULL,
    salt          varchar NULL,
    "role"        varchar NULL,
    verified      boolean DEFAULT false NOT NULL,
    "locked"      boolean DEFAULT false NOT NULL,
    lock_until    timestamp NULL,
    created_at    timestamp   NOT NULL,
    created_by    varchar               NOT NULL,
    updated_at    timestamp   NOT NULL,
    updated_by    varchar               NOT NULL,
    CONSTRAINT users_pk PRIMARY KEY (id)
);

-- product microservice
CREATE TABLE public.products
(
    id                bigserial                        NOT NULL,
    user_id           bigserial                        NOT NULL,
    "name"            varchar                          NOT NULL,
    description       varchar NULL,
    price             numeric(10, 2) DEFAULT 0.00      NOT NULL,
    category          varchar NULL,
    product_file_id   bigserial                        NOT NULL,
    approved_for_sale varchar        DEFAULT 'pending' NOT NULL,
    CONSTRAINT products_pk PRIMARY KEY (id)
);

-- Column comments

COMMENT
ON COLUMN public.products.category IS 'ui_kits | icons';
COMMENT
ON COLUMN public.products.approved_for_sale IS 'pending | approved | denied';

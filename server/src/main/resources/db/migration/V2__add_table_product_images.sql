create table product_images
(
    id            varchar(255) not null,
    product_id    varchar(255) not null,
    image_url     varchar(255) not null,
    display_order integer      not null,
    primary key (id),
    constraint fk_product_images_product
        foreign key (product_id) references products (id)
);

create table task_status
(
    id   smallint auto_increment
        primary key,
    name varchar(64) not null,
    created_date    timestamp default current_timestamp not null,
    last_modified_date     timestamp,
    created_by     integer,
    last_modified_by      integer,
    deleted   boolean  default  false,
    constraint task_status_fk_created_by
        foreign key (created_by) references user (id),
    constraint task_status_fk_last_modified_by
        foreign key (last_modified_by) references user (id),
    constraint task_status_unique_name
        unique (name)
);

create table task
(
    id             integer auto_increment primary key,
    name           varchar(80)       not null,
    description    varchar(30),
    task_status_id smallint          not null,
    due_date       date,
    created_date    timestamp default current_timestamp,
    last_modified_date     timestamp,
    created_by     integer,
    last_modified_by      integer,
    deleted   boolean  default  false,
    constraint task_fk_created_by
        foreign key (created_by) references user (id),
    constraint task_fk_last_modified_by
        foreign key (last_modified_by) references user (id),
    constraint task_fk_task_status_id
        foreign key (task_status_id) references task_status (id)
);

create table task_assignment
(
    id             integer auto_increment primary key,
    task_id        integer not null,
    user_id        integer not null,
    comment        text,
    created_date    timestamp default current_timestamp not null,
    last_modified_date     timestamp,
    created_by     integer,
    last_modified_by      integer,
    deleted   boolean  default  false,
    constraint task_assignment_fk_created_by
        foreign key (created_by) references user (id),
    constraint task_assignment_fk_last_modified_by
        foreign key (last_modified_by) references user (id),
    constraint task_assignment_unique_task_user
        unique         (task_id, user_id),
    constraint task_assignment_fk_task_id
        foreign key (task_id) references task (id),
    constraint task_assignment_fk_user_id
        foreign key (user_id) references user (id),
    constraint task_assignment_unique_task_id_user_id
        unique (task_id, user_id)
);
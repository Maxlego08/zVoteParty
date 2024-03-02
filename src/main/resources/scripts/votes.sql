CREATE TABLE IF NOT EXISTS zvoteparty_votes
(
    player_uuid             varchar(36)          not null,
    service_name   varchar(255)         not null,
    is_reward_give boolean default true not null,
    reward_percent float   default 100  not null,
    commands       longtext             not null,
    need_online    boolean default true not null,
    created_at     long    				not null
)
comment 'Voting history';


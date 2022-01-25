insert into player (id, nick)
values ('9c6066bf-1579-4ec0-bb94-f3bb64d85c28', 'redMen'),
       ('2d96dac3-6934-44b9-bded-f7cb4c1b3867', 'greenMen');
insert into game (id, next_player_id, status)
values ('b2303e5a-eab6-4e9a-ad46-592075eeb675', '2d96dac3-6934-44b9-bded-f7cb4c1b3867', 'IN_PROGRESS'),
       ('b2303e5a-eab6-4e9a-ad46-592075eeb676', '2d96dac3-6934-44b9-bded-f7cb4c1b3867', 'NEW');
insert into game_players (games_id, players_id)
values ('b2303e5a-eab6-4e9a-ad46-592075eeb675', '2d96dac3-6934-44b9-bded-f7cb4c1b3867'),
       ('b2303e5a-eab6-4e9a-ad46-592075eeb675', '9c6066bf-1579-4ec0-bb94-f3bb64d85c28');
insert into pawn (id, color, location, game_id, player_id)
values ('7bdf6148-0fa1-4866-87ea-d2f93a0ee3d7', 'RED', 'R_BASE', 'b2303e5a-eab6-4e9a-ad46-592075eeb675',
        '9c6066bf-1579-4ec0-bb94-f3bb64d85c28'),
       ('b4014a6e-844e-44e8-a5f9-6693e7497fac', 'RED', 'R_BASE', 'b2303e5a-eab6-4e9a-ad46-592075eeb675',
        '9c6066bf-1579-4ec0-bb94-f3bb64d85c28'),
       ('2dd9421d-8194-4a64-8327-e9c8bc660910', 'RED', 'R_BASE', 'b2303e5a-eab6-4e9a-ad46-592075eeb675',
        '9c6066bf-1579-4ec0-bb94-f3bb64d85c28'),
       ('f601cb42-3662-4a18-9168-10bf00a5cc94', 'RED', 'R_BASE', 'b2303e5a-eab6-4e9a-ad46-592075eeb675',
        '9c6066bf-1579-4ec0-bb94-f3bb64d85c28'),
       ('37b994a4-17ed-42e9-aa17-b30718ae0838', 'GREEN', 'G_BASE', 'b2303e5a-eab6-4e9a-ad46-592075eeb675',
        '2d96dac3-6934-44b9-bded-f7cb4c1b3867'),
       ('f3713840-2e13-4af7-a950-32bcb74f5f1d', 'GREEN', 'G_BASE', 'b2303e5a-eab6-4e9a-ad46-592075eeb675',
        '2d96dac3-6934-44b9-bded-f7cb4c1b3867'),
       ('d45e7ed6-1efe-4c28-a809-0d4674cfb81e', 'GREEN', 'G_BASE', 'b2303e5a-eab6-4e9a-ad46-592075eeb675',
        '2d96dac3-6934-44b9-bded-f7cb4c1b3867'),
       ('677c0b29-2daa-4da9-acf8-d737285f1b3f', 'GREEN', 'G_BASE', 'b2303e5a-eab6-4e9a-ad46-592075eeb675',
        '2d96dac3-6934-44b9-bded-f7cb4c1b3867');

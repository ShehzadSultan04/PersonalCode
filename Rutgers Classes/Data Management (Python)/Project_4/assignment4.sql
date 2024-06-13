create database if not exists dl1058_music;
use dl1058_music;
create table artist (name varchar(55) not null primary key);

create table album(
    id int unsigned auto_increment primary key,
    date date not null,
    name varchar(200) not null,
    artist_name varchar(55) not null,
    unique (name, artist_name),
    foreign key (artist_name) references artist(name)
);

create table users (uname varchar(30) not null primary key);

create table playlist(
    id int unsigned auto_increment primary key,
    name varchar(200) not null,
    date datetime not null,
    uname varchar(30) not null,
    unique(name, uname),
    foreign key (uname) references users(uname)
);

create table song(
    id int unsigned auto_increment primary key,
    title varchar(200) not null,
    artist_name varchar(55) not null,
    date date,
    album_name varchar(200),
    check (date is not null or album_name is not null),
    unique (title, artist_name),
    foreign key (artist_name) references artist(name),
    foreign key (album_name) references album(name)
);

create table genre(name varchar(55) not null primary key);

create table genre_to_song(
    genre_name varchar(55) not null,
    song_id int unsigned not null,
    primary key (genre_name, song_id),
    foreign key (genre_name) references genre(name),
    foreign key (song_id) references song(id)
);

create table playlist_to_song(
    playlist_id int unsigned not null,
    song_id int unsigned not null,
    primary key (playlist_id, song_id),
    foreign key (playlist_id) references playlist(id),
    foreign key (song_id) references song(id)
);

create table rating(
    id int unsigned auto_increment primary key,
    date date not null,
    uname varchar(30) not null,
    rating_type ENUM("song", "album", "playlist") not null,
    rated_id int unsigned not null,
    rating tinyint unsigned not null check(rating between 1 and 5),
    foreign key (uname) references users(uname)
);

insert into genre (name) values ("pop"), ("hip-hop"), ("indian"), ("rock");
insert into artist (name) values ("kdot"), ("eminem"), ("drake"), ("tswift"), ("kanye"), ("anirudh"), ("juice");
insert into users (uname) values ("darshan"), ("shehzad"), ("mauli"), ("barg"), ("deepika"), ("sam");
insert into album (date, name, artist_name) values ("2017-04-14", "DAMN", "kdot"), ("2018-08-31", "Kamikaze", "eminem"), ("2018-06-29", "Scorpion", "drake"), ("2019-08-23", "Lover", "tswift"), ("2019-10-25", "Jesus is King", "kanye"), ("2016-01-01", "Avalukena", "anirudh"), ("2018-07-20", "Legends Never Die", "juice");
insert into song (title, artist_name, date, album_name) values ("Humble", "kdot", NULL, "DAMN"), ("DNA", "kdot", NULL, "DAMN"), ("Lose Yourself", "eminem", NULL, "Kamikaze"), ("Venom", "eminem", NULL, "Kamikaze"), ("God's Plan", "drake", NULL, "Scorpion"), ("In My Feelings", "drake", NULL, "Scorpion"), ("Me", "tswift", NULL, "Lover"), ("You Need to Calm Down", "tswift", NULL, "Lover"), ("Follow God", "kanye", NULL, "Jesus is King"), ("Closed on Sunday", "kanye", NULL, "Jesus is King"), ("Avalukena", "anirudh", NULL, "Avalukena"), ("Thangamey", "anirudh", NULL, "Avalukena"), ("Lucid Dreams", "juice", NULL, "Legends Never Die"), ("Robbery", "juice", NULL, "Legends Never Die");
insert into song (title, artist_name, date, album_name) values ("Family ties", "kdot", "2019-09-05", NULL), ("The Heart Part 4", "kdot", "2017-03-23", NULL), ("Rap God", "eminem", "2013-10-15", NULL), ("Not Afraid", "eminem", "2010-04-29", NULL), ("Nonstop", "drake", "2018-06-29", NULL), ("Blank Space", "tswift", "2014-10-27", NULL), ("Shake it off", "tswift", "2014-08-18", NULL), ("Ultralight Beam", "kanye", "2016-02-14", NULL), ("Famous", "kanye", "2016-06-14", NULL), ("Don'u Don'u Don'u", "anirudh", "2016-01-01", NULL), ("Enakenna Yaarum Illaye", "anirudh", "2015-06-14", NULL), ("Bee's Knees", "juice", "2018-05-04", NULL), ("In my head", "juice", "2019-02-13", NULL);
insert into song (title, artist_name, date, album_name) values ("Humble", "juice", "2032-12-31", NULL);
insert into genre_to_song (genre_name, song_id) values ("hip-hop", 1), ("hip-hop", 2), ("hip-hop", 3), ("hip-hop", 4), ("hip-hop", 5), ("hip-hop", 6), ("pop", 7), ("pop", 8), ("rock", 9), ("hip-hop", 10), ("indian", 11), ("indian", 12), ("hip-hop", 13), ("hip-hop", 14), ("hip-hop", 15), ("hip-hop", 16), ("hip-hop", 17), ("hip-hop", 18), ("hip-hop", 19), ("pop", 20), ("pop", 21), ("hip-hop", 22), ("hip-hop", 23), ("indian", 24), ("indian", 25), ("rock", 26), ("rock", 27), ("rock", 28);
insert into playlist (name, date, uname) values ("Top 10", "2019-11-01 12:00:00", "darshan"), ("indian", "2020-11-11 12:32:32", "darshan"), ("Top 10", "2019-11-01 12:00:00", "shehzad"), ("Top 10", "2019-11-01 12:00:00", "mauli"), ("Top 10", "2019-11-01 12:00:00", "barg"), ("Top 10", "2019-11-01 12:00:00", "deepika"), ("Top 10", "2019-11-01 12:00:00", "sam");
insert into playlist_to_song (playlist_id, song_id) values (1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9), (1, 10), (2, 11), (2, 12), (3, 13), (3, 14), (3, 15), (3, 16), (3, 17), (3, 18), (3, 19), (3, 20), (3, 21), (4, 22), (4, 23), (5, 24), (5, 25), (6, 26), (6, 27), (6, 28);
insert into rating(date, uname, rating_type, rated_id, rating) values ("2022-01-01", "darshan", "song", 1, 5), ("2022-01-01", "darshan", "album", 1, 5), ("2022-01-01", "darshan", "playlist", 6, 1), ("2022-01-01", "shehzad", "song", 2, 4), ("2022-01-01", "shehzad", "album", 2, 4), ("2022-01-01", "shehzad", "playlist", 6, 2), ("2022-01-01", "mauli", "song", 3, 3), ("2022-01-01", "mauli", "album", 3, 3), ("2022-01-01", "mauli", "playlist", 6, 3), ("2022-01-01", "barg", "song", 4, 2), ("2022-01-01", "barg", "album", 4, 2), ("2022-01-01", "barg", "playlist", 6, 4), ("2022-01-01", "deepika", "song", 5, 1), ("2022-01-01", "deepika", "album", 5, 1), ("2022-01-01", "deepika", "playlist", 6, 5), ("2022-01-01", "sam", "song", 6, 5), ("2022-01-01", "sam", "album", 6, 5), ("2022-01-01", "sam", "playlist", 6, 5);
insert into rating(date, uname, rating_type, rated_id, rating) values 
        ("1990-03-12", "barg", "album", 2, 2), 
        ("1992-10-03", "barg", "album", 4, 3), 
        ("1999-12-31", "deepika", "album", 3, 4), 
        ("1993-11-29", "deepika", "album", 5, 5), 
        ("1990-01-01", "sam", "album", 6, 5), 
        ("1995-06-24", "sam", "album", 5, 3),
        ("1994-05-15", "barg", "album", 1, 4),
        ("1997-09-18", "deepika", "album", 2, 3),
        ("1998-07-07", "sam", "album", 4, 2);
    insert into rating(date, uname, rating_type, rated_id, rating) values 
        ("1990-03-12", "barg", "album", 2, 2), 
        ("1992-10-03", "barg", "album", 4, 3), 
        ("1999-12-31", "deepika", "album", 3, 4), 
        ("1993-11-29", "deepika", "album", 5, 5), 
        ("1990-01-01", "sam", "album", 6, 5), 
        ("1995-06-24", "sam", "album", 5, 3),
        ("1994-05-15", "barg", "album", 1, 4),
        ("1997-09-18", "deepika", "album", 2, 3),
        ("1998-07-07", "sam", "album", 4, 2),
        ("1991-04-20", "darshan", "album", 3, 5),
        ("1996-08-12", "shehzad", "album", 1, 3),
        ("1992-11-05", "mauli", "album", 4, 4),
        ("1993-09-02", "barg", "album", 2, 2),
        ("1995-12-25", "deepika", "album", 5, 5),
        ("1998-06-10", "sam", "album", 6, 4),
        ("1990-07-15", "darshan", "album", 1, 3),
        ("1994-03-28", "shehzad", "album", 3, 2),
        ("1996-09-17", "mauli", "album", 4, 4),
        ("1999-11-11", "barg", "album", 5, 5),
        ("1997-05-03", "deepika", "album", 6, 3);
    insert into rating (date,uname,rating_type, rated_id,rating) values ("1993-02-23", "darshan", "album", 1, 4), ("1996-07-14", "shehzad", "album", 3, 3);
    insert into rating (date,uname,rating_type, rated_id,rating) values
        ("1991-01-01", "darshan", "song", 1, 5), 
        ("1993-03-12", "barg", "song", 3, 2),
        ("1995-12-31", "shehzad", "song", 19, 4),
        ("1993-11-29", "mauli", "song", 20, 3),
        ("1991-01-01", "deepika", "song", 21, 1),
        ("1990-06-24", "sam", "song", 22, 5),
        ("1992-05-15", "darshan", "song", 23, 4),
        ("1994-09-18", "barg", "song", 24, 2),
        ("1993-07-07", "shehzad", "song", 25, 3),
        ("1992-04-20", "mauli", "song", 26, 4),
        ("1994-08-12", "deepika", "song", 27, 5),
        ("1995-11-05", "sam", "song", 28, 3),
        ("1992-09-02", "darshan", "song", 14, 1),
        ("1994-12-25", "barg", "song", 15, 4),
        ("1993-06-10", "shehzad", "song", 12, 5),
        ("1995-07-15", "mauli", "song", 13, 3),
        ("1991-03-28", "deepika", "song", 10, 2),
        ("1993-02-14", "mauli", "song", 19, 4);

select genre_name as genre, count(title) as number_of_songs from genre_to_song, song where genre_to_song.song_id = song.id group by genre order by number_of_songs desc limit 3;
select distinct artist_name from song where album_name is not null and artist_name in (select artist_name from song where album_name is null);
select name as album_name, avg(rating) as average_user_rating from album, rating where rating.rated_id = album.id and rating_type = 'album' and rating.date between '1990-01-01' and '1999-12-31' group by album_name order by average_user_rating desc, album_name asc limit 10;
select genre_name, count(distinct uname) as num_users from genre_to_song, rating where genre_to_song.song_id = rating.rated_id and rating_type = 'song' and rating.date between '1991-01-01' and '1995-12-31' group by genre_name order by num_users desc limit 3;
select playlist.uname as username, playlist.name as playlist_title, avg(rating) as average_song_rating from playlist, playlist_to_song, rating where playlist.id = playlist_to_song.playlist_id and playlist_to_song.song_id = rating.rated_id and rating.rating_type = 'song' group by playlist.uname, playlist.name having average_song_rating >= 4.0;
select uname as username, count(rating) as number_of_ratings from rating where rating.rating_type = 'song' or rating.rating_type = 'album' group by username order by number_of_ratings desc limit 5; -- 6
select artist_name, count(distinct title) as number_of_songs from song where (case when song.date is null then (select album.date from album where album.name = song.album_name) else song.date end) between '1990-01-01' and '2010-12-31' group by artist_name order by number_of_songs desc limit 10; -- 7
select song.title as song_title, count(distinct playlist_id) as number_of_playlists from song, playlist_to_song where song.id = playlist_to_song.song_id group by song_title order by number_of_playlists desc, song_title asc limit 10; -- 8
select song.title as song_title, artist_name, count(rating) as number_of_ratings from song, rating where song.album_name is null and rating.rated_id = song.id group by song_title, artist_name order by number_of_ratings desc limit 20; -- 9
select distinct artist_name as artist_title from song where artist_name not in (select distinct artist_name from song where (case when song.date is null then (select album.date from album where album.name = song.album_name) else song.date end) > '1993-12-31'); -- 10
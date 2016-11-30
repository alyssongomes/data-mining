select * from movies_full where movieid not in (select movieid from ratings where userid = 1) and (genres like '%Action%' or genres like '%Adventure%') and (actors like '%Peter Hyams%' or actors like '%Jean-Claude Van Damme%');
select * from movies_full where movieid not in (select movieid from ratings where userid = 1);

SELECT * FROM movies_full WHERE movieid NOT IN (SELECT movieid FROM ratings WHERE userid = 1) and genres like '%Drama%' or genres like '%Comedy%' or genres like '%Horror%';

select * from map;

delete from map;

select * from movies_full where movieid = 33573;

SELECT * FROM movies_full WHERE movieid NOT IN (SELECT movieid FROM ratings WHERE userid =1) and  directors like '%Terry Gilliam%' or genres like '%Steven Spielberg%' or directors like '%Sam Raimi%' or genres like '%Drama%' or genres like '%Comedy%' or directors like '%Tim Burton%'

SELECT * FROM movies_full WHERE movieid NOT IN (SELECT movieid FROM ratings WHERE userid =1) and genres like '%Drama%' or
 genres like '%Comedy%' or genres like '%Horror%' and actors like '%Steven Spielberg%' or actors like '%Stanley Kubrick%' and director like '%Terry Gilliam%'

SELECT * FROM movies_full WHERE movieid NOT IN (SELECT movieid FROM ratings WHERE userid =1) and actors like '%Steven Spielberg%' or actors like '%Stanley Kubrick%' 
and genres like '%Drama%' or genres like '%Comedy%' or genres like '%Horror%' and director like '%Terry Gilliam%'

SELECT * FROM movies_full WHERE movieid NOT IN (SELECT movieid FROM ratings WHERE userid =1) and director like '%Terry Gilliam%' and actors like '%Steven Spielberg%' or actors like '%Stanley Kubrick%' 
and genres like '%Drama%' or genres like '%Comedy%' or genres like '%Horror%'

SELECT * FROM movies_full WHERE movieid NOT IN (SELECT movieid FROM ratings WHERE userid =1) and genres like '%Drama%' or genres like '%Comedy%' or genres like '%Horror%' and actors like '%Steven Spielberg%' or actors like '%Stanley Kubrick%' or actors like '%James Cameron%' and director like '%Terry Gilliam%' or director like '%Sam Raimi%' or director like '%Tim Burton%'

create index idx_genres on movies_full using hash(genres);

select * from ratings r, movies_full m where r.movieid = m.movieid and r.userid = 30;
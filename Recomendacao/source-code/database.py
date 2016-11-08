#-*- coding:utf-8 -*-
#import psycopg2
from decimal import *

fake = [
    (1L, 'Toy Story (1995)', 'Adventure|Animation|Children|Comedy|Fantasy', 'John Lasseter', 'Tom Hanks, Tim Allen, Don Rickles, Jim Varney', Decimal('3.9212395613240769')),
    (2L, 'Mixed Blood (1984)', 'Action|Drama', 'Paul Morrissey,Bryan Singer', 'Marília Pêra, Richard Ulacia, Linda Kerridge, Geraldine Smith', Decimal('4.9212395613240769')),
    (3L, 'Buying the Cow (2002)', 'Comedy|Romance', 'Walt Becker,Terry Gilliam', 'Jerry O\'Connell, Bridgette Wilson-Sampras, Ryan Reynolds, Bill Bellamy', Decimal('2.9212395613240769')),
    (4L, 'Julie Johnson (2001)', 'Drama', 'Bob Gosse,Gabriele Muccino', 'Lili Taylor, Courtney Love, Noah Emmerich, Mischa Barton', Decimal('3.9219895613240769')),
    (5L, 'Miracles - Mr. Canton and Lady Rose (1989)', 'Action|Comedy|Crime|Drama', 'Jackie Chan', 'Jackie Chan, Anita Mui, Ya-Lei Kuei, Chun Hsiung Ko', Decimal('3.1722395613240769'))
]

movieUserFake = [
    (32L,1112484819,4.5,32,'Twelve Monkeys (a.k.a. 12 Monkeys) (1995)','Mystery|Sci-Fi|Thriller','Terry Gilliam','Joseph Melito, Bruce Willis, Jon Seda, Michael Chance','05 Jan 1996',Decimal(3.89805469097377)),
    (47,1112484727,1.5,47,'Seven (a.k.a. Se7en) (1995)','Mystery|Thriller','Gabriele Muccino','Will Smith, Rosario Dawson, Woody Harrelson, Michael Ealy','19 Dec 2008',Decimal(4.05349256630211)),
    (50,1112484580,2.5,50,'Usual Suspects, The (1995)','Crime|Mystery|Thriller','Bryan Singer','Stephen Baldwin, Gabriel Byrne, Benicio Del Toro, Kevin Pollak','15 Sep 1995',Decimal(4.33437220780326))
]

'''
try:
    con = psycopg2.connect(host='localhost', user='postgres', password='postgres',dbname='db_movies')
except Exception as e:
    print "Cannot to connect!"
c = con.cursor()
'''
def allMovies():
    return fake

def findMovie(movieid):
    #c.execute('SELECT m.movieid,m.title,m.genres,m.director,m.actors, avg(r.rating) FROM ratings r,movies m WHERE r.movieid = '+str(movieid)+' AND m.movieid = '+str(movieid)+' GROUP BY m.movieid,m.title,m.genres,m.director,m.actors')
    #return c.fetchall()
    for m in fake:
        if m[0] == movieid:
            return m
    return None

def findAvgUser(userid):
    #c.execute('SELECT * FROM user_rating_avg WHERE userid = '+userid)
    #return c.fetchall()
    return 3.4

def findMoviesUser(userid):
    #c.execute('SELECT * FROM ratings r, movies_full m WHERE r.movieid = m.movieid AND r.userid = 1')
    #c.fetchall()
    return movieUserFake


#row = findMovie(1)
#row = [(1L, 'Toy Story (1995)', 'Adventure|Animation|Children|Comedy|Fantasy', 'John Lasseter', 'Tom Hanks, Tim Allen, Don Rickles, Jim Varney', Decimal('3.9212395613240769'))]
#print str(row[0][0])
#print row[0][1]
#agg = row[0][2].split('|') + row[0][3].split(',') + row[0][4].split(',') + [float(row[0][5])]
#print agg

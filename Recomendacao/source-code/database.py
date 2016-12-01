#-*- coding:utf-8 -*-
import psycopg2
from decimal import *

try:
    con = psycopg2.connect(host='localhost', user='postgres', password='postgres',dbname='db_movies')
    con.autocommit = True
except Exception as e:
    print "Cannot to connect!"
c = con.cursor()


def allMovies():
    c.execute('SELECT * FROM movies_full')
    return c.fetchall()

def findMoviesUser(userid):
    c.execute('SELECT * FROM ratings r, movies_full m WHERE r.movieid = m.movieid AND r.userid ='+str(userid))
    return c.fetchall()

def findMovie(movieid):
    c.execute('SELECT * FROM movies_full WHERE movieid='+str(movieid))
    return c.fetchall()

def findMovieMatriz(movieid):
    c.execute('SELECT * FROM matriz WHERE movieid='+str(movieid))
    return c.fetchall()

def findAllMoviesMatriz():
    c.execute('SELECT * FROM matriz')
    return c.fetchall()

def findMoviesNotAssited(profileUser,matriz,userId):

    p,i = [],0
    while i < len(matriz)-1:
        p.append((profileUser[i],matriz[i]))
        i += 1

    genres = p[:19]
    actors = p[19:51871]
    directors = p[51871:64958]

    genres.sort(reverse = True)
    actors.sort(reverse = True)
    directors.sort(reverse = True)

    queryGenres = "select distinct(m.movieid),m.title,m.genres,m.director,m.actors,m.released,m.average from ratings r, movies_full m where m.movieid NOT IN (SELECT movieid FROM ratings WHERE userid ="+str(userId)+") and r.movieid = m.movieid and m.genres like '%"+genres[0][1]+"%' and m.genres like '%"+genres[1][1]+"%' and m.genres like '%"+genres[2][1]+"%'"
    queryDirector = "select distinct(m.movieid),m.title,m.genres,m.director,m.actors,m.released,m.average from ratings r, movies_full m where m.movieid NOT IN (SELECT movieid FROM ratings WHERE userid ="+str(userId)+") and r.movieid = m.movieid and m.director like '%"+directors[0][1]+"%'"
    queryActors = "select distinct(m.movieid),m.title,m.genres,m.director,m.actors,m.released,m.average from ratings r, movies_full m where r.movieid = m.movieid and m.movieid NOT IN (SELECT movieid FROM ratings WHERE userid ="+str(userId)+")  and (actors like '%"+actors[0][1]+"%' or m.actors like '%"+actors[1][1]+"%' ) and ( m.genres like '%"+genres[0][1]+"%' or m.genres like '%"+genres[1][1]+"%' or m.genres like '%"+genres[2][1]+"%')"

    print queryGenres
    print queryDirector
    print queryActors

    c.execute(queryGenres)
    g = c.fetchall()
    c.execute(queryDirector)
    d = c.fetchall()
    c.execute(queryActors)
    a = c.fetchall()
    return g,d,a

def insertMatriz(mapped,movieid):
    mapped = str(mapped).replace('[','').replace(']','')
    movieid = str(movieid)
    try:
        c.execute('INSERT INTO matriz VALUES ('+movieid+',\'{'+mapped+'}\')')
        return True
    except Exception as e:
        print "Cannot insert"+str(e)
        return False

def insertMap(matriz):
    i = 0
    while i < len(matriz):
        matriz[i] = matriz[i].replace('\'',';')
        i += 1

    matriz = str(matriz).replace('[','').replace(']','')

    try:
        c.execute('INSERT INTO map VALUES (ARRAY['+matriz+'])')
        return True
    except Exception as e:
        print "Cannot insert"+str(e)
        return False

def loadMatriz():
    c.execute('SELECT * FROM map')
    matriz = c.fetchall()[0][0]
    i = 0
    while i < len(matriz):
        matriz[i] = matriz[i].decode('string_escape')
        matriz[i] = matriz[i].replace(';','\'')
        i += 1

    return matriz

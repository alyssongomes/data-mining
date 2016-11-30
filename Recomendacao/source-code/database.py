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

    query,j = "SELECT * FROM movies_full WHERE movieid NOT IN (SELECT movieid FROM ratings WHERE userid ="+str(userId)+") and ",0
    query += "actors like '%"+actors[0][1]+"%' or actors like '%"+actors[1][1]+"%' and "
    query += "genres like '%"+genres[0][1]+"%' or genres like '%"+genres[1][1]+"%' or genres like '%"+genres[2][1]+"%' and "
    query += "director like '%"+directors[0][1]+"%'"

    '''while j < len(p):
        if 0 <= matriz.index(p[j][1]) <= 18:
            #if j != len(p)-1:
            if g < 2:
                g += 1
                query += " genres like '%"+p[j][1]+"%' or"
            elif g == 2:
                query += " genres like '%"+p[j][1]+"%'"

        if 19 <= matriz.index(p[j][1]) <= 51871:
            if a < 2:
                a += 1
                query += " genres like '%"+p[j][1]+"%' or"
            elif a == 2:
                query += " genres like '%"+p[j][1]+"%'"

        if 51872 <= matriz.index(p[j][1]) <= 64957:
            if d < 2:
                d += 1
                query += " directors like '%"+p[j][1]+"%' or"
            elif d == 2:
                query += " directors like '%"+p[j][1]+"%'"
        j += 1'''

    print query

    c.execute(query)
    return c.fetchall()

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

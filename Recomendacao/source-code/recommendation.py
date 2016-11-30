
#-*- coding:utf-8 -*-
import copy
import time
from database import *
from profile import *
from decimal import *

matriz = []
#matriz = [["movieid","Action","Adventure","Animation","Children","Comedy","Crime","Documentary","Drama","Fantasy","Film-Noir","Horror","Musical","Mystery","Romance","Sci-Fi","Thriller","War","Western","(no genres listed)"]]
#actors = [] -> [19:51871]
#directors = [] -> [51872:64958]

#Constroi a matriz com todas as caracterista dos filmes
def buildMatriz():
    matriz.append(loadMatriz())
    '''rows = allMovies()
    for r in rows:
        #Directors
        for d in r[3].split(','):
            if len(directors) == 0:
                #directors.append([d])
                directors.append(d)
            else:
                if d not in directors:
                    directors.append(d)

        #Actor
        for a in r[4].split(','):
            if len(actors) == 0:
                actors.append(a)
            else:
                if a not in actors:
                    actors.append(a)

    matriz[0] = matriz[0] + actors + directors
    matriz[0].append('avg')'''

#Mapeia cada filme e compara com o perfil do usuário, e recomenda N filmes para o usuário
def recommended(profileUser,userId,n = 5):

    rows = findMoviesNotAssited(profileUser,matriz[0][1:],userId)

    movies = []

    #popular com gêneros, diretores e atores
    for r in xrange(0,len(rows)):

        #Inicializar a linha de mapeamento
        matriz.append([])
        for j in xrange(0,len(matriz[0])):
            matriz[1].append(0.0)

        #Movieid
        matriz[1][0] = rows[r][0]

        #Genres
        for g in rows[r][2].split('|'):
            if g in matriz[0]:
                matriz[1][matriz[0].index(g)] = 1.0

        #Directors
        for d in rows[r][3].split(','):
            if d in matriz[0]:
                matriz[1][matriz[0].index(d)] = 1.0

        #Actors
        for a in rows[r][4].split(','):
            if a in matriz[0]:
                matriz[1][matriz[0].index(a)] = 1.0

        if rows[r][6] != None:
            matriz[1][matriz[0].index('avg')] = float(rows[r][6])
        else:
            matriz[1][matriz[0].index('avg')] = 0.0

        movie = matriz.pop()
        movies.append((distanceCos(profileUser,movie[1:]),movie[0]))
    #movies.sort(reverse = True)
    return movies[0:n]


if __name__ == '__main__':
    #x = [(0.0, 33573L), (0.0, 69565L), (0.0, 69864L), (0.0, 80226L), (0.0, 89100L), (0.0, 89215L), (0.0, 90493L), (0.0, 92268L), (0.0, 92516L), (0.0, 94076L)]
    userid = 1

    print "begin building matriz..."
    buildMatriz()
    print "ended building matriz..."

    print "begin building profile user.."
    user = defineProfile(copy.copy(matriz),userid)
    print "ended building profile user.."

    '''i,j,mapa = 0,1,[]
    while i < len(user) and j < len(matriz[0]):
        mapa.append((user[j-1],matriz[0][j]))
        i += 1
        j += 1

    mapa.sort(reverse = True)
    print str(mapa[1:10])'''

    print "begin comparating movies..."
    movies = recommended(user,userid,10)
    print "ended comparating movies..."
    print movies
    for m in movies:
        print findMovie(m[1])

    '''
    print 'Total: '+str(len(matriz[0]))
    for l in matriz:
        print l

    print 'Usuário'
    print user
    print str(len(user))

    movies = recommended(user,5)
    print movies

    print '####### verify #######'
    for i in xrange(1,len(matriz)):
        print verify(matriz[i])
    print verify(user)

    print findMovie(movies[0][1])
    '''

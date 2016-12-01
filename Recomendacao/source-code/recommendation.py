
#-*- coding:utf-8 -*-
import copy
import time
from database import *
from profile import *
from decimal import *

matriz = []
#matriz = [["movieid","Action","Adventure","Animation","Children","Comedy","Crime","Documentary","Drama","Fantasy","Film-Noir","Horror","Musical","Mystery","Romance","Sci-Fi","Thriller","War","Western","(no genres listed)"]]

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

def mapper(rows, profileUser):
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
    movies.sort()
    return movies

#Mapeia cada filme e compara com o perfil do usuário, e recomenda N filmes para o usuário
def recommended(profileUser,userId,n = 5):

    rowsg,rowsd,rowsa = findMoviesNotAssited(profileUser,matriz[0][1:],userId)
    moviesg = mapper(rowsg,profileUser)
    moviesd = mapper(rowsd,profileUser)
    moviesa = mapper(rowsa,profileUser)

    if len(moviesg) < n or len(moviesd) < n or len(moviesa) < n:
        n = min([len(moviesg),len(moviesd),len(moviesa)])
    return moviesg[0:n],moviesd[0:n],moviesa[0:n]


if __name__ == '__main__':
    userid = 1

    print "begin building matriz..."
    buildMatriz()
    print "ended building matriz..."

    print "begin building profile user.."
    user = defineProfile(copy.copy(matriz),userid)
    print "ended building profile user.."

    print "begin comparating movies..."
    moviesByGenres,moviesByDirector,moviesByActors = recommended(user,userid)
    print "ended comparating movies..."
    print
    print "Recomendação por Gênero"
    for m in moviesByGenres:
        print findMovie(m[1])
    print
    print "Recomendação por Diretor"
    for m in moviesByDirector:
        print findMovie(m[1])
    print
    print "Recomendação por Atores"
    for m in moviesByActors:
        print findMovie(m[1])

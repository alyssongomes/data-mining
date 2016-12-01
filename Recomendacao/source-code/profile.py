#-*- coding:utf-8 -*-
from database import *
from math import *


#Popula (1 se tem a caract., 0 se não tem) a matriz através das caracterista de cada filme assistido pelo usuário
def popularMatrizUser(matriz,userId):
    rows = findMoviesUser(userId)

    for i in xrange(0,len(rows)):
        matriz.append([])
        for j in xrange(0,len(matriz[0])):
            matriz[i+1].append(0.0)

    #popular com gêneros, diretores e atores
    for r in xrange(0,len(rows)):

        #Movieid
        matriz[r+1][0] = rows[r][0]

        #Genres
        for g in rows[r][6].split('|'):
            if g in matriz[0]:
                matriz[r+1][matriz[0].index(g)] = 1.0

        #Directors
        for d in rows[r][7].split(','):
            if d in matriz[0]:
                matriz[r+1][matriz[0].index(d)] = 1.0

        #Actors
        for a in rows[r][8].split(','):
            if a in matriz[0]:
                matriz[r+1][matriz[0].index(a)] = 1.0

        matriz[r+1][matriz[0].index('avg')] = float(rows[r][2])

#agregação das informções na matriz do usuário, ou seja, traçar um perfil
def defineProfile(matriz,userId):
    popularMatrizUser(matriz,userId)

    matrizUser = []
    count = 0

    i,j = 1,1
    while i < len(matriz[0]):
        while j < len(matriz):
            count += matriz[j][i]
            j += 1
        matrizUser.append(float(count/(len(matriz)-1)))
        count = 0
        j = 1
        i += 1

    return matrizUser

#calcular a distancia cosseno entre 2 vetores
def distanceCos(v1,v2):
    num,n1,n2 = 0,0,0
    for i in xrange(0,len(v1)):
        num += v1[i]*v2[i]
    for j in xrange(0,len(v1)):
        n1 += pow(v1[j],2)
        n2 += pow(v2[j],2)
    return num/(sqrt(n1)*sqrt(n2))

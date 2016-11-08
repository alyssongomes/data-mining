#-*- coding:utf-8 -*-
from database import *
from math import *

matriz = [['Action','Adventure','Animation','Children','Comedy','Crime','Documentary','Drama','Fantasy','Film-Noir','Horror','Musical','Mystery','Romance','Sci-Fi','Thriller','War','Western','(no genres listed)']]

#Constroi a matriz com todas as caracterista dos filmes
def buildMatriz():
    rows = allMovies()
    for r in rows:

        #Directors
        for d in r[3].split(','):
            if len(matriz) == 0:
                matriz.append([d])
            else:
                if d not in matriz[0]:
                    matriz[0].append(d)
                    
        #Actor
        for a in r[4].split(','):
            if len(matriz) == 0:
                matriz.append([a])
            else:
                if a not in matriz[0]:
                    matriz[0].append(a)

    matriz[0].append('avg')

#Popula (1 se tem a caract., 0 se não tem) a matriz através das caracterista de cada filme assistido pelo usuário
def popularMatrizUser(userId):
    rows = findMoviesUser(userId)

    for i in xrange(0,len(rows)):
        matriz.append([])
        for j in xrange(0,len(matriz[0])):
            matriz[i+1].append(0.0)
    
    #popular com gêneros, diretores e atores    
    for r in xrange(0,len(rows)):

        #Genres
        for g in rows[r][5].split('|'):
            if g in matriz[0]:
                matriz[r+1][matriz[0].index(g)] = 1.0      

        #Directors
        for d in rows[r][6].split(','):
            if d in matriz[0]:
                matriz[r+1][matriz[0].index(d)] = 1.0      

        #Actors
        for a in rows[r][7].split(','):
            if a in matriz[0]:
                matriz[r+1][matriz[0].index(a)] = 1.0

        matriz[r+1][matriz[0].index('avg')] = float(rows[r][2])

#agregação das informções na matriz do usuário, ou seja, traçar um perfil
def defineProfile(userId):
    matrizUser = []
    count = 0

    i,j = 0,1
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

'''
def popularMatriz():
    rows = allMovies()

    for i in xrange(0,len(rows)):
        matriz.append([])
        for j in xrange(0,len(matriz[0])):
            matriz[i+1].append(0)
    
    #popular com gêneros, diretores e atores    
    for r in xrange(0,len(rows)):

        #Genres
        for g in rows[r][2].split('|'):
            if g in matriz[0]:
                matriz[r+1][matriz[0].index(g)] = 1      

        #Directors
        for d in rows[r][3].split(','):
            if d in matriz[0]:
                matriz[r+1][matriz[0].index(d)] = 1      

        #Actors
        for a in rows[r][4].split(','):
            if a in matriz[0]:
                matriz[r+1][matriz[0].index(a)] = 1

        matriz[r+1][matriz[0].index('avg')] = float(rows[r][5])
'''

user = 1

buildMatriz()
popularMatrizUser(user)

print 'Caracteristicas'
print 'Total: '+str(len(matriz[0]))
for l in matriz:
    print l

u = defineProfile(user)
print 'Matriz do usuário'
print 'Total: '+str(len(u))
print u
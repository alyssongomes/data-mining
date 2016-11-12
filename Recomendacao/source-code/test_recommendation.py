temp = file('recommendation.txt', 'r')

matriz = temp.readline()

matriz = matriz.replace('[[','')
matriz = matriz.replace(']]','')
matriz = matriz.replace('\'','')

matriz = matriz.split(',')
'''matriz = list(matriz)
print len(matriz)'''
print matriz[0:20]

temp.close()
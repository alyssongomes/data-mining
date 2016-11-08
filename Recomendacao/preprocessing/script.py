import psycopg2
import csv

try:
    con = psycopg2.connect(host='localhost', user='postgres', password='postgres',dbname='db_movies')
except Exception as e:
    print "Cannot to connect!"
c = con.cursor()

out = csv.writer(file('movies.csv', 'w'))

c.execute('SELECT * FROM movies_full ORDER BY movieid')
rows = tuple(c.fetchall())

out.writerows(rows)
#for row in rows:
	#print str(row[0])+";"+row[1]+";"+row[2]+";"+row[3]+";"+row[4]+";"+row[5]

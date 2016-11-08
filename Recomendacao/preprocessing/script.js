var http = require('http');
var pg = require('pg');

var url = "postgres://postgres:postgres@localhost/db_movies";

function findMovieOMDB(imdbid,id,callback){
	var data;
	console.log("Localizando: "+imdbid);
	http.get({
		host:'www.omdbapi.com',
		path: '/?i=tt'+imdbid+'&plot=short&r=json'
	},function (response) {
		response.on('data',function(d){
			data += d;
		});
		response.on('end',function() {
			try {
				var json = JSON.parse(data.replace('undefined',''));
			} catch (e) {
				console.log(data);
				console.log("[ERROR]:"+e);
			}
			if (json.Response == 'True') {
				callback(json,id);
			}else{
				console.log("No found movie:"+'/?i=tt'+imdbid+'&plot=short&r=json');
				callback(undefined,id);
			}
		});
	})
}

function findAllMovies(){
	pg.connect(url, function(err, client, done) {
		if (err)
  		return console.error('error fetching client from pool', err);
  	client.query('SELECT l.movieid,tmdbid,imdbid FROM links l, movies m WHERE m.movieid = l.movieid AND m.director IS NULL', function(err, result) {
    	if (err) throw err;

			updateMovies(result.rows);

			client.end(function (err) {
		  	if (err) throw err;
		  });
  	});
	});
}

function updateMovies(movies){
	for (var i = 0; i < movies.length; i++) {
	//for (var i = 0; i < 1000; i++) {
		var imdb = String(movies[i].imdbid);
		imdb = "0".repeat(7 - imdb.length)+""+imdb;

		findMovieOMDB(imdb,i,function(movie,id){
			if (movie != undefined) {
				pg.connect(url, function(err, client, done) {
					if (err)	return console.error('error fetching client from pool', err);
					client.query('UPDATE movies SET director=$1,actors=$2,released=$3 WHERE movieid=$4',[movie.Director,movie.Actors,movie.Released,movies[id].movieid], function(err, result) {
						if (err) throw err;
						console.log(result);
						client.end(function (err) {
							if (err) throw err;
						});
					});
				});
			}
		});
	}
}


findAllMovies();

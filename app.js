var express = require('express');
var path = require('path');
var busboy = require('connect-busboy');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var fs = require('fs');


var routes = require('./routes/index');
var users = require('./routes/users');

var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');

// uncomment after placing your favicon in /public
//app.use(favicon(__dirname + '/public/favicon.ico'));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));
app.use(busboy());


app.post('/fileupload', function(req, res) {
    console.log("entered");
    var fstream;
    req.pipe(req.busboy);
    req.busboy.on('file', function (fieldname, file, filename) {
        console.log("Uploading: " + filename); 
        var name = (__dirname + '/files/' + filename);
        fstream = fs.createWriteStream(name);
        file.pipe(fstream);
        var spawn = require('child_process').spawn,
            javaProg  = spawn('java', ['Compressor', name, 
                name.substring(0, name.lastIndexOf('.')) + '.png']);

        fstream.on('close', function () {
            console.log("Upload finished.")
            res.redirect('back');
        });
        
    });
});

app.post('/decompress', function(req, res) {
    console.log("entered");
    var fstream;
    req.pipe(req.busboy);
    req.busboy.on('file', function (fieldname, file, filename) {
        console.log("Uploading: " + filename); 
        var name = (__dirname + '/files/' + filename);
        fstream = fs.createWriteStream(name);
        file.pipe(fstream);
        var spawn = require('child_process').spawn,
            javaProg  = spawn('java', ['Decompressor', name, 
                name.substring(0, name.lastIndexOf('.')) + '.txt']);

        fstream.on('close', function () {
            console.log("Upload finished.")
            res.redirect('back');
        });
        
    });
});


app.use('/', routes);
app.use('/users', users);


// catch 404 and forward to error handler
app.use(function(req, res, next) {
    var err = new Error('Not Found');
    err.status = 404;
    next(err);
});

// error handlers

// development error handler
// will print stacktrace
if (app.get('env') === 'development') {
    app.use(function(err, req, res, next) {
        res.status(err.status || 500);
        res.render('error', {
            message: err.message,
            error: err
        });
    });
}

// production error handler
// no stacktraces leaked to user
app.use(function(err, req, res, next) {
    res.status(err.status || 500);
    res.render('error', {
        message: err.message,
        error: {}
    });
});




module.exports = app;
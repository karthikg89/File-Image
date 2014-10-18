var express = require('express');
var router = express.Router();
var fs = require('fs');
var busboy = require('connect-busboy');

/* GET home page. */
router.get('/', function(req, res) {
  res.render('index', { title: 'Text to Image Compressor' });
});

router.get('/fileupload', function(req, res) {
	res.render('fileupload', {title: "Upload Page", image: name + '.png'});
});



module.exports = router;

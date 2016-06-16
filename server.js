    var dispatcher = require('httpdispatcher');
    var http = require('http');
    var finder = require('./finder.js');
    var dbURL = "http://192.168.1.138:7474/db/data/transaction"

    dispatcher.setStatic('/resources');
    dispatcher.setStaticDirname('static');

    dispatcher.onGet("/find", function(req, res) {
        res.writeHead(200, {'Content-Type': 'text/plain'});
        res.end('check this result: \n'+finder.findExpert("java", dbURL));
    }); 

    dispatcher.onPost("/find", function(req, res) {
        res.writeHead(200, {'Content-Type': 'text/plain'});
        res.end('Page Two');
    });

    dispatcher.beforeFilter(/\//, function(req, res, chain) { //any url
        console.log("Before filter");
        chain.next(req, res, chain);
    });

    dispatcher.afterFilter(/\//, function(req, res, chain) { //any url
        console.log("After filter");
        chain.next(req, res, chain);
    });

    dispatcher.onError(function(req, res) {
        res.writeHead(404);
        res.end();
    });

    http.createServer(function (req, res) {
        dispatcher.dispatch(req, res);
    }).listen(80, '127.0.0.1');
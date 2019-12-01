const http = require('http');
const fs = require('fs');
const server = http.createServer();

const origin = 'http://127.0.0.1:8080';

server.on('request', function(req, res) {
    console.log(`origin = ${req.headers.origin}`);
    if (req.url == '/index.html') {
        fs.readFile(__dirname + '/../Resources/index.html', 'utf-8', function (err, data) {
            if (err) {
                res.writeHead(404, {'Content-Type' : 'text/plain'});
                res.write('page not found');
                return res.end();
            }

            res.writeHead(200, {'Content-Type' : 'text/html'});
            res.write(data);
            res.end();
        });
        return;
    }
    const headers = {
        'Content-Type' : 'text/plain',
    };
    if (req.headers.origin == origin) {
        headers['Access-Control-Allow-Origin'] = origin;
        headers['Access-Control-Allow-Credentials'] = 'true';
    }
    res.writeHead(200, headers);
    res.write('ok');
    res.end();
});

server.listen(4000);

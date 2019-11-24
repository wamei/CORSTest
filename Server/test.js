const http = require('http');
const server = http.createServer();

const origin = 'http://127.0.0.1:8080';

server.on('request', function(req, res) {
    if (req.headers.origin != origin) {
        res.writeHead(400);
        res.end();
        return;
    }
    res.writeHead(200, {
        'Content-Type' : 'text/plain',
        'Access-Control-Allow-Origin': origin,
        'Access-Control-Allow-Credentials': 'true',
    });
    res.write('ok');
    res.end();
});

server.listen(4000);

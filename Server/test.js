const http = require('http');
const fs = require('fs');
const server = http.createServer();

const origin = 'http://127.0.0.1:8080';

server.on('request', function(req, res) {
    console.log(`url = ${req.url}`);
    console.log(`origin = ${req.headers.origin}`);

    if (req.url == '/') {
        req.url = '/index.html';
    }

    const headers = {};
    fs.readFile(`${__dirname}/../Resources${req.url}`, 'utf-8', function (err, data) {
        if (err) {
            res.writeHead(404, {'Content-Type' : 'text/plain'});
            res.write('not found');
            return res.end();
        }

        if (req.url.endsWith(".ico")) {
            headers['Content-Type'] = "image/x-icon";
        } else if (req.url.endsWith(".png") || req.url.endsWith(".PNG")) {
            headers['Content-Type'] = "image/png";
        } else if (req.url.endsWith(".jpg") || req.url.endsWith(".JPG") || req.url.endsWith(".jpeg") || req.url.endsWith(".JPEG")) {
            headers['Content-Type'] = "image/jpeg";
        } else if (req.url.endsWith(".js")) {
            headers['Content-Type'] = "application/javascript";
        } else if (req.url.endsWith(".css")) {
            headers['Content-Type'] = "text/css";
        } else if (req.url.endsWith(".html") || req.url.endsWith(".htm")) {
            headers['Content-Type'] = "text/html";
        } else if (req.url.endsWith(".map")) {
            headers['Content-Type'] = "application/json";
        } else {
            headers['Content-Type'] = "text/plain";
        }
        if (req.headers.origin == origin) {
            headers['Access-Control-Allow-Origin'] = origin;
            headers['Access-Control-Allow-Credentials'] = 'true';
        }

        res.writeHead(200, headers);
        res.write(data);
        res.end();
    });
});

server.listen(4000);

const http = require("http");
const fs = require("fs").promises;
const host = "localhost";
const port = 8080;

const sayHello = function(req, res) {
    res.writeHead(200);
    res.end("Hello World!");
}

let fuji;
let index;
let styles;

const serveIndexHtml = function(req, res) {
    switch(req.url) {
        case "/images/fuji.jpg":
            res.setHeader("Content-Type", "image/jpeg");
            res.writeHead(200);
            res.end(fuji)
            break
        case "/styles/styles.css":
            res.setHeader("Content-Type", "text/css");
            res.writeHead(200);
            res.end(fuji)
            break
        default:
            res.setHeader("Content-Type", "text/html");
            res.writeHead(200);
            res.end(index)
    }
}

// Read files.
fs.readFile("index.html").then(contents => { index = contents })
fs.readFile("./images/fuji.jpg").then(contents => { fuji = contents })
fs.readFile("./styles/styles.css").then(contents => { styles = contents })

// Server with callback to serve requests.
const server = http.createServer(serveIndexHtml);
server.listen(port, host, () => { console.log(`Server is running on http://${host}:${port}`) });

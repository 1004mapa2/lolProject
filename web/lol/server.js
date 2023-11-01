const express = require('express');
const app = express();
const port = 3000;

app.use(express.static(__dirname + "/public"));

app.get('/', (req, res) => {
    res.sendFile(__dirname + "/src/home.html");
});

app.get('/detail', (req, res) => {
    res.sendFile(__dirname + "/src/detail.html");
})

app.listen(port, () => {
    console.log(`${port} 열었따`);
});
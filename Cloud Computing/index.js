const express = require('express')
const cors = require("cors")
const bodyParser = require("body-parser")
const router = require('./src/routes/router')


const app = express()
const port = process.env.PORT || 5000 



const corsOptions = {
    origin: '*',
    Credentials: true,
    optionSuccessStatus: 200
};

app.use(cors(corsOptions));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));
app.use('/nutricipe', router)

app.get('/', (req, res) => {
    res.send('Welcome to nutricipe backend!')
})

app.listen(port, () => {
    console.log(`Nutricipe app listening on port ${port}`)
})
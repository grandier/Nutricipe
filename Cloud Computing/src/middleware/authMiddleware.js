const jwt = require('jsonwebtoken');
const {checkUser} = require('../db/dbConfig');

async function authMiddleware(req, res, next) {
    let token;
    if(req.headers.authorization && req.headers.authorization.startsWith('Bearer')) {
        try{
            token = req.headers.authorization.split(' ')[1];
            const decoded = jwt.verify(token, 'hashed');
            const user = await checkUser(decoded.userId);
            if(!user) {
                return res.status(401).json({error: true, message: 'Unauthorized, no user'});
            }
            req.userId = decoded
            next();
        }
        catch(error) {
            return res.status(401).json({error: true, message: 'Unauthorized, token failed'});
        }
        if(!token) {
            return res.status(401).json({error: true, message: 'Unauthorized, no token'});
        }
    }
}

module.exports = {
    authMiddleware
}
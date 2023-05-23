const jwt = require('jsonwebtoken');
const {checkUser} = require('../db/dbConfig');

async function authMiddleware(req, res, next) {
    let token;
    if(req.headers.authorization && req.headers.authorization.startsWith('Bearer')) {
        try{
            token = req.headers.authorization.split(' ')[1];
            if(!token) {
                return res.status(401).json({error: true, message: 'Unauthorized, no token'});
            }
            const decoded = jwt.verify(token, 'hashed');
            const user = await checkUser(decoded.userId);
            if(!user) {
                return res.status(401).json({error: true, message: 'Unauthorized, no user'});
            }
            req.userId = decoded.userId;
            next();
        }
        catch(error) {
            return res.status(401).json({error: true, message: 'Unauthorized, token failed'});
        }
    }
}

module.exports = {
    authMiddleware
}
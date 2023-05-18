const jwt = require('jsonwebtoken');

async function authMiddleware(req, res, next) {
    let token;
    if(req.headers.authorization && req.headers.authorization.startsWith('Bearer')) {
        try{
            token = req.headers.authorization.split(' ')[1];
            const decoded = jwt.verify(token, 'hashed');
            req.userID = decoded
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
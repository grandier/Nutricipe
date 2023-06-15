const hash = require('../helpers/hashPw');
const register = require('../db/dbConfig');


async function registerUser(req, res) {
    let {name, email, password} = req.body;
    if(!name || !email || !password) {
        return res.status(400).send('Bad Request');
    }
    try{
        const check = await register.checkEmail(email);
        if(check) {
            return res.status(208).json({error: true, message: 'email already in use'});
        }

        const hashed = await hash.hashPw(password);
        const data = {
            name: name,
            email: email,
            password: hashed
        }
        const result = await register.addUser(data);
        if(!result.id) {
            return res.status(500).json({error: true, message: 'add data failed'});
        }
        return res.status(200).json({error: false, message: 'User added successfully'});
    }
    catch(error) {
        return res.status(500).json({error: true, message: 'Internal Server Error'});
    }
}

module.exports = {
    registerUser
}
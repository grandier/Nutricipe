const {checkUser} = require('../db/dbConfig');

async function getProfile(req,res) {
    try{
        const id = req.userId;
        const result = await checkUser(id);
        const temp = result.data();
        if(!temp) return res.status(404).json({error: true, message: 'User not found'});
        const data = {
            name: temp.name,
            email: temp.email,
        }
        return res.status(200).json({error: false, message: 'Success', data});
    }
    catch(error) {
        return res.status(500).json({error: true, message: 'Internal Server Error'});
    }
}

module.exports = {
    getProfile
}
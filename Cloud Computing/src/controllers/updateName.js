const { checkUser, editName } = require('../db/dbConfig');

async function updateName(req, res) {
    try{
        const name = req.body
        const id = req.userId
        const result = await editName(id, name);
        if(result){
            const newData = await checkUser(id);
            const temp = newData.data();
            const data = {
            name: temp.name,
            email: temp.email,
        }
        return res.status(200).json({error: false, message: 'success', data});
        }
        return(res.status(400).json({error: true, message: 'Update Failed'}))
    }
    catch(error) {
        return res.status(500).json({error: true, message: 'Internal Server Error'});
    }
}

module.exports = {
    updateName
}
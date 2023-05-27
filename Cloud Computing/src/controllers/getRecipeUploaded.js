const {getHistoryUpload} = require('../db/dbConfig');

async function getRecipeUploaded(req, res) {
    try{
        const id = req.body.idHistory;
        if(!id) {
            return res.status(400).json({error: true, message: 'Bad Request'});
        }
        const result = await getHistoryUpload(id, res);
        return result;
    }
    catch(error) {
        return res.status(500).json({error: true, message: 'Internal Server Error'});
    }
}

module.exports = {
    getRecipeUploaded
}
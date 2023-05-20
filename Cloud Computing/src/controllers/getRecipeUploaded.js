const {getHistoryUpload} = require('../db/dbConfig');

async function getRecipeUploaded(req, res) {
    try{
        const id = req.body.idHistory;
        const result = await getHistoryUpload(id);
        const temp = result.data();
        return res.status(200).json({error: false, message: 'success', temp});
    }
    catch(error) {
        return res.status(500).json({error: true, message: 'Internal Server Error'});
    }
}

module.exports = {
    getRecipeUploaded
}
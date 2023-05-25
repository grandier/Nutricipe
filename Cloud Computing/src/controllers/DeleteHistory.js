const { cekDataHistory, deleteHistory } = require('../db/dbConfig.js');
const {deleteFile} = require('../db/deleteStorage.js');

async function DeleteHistory(req, res) {
    try {
        const id = req.body.idHistory;
        const userId = req.userId;
        if (!id) {
            return res.status(400).json({ error: true, message: 'Bad Request' });
        }
        const img = await cekDataHistory(id,userId);
        if(img === false){
            return res.status(404).json({ error: true, message: 'Not Found' });
        }
        else if(img === 'unauthorized'){
            return res.status(401).json({ error: true, message: 'Unauthorized' });
        }
        const fileName = img.imageUrl.split('/')[4];
        const result = await deleteHistory(id, res);
        if(result.error === false){
            const ObjectDelete = await deleteFile(fileName);
            if(ObjectDelete){
                return result;
            }
            
        }
        return result;
    }
    catch (error) {
        return res.status(500).json({ error: true, message: 'Internal Server Error' });
    }
}

module.exports = {
    DeleteHistory
}
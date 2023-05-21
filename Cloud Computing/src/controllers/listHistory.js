const {readHistory} = require('../db/dbConfig.js');

async function listHistory(req, res) {
    try {
        const result = await readHistory(req);
        if(result.empty) {
            return res.status(404).json({error: true, message: 'Not Found'});
        }
        return res.status(200).json({error: false, message: 'success', result});
    }
    catch(error) {
        return res.status(500).json({error: true, message: 'Internal Server Error'});
    }
}

module.exports = {
    listHistory
}
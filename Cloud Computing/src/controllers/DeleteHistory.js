const { deleteHistory } = require('../db/dbConfig.js');

async function DeleteHistory(req, res) {
    try {
        const result = await deleteHistory(req, res);
        return result;
    }
    catch (error) {
        return res.status(500).json({ error: true, message: 'Internal Server Error' });
    }
}

module.exports = {
    DeleteHistory
}
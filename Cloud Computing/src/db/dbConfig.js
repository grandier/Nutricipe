const Firestore = require('@google-cloud/firestore');
const path = require('path');

const pathKey = path.resolve('./keyFile.json');
const idProject = process.env.PROJECT_ID || 'nutricipe-coba';


const db = new Firestore({
    projectId: idProject,
    keyFilename: pathKey,
});


async function addUser(data) {
    try {
        const res = await db.collection('users').add(data);
        return res;
    }
    catch (error) {
        return error;
    }
}

async function readUser(data) {
    try {
        const snapshot = await db.collection('users').where('email', '==', data.email).limit(1).get();
        return snapshot;
    }
    catch (error) {
        return error;
    }
}

async function checkEmail(email) {
    try {
        let isUsed = false;
        const res = await db.collection('users').where('email', '==', email).get();
        if (!res.empty) {
            isUsed = true;
        }
        return isUsed;
    }
    catch (error) {
        return error;
    }
}

async function checkUser(id) {
    try {
        const result = await db.collection('users').doc(id).get();
        if (result.empty) {
            return res.status(404).json({ error: true, message: 'User Not Found' });
        }
        return result;
    }
    catch (error) {
        return error;
    }
}

async function saveHistory(data) {
    try {
        const res = await db.collection('history').add(data);
        return res;
    }
    catch (error) {
        return error;
    }
}

async function getHistoryUpload(id, res) {
    try {
        const snapshot = await db.collection('history').doc(id).get();
        if (snapshot.exists) {
            const result = [];
            const temp = snapshot.data();
            temp.id = snapshot.id;
            result.push(temp);
            return res.status(200).json({error: false, message: 'success', result});
            
        }
        return res.status(404).json({ error: true, message: 'Not Found' });
    }
    catch (error) {
        return error;
    }
}

async function editName(id, name) {
    try {
        const res = await db.collection('users').doc(id).update(name);
        if (res.empty) {
            return res.status(400).json({ error: true, message: 'Update failed, please try again' });
        }
        return res;
    }
    catch (error) {
        return error;
    }
}

async function readHistory(req) {
    try {
        const id = req.userId;
        const page = req.query.page;
        const size = parseInt(req.query.size);
        const start = (page - 1) * size;

        const snapshot = await db.collection("history")
            .where("owner", "==", id)
            .orderBy("createdAt", "desc")
            .get();

        const historyList = [];
        snapshot.forEach((doc) => {
            const historyData = doc.data();
            // Add the document ID to the historyData object
            historyData.id = doc.id;
            historyList.push(historyData);
        });

        const list = historyList.slice(start, start + size);
        return list;
    } catch (error) {
        return {
            error: true,
            message: 'Internal server error',
        };
    }


}
async function deleteHistory(idHistory, res) {
    try {
            const result = await db.collection('history').doc(idHistory).delete();
            if(!result){
                return res.status(400).json({ error: true, message: 'Delete Failed' });
            }
            return res.status(200).json({ error: false, message: 'Delete Success' });
    }
    catch (error) {
        return error;
    }
}

async function cekDataHistory(id, userId) {
    try {
        const snapshot = await db.collection('history').doc(id).get();
        if (snapshot.exists) {
            if(snapshot.data().owner !== userId){
                return 'unauthorized';
            }
            return snapshot.data();
        }
        return false;
    }
    catch (error) {
        return error;
    }
}


module.exports = {
    addUser,
    readUser,
    checkEmail,
    checkUser,
    saveHistory,
    getHistoryUpload,
    editName,
    readHistory,
    deleteHistory,
    cekDataHistory
}
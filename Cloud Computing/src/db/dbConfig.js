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
        if(result.empty) {
            return res.status(404).json({error: true, message: 'User Not Found'});
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


module.exports = {
    addUser,
    readUser,
    checkEmail,
    checkUser,
    saveHistory
}
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

async function checkEmail(email) {
    try {
        let isUsed = false;
        const res = await db.collection('users').where('email', '==', email).get();
        if(!res.empty) {
            isUsed = true;
        }
        return isUsed;
    }
    catch (error) {
        return error;
    }
}

module.exports = {
    addUser,
    checkEmail
}
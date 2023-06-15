'use strict'
const { Storage } = require('@google-cloud/storage')
const path = require('path');

const pathKey = path.resolve('./keyFile.json');
const idProject = process.env.PROJECT_ID || 'nutricipe-coba';


// TODO: Sesuaikan konfigurasi Storage
const gcs = new Storage({
    projectId: idProject,
    keyFilename: pathKey,
})

// TODO: Tambahkan nama bucket yang digunakan
const bucketName = 'nutricipe-bucket'
const bucket = gcs.bucket(bucketName)

async function deleteFile(fielName){
    const result = await bucket.file(fielName).delete();
    console.log(result)
    if(result){
        return true;
    }
    else{
        return false;
    }
}

module.exports = {
    deleteFile
}


'use strict'
const { Storage } = require('@google-cloud/storage')
const fs = require('fs');
const { get } = require('http');
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

let ImgUpload = {}

ImgUpload.uploadToGcs = (req) =>{
    
    if (!req.file) return false;


    const date = new Date()
    const gcsname = date.getTime();
    const file = bucket.file(gcsname)

    const stream = file.createWriteStream({
        metadata: {
            contentType: req.file.mimetype
        }
    })

    stream.on('error', (err) => {
        req.file.cloudStorageError = err
        return false;
    })

    stream.end(req.file.buffer)
    {
        return 'https://storage.googleapis.com/nutricipe-bucket/'+gcsname;
    }
}

module.exports = ImgUpload
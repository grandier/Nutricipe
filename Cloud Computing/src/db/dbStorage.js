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

function getPublicUrl(filename) {
    return 'https://storage.googleapis.com/' + bucketName + '/' + filename;
}

let ImgUpload = {}

ImgUpload.uploadToGcs = (req, res, next) =>{
    
    if (!req.file) return next()


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
        next(err)
    })

    stream.on('finish', () => {
        // req.file.cloudStorageObject = gcsname
        // req.file.cloudStoragePublicUrl = getPublicUrl(gcsname)
        next()
    })

    stream.end(req.file.buffer)
    {
        const data = {
            imageUrl: getPublicUrl(gcsname),
            cloudStorageObject: gcsname,
        }
        return data;
    }
}

module.exports = ImgUpload
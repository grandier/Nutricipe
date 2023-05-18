const imgUpload = require('../db/dbStorage');


async function handleUpload(req, res, next) {
    try {
        // Call the uploadImage function
        const resultImage = await imgUpload.uploadToGcs(req, res, next);
        const linkImage =  req.file.cloudStoragePublicUrl;

        // Send a success response
        res.status(200).json({ message: 'Image uploaded successfully', resultImage });
    } catch (error) {
        // Send an error response
        res.status(500).json({ error: 'Error uploading image' });
    }
}

module.exports = {
    handleUpload
}
const imgUpload = require('../db/dbStorage');


async function handleUpload(req, res, next) {
    try {
        // Call the uploadImage function
        const resultImage = await imgUpload.uploadToGcs(req, res, next);

        // Send a success response
        res.status(200).json({ error: false,  message: 'success'});
    } catch (error) {
        // Send an error response
        res.status(500).json({ error: 'Error uploading image' });
    }
}

module.exports = {
    handleUpload
}
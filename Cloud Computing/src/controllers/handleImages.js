const uploadImages = require('../db/dbStorage');


async function handleUpload(req, res) {
    let { Filename } = req.body;
    if (!Filename) {
        return res.status(400).send('Bad Request');
    }
    try {
        // Call the uploadImage function
        const resultImage = await uploadImages.uploadImage(Filename);

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
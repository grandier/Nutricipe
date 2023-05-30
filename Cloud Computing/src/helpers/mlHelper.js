const axios = require('axios');

async function MlHelp(req) {
    const formData = new FormData();
    const fileBlob = new Blob([req.file.buffer], {
        type: req.file.mimetype
    });
    formData.append('image', fileBlob, req.file.originalname);
    let mlres = '';
    try {
        mlres = await axios.post('http://127.0.0.1:8080/yolo/predict', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            })
    } catch (error) {
        return false
    }
    return mlres.data.data;
}

module.exports = {
    MlHelp
}
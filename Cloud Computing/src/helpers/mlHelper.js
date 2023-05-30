const axios = require('axios');

async function MlHelp(req) {
    const formData = new FormData();
    const fileBlob = new Blob([req.file.buffer], {
        type: req.file.mimetype
    });
    formData.append('image', fileBlob, req.file.originalname);
    let mlres = '';
    try {
        mlres = await axios.post('https://nutricipe-ml-zyh6a3mnya-et.a.run.app/yolo/predict', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            }).then(res => {
                return res;
            })
            .catch(err => {
                return false;
            });
            if(mlres.data.data.length == 0){
                return false;
            }
    } catch (error) {
        return false
    }
    return mlres.data.data;
}

module.exports = {
    MlHelp
}
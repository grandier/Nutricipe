const imgUpload = require('../db/dbStorage');
const {
    saveHistory
} = require('../db/dbConfig');
const {
    MlHelp
} = require('../helpers/mlHelper');



let resultImage = '';

async function handleUpload(req, res) {
    try {
      if (!req.file) {
        throw new Error('Bad Request');
      }
  
      resultImage = await imgUpload.uploadToGcs(req, res);
      if (resultImage === undefined || resultImage === false) {
        throw new Error('Error uploading image');
      }
  
      const mlRes = await MlHelp(req);
      console.log(mlRes);
      if (mlRes === false) {
        return res.status(400).json({ error: true, message: 'No object found' });
      }
  
      const arr = mlRes.map(item => item);
      const ingredients = arr.join(',');
  
      if (!req.body.title || !req.body.description) {
        throw new Error('Bad Request');
      }
  
      const data = {
        owner: req.userId,
        title: req.body.title,
        description: req.body.description,
        imageUrl: resultImage,
        createdAt: new Date().getTime(),
        ingredients: ingredients,
      };
  
      const save = await saveHistory(data);
      if (save === false) {
        throw new Error('Failed to save history');
      }
  
      return res.status(200).json({ error: false, message: 'success', data: save.id });
    } catch (error) {
      return res.status(400).json({ error: true, message: error.message });
    }
  }
  

module.exports = {
    handleUpload
};
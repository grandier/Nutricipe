const imgUpload = require('../db/dbStorage');
const {
    saveHistory
} = require('../db/dbConfig');
const {
    MlHelp
} = require('../helpers/mlHelper');



let resultImage = '';

async function handleUpload(req, res, next) {
    try {
        if (!req.file) {
            throw new Error('Bad Request');
        }
        resultImage = await imgUpload.uploadToGcs(req, res, next);
        if(resultImage == undefined){
            throw new Error('Error uploading image');
        }

        const mlRes = await MlHelp(req);
        if (mlRes == null || false) {
            throw new Error('No object can be detected');
        }
        const arr = mlRes.map(item => item);
        const ingredients = arr.join(',');
        // const recipeUrl = `https://api.spoonacular.com/recipes/findByIngredients?apiKey=bdb2e5f6c7a247aab66251096842af5b&ingredients=${ingredients}&number=2`;
        // const recipeRes = await getRecipe(recipeUrl);
        // if (recipeRes == false) {
        //     throw new Error('No recipe found');
        // }
        if (!resultImage || !req.body.title || !req.body.description) {
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
        if (save == false) {
            throw new Error('Failed to save history');
        }
        console.log('udah sampe sini lo 52')
        return res.status(200).json({error: false, message: 'success', data: save.id});

    } catch (err) {
        return res.status(400).json({
            error: true,
            message: err.message
        });
    }
}

module.exports = {
    handleUpload
};
const imgUpload = require('../db/dbStorage');
const {
    saveHistory,
    saveRecipe
} = require('../db/dbConfig');
const {
    MlHelp
} = require('../helpers/mlHelper');

const { getRecipe } = require('../helpers/recipe.helper');



let resultImage = '';

async function handleUpload(req, res) {
    try {
      if (!req.file || !req.body.title || !req.body.description) {
        throw new Error('Bad Request');
      }
  
      resultImage = await imgUpload.uploadToGcs(req, res);
      if (resultImage === undefined || resultImage === false) {
        throw new Error('Error uploading image');
      }
  
      const mlRes = await MlHelp(req);
      if (mlRes === false) {
        return res.status(400).json({ error: true, message: 'No object found' });
      }
  
      const arr = mlRes.map(item => item);
      const ingredients = arr.join(',');

      const recipeUrl = `https://api.spoonacular.com/recipes/findByIngredients?apiKey=bdb2e5f6c7a247aab66251096842af5b&ingredients=${ingredients}&number=2`;
      const recipeRes = await getRecipe(recipeUrl);

      let recipeArr = [];
      for (let i = 0; i < recipeRes.data.length; i++) {
        let recipe = {
          title: recipeRes.data[i].title,
          image: recipeRes.data[i].image,
          usedIngredients: recipeRes.data[i].usedIngredients.map(ingredient => ingredient.name).join(", "),
          missedIngredients: recipeRes.data[i].missedIngredients.map(ingredient => ingredient.name).join(", ")
        };
        recipeArr.push(recipe);
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

      recipeArr[0].owner = req.userId;
      recipeArr[1].owner = req.userId;
      recipeArr[0].idHistory = save.id;
      recipeArr[1].idHistory = save.id;

      const addRecipe = await saveRecipe(recipeArr[0], recipeArr[1]);
      if (!addRecipe) {
        throw new Error('Cannot save recipe');
      }
  
      return res.status(200).json({ error: false, message: 'success', idHistory: save.id });
    } catch (error) {
      return res.status(400).json({ error: true, message: error.message });
    }
  }
  

module.exports = {
    handleUpload
};
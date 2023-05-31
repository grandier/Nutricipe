const { getHistoryUpload, saveRecipe } = require('../db/dbConfig');
const { getRecipe } = require('../helpers/recipe.helper');

async function getRecipeUploaded(req, res) {
  try {
    const id = req.body.idHistory;
    if (!id) {
      return res.status(400).json({ error: true, message: 'Bad Request' });
    }
    
    const result = await getHistoryUpload(id, res);
    const ingredients = result.ingredients;
    const recipeUrl = `https://api.spoonacular.com/recipes/findByIngredients?apiKey=bdb2e5f6c7a247aab66251096842af5b&ingredients=${ingredients}&number=2`;
    const recipeRes = await getRecipe(recipeUrl);

    let arr = [];
    for (let i = 0; i < recipeRes.data.length; i++) {
      let recipe = {
        title: recipeRes.data[i].title,
        image: recipeRes.data[i].image,
        missedIngredients: []
      };
      for (let j = 0; j < recipeRes.data[i].missedIngredientCount; j++) {
        recipe.missedIngredients[j] = { name: recipeRes.data[i].missedIngredients[j].name };
      }
      arr.push(recipe);
    }

    arr[0].owner = result.owner;
    arr[1].owner = result.owner;
    arr[0].idHistory = result.id;
    arr[1].idHistory = result.id;

    // Save arr[0] and arr[1] to Firestore
    const save = await saveRecipe(arr[0], arr[1]);
    if (!save) {
      throw new Error('Cannot save recipe');
    }

    if (!recipeRes) {
      throw new Error('No recipe found');
    }

    const data = { ...result };
    data.recipe = arr;
    return res.status(200).json({ error: false, message: 'success', data: data });
  } catch (error) {
    return res.status(500).json({ error: true, message: error.message });
  }
}

module.exports = {
  getRecipeUploaded
};

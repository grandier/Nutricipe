const { getHistoryUpload, getRecipe } = require('../db/dbConfig');


async function getRecipeUploaded(req, res) {
  try {
    const id = req.body.idHistory;
    if (!id) {
      return res.status(400).json({ error: true, message: 'Bad Request' });
    }
    
    const result = await getHistoryUpload(id,);
    const recipeRes = await getRecipe(id);
    if(!recipeRes || !result){
      throw new Error('No recipe found');
    }


    const data = { ...result };
    data.recipe = recipeRes;
    return res.status(200).json({ error: false, message: 'success', data: data });
  } catch (error) {
    return res.status(500).json({ error: true, message: error.message });
  }
}

module.exports = {
  getRecipeUploaded
};

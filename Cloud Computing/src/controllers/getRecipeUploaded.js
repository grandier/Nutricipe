const {getHistoryUpload} = require('../db/dbConfig');
const {
    getRecipe
} = require('../helpers/recipe.helper');

async function getRecipeUploaded(req, res) {
    try{
        const id = req.body.idHistory;
        if(!id) {
            return res.status(400).json({error: true, message: 'Bad Request'});
        }
        const result = await getHistoryUpload(id, res);  
        const ingredients = result.ingredients;
        const recipeUrl = `https://api.spoonacular.com/recipes/findByIngredients?apiKey=bdb2e5f6c7a247aab66251096842af5b&ingredients=${ingredients}&number=2`;
        const recipeRes = await getRecipe(recipeUrl);
        let arr;
        for(i=0; i<recipeRes.data.length; i++){
            arr.title = recipeRes.data[i].title;
            arr.image = recipeRes.data[i].image;
            for(j=0; j< recipeRes.data[i].missedIngredientCount; j++){
                arr.missedIngredients[j].name = recipeRes.data[i].missedIngredients[j].name;
            }
            
        }
        if (recipeRes == false) {
            throw new Error('No recipe found');
        }
        const data = result;
        data.recipe = recipeRes.data;
        return res.status(200).json({error: false, message: 'success', data: data});
        
    }
    catch(error) {
        return res.status(500).json({error: true, message: error.message});
    }
}

module.exports = {
    getRecipeUploaded
}
const axios = require('axios');

async function getRecipe(recipeUrl) {
  try {
    // Make a GET request using Axios
    const recipe = await axios.get(recipeUrl)
    return recipe;
  } catch (error) {
    return false;
  }
}

module.exports = {
  getRecipe
};

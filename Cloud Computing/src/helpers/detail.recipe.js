const axios = require('axios');
async function getDetailRecipe(id){
    try{
        const result = await axios.get(`https://api.spoonacular.com/recipes/${id}/information?apiKey=bdb2e5f6c7a247aab66251096842af5b`)
        return result.data;
    }catch(error){
        return false;
    }
}

module.exports = {
    getDetailRecipe
}

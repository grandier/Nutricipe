const { getHistoryUpload, getRecipe } = require('../db/dbConfig');
const axios = require('axios');

async function getRecipeUploaded(req, res) {
  try {
    const id = req.body.idHistory;
    if (!id) {
      return res.status(400).json({ error: true, message: 'Bad Request' });
    }

    const result = await getHistoryUpload(id);
    const recipeRes = await getRecipe(id, req.userId, res);
    if(recipeRes[0].error === true || recipeRes[1].error === true){
      return res.status(401).json({ error: true, message: 'unauthorized' });
    }
    if (!recipeRes || !result) {
      throw new Error('No recipe found');
    }
    delete recipeRes[0].error
    delete recipeRes[1].error

    recipeRes[0].link = await searchGoogleCustom(recipeRes[0].title);
    recipeRes[1].link = await searchGoogleCustom(recipeRes[1].title);

    result.recipe = recipeRes;
    const data = [];
    data.push(result);
    return res.status(200).json({ error: false, message: 'success', dataRecipe: data });
  } catch (error) {
    return res.status(500).json({ error: true, message: error.message });
  }
}

async function searchGoogleCustom(title) {
  const query = 'recipe ' + title;
  const numResults = 5;
  try {
    const cx = '503c8675914a94324';
    const apiKey = 'AIzaSyDzrOfz-IcdhgxrxJanrSc1DnnFBDOq8JQ';
    const url = `https://www.googleapis.com/customsearch/v1?cx=${cx}&key=${apiKey}&q=${encodeURIComponent(query)}&num=${numResults}`;

    const response = await axios.get(url);
    const searchResults = response.data.items;

    // Process and format the search results as needed
    const formattedResults = searchResults.map(result => ({
      link: result.link,
    }));

    return formattedResults;
    // Send the formatted search results as the response
  } catch (error) {
    return false
  }
};

module.exports = {
  getRecipeUploaded
};

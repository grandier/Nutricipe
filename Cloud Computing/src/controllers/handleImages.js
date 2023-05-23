const imgUpload = require('../db/dbStorage');
const {saveHistory} = require('../db/dbConfig');

let resultImage = '';
/*
Done Upload Image to bucket and save the image url to the database
*/
async function handleUpload(req, res, next) {
    if(!req.file){
        return res.status(400).json({ error: true, message: 'Bad Request' });
    }

    try {
        // Call the uploadImage function
        resultImage = await imgUpload.uploadToGcs(req, res, next);
        // Send a success response
        //res.status(200).json({ error: false,  message: 'success'});

    } catch (error) {
        // Send an error response
        res.status(500).json({ error: 'Error uploading image' });
    }
 
    /*
    TODO: Pass the image url to machine learning api
    And get the result of the prediction recipe from ml and save it to the database
    */


   if(!resultImage || !req.body.title || !req.body.description) {
         return res.status(400).json({ error: true, message: 'Bad Request' });
    }
    // Save the data from machine learning and the image url to the database
    try{
        const data = {
            owner: req.userId,
            title: req.body.title,
            description: req.body.description,
            imageUrl: resultImage.imageUrl,
            createdAt: new Date().getTime(),
            recipe: {
                recipe1: 'idRecipe1',
            }
        }
        const saveData = await saveHistory(data);
        if(saveData.error) {
            return res.status(500).json({ error: true, message: 'Internal Server Error'});
        }
        return res.status(200).json({ error: false, message: 'success', idHistory: saveData.id});
    }
    catch(error) {
        return res.status(500).json({ error: true, message: 'Internal Server Error'});
    }
   
}

module.exports = {
    handleUpload
}
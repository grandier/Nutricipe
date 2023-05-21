const express = require('express');
const Multer = require('multer')
const router = express.Router();
const register = require('../controllers/register');
const login = require('../controllers/login');
const authMiddleware = require('../middleware/authMiddleware')
const handleImages = require('../controllers/handleImages');
const {getProfile} = require('../controllers/profile');
const {getRecipeUploaded} = require('../controllers/getRecipeUploaded');
const {updateName} = require('../controllers/updateName');

const multer = Multer({
    storage: Multer.MemoryStorage,
    fileSize: 5 * 1024 * 1024
})

router.post('/register', register.registerUser);
router.post('/login', login.loginUser);
router.post('/uploadImage', authMiddleware.authMiddleware, multer.single('image'), handleImages.handleUpload);

router.get('/profile', authMiddleware.authMiddleware, getProfile);
router.post('/getUploaded', authMiddleware.authMiddleware, getRecipeUploaded);


router.put('/updateName',authMiddleware.authMiddleware, updateName)

module.exports = router;
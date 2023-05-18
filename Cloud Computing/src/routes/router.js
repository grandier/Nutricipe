const express = require('express');
const router = express.Router();
const register = require('../controllers/register');
const login = require('../controllers/login');
const uploadImages = require('../controllers/imgUpload');

router.post('/register', register.registerUser);
router.post('/login', login.loginUser);
router.post('/uploadImage', uploadImages.handleUpload);

module.exports = router;
const express = require('express');
const router = express.Router();
const register = require('../controllers/register');
const login = require('../controllers/login');
const handleImages = require('../controllers/handleImages');

router.post('/register', register.registerUser);
router.post('/login', login.loginUser);
router.post('/uploadImage', handleImages.handleUpload);

module.exports = router;
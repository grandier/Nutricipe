const express = require('express');
const Multer = require('multer')
const router = express.Router();
const register = require('../controllers/register');
const login = require('../controllers/login');
const authMiddleware = require('../middleware/authMiddleware')
const handleImages = require('../controllers/handleImages');
const { getProfile } = require('../controllers/profile');
const { getRecipeUploaded } = require('../controllers/getRecipeUploaded');
const { updateName } = require('../controllers/updateName');
const { listHistory } = require('../controllers/listHistory');
const { DeleteHistory } = require('../controllers/DeleteHistory');

const multer = Multer({
    storage: Multer.MemoryStorage,
    fileSize: 5 * 1024 * 1024
})

router.post('/register', register.registerUser);
router.post('/login', login.loginUser);
router.post('/uploadImage', authMiddleware.authMiddleware, multer.single('image'), handleImages.handleUpload);
router.post('/getUploaded', authMiddleware.authMiddleware, getRecipeUploaded);

router.get('/profile', authMiddleware.authMiddleware, getProfile);
router.get('/history', authMiddleware.authMiddleware, listHistory)


router.put('/updateName', authMiddleware.authMiddleware, updateName);
router.delete('/deleteHistory', authMiddleware.authMiddleware, DeleteHistory);

module.exports = router;
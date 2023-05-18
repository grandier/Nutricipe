const bcrypt = require('bcrypt');

async function hashPw(password) {
    try {
        const salt = await bcrypt.genSalt(10);
        const hash = await bcrypt.hash(password, salt);
        return hash;
    }
    catch (error) {
        return error;
    }
}

module.exports = {
    hashPw
}
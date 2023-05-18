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

async function comparePw(password, hashed) {
    try {
        const result = await bcrypt.compareSync(password, hashed);
        return result;
    }
    catch (error) {
        return error;
    }
}

module.exports = {
    hashPw,
    comparePw
}
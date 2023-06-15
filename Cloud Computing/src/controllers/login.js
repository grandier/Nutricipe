const jwt = require('jsonwebtoken');
const compare = require('../helpers/hashPw');
const login = require('../db/dbConfig');


async function loginUser(req, res) {
    let { email, password } = req.body;
    if (!email || !password) {
        return res.status(400).send('Bad Request');
    }
    try {
        const data = {
            email: email,
            password: password
        }
        const result = await login.readUser(data);
        if(result.empty) {
            return res.status(401).json({ error: true, message: 'Email Not Found' });
        }
        const resultDoc = result.docs[0];
        const resultData = resultDoc.data();
        const resultId = resultDoc.id;
        const resultPassword = resultData.password;
        const resultName = resultData.name;

        const compared = await compare.comparePw(password, resultPassword);
        if (compared) {
            // Generate JWT token
            const token = jwt.sign({ userId: resultId }, 'hashed');

            const loginResult = {
                userId: resultId,
                name: resultName,
                token: token
            };
            return res.status(200).json({ error: false, message: 'Success', loginResult });
        }
        return res.status(401).json({ error: true, message: 'Invalid Password' });
    }
    catch (error) {
        return res.status(500).json({ error: true, message: 'Internal Server Error', data: error });
    }
}

module.exports = {
    loginUser
}
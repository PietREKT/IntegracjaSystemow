import axios from 'axios'

const API_URL = 'http://localhost:8080/api/users'

export async function loginUser(username, password) {
    const response = await axios.post('http://localhost:8080/auth/login', {
        username,
        password,
    })
    return response.data
}

export async function registerUser(username, password) {
    const response = await axios.post('http://localhost:8080/auth/register', {
        username,
        password,
    })
    return response.data
}


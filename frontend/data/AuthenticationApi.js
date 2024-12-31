import axios from 'axios';

export async function login(email, password) {
    try {
        await axios.post('http://localhost:8080/api/login', {
            email,
            password,
        }, {
            withCredentials: true,
        });

    } catch (error) {
        console.error('Login error:', error);
        throw error;
    }
}


export async function registerUser(userData) {
    try {
        const response = await axios.post('http://localhost:8080/api/register', userData); // No need for withCredentials

        console.log('Registration successful:', response.data);

    } catch (error) {
        console.error('Registration failed:', error);
    }
}


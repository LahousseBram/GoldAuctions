const API_BASE_URL = 'http://localhost:8080/api';

async function fetchAPI(url, method = 'GET', body = null) {
    const headers = {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
    };

    const options = {
        method,
        headers,
        credentials: 'include'
    };

    if (body) {
        options.body = JSON.stringify(body);
    }


    const response = await fetch(`${API_BASE_URL}${url}`, options);

    if (!response.ok) {
        const errorData = await response.json();
        console.error('Error response:', errorData);
        throw new Error(errorData.message || 'Failed to fetch data');
    }

    return response.json();
}

export async function fetchAllUsers() {
    return fetchAPI('/users');
}

export async function fetchUserById(userId) {
    return fetchAPI(`/users/profile/${userId}`);
}


export async function fetchUserByEmail(email) {
    return fetchAPI(`/users/email`, 'POST', { email });
}

export async function fetchUserProfileById(id) {
    return fetchAPI(`/users/id`, 'POST', { id });
}

export async function verifyUser(token) {
    return fetchAPI('/verify?token=' + token);
}

export async function updateUser(userId, userData) {
    return fetchAPI(`/users/${userId}`, 'PUT', userData);
}


export async function resetPassword(email) {
    return fetchAPI('/reset-password', 'POST', email);
}


export async function resetPasswordConfirm(data) {
    return fetchAPI('/reset-password-confirm', 'POST', data);
}

export async function sendContactEmail(data) {
    return fetchAPI('/send-contact-email', 'POST', data);
}

export async function getCurrentLoggedInUser() {
    return fetchAPI("/users/profile");
}
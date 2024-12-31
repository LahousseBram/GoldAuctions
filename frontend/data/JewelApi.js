import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/jewels';

axios.interceptors.response.use(
    response => response,
    async error => {
        const originalRequest = error.config;
        if (error.response.status === 403 && !originalRequest._retry) {
            originalRequest._retry = true;
            try {
                const refreshResponse = await axios.post('http://localhost:8080/api/refresh', {}, { withCredentials: true });

                if (refreshResponse.data.accessToken) {
                    const accessTokenCookie = refreshResponse.headers['set-cookie']
                        .find(cookie => cookie.startsWith('access_token='));
                    document.cookie = accessTokenCookie;
                }

                return axios(originalRequest);
            } catch (refreshError) {
                console.error('Failed to refresh token:', refreshError);
                return Promise.reject(refreshError);
            }
        }
        return Promise.reject(error);
    }
);

export async function saveJewel(jewelData, selectedFiles) {
    try {
        const formData = new FormData();
        
        const jewelBlob = new Blob([JSON.stringify(jewelData)], {
            type: 'application/json'
        });
        formData.append('jewel', jewelBlob);
        selectedFiles.value.forEach((file) => {
            formData.append('images', file);
        });
        const response = await axios.post(API_BASE_URL, formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
            withCredentials: true
        });
        return response.data;
    } catch (error) {
        console.error('Error saving jewel:', error);
        throw error;
    }
}

export async function fetchJewelById(jewelId) {
    try {
        const response = await axios.get(`${API_BASE_URL}/${jewelId}`, {
            withCredentials: true,
        });
        return response.data;
    } catch (error) {
        console.error('Error fetching jewel by ID:', error);
        throw error;
    }
}

export async function getAllJewels() {
    try {
        const response = await axios.get(API_BASE_URL, {
            withCredentials: true,
        });
        return response.data;
    } catch (error) {
        console.error('Error fetching all jewels:', error);
        throw error;
    }
}

export async function deleteJewel(jewelId) {
    try {
        const response = await axios.delete(`${API_BASE_URL}/${jewelId}`, {
            withCredentials: true,
        });
        return response.data;
    } catch (error) {
        console.error('Error deleting jewel:', error);
        throw error;
    }
}
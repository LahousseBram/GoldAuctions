import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

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


export async function fetchJewelColors() {
    try {
        const response = await axios.get(`${API_BASE_URL}/colors`, {
            withCredentials: true,
        });
        return response.data;
    } catch (error) {
        console.error('Error fetching jewel colors:', error);
        throw error;
    }
}

export async function fetchAuctionTypes() {
    try {
        const response = await axios.get(`${API_BASE_URL}/jewel/auction-type`, {
            withCredentials: true,
        });
        return response.data;
    } catch (error) {
        console.error('Error fetching auction types:', error);
        throw error;
    }
}

export async function fetchJewelTypes() {
    try {
        const response = await axios.get(`${API_BASE_URL}/jewel-types`, {
            withCredentials: true,
        });
        return response.data;
    } catch (error) {
        console.error('Error fetching jewel types:', error);
        throw error;
    }
}

export async function fetchKarats() {
    try {
        const response = await axios.get(`${API_BASE_URL}/karats`, {
            withCredentials: true,
        });
        return response.data;
    } catch (error) {
        console.error('Error fetching karats:', error);
        throw error;
    }
}

export async function fetchJewelImage(jewelId) {
    try {
        const response = await axios.get(`${API_BASE_URL}/jewels/${jewelId}`);
        const jewel = response.data;

        const imageName = jewel.imageNames[0];
        const imageUrl = `${API_BASE_URL}/jewels/image/${imageName}`;

        const imageResponse = await axios.get(imageUrl, { responseType: 'blob' });
        const imageObjectUrl = URL.createObjectURL(imageResponse.data);

        return imageObjectUrl;
    } catch (error) {
        console.error('Error fetching jewel image:', error);
        throw error;
    }
}
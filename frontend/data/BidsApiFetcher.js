import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/bids';

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


export async function fetchBidsForJewel(jewelId) {
    try {
        const response = await axios.get(`${API_BASE_URL}/${jewelId}`, {
            withCredentials: true,
        });
        return response.data;
    } catch (error) {
        console.error('Error fetching bids for jewel:', error);
        throw error;
    }
}

export async function placeBid(bidData) {
    try {
        const response = await axios.post(API_BASE_URL, bidData, {
            withCredentials: true,
        });
        return response.data;
    } catch (error) {
        console.error('Error placing bid:', error);
        throw error;
    }
}

export async function placeAutoBid(bidData) {
    try {
        const response = await axios.post(`${API_BASE_URL}/register-autobid`, bidData, {
            withCredentials: true,
        });
        return response.data;
    } catch (error) {
        console.error('Error placing bid:', error);
        throw error;
    }
}

export async function fetchLatestBidForJewel(jewelId) {
    try {
        const response = await axios.get(`${API_BASE_URL}/latest/${jewelId}`, {
            withCredentials: true,
        });
        return response.data;
    } catch (error) {
        console.error('Error fetching latest bid for jewel:', error);
        throw error;
    }
}

export async function registerAutoBid(autoBidData) {
    try {
        const response = await axios.post(`${API_BASE_URL}/register-autobid`, autoBidData, {
            withCredentials: true,
        });
        return response.data;
    } catch (error) {
        console.error('Error registering auto bid:', error);
        throw error;
    }
}
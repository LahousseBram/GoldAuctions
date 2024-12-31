import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/orders';

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


export async function getOrders() {
    try {
        const response = await axios.get(API_BASE_URL, {
            withCredentials: true,
        });
        return response.data;
    } catch (error) {
        console.error('Error fetching orders:', error);
        throw error;
    }
}

export async function saveOrder(orderData) {
    try {
        const response = await axios.post(API_BASE_URL, orderData, {
            withCredentials: true,
        });
        return response.data;
    } catch (error) {
        console.error('Error saving order:', error);
        throw error;
    }
}
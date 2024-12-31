import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/auctions';

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


export async function getAllAuctions() {
    try {
        const response = await axios.get(API_BASE_URL, {
            withCredentials: true,
        });
        return response.data;
    } catch (error) {
        console.error('Error fetching auctions:', error);
        throw error;
    }
}

export async function saveAuction(auctionData, imageFile) {
    try {
        const formData = new FormData();
        
        const auctionBlob = new Blob([JSON.stringify(auctionData)], {
            type: 'application/json'
        });
        
        formData.append('auction', auctionBlob);
        formData.append('image', imageFile);

        const response = await axios.post(API_BASE_URL, formData, {
        headers: {
            'Content-Type': 'multipart/form-data',
        },
        withCredentials: true
        });
        return response.data;
    } catch (error) {
        console.error('Error saving auction:', error);
        throw error;
    }
}
  
export async function getAuctionTypes() {
try {
    const response = await axios.get(`${API_BASE_URL}/types`, {
    withCredentials: true
    });
    return response.data;
} catch (error) {
    console.error('Error fetching auction types:', error);
    throw error;
}
}

export async function getAuctionById(id) {
    try {
        const response = await axios.get(`${API_BASE_URL}/${id}`, {
            withCredentials: true,
        });
        return response.data;
    } catch (error) {
        console.error('Error fetching auction by ID:', error);
        throw error;
    }
}
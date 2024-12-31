import axios from 'axios';

export default defineNuxtRouteMiddleware(async (to, from) => {
    const profile = ref(null);

    const fetchProfile = async () => {
        try {
            const response = await axios.get('http://localhost:8080/api/users/profile', { withCredentials: true });
            profile.value = response.data;
        } catch (error) {
            console.error("Error fetching profile:", error);
        }
    };

    await fetchProfile();

    const isAdmin = computed(() => profile.value?.role === 'ADMIN');

    if (to.path.startsWith('/admin') && !isAdmin.value) {
        return navigateTo('/');
    }
});
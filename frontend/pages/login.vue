<template>
  <Header title="Log in" />

  <div class="login-page flex flex-col items-center">
    <section class="login-form-section w-full max-w-5xl py-8 lg:pl-40 flex flex-col items-center">
      <form @submit.prevent="handleLogin" class="w-full space-y-6">
        <div class="form-group space-y-2 px-8">
          <label for="email" class="text-white">Email</label>
          <input
              type="email"
              id="email"
              v-model="email"
              placeholder="Enter your email"
              required
              class="w-full p-2 bg-gray-800 text-white rounded-md border border-gray-500 placeholder-gray-400 focus:outline-none focus:border-green-300"
          />
        </div>

        <div class="form-group space-y-2 px-8">
          <label for="password" class="text-white">Password</label>
          <input
              type="password"
              id="password"
              v-model="password"
              placeholder="Enter your password"
              required
              class="w-full p-2 bg-gray-800 text-white rounded-md border border-gray-500 placeholder-gray-400 focus:outline-none focus:border-green-300"
          />
        </div>

        <div class="form-group px-8">
          <a href="/SendMailToResetPass" class="forgot-password text-green-400 hover:text-green-300">Did you forget your password?</a>
        </div>

        <button type="submit" class="login-button w-full p-3 bg-green-400 text-black rounded-md hover:bg-green-700">
          Log in
        </button>
      </form>
    </section>

    <Footer />
  </div>
</template>

<script>
import Header from '~/components/Header.vue';
import Footer from '~/components/Footer.vue';
import {login} from '~/data/AuthenticationApi.js'; // Import the login function

export default {
  components: {
    Header,
    Footer,
  },
  data() {
    return {
      email: '',
      password: '',
    };
  },
  methods: {
    async handleLogin() {
      try {
        // 1. Call the login function from auth.js
        await login(this.email, this.password);

        // 2. (Optional) Store the access token (consider HttpOnly cookies)
        // localStorage.setItem('access_token', accessToken);

        // 3. Redirect the user to the home page or a protected page
        this.$router.push('/');

      } catch (error) {
        // 4. Handle login errors (e.g., display an error message)
        console.error('Login failed:', error);
        // You might want to display an error message to the user here
      }
    }
  }
};
</script>
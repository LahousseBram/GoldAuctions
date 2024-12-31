<template>
  <Header title="Reset your password" />

  <div class="flex flex-col items-center">
    <section class="w-full max-w-md p-4">
      <form @submit.prevent="handleResetPassword" class="space-y-4">
        <div class="flex flex-col space-y-1">
          <label for="password" class="text-gray-400">Password</label>
          <input
              type="password"
              id="password"
              v-model="password"
              placeholder="Enter your password"
              required
              class="p-2 border border-gray-600 rounded-lg bg-gray-800 text-white focus:outline-none focus:border-green-300"
          />
        </div>

        <div class="flex flex-col space-y-1">
          <label for="cPassword" class="text-gray-400">Confirm password</label>
          <input
              type="password"
              id="cPassword"
              v-model="cPassword"
              placeholder="Enter your password"
              required
              class="p-2 border border-gray-600 rounded-lg bg-gray-800 text-white focus:outline-none focus:border-green-300"
          />
        </div>

        <button
            type="submit"
            class="w-full p-3 mt-4 bg-green-400 text-black rounded-lg hover:bg-green-700"
        >
          Send
        </button>
      </form>
    </section>

    <Footer />
  </div>
</template>

<script>
import Header from '~/components/Header.vue';
import Footer from '~/components/Footer.vue';

import { resetPasswordConfirm } from "../data/ApiFetcher.js";

export default {
  components: {
    Header,
    Footer
  },
  data() {
    return {
      password: '',
      cPassword: '',
      token: this.$route.query.token
    };
  },
  methods: {
    async handleResetPassword()
    {
      if (this.password !== this.cPassword) {
        alert("Passwords do not match");
        return;
      }

      try {
        await resetPasswordConfirm({
          token: this.token,
          newPassword: this.password
        });

        alert("Password has been reset successfully");
        this.$router.push('/login');
      } catch (error) {
        console.error("Error resetting password:", error);
        alert("Failed to reset password. Please try again.");
      }
    }

  }
};
</script>

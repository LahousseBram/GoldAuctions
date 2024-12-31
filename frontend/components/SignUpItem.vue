<template>
  <div class="flex justify-center">
    <section class="w-full max-w-5xl p-8">
      <form @submit.prevent="handleLogup">
        <div class="grid grid-cols-2 gap-6">
          <div class="flex flex-col">
            <label for="email" class="mb-2 text-gray-400">Email</label>
            <input type="email" id="email" v-model="email" placeholder="Enter your email" required
                   class="p-2 border border-gray-600 rounded-lg bg-gray-800 text-white focus:outline-none focus:border-green-300" />
          </div>

          <div class="flex flex-col">
            <label for="phone" class="mb-2 text-gray-400">Phone</label>
            <input type="text" id="phone" v-model="phone" placeholder="Enter your phone" required
                   class="p-2 border border-gray-600 rounded-lg bg-gray-800 text-white focus:outline-none focus:border-green-300" />
          </div>

          <div class="flex flex-col">
            <label for="password" class="mb-2 text-gray-400">Password</label>
            <input type="password" id="password" v-model="password" placeholder="Enter your password" required
                   class="p-2 border border-gray-600 rounded-lg bg-gray-800 text-white focus:outline-none focus:border-green-300" />
          </div>

          <div class="flex flex-col">
            <label for="cPassword" class="mb-2 text-gray-400">Confirm password</label>
            <input type="password" id="cPassword" v-model="cPassword" placeholder="Confirm your password" required
                   class="p-2 border border-gray-600 rounded-lg bg-gray-800 text-white focus:outline-none focus:border-green-300" />
          </div>

          <div class="flex flex-col">
            <label for="fname" class="mb-2 text-gray-400">First name</label>
            <input type="text" id="fname" v-model="fname" placeholder="Enter your first name" required
                   class="p-2 border border-gray-600 rounded-lg bg-gray-800 text-white focus:outline-none focus:border-green-300" />
          </div>

          <div class="flex flex-col">
            <label for="lname" class="mb-2 text-gray-400">Last name</label>
            <input type="text" id="lname" v-model="lname" placeholder="Enter your last name" required
                   class="p-2 border border-gray-600 rounded-lg bg-gray-800 text-white focus:outline-none focus:border-green-300" />
          </div>

          <div class="flex flex-col">
            <label for="country" class="mb-2 text-gray-400">Country</label>
            <input type="text" id="country" v-model="country" placeholder="Enter your county" required
                   class="p-2 border border-gray-600 rounded-lg bg-gray-800 text-white focus:outline-none focus:border-green-300" />
          </div>

          <div class="flex flex-col">
            <label for="city" class="mb-2 text-gray-400">City</label>
            <input type="text" id="city" v-model="city" placeholder="Enter your province" required
                   class="p-2 border border-gray-600 rounded-lg bg-gray-800 text-white focus:outline-none focus:border-green-300" />
          </div>

          <div class="flex flex-col col-span-2">
            <label for="street" class="mb-2 text-gray-400">Street + NR</label>
            <input type="text" id="street" v-model="street" placeholder="Enter your street and house number" required
                   class="p-2 border border-gray-600 rounded-lg bg-gray-800 text-white focus:outline-none focus:border-green-300" />
          </div>

          <div class="flex flex-col">
            <label for="postalCode" class="mb-2 text-gray-400">Postal Code</label>
            <input type="text" id="postalCode" v-model="postalCode" placeholder="Enter your postal code" required
                   class="p-2 border border-gray-600 rounded-lg bg-gray-800 text-white focus:outline-none focus:border-green-300" />
          </div>

          <div class="flex items-center col-span-2">
            <input type="checkbox" id="termsAccepted" v-model="termsAccepted" required
                   class="mr-2 border border-gray-600 rounded bg-gray-800 text-green-300 focus:outline-none focus:border-green-300" />
            <label for="termsAccepted" class="text-gray-400">I accept the terms and conditions</label>
          </div>
        </div>

        <button type="submit"
                class="w-full p-3 mt-6 bg-green-400 text-black rounded-lg hover:bg-green-700">
          Register
        </button>
      </form>
    </section>
  </div>
</template>

<script>
import {registerUser} from "~/data/AuthenticationApi.js";
export default {
  data() {
    return {
      email: '',
      phone: '',
      password: '',
      cPassword: '',
      fname: '',
      lname: '',
      country: '',
      city: '',
      street: '',
      postalCode: '',
      termsAccepted: false
    };
  },
  methods: {
    handleLogup() {
      if (this.password !== this.cPassword) {
        alert('Passwords do not match');
        return;
      }
      if (!this.termsAccepted) {
        alert('You must accept the terms and conditions');
        return;
      }
      registerUser({
        email: this.email,
        phoneNumber: this.phone,
        password: this.password,
        firstName: this.fname,
        lastName: this.lname,
        country: this.country,
        postalCode: this.postalCode,
        city: this.city,
        street: this.street,
        termsAccepted: this.termsAccepted
      });
      this.$router.push({ path: '/ConfirmMail' });
    }
  }
};
</script>

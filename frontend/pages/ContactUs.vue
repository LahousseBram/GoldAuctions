<template>
  <div class="contact-page mt-4 flex flex-col items-center">
    <section class="flex flex-col lg:flex-row justify-center items-center w-4/5 p-8 bg-gray-900 border border-gray-800 rounded-lg mb-8">
      <div class="flex-1 lg:mr-8 mb-4 lg:mb-0">
        <img src="http://picsum.photos/640/480/" alt="Jewelry display" class="w-full h-auto object-cover rounded-lg" />
      </div>

      <div class="flex-1 w-full lg:w-1/2 border border-gray-700 rounded-lg p-6 bg-gray-800">
        <h2 class="mb-6 text-2xl font-bold text-white">Contact us</h2>
        <form @submit.prevent="handleSubmit" class="space-y-4">
          <div>
            <input
                type="text"
                placeholder="Full name"
                v-model="fullName"
                required
                class="w-full p-3 bg-gray-700 text-white rounded-lg border border-gray-600 placeholder-gray-400 focus:outline-none focus:border-green-300"
            />
          </div>
          <div>
            <input
                type="email"
                placeholder="E-mail"
                v-model="email"
                required
                class="w-full p-3 bg-gray-700 text-white rounded-lg border border-gray-600 placeholder-gray-400 focus:outline-none focus:border-green-300"
            />
          </div>
          <div>
            <textarea
                placeholder="Message"
                v-model="message"
                required
                class="w-full p-3 bg-gray-700 text-white rounded-lg border border-gray-600 placeholder-gray-400 focus:outline-none focus:border-green-300 resize-vertical h-24"
            ></textarea>
          </div>
          <button type="submit" class="w-full p-3 bg-green-400 text-black rounded-lg hover:bg-green-700">
            Submit
          </button>
        </form>
      </div>
    </section>
    <Footer />
  </div>
</template>

<script>
import Footer from '~/components/Footer.vue';
import {sendContactEmail} from '~/data/ApiFetcher.js';

export default {
  components: {
    Footer
  },
  data() {
    return {
      fullName: '',
      email: '',
      message: ''
    };
  },
  methods: {
    handleSubmit() {
      sendContactEmail({
        name: this.fullName,
        email: this.email,
        message: this.message
      });
    }
  }
};
</script>

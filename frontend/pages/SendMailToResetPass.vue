<script>
import Header from '~/components/Header.vue'
import Footer from '~/components/Footer.vue'
import SendForm from '~/components/SendForm.vue'
import {resetPassword} from "../data/ApiFetcher.js";

export default {
  components: {
    Header,
    Footer,
    SendForm
  },
  data() {
    return {
      userInput: '',
      showForm: true,
    };
  },
  methods: {
    handleUserInput(input) {
      this.showForm = false;
      resetPassword(input);
    }
  }
}
</script>

<template>
  <Header title="Send code to your mail" />

  <div class="flex flex-col items-center">

    <section v-if="showForm" class="form-section">
      <SendForm label="Mail" inputText="Enter your e-mail here" @update:userInput="handleUserInput" />
    </section>

    <section v-else class="check_text">
      <p class="text-white">Check your mail for the link to reset your password</p>
      <br>
    </section>


    <Footer />
  </div>
</template>


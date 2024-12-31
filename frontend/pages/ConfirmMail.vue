<template>
  <Header title="Confirm your mail" />

  <div class="flex flex-col items-center">
    {{ verificationStatus }}
    <section class="check_text">
      <p class="text-white m-auto mb-36">

      </p>
    </section>


    <Footer />
  </div>
</template>

<script>
import {verifyUser} from "~/data/ApiFetcher.js";
export default {
  data() {
    return {
      verificationStatus: "Verifying your email, please check you mail..."
    };
  },
  async mounted() {

    const token = this.$route.query.token;

    if (token) {
      await this.verifyToken(token);
    } else {
      this.verificationStatus = "Invalid verification link.";
    }
  },
  methods: {
    async verifyToken(token) {
      try {
        const response = verifyUser(token);

        if (response.ok) {
          this.verificationStatus = "Your email has been successfully verified. You can now log in.";
        } else {
          this.verificationStatus = "Invalid or expired verification token.";
        }
      } catch (error) {
        this.verificationStatus = "An error occurred during verification. Please try again.";
      }
    }
  }
};
</script>

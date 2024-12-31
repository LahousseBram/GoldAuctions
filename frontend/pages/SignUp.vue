<script>
import SignUpItem from '../components/SignUpItem.vue';
import Header from '~/components/Header.vue'
import Footer from '~/components/Footer.vue'

export default {
  components: {
    Header,
    Footer,
    SignUpItem
  },
  data() {
    return {
      userInput: ''
    };
  }
}

</script>

<template>
  <div>
    <Header title="Sign up" />
      <div class="">
        <SignUpItem
        />
      </div>
    <Footer />
  </div>
</template>

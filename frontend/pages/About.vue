<template>
  <Header :title="aboutText.title" />

  <div class="about-page flex flex-col items-center">
    <section class="about-section w-full max-w-5xl py-8 lg:pl-40 text-center">
      <h1 class="about-title font-bold text-6xl">{{ aboutText.title }}</h1>
      <br />
      <p class="text-gray-300 mb-8">{{ aboutText.introduction }}</p>

      <div v-for="(section, index) in aboutText.sections" :key="index" class="mb-6 text-gray-300 text-left">
        <h2 class="font-semibold text-3xl mb-2">{{ section.heading }}</h2>
        <p v-for="(paragraph, pIndex) in section.paragraphs" :key="pIndex" class="mb-4">{{ paragraph }}</p>
      </div>
    </section>

    <Footer />
  </div>
</template>

<script>
import aboutText from '../data/aboutPageData.json';
import Header from '~/components/Header.vue';
import Footer from '~/components/Footer.vue';

export default {
  components: {
    Header,
    Footer
  },
  data() {
    return {
      aboutText
    };
  }
};
</script>

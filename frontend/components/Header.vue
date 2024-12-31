<script setup lang="ts">
import { defineProps } from 'vue'

// Define props directly using defineProps
const props = defineProps({
  title: {
    type: String,
    default: 'Login'
  },
  bannerImage: {
    type: String,
    default: 'http://picsum.photos/640/480/'
  }
})
</script>

<template>
  <header class="header">
    <div class="header-content">
      <img :src="bannerImage" alt="Banner" class="w-full h-72" />
      <h2 class="text-right pr-5 mb-3 font-bold text-2xl">{{ title }}</h2>
    </div>
  </header>
</template>


<style scoped>

</style>

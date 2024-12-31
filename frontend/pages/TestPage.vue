<script setup lang="ts">
import { ref, onMounted } from 'vue';
import {getOrders} from "~/data/OrderApi";
// Import your API functions

onMounted(async () => {
  try {
    await getOrders();
  } catch (error) {
    console.error('Error:', error);
  }
});
</script>

<template>
  <div>
  </div>
</template>
<script setup lang="ts">
defineProps({
  bid: {
    type: Object,
    required: true,
    default: () => ({}),
  },
});
</script>

<template>
  <div class="bg-base-100 flex items-center justify-between h-16 p-4 rounded-lg">
    <h2 class="text-xl font-bold">{{ bid.userName || 'Unknown User' }}</h2>
    <div class="badge badge-primary text-lg p-3">€ <span>{{ bid.amount }}</span></div>
  </div>
</template>



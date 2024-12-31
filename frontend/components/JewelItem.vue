<template>
  <div class="card bg-neutral flex flex-col gap-1">
    <NuxtImg
        :src="dqsd"
        alt="Jewel Image"
        class="w-full h-1/2 rounded-tr-xl rounded-tl-xl"
    />
    <div class="flex  items-center justify-between gap-3 p-4">
      <h2 class="text-2xl">{{ jewel.name }}</h2>
      <div class="badge badge-success">{{ price }} €</div>
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  jewel: {
    name: string;
    title: string;
  };
  price: number;
}>();
</script>

<style scoped>

</style>
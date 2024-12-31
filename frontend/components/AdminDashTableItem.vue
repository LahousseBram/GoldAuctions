<template>
  <tr class="cursor-pointer hover:bg-gray-700 transition-colors">
    <td class="p-4">
      <NuxtLink :to="`/admin/addItem/${itemId}`">
        <div class="flex items-center gap-3">
          <div class="avatar">
            <div class="mask mask-squircle h-12 w-12">
              <img :src="imageSrc" alt="Avatar Tailwind CSS Component" />
            </div>
          </div>
          <div>
            <div class="font-bold">{{ name }}</div>
          </div>
        </div>
      </NuxtLink>
    </td>
    <td class="p-4">{{ karaat }}</td>
    <td class="p-4">{{ color }}</td>
    <td class="p-4">{{ type }}</td>
  </tr>
</template>

<script setup lang="ts">
import { defineProps } from 'vue';

const props = defineProps({
  itemId: {
    type: Number,
    required: true
  },
  name: String,
  karaat: String,
  color: String,
  type: String,
  imageSrc: String
});
console.log(props)
</script>

<style scoped>
</style>
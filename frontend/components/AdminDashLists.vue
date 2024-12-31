<template>
  <div class="bg-neutral p-2 rounded-2xl h-full flex flex-col overflow-y-auto">
    <h2 class="text-3xl font-bold text-center">{{ title }}</h2>
    <div class="divider"></div>
    <div class="h-full overflow-y-auto p-4 rounded-2xl">
      <ul class="flex flex-col gap-4">
        <li v-for="(item, index) in items" :key="index">
          <div class="bg-base-100 w-full flex items-center justify-between rounded-2xl p-2">
            <slot
                v-if="$slots.item"
                name="item"
                :item="item"
                :urlLink="urlLink"
                :buttonText="buttonText"
            />
            <div v-else>
              <p class="text-2xl font-bold">{{ item.name }}</p>
              <NuxtLink :to="urlLink" class="btn btn-primary">{{ buttonText }}</NuxtLink>
            </div>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineProps } from "vue";



const props = defineProps({
  title: {
    type: String,
    required: true,
  },
  items: {
    type: Array,
    required: true,
    default: () => [],
  },
  buttonText: {
    type: String,
    default: "View orders",
  },
  urlLink: {
    type: String,
  },
});
</script>

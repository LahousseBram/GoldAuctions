<template>
  <div>
    <h1 class="text-3xl font-bold underline">
      List of active auctions
    </h1>
    <div v-if="loading" class="text-center">Loading...</div>
    <div v-else-if="error" class="text-red-500">
      {{ error }}
    </div>
    <div v-else-if="auctionData.length === 0" class="text-center">
      No auctions found
    </div>
    <div v-else class="w-screen flex flex-column items-center justify-center">
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-16">
        <AuctionCard
          v-for="(data, index) in auctionData"
          :key="index"
          :auctionPiece="data"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import AuctionCard from '../components/AuctionCard.vue';
import { getAllAuctions } from '~/data/AuctionApi';

const auctionData = ref([]);
const loading = ref(true);
const error = ref(null);

onMounted(async () => {
  try {
    loading.value = true;
    auctionData.value = await getAllAuctions();
    console.log(auctionData.value[0])
  } catch (err) {
    console.error('Error fetching auction data:', err);
    error.value = err.message || 'Failed to load auctions';
  } finally {
    loading.value = false;
  }
});
</script>

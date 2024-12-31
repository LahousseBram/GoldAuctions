<script setup lang="ts">
import { ref, onMounted } from 'vue';
import AuctionItem from '~/components/AuctionItem.vue';
import { getAuctionById } from '~/data/AuctionApi';

const route = useRoute();
const auction = ref(null);

onMounted(async () => {
  try {
    const auctionId = route.params.auction;
    auction.value = await getAuctionById(auctionId);
  } catch (error) {
    console.error('Error fetching auction:', error);
  }
});
</script>

<template>
  <div v-if="auction" class="h-[calc(100vh-70px)] flex flex-col items-center gap-4 p-2">
    <div class="w-1/2">
      <h1 class="text-3xl font-semibold">{{ auction.name }}</h1>
      <div class="flex justify-between gap-2">
        <p>Auction number <span class="font-bold">{{ auction.auctionId }}</span></p>
        <p><span>{{ auction.jewels.length }}</span> jewel(s)</p>
      </div>
    </div>

    <ul class="justify-self-center self-center h-[80vh] bg-neutral w-1/2 rounded-2xl p-4 overflow-y-auto gap-4 flex flex-col mt-4">
      <li v-for="jewel in auction.jewels" :key="jewel.jewelId">
        <AuctionItem 
          v-if="jewel.published" 
          :jewel="{ ...jewel, auctionId: auction.auctionId }" 
        />
      </li>
    </ul>
  </div>
</template>

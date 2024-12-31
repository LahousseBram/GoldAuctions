<script setup lang="ts">
import { defineProps, ref, onMounted, computed } from 'vue';
import { fetchLatestBidForJewel } from '~/data/BidsApiFetcher';

const bid = ref<Bid | null>(null);
const currentImageIndex = ref(0);
const isLoading = ref(true);
const isImageLoading = ref(true);

interface Bid {
  bidId: number;
  amount: number;
  bidTime: string;
  userId: number;
  jewelId: number;
}

interface Jewel {
  name: string;
  imageNames: string[];
  bids: Bid[];
  startingPrice: number;
  auctionId: string | number;
  jewelId: string | number;
  endDate: string;
}

const props = defineProps<{
  jewel: Jewel;
}>();

const remainingTime = ref('');
const timerColor = ref('text-green-500');
const timerIcon = ref('⌛');

const updateCountdown = () => {
  const endDate = new Date(props.jewel.endDate).getTime();
  const now = Date.now();
  const timeRemaining = endDate - now;

  if (timeRemaining <= 0) {
    remainingTime.value = '00:00:00';
    timerColor.value = 'text-gray-500';
    timerIcon.value = '❌';
  } else {
    const days = Math.floor(timeRemaining / (1000 * 60 * 60 * 24));
    const hours = Math.floor((timeRemaining % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
    const minutes = Math.floor((timeRemaining % (1000 * 60 * 60)) / (1000 * 60));
    const seconds = Math.floor((timeRemaining % (1000 * 60)) / 1000);
    remainingTime.value = `${days}d ${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;

    if (timeRemaining <= 3600000) {
      timerColor.value = 'text-red-500';
      timerIcon.value = '❗';
    } else {
      timerColor.value = 'text-green-500';
      timerIcon.value = '⌛';
    }
  }
};

onMounted(async () => {
  updateCountdown();
  setInterval(updateCountdown, 1000);

  try {
    const latestBid = await fetchLatestBidForJewel(props.jewel.jewelId);
    bid.value = latestBid;
  } catch (error) {
    console.error('Error fetching latest bid:', error);
  } finally {
    isLoading.value = false;
  }
});

const currentImageUrl = computed(() => {
  if (!props.jewel.imageNames || props.jewel.imageNames.length === 0) {
    return ''; 
  }
  const encodedImageName = encodeURIComponent(props.jewel.imageNames[currentImageIndex.value]);
  return `http://localhost:8080/api/jewels/image/${encodedImageName}`;
});

const formatCurrency = (amount: number) => {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
  }).format(amount);
};

const nextImage = () => {
  if (props.jewel.imageNames && props.jewel.imageNames.length > 0) {
    isImageLoading.value = true;
    currentImageIndex.value = (currentImageIndex.value + 1) % props.jewel.imageNames.length;
  }
};

const previousImage = () => {
  if (props.jewel.imageNames && props.jewel.imageNames.length > 0) {
    isImageLoading.value = true;
    currentImageIndex.value = currentImageIndex.value === 0 
      ? props.jewel.imageNames.length - 1 
      : currentImageIndex.value - 1;
  }
};

const handleImageLoad = () => {
  isImageLoading.value = false;
};

const isExpired = computed(() => {
  const endDate = new Date(props.jewel.endDate).getTime();
  return Date.now() > endDate;
});
</script>

<template>
  <div 
    class="bg-base-100 rounded-3xl p-2 flex items-center gap-4 min-h-[144px]"
    :class="{ 'opacity-50 grayscale': isExpired }"
  >
    <template v-if="isLoading">
      <div class="animate-pulse flex items-center gap-4 w-full">
        <div class="bg-gray-200 h-32 w-32 rounded-3xl"></div>
        <div class="flex flex-col gap-4 flex-grow">
          <div class="h-8 bg-gray-200 rounded w-1/3"></div>
          <div class="h-6 bg-gray-200 rounded w-1/2"></div>
        </div>
        <div class="h-10 w-24 bg-gray-200 rounded"></div>
      </div>
    </template>

    <template v-else>
      <div class="relative w-32 h-32">
        <div 
          v-if="isImageLoading" 
          class="absolute inset-0 flex items-center justify-center bg-gray-100 rounded-3xl"
        >
          <div class="w-8 h-8 border-4 border-primary border-t-transparent rounded-full animate-spin"></div>
        </div>

        <NuxtImg 
          :src="currentImageUrl" 
          :alt="jewel.name"
          class="h-32 w-32 rounded-3xl object-cover"
          @load="handleImageLoad"
          :class="{ 'opacity-0': isImageLoading }" 
        />

        <template v-if="jewel.imageNames && jewel.imageNames.length > 1">
          <button 
            @click="previousImage" 
            class="absolute left-0 top-1/2 -translate-y-1/2 bg-black/30 hover:bg-black/50 text-white rounded-r p-1"
          >
            ←
          </button>
          <button 
            @click="nextImage" 
            class="absolute right-0 top-1/2 -translate-y-1/2 bg-black/30 hover:bg-black/50 text-white rounded-l p-1"
          >
            →
          </button>
          <div class="absolute bottom-1 right-1 bg-black/30 text-white text-xs px-2 py-1 rounded">
            {{ currentImageIndex + 1 }}/{{ jewel.imageNames.length }}
          </div>
        </template>
      </div>

      <div class="flex flex-col justify-between p-4">
        <h2 class="text-2xl">{{ jewel.name }}</h2>
        <div class="flex flex-col gap-1">
          <p v-if="bid" class="text-lg text-primary">
            Highest bid: <span class="font-bold">{{ formatCurrency(bid.amount) }}</span>
          </p>
          <p v-else class="text-lg text-secondary">
            No bids yet. Starting price: <span class="font-bold">{{ formatCurrency(jewel.startingPrice) }}</span>
          </p>
        </div>
        <p :class="[timerColor, isExpired ? 'text-error' : '']" class="text-sm mt-2 font-semibold">
          {{ isExpired ? 'Auction ended' : `${timerIcon} ${remainingTime}` }}
        </p>
      </div>
    </template>

    <NuxtLink 
      v-if="!isExpired"
      :to="`/auctions/${jewel.auctionId}/items/${jewel.jewelId}`" 
      class="btn btn-primary ml-auto px-6 text-lg"
    >
      View more
    </NuxtLink>
    <span 
      v-else 
      class="btn btn-disabled ml-auto px-6 text-lg cursor-not-allowed"
    >
      Expired
    </span>
  </div>
</template>

<style scoped>
</style>

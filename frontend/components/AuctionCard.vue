<template>
  <div>
    <NuxtLink
      v-if="!isExpired"
      :to="`/auctions/${auctionPiece.auctionId}/items/`"
      class="card-container max-w-xs overflow-hidden relative"
    >
      <div class="card bg-neutral flex flex-col gap-1">
        <NuxtImg
          :src="`http://localhost:8080/api/auctions/image/${auctionPiece.imageName}` || 'http://picsum.photos/640/480/'"
          :alt="auctionPiece.name || 'Auction image'"
          class="w-full h-auto rounded-tr-xl rounded-tl-xl"
        />

        <div class="flex flex-col gap-3 p-4">
          <h2 class="card-title">{{ auctionPiece.name }}</h2>
          <div :class="['badge font-bold', isExpired ? 'badge-error' : 'badge-primary']">
            {{ isExpired ? 'Expired' : remainingTime }}
          </div>
        </div>
      </div>
    </NuxtLink>

    <div 
      v-else
      class="card-container max-w-xs overflow-hidden relative opacity-50 cursor-not-allowed"
    >
      <div class="card bg-neutral flex flex-col gap-1">
        <NuxtImg
          :src="`http://localhost:8080/api/auctions/image/${auctionPiece.imageName}` || 'http://picsum.photos/640/480/'"
          :alt="auctionPiece.name || 'Auction image'"
          class="w-full h-auto rounded-tr-xl rounded-tl-xl grayscale"
        />

        <div class="flex flex-col gap-3 p-4">
          <h2 class="card-title">{{ auctionPiece.name }}</h2>
          <div class="badge badge-error font-bold">
            Expired
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';

const props = defineProps({
  auctionPiece: {
    type: Object,
    required: true,
  },
});

const remainingTime = ref('');
const isExpired = computed(() => {
  const endDate = new Date(props.auctionPiece.endDate).getTime();
  return Date.now() > endDate;
});

const updateCountdown = () => {
  const endDate = new Date(props.auctionPiece.endDate).getTime();
  const now = Date.now();
  const timeRemaining = endDate - now;

  if (timeRemaining <= 0) {
    remainingTime.value = '00:00:00';
    return;
  }

  const days = Math.floor(timeRemaining / (1000 * 60 * 60 * 24));
  const hours = Math.floor((timeRemaining % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
  const minutes = Math.floor((timeRemaining % (1000 * 60 * 60)) / (1000 * 60));
  const seconds = Math.floor((timeRemaining % (1000 * 60)) / 1000);
  remainingTime.value = `${days}d ${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
};

onMounted(() => {
  updateCountdown();
  const timer = setInterval(updateCountdown, 1000);

  onBeforeUnmount(() => {
    clearInterval(timer);
  });
});
</script>

<style scoped>
.grayscale {
  filter: grayscale(100%);
}
</style>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from "vue";
import { getCurrentLoggedInUser } from "~/data/ApiFetcher";
import { placeAutoBid, placeBid } from "~/data/BidsApiFetcher";
import { fetchJewelById } from "~/data/JewelApi";

const router = useRouter();
const currentIndex = ref(0);

const prevSlide = () => {
  currentIndex.value = (currentIndex.value - 1 + jewel.value?.imageNames.length) % jewel.value?.imageNames.length;
};

const nextSlide = () => {
  currentIndex.value = (currentIndex.value + 1) % jewel.value?.imageNames.length;
};

const goToSlide = (index: number) => {
  currentIndex.value = index;
};

const handleBack = () => {
  window.location.href = `/auctions/${jewel.value.auctionId}/items`;
};

interface WebSocketMessage {
  type: string;
  data: string;
}

interface Jewel {
  jewelId: number;
  name: string;
  title: string;
  description: string;
  startingPrice: number;
  imageNames: string[];
  colorName: string;
  karat: string;
  typeName: string;
  endDate: string;
  published: boolean;
  bids: Bid[];
}

interface Bid {
  bidId: number;
  amount: number;
  timestamp: string;
  userId: number;
  jewelId: number;
  userName: string;
}

interface WebSocketBidMessage {
  amount: number;
  userId: number;
  jewelId: number;
  userName: string;
}

interface User {
  city: string,
  email: string,
  id: number,
  phoneNumber: string,
  postalCode: string,
  role: string,
  street: string,
  userName: string
}

interface Alert {
  message: string;
  type: 'success' | 'error';
}

const route = useRoute();
const jewel = ref<Jewel | null>(null);
const isLoading = ref(true);
const websocket = ref<WebSocket | null>(null);
const bids = ref<Bid[]>([]);
const currentUser = ref<User | null>(null);
const showAlert = ref(false);
const alert = ref<Alert>({ message: '', type: 'success' });
const highestBid = ref<number | null>(null);

const sortedBids = computed(() => {
  const sorted = [...bids.value].sort((a, b) => b.amount - a.amount);
  if (sorted.length > 0) {
    highestBid.value = sorted[0].amount;
  }
  return sorted;
});

const isHighestBid = (bid: Bid) => {
  return bid.amount === highestBid.value;
};

const displayAlert = (message: string, type: 'success' | 'error') => {
  alert.value = { message, type };
  showAlert.value = true;
  setTimeout(() => {
    showAlert.value = false;
  }, 3000);
};

const handleWebSocketBid = (bidMessage: WebSocketBidMessage) => {
  if (!jewel.value || bidMessage.jewelId !== jewel.value.jewelId) return;

  const newBid: Bid = {
    bidId: Date.now(),
    amount: bidMessage.amount,
    timestamp: new Date().toISOString(),
    userId: bidMessage.userId,
    jewelId: bidMessage.jewelId,
    userName: bidMessage.userName
  };

  bids.value = [newBid, ...bids.value];
  
  if (jewel.value) {
    jewel.value = {
      ...jewel.value,
      bids: [newBid, ...jewel.value.bids]
    };
  }

  if (!highestBid.value || bidMessage.amount > highestBid.value) {
    highestBid.value = bidMessage.amount;
  }

  if (bidMessage.userId === currentUser.value?.id) {
    displayAlert('Your bid has been placed successfully!', 'success');
  } else {
    displayAlert(`New bid placed by ${bidMessage.userName}!`, 'success');
  }
};

onMounted(async () => {
  try {
    currentUser.value = await getCurrentLoggedInUser();
    const jewelId = parseInt(route.params.id as string);
    const fetchedJewel = await fetchJewelById(jewelId);
    
    fetchedJewel.bids = fetchedJewel.bids.map(bid => ({
      ...bid,
      userName: bid.username || 'Unknown User'
    }));
    
    jewel.value = fetchedJewel;

    console.log(jewel)
    bids.value = [...fetchedJewel.bids].sort((a, b) => b.amount - a.amount);
    
    if (bids.value.length > 0) {
      highestBid.value = bids.value[0].amount;
    }
  } catch (error) {
    console.error("Error fetching jewel:", error);
    displayAlert("Error loading jewel details", 'error');
  } finally {
    isLoading.value = false;
  }

  websocket.value = new WebSocket("ws://localhost:3001");
  
  websocket.value.onopen = () => {
    console.log("WebSocket connected");
    if (websocket.value && jewel.value) {
      websocket.value.send(JSON.stringify({
        type: 'subscribe',
        jewelId: jewel.value.jewelId
      }));
    }
  };

  websocket.value.onmessage = (event) => {
    try {
      const wrapper = JSON.parse(event.data);
      
      if (wrapper.type === 'message') {
        const innerData = JSON.parse(wrapper.data);
        
        const bidMessage: WebSocketBidMessage = {
          amount: innerData.amount,
          userId: innerData.userId,
          jewelId: innerData.jewelId,
          userName: innerData.username || 'Unknown User'
        };
        
        console.log("Processed message:", bidMessage);
        handleWebSocketBid(bidMessage);
      } else {
        console.log("Received non-bid message:", wrapper);
      }
    } catch (error) {
      console.error("Error processing WebSocket message:", error);
      displayAlert("Error processing bid update", 'error');
    }
  };

  websocket.value.onclose = () => {
    console.log("WebSocket disconnected");
    displayAlert("Connection lost. Please refresh the page.", 'error');
  };

  websocket.value.onerror = (error) => {
    console.error("WebSocket error:", error);
    displayAlert("Connection error occurred", 'error');
  };
});

onBeforeUnmount(() => {
  if (websocket.value) {
    if (jewel.value) {
      websocket.value.send(JSON.stringify({
        type: 'unsubscribe',
        jewelId: jewel.value.jewelId
      }));
    }
    websocket.value.close();
  }
});

async function handlePlaceBid(amount: string) {
  if (!jewel.value || !currentUser.value) return;
  
  try {
    const numericAmount = parseFloat(amount);
    if (isNaN(numericAmount)) throw new Error("Invalid bid amount");
    
    await placeBid({
      amount: numericAmount,
      jewelId: jewel.value.jewelId,
      userId: currentUser.value.id
    });
    
  } catch (error) {
    console.error('Error placing bid:', error);
    displayAlert('Error placing bid. Please try again.', 'error');
  }
}

async function handleAutoBid(amount: string) {
  if (!jewel.value) return;
  
  try {
    const numericAmount = parseFloat(amount);
    if (isNaN(numericAmount)) throw new Error("Invalid auto-bid amount");
    
    await placeAutoBid({
      maxAmount: numericAmount,
      jewelId: jewel.value.jewelId,
      userId: currentUser.value.id
    });
    displayAlert('Auto-bid set successfully!', 'success');
  } catch (error) {
    console.error('Error setting auto-bid:', error);
    displayAlert('Error setting auto-bid. Please try again.', 'error');
  }
}

const getImageUrl = (imageName: string) => {
  return `http://localhost:8080/api/jewels/image/${encodeURIComponent(imageName)}`;
};
</script>

<template>
  <div class="relative">
    <button 
      @click="handleBack" 
      class="absolute top-4 left-4 flex items-center text-base-content hover:text-primary transition-colors"
      aria-label="Go Back"
    >
      <svg 
        xmlns="http://www.w3.org/2000/svg" 
        fill="none" 
        viewBox="0 0 24 24" 
        stroke-width="2" 
        stroke="currentColor" 
        class="w-6 h-6"
      >
        <path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7" />
      </svg>
    </button>

    <div class="p-4">
      <h1 class="text-2xl font-bold text-center">Jewel Details</h1>
    </div>
  </div>
  <Transition
    enter-active-class="transition ease-out duration-300"
    enter-from-class="transform opacity-0 scale-95"
    enter-to-class="transform opacity-100 scale-100"
    leave-active-class="transition ease-in duration-200"
    leave-from-class="transform opacity-100 scale-100"
    leave-to-class="transform opacity-0 scale-95"
  >
    <div
      v-if="showAlert"
      :class="{
        'fixed top-4 right-4 p-4 rounded-lg shadow-lg z-50 max-w-md transition-all duration-300': true,
        'bg-success text-success-content': alert.type === 'success',
        'bg-error text-error-content': alert.type === 'error'
      }"
    >
      {{ alert.message }}
    </div>
  </Transition>

  <div v-if="isLoading" class="flex justify-center items-center h-[calc(100vh-70px)]">
    <div class="w-8 h-8 border-4 border-primary border-t-transparent rounded-full animate-spin"></div>
  </div>

  <div v-else-if="jewel" class="flex p-4 h-[calc(100vh-70px)] w-full">
    <div class="grid grid-cols-[0.5fr_1fr] w-full grid-rows-2 gap-4">
      <div>
        <div class="carousel w-full h-[90%] rounded-2xl">
          <div
            v-for="(imageName, index) in jewel.imageNames"
            :key="index"
            :id="`item${index + 1}`"
            class="carousel-item w-full"
          >
            <NuxtImg 
              :src="getImageUrl(imageName)" 
              class="w-full h-full rounded-2xl object-cover"
              :alt="jewel.name"
            />
          </div>
        </div>

        <div class="flex w-full justify-center gap-2 py-2">
          <a
            v-for="(imageName, index) in jewel.imageNames"
            :key="`button-${index}`"
            :href="`#item${index + 1}`"
            class="btn btn-xs"
          >
            {{ index + 1 }}
          </a>
        </div>
      </div>

      <div class="bg-neutral rounded-2xl grid grid-cols-2">
        <div class="p-2">
  <h2 class="text-2xl font-bold text-center">Bids</h2>
  <div class="divider"></div>
  <ul class="overflow-y-auto flex flex-col gap-2 h-[17rem] p-2">
    <li 
      v-for="bid in sortedBids" 
      :key="bid.bidId"
      class="p-4 rounded-lg shadow-lg transition-transform duration-200 transform"
      :class="{
        'border-2 border-primary bg-primary bg-opacity-10': isHighestBid(bid),
        'border-2 border-base-300 bg-base-100': !isHighestBid(bid)
      }"
    >
              <div class="flex justify-between items-center mb-1">
                <span 
                  class="font-semibold text-lg flex items-center gap-2"
                  :class="{ 'text-primary': isHighestBid(bid) }"
                >
                  <span v-if="isHighestBid(bid)" class="text-yellow-500">
                    🏆
                  </span>
                  {{ bid.userName }}
                </span>
                <span 
                  class="text-xl font-bold"
                  :class="{ 'text-primary': isHighestBid(bid), 'text-base-content': !isHighestBid(bid) }"
                >
                  ${{ bid.amount.toFixed(2) }}
                </span>
              </div>
              <div class="text-sm text-base-content/70">
                {{ new Date(bid.timestamp).toLocaleString() }}
              </div>
            </li>
          </ul>
        </div>

        <div class="p-4 flex flex-col justify-center">
          <BidPlacing
            title="Place Bid"
            placeholder="Enter bid"
            buttonText="Place bid"
            @bid="handlePlaceBid"
          />
          <div class="divider"></div>
          <BidPlacing
            title="Auto-Bid"
            placeholder="Enter auto-bid"
            buttonText="Set auto-bid"
            @bid="handleAutoBid"
          />
        </div>
      </div>

      <div class="bg-neutral col-span-2 rounded-2xl p-6 flex flex-col gap-6">
        <div class="p-4 bg-base-100 rounded-lg shadow-lg">
          <h2 class="text-3xl font-extrabold mb-4">Jewel Information</h2>
          <ul class="text-lg">
            <li class="mb-2">
              <span class="font-semibold">Name: </span>
              <span class="text-base-content/90">{{ jewel.name }}</span>
            </li>
            <li class="mb-2">
              <span class="font-semibold">Color: </span>
              <span class="text-base-content/90">{{ jewel.colorName }}</span>
            </li>
            <li class="mb-2">
              <span class="font-semibold">Karat: </span>
              <span class="text-base-content/90">{{ jewel.karat }}</span>
            </li>
            <li class="mb-2">
              <span class="font-semibold">Type: </span>
              <span class="text-base-content/90">{{ jewel.typeName }}</span>
            </li>
          </ul>
        </div>

        <div class="p-4 bg-base-100 rounded-lg shadow-lg">
          <h2 class="text-3xl font-extrabold mb-4">Description</h2>
          <p class="text-base-content/90 text-lg leading-relaxed">{{ jewel.description }}</p>
        </div>
      </div>
    </div>
  </div>

  <div v-else class="flex justify-center items-center h-[calc(100vh-70px)]">
    <p class="text-xl">Jewel not found</p>
  </div>
</template>
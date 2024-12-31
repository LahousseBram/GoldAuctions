<template>
  <div class="min-h-screen bg-gray-900 p-8">
    <BackButton />
    <section class="max-w-4xl mx-auto bg-gray-800 rounded-2xl shadow-2xl p-8">
      <h1 class="text-3xl font-bold text-gray-100 mb-8">Add New Jewel</h1>
      
      <form @submit.prevent="handleAddJewel" class="space-y-6">
        <div class="grid grid-cols-2 gap-6">
          <div class="flex flex-col">
            <label for="name" class="mb-2 text-gray-400">Name</label>
            <input 
              type="text" 
              id="name" 
              v-model="jewel.name" 
              placeholder="Enter name" 
              required
              class="p-2 border border-gray-600 rounded-lg bg-gray-800 text-white focus:outline-none focus:border-green-300" 
            />
          </div>

          <div class="flex flex-col">
            <label for="title" class="mb-2 text-gray-400">Title</label>
            <input 
              type="text" 
              id="title" 
              v-model="jewel.title" 
              placeholder="Enter title" 
              required
              class="p-2 border border-gray-600 rounded-lg bg-gray-800 text-white focus:outline-none focus:border-green-300" 
            />
          </div>

          <div class="flex flex-col col-span-2">
            <label for="description" class="mb-2 text-gray-400">Description</label>
            <textarea 
              id="description" 
              v-model="jewel.description" 
              placeholder="Enter description" 
              required 
              rows="4"
              class="p-2 border border-gray-600 rounded-lg bg-gray-800 text-white focus:outline-none focus:border-green-300"
            ></textarea>
          </div>

          <div class="flex flex-col">
            <label for="startingPrice" class="mb-2 text-gray-400">Starting Price</label>
            <input 
              type="number" 
              id="startingPrice" 
              v-model.number="jewel.startingPrice" 
              placeholder="Enter starting price" 
              required 
              min="1"
              class="p-2 border border-gray-600 rounded-lg bg-gray-800 text-white focus:outline-none focus:border-green-300" 
            />
          </div>

          <div class="flex flex-col">
            <label for="karat" class="mb-2 text-gray-400">Karat</label>
            <input 
              type="number" 
              id="karat" 
              v-model.number="jewel.karat" 
              required 
              min="1" 
              max="24"
              placeholder="Enter karat (1-24)"
              class="p-2 border border-gray-600 rounded-lg bg-gray-800 text-white focus:outline-none focus:border-green-300" 
            />
          </div>

          <div class="flex flex-col">
            <label for="color" class="mb-2 text-gray-400">Color</label>
            <select 
              id="color" 
              v-model="jewel.colorName" 
              required
              class="p-2 border border-gray-600 rounded-lg bg-gray-800 text-white focus:outline-none focus:border-green-300"
            >
              <option disabled value="">Select Color</option>
              <option 
                v-for="color in colors" 
                :key="color.colorId" 
                :value="color.name"
              >
                {{ color.name }}
              </option>
            </select>
          </div>

          <div class="flex flex-col">
            <label for="type" class="mb-2 text-gray-400">Type</label>
            <select 
              id="type" 
              v-model="jewel.typeName" 
              required
              class="p-2 border border-gray-600 rounded-lg bg-gray-800 text-white focus:outline-none focus:border-green-300"
            >
              <option disabled value="">Select Type</option>
              <option 
                v-for="type in types" 
                :key="type.typeId" 
                :value="type.name"
              >
                {{ type.name }}
              </option>
            </select>
          </div>

          <div class="flex flex-col">
            <label for="endDate" class="mb-2 text-gray-400">End Date</label>
            <input 
              type="datetime-local" 
              id="endDate" 
              v-model="jewel.endDate" 
              required
              class="p-2 border border-gray-600 rounded-lg bg-gray-800 text-white focus:outline-none focus:border-green-300" 
            />
          </div>

          <div class="flex flex-col">
            <label for="auction" class="mb-2 text-gray-400">Auction</label>
            <select 
              id="auction" 
              v-model="jewel.auctionId" 
              required
              class="p-2 border border-gray-600 rounded-lg bg-gray-800 text-white focus:outline-none focus:border-green-300"
            >
              <option disabled :value="null">Select Auction</option>
              <option 
                v-for="auction in auctions" 
                :key="auction.auctionId" 
                :value="auction.auctionId"
              >
                {{ auction.naam }} (ID: {{ auction.auctionId }})
              </option>
            </select>
          </div>

          <div class="flex flex-col col-span-2">
            <label for="imageFile" class="mb-2 text-gray-400">Upload Images</label>
            <input 
              type="file" 
              id="imageFile" 
              @change="handleFileUpload" 
              accept="image/*" 
              multiple
              class="p-2 border border-gray-600 rounded-lg bg-gray-800 text-white focus:outline-none focus:border-green-300" 
            />
            <div v-if="selectedFiles.length" class="mt-2 text-gray-400">
              Selected files: {{ selectedFiles.length }}
            </div>
          </div>

          <div class="flex items-center col-span-2">
            <input 
              type="checkbox" 
              id="published" 
              v-model="jewel.published"
              class="mr-2 border border-gray-600 rounded bg-gray-800 text-green-300 focus:outline-none focus:border-green-300" 
            />
            <label for="published" class="text-gray-400">Published</label>
          </div>
        </div>

        <button 
          type="submit"
          :disabled="isSubmitting"
          class="w-full p-3 mt-6 bg-green-400 text-black rounded-lg hover:bg-green-700 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          {{ isSubmitting ? 'Adding Jewel...' : 'Add Jewel' }}
        </button>
      </form>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { fetchJewelColors, fetchJewelTypes } from '~/data/JewelDataApiFetcher';
import { saveJewel } from '~/data/JewelApi';
import { navigateTo } from '#app';
import BackButton from '~/components/BackButton.vue';
import { getAllAuctions } from '~/data/AuctionApi';

// Form data
const jewel = ref({
  jewelId: null,
  name: '',
  title: '',
  description: '',
  startingPrice: null,
  colorName: '',
  karat: null,
  typeName: '',
  endDate: null,
  published: false,
  imageNames: [],
  auctionId: null,
  bids: []
});

// UI state
const colors = ref([]);
const types = ref([]);
const selectedFiles = ref([]);
const isSubmitting = ref(false);
const auctions = ref([]);

onMounted(async () => {
  try {
    colors.value = await fetchJewelColors();
    types.value = await fetchJewelTypes();
    auctions.value = await getAllAuctions();
  } catch (error) {
    console.error("Error fetching form options:", error);
    alert("Error loading form options. Please try again.");
  }
});

const handleFileUpload = (event) => {
  selectedFiles.value = Array.from(event.target.files);
};

const handleAddJewel = async () => {
  if (isSubmitting.value) return;
  
  try {
    isSubmitting.value = true;
    
    const jewelData = {
      ...jewel.value,
      karat: Number(jewel.value.karat),
      startingPrice: Number(jewel.value.startingPrice),
      endDate: new Date(jewel.value.endDate).toISOString()
    };

    Object.keys(jewelData).forEach(key => {
      if (jewelData[key] === null || jewelData[key] === '') {
        delete jewelData[key];
      }
    });

    await saveJewel(jewelData, selectedFiles);
    alert('Jewel added successfully!');
    navigateTo('/admin');
  } catch (error) {
    console.error("Error adding jewel:", error);
    const errorMessage = error.response?.data?.message || 'Error adding jewel. Please try again.';
    alert(errorMessage);
  } finally {
    isSubmitting.value = false;
  }
};
</script>
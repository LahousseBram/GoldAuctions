<template>
  <div class="h-[calc(100vh-4.3rem)] relative p-4">
    <BackButton/>
    <div class="h-full w-full flex justify-center items-center">
      <form @submit.prevent="submitForm" class="bg-neutral h-auto w-1/4 rounded-lg p-4">
        <input type="text" placeholder="Name" v-model="name" class="input input-bordered w-full mb-2"/>
        <input type="text" placeholder="Description" v-model="description" class="input input-bordered w-full mb-2"/>
        <input type="text" placeholder="Short description" v-model="shortDescription" class="input input-bordered w-full mb-2"/>
        
        <div class="flex flex-col mb-2">
          <select id="type" v-model="selectedType" class="p-2 border border-gray-600 rounded-lg bg-gray-800 text-white focus:outline-none focus:border-green-300">
            <option disabled value="">Select a type</option>
            <option v-for="type in auctionTypes" :key="type" :value="type">
              {{ type }}
            </option>
          </select>
        </div>

        <div class="flex flex-col mb-2">
          <label for="imageFile" class="mb-2 text-gray-400">Upload Image</label>
          <input 
            type="file" 
            id="imageFile" 
            @change="handleFileChange" 
            accept="image/*"
            class="p-2 border border-gray-600 rounded-lg bg-gray-800 text-white focus:outline-none focus:border-green-300"
          />
        </div>

        <div class="flex flex-col mb-2">
          <label for="startDate" class="mb-2 text-gray-400">Start Date</label>
          <input type="datetime-local" id="startDate" v-model="startDate" required
                 class="p-2 border border-gray-600 rounded-lg bg-gray-800 text-white focus:outline-none focus:border-green-300" />
        </div>

        <div class="flex flex-col mb-2">
          <label for="endDate" class="mb-2 text-gray-400">End Date</label>
          <input type="datetime-local" id="endDate" v-model="endDate" required
                 class="p-2 border border-gray-600 rounded-lg bg-gray-800 text-white focus:outline-none focus:border-green-300" />
        </div>

        <div class="form-control">
          <label class="label cursor-pointer">
            <span class="label-text">Published</span>
            <input type="checkbox" class="toggle toggle-primary" v-model="published" />
          </label>
        </div>

        <button type="submit" class="btn btn-primary w-full">Create Auction</button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getAuctionTypes, saveAuction } from '~/data/AuctionApi';
import BackButton from "~/components/BackButton.vue";
import { navigateTo } from '#app';

const auctionTypes = ref([]);
const selectedType = ref("");
const name = ref('');
const description = ref('');
const shortDescription = ref('');
const startDate = ref('');
const endDate = ref('');
const published = ref(false);
const selectedFile = ref(null);

onMounted(async () => {
  try {
    auctionTypes.value = await getAuctionTypes();
  } catch (error) {
    console.error('Error fetching auction types:', error);
  }
});

const handleFileChange = (event) => {
  selectedFile.value = event.target.files[0];
};

const submitForm = async () => {
  try {
    if (!selectedFile.value) {
      alert('Please select an image');
      return;
    }

    const auctionData = {
      naam: name.value,
      type: selectedType.value,
      startDate: startDate.value,
      endDate: endDate.value,
      published: published.value,
      description: description.value,
      shortDescription: shortDescription.value
    };

    await saveAuction(auctionData, selectedFile.value);
    alert('Auction created successfully!');
    
    // Reset form
    name.value = '';
    description.value = '';
    shortDescription.value = '';
    selectedType.value = '';
    startDate.value = '';
    endDate.value = '';
    published.value = false;
    selectedFile.value = null;
    
    navigateTo('/admin');
  } catch (error) {
    console.error('Error creating auction:', error);
    alert('Error creating auction. Please try again.');
  }
};
</script>
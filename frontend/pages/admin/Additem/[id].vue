<template>
  <div class="flex justify-center">
    <section class="w-full max-w-5xl p-8">
      <form @submit.prevent="handleSaveJewel">
        <div class="grid grid-cols-2 gap-6">
          <div class="flex flex-col">
            <label for="name" class="mb-2 text-gray-400">Name</label>
            <input type="text" id="name" v-model="jewel.name" placeholder="Enter jewel name" required
                   class="p-2 border border-gray-600 rounded-lg bg-gray-800 text-white focus:outline-none focus:border-green-300" />
          </div>

          <div class="flex flex-col">
            <label for="title" class="mb-2 text-gray-400">Title</label>
            <input type="text" id="title" v-model="jewel.title" placeholder="Enter jewel title" required
                   class="p-2 border border-gray-600 rounded-lg bg-gray-800 text-white focus:outline-none focus:border-green-300" />
          </div>

          <div class="flex flex-col col-span-2">
            <label for="description" class="mb-2 text-gray-400">Description</label>
            <textarea id="description" v-model="jewel.description" placeholder="Enter description" required
                      class="p-2 border border-gray-600 rounded-lg bg-gray-800 text-white focus:outline-none focus:border-green-300"></textarea>
          </div>

          <div class="flex flex-col">
            <label for="startingPrice" class="mb-2 text-gray-400">Starting Price</label>
            <input type="number" id="startingPrice" v-model.number="jewel.startingPrice" placeholder="Enter starting price" required min="1"
                   class="p-2 border border-gray-600 rounded-lg bg-gray-800 text-white focus:outline-none focus:border-green-300" />
          </div>

          <div class="flex flex-col">
            <label for="color" class="mb-2 text-gray-400">Color</label>
            <select id="color" v-model="jewel.colorName" class="p-2 border border-gray-600 rounded-lg bg-gray-800 text-white focus:outline-none focus:border-green-300">
              <option disabled value="">Select Color</option>
              <option v-for="color in colors" :key="color.colorId" :value="color.name">
                {{ color.name }}
              </option>
            </select>
          </div>

          <div class="flex flex-col">
            <label for="karat" class="mb-2 text-gray-400">Karat</label>
            <input type="number" id="karat" v-model.number="jewel.karat" placeholder="Enter jewel karat" required
                   class="p-2 border border-gray-600 rounded-lg bg-gray-800 text-white focus:outline-none focus:border-green-300" />
          </div>

          <div class="flex flex-col">
            <label for="type" class="mb-2 text-gray-400">Type</label>
            <select id="type" v-model="jewel.typeName" class="p-2 border border-gray-600 rounded-lg bg-gray-800 text-white focus:outline-none focus:border-green-300">
              <option disabled value="">Select Type</option>
              <option v-for="type in types" :key="type.typeId" :value="type.name">
                {{ type.name }}
              </option>
            </select>
          </div>

          <div class="flex flex-col">
            <label for="endDate" class="mb-2 text-gray-400">End Date</label>
            <input type="datetime-local" id="endDate" v-model="jewel.endDate" required
                   class="p-2 border border-gray-600 rounded-lg bg-gray-800 text-white focus:outline-none focus:border-green-300" />
          </div>

          <div class="flex flex-col">
            <label for="auctionId" class="mb-2 text-gray-400">Auction ID</label>
            <input type="number" id="auctionId" v-model.number="jewel.auctionId" placeholder="Enter auction ID" required
                   class="p-2 border border-gray-600 rounded-lg bg-gray-800 text-white focus:outline-none focus:border-green-300" />
          </div>

          <div class="flex flex-col col-span-2">
            <label for="imageFile" class="mb-2 text-gray-400">Upload Images</label>
            <input type="file" id="imageFile" @change="onFileChange" accept="image/*" multiple
                   class="p-2 border border-gray-600 rounded-lg bg-gray-800 text-white focus:outline-none focus:border-green-300" />
          </div>

          <div v-if="isEditMode && jewel.imageNames && jewel.imageNames.length > 0" class="col-span-2">
            <p class="text-gray-400 mb-2">Current Images:</p>
            <div class="grid grid-cols-4 gap-4">
              <div v-for="(imageName, index) in jewel.imageNames" :key="index" class="relative">
                <img :src="`http://localhost:8080/api/jewels/image/${imageName}`" :alt="jewel.name"
                     class="w-full h-32 object-cover rounded-lg" />
              </div>
            </div>
          </div>

          <div class="flex items-center">
            <input type="checkbox" id="published" v-model="jewel.published"
                   class="mr-2 border border-gray-600 rounded bg-gray-800 text-green-300 focus:outline-none focus:border-green-300" />
            <label for="published" class="text-gray-400">Published</label>
          </div>
        </div>

        <button type="submit"
                class="w-full p-3 mt-6 bg-green-400 text-black rounded-lg hover:bg-green-700">
          {{ isEditMode ? 'Update' : 'Create' }} Jewel
        </button>
      </form>
    </section>
  </div>
</template>

<script>
import { fetchJewelColors, fetchJewelTypes } from '~/data/JewelDataApiFetcher.js';
import { fetchJewelById, saveJewel } from '~/data/JewelApi.js';

export default {
  data() {
    return {
      jewel: {
        name: '',
        title: '',
        description: '',
        startingPrice: null,
        colorName: '',
        karat: null,
        typeName: '',
        endDate: '',
        published: false,
        auctionId: null,
        imageNames: []
      },
      selectedFiles: [],
      colors: [],
      types: [],
      isEditMode: false
    }
  },
  async created() {
    try {
      this.colors = await fetchJewelColors();
      this.types = await fetchJewelTypes();

      if (this.$route.params.id) {
        this.isEditMode = true;
        await this.loadJewelData(this.$route.params.id);
      }
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  },
  methods: {
    async loadJewelData(id) {
      try {
        const data = await fetchJewelById(id);
        if (data) {
          this.jewel = {
            ...data,
            colorName: data.colorName,
            typeName: data.typeName,
            imageNames: data.imageNames || []
          };
        }
      } catch (error) {
        console.error('Failed to load jewel data:', error);
      }
    },
    onFileChange(event) {
      this.selectedFiles = Array.from(event.target.files);
    },
    async handleSaveJewel() {
      try {
        const formData = new FormData();
        formData.append('jewel', JSON.stringify(this.jewel));

        if (this.selectedFiles.length > 0) {
          this.selectedFiles.forEach(file => {
            formData.append('images', file);
          });
        }

        await saveJewel(formData);
        alert('Jewel saved successfully');
        navigateTo('/admin');
      } catch (error) {
        console.error(error);
        alert('Error saving jewel');
      }
    },
    resetForm() {
      this.jewel = {
        name: '',
        title: '',
        description: '',
        startingPrice: null,
        colorName: '',
        karat: null,
        typeName: '',
        endDate: '',
        published: false,
        auctionId: null,
        imageNames: []
      };
      this.selectedFiles = [];
    }
  }
}
</script>
<template>
  <div class="grid grid-rows-[0.2fr_1fr_1fr] grid-cols-2 h-[calc(100vh-70px)] p-4 gap-2">
    <div class="col-span-2 flex justify-between items-center mb-4">
      <div class="flex justify-between w-full items-center gap-4">
        <div class="flex justify-between w-1/2">
          <input
              type="text"
              class="input input-bordered w-1/3"
              placeholder="Search orders by name..."
              v-model="searchQuery"
          />
          <div class="col-span-2 flex items-center">
            <input type="checkbox" class="toggle toggle-primary" v-model="hideCompleted" />
            <span class="ml-2">Hide completed orders</span>
          </div>
          <div class="relative">
            <select v-model="selectedStatus" class="select select-bordered w-48">
              <option value="">All Statuses</option>
              <option value="completed">Completed</option>
              <option value="pending">Pending</option>
              <option value="waiting">Waiting</option>
            </select>
          </div>
        </div>
        <input
            type="text"
            class="input input-bordered w-1/3"
            placeholder="Search auctions by name..."
            v-model="auctionSearchQuery"
        />
      </div>
    </div>


    <AdminDashLists
        title="Active orders"
        :items="filteredOrders"
        buttonText="View order"
    >
      <template #item="{ item, buttonText }">
        <div class="flex items-center justify-between w-full">
          <p class="text-2xl font-bold">{{ item.jewelName }}</p>
          <div class="flex gap-6 items-center">
            <div class="badge badge-secondary">{{ item.status }}</div>
            <NuxtLink :to="`/orders/${item.orderId}`" class="btn btn-primary">
              {{ buttonText }}
            </NuxtLink>
          </div>
        </div>
      </template>
    </AdminDashLists>

    <AdminDashLists
        title="Auctions"
        :items="auctions"
        buttonText="View auction"
    >
      <template #item="{ item, buttonText }">
        <div class="flex items-center justify-between w-full">
          <p class="text-2xl font-bold">{{ item.name }}</p>
          <NuxtLink :to="`/auctions/${item.auctionId}/items`" class="btn btn-primary">
            {{ buttonText }}
          </NuxtLink>
        </div>
      </template>
    </AdminDashLists>

    <div class="bg-neutral col-span-2 rounded-2xl">
      <div class="overflow-y-auto h-full">
        <table class="table">
          <thead>
          <tr>
            <th>Name</th>
            <th>Karaat</th>
            <th>Color</th>
            <th>Type</th>
          </tr>
          </thead>
          <tbody>
          <AdminDashTableItem
              v-for="jewel in getAllJewels"
              :key="jewel.jewelId"
              :itemId="jewel.jewelId"
              :name="jewel.name"
              :karaat="jewel.karat"
              :color="jewel.colorName"
              :type="jewel.typeName"
              :imageSrc="jewel.imageUrl"
          />
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from "vue";
import { getAllAuctions } from '../../data/AuctionApi';
import { fetchJewelById, getAllJewels} from '../../data/JewelApi';
import {fetchJewelImage } from "~/data/JewelDataApiFetcher"
import { getOrders } from "../../data/OrderApi";

const activeOrders = ref([]);
const auctions = ref([]);
const unpublishedJewels = ref([]);

const hideCompleted = ref(false);

const searchQuery = ref("");
const selectedStatus = ref("");
const auctionSearchQuery = ref("");

const filteredOrders = computed(() => {
  return activeOrders.value.filter((order) => {
    const matchesName = order.jewelName.toLowerCase().includes(searchQuery.value.toLowerCase());
    const matchesStatus = selectedStatus.value === "" || order.status.toLowerCase() === selectedStatus.value.toLowerCase();
    const isNotCompleted = !hideCompleted.value || order.status.toLowerCase() !== 'completed';
    return matchesName && matchesStatus && isNotCompleted;
  });
});

onMounted(async () => {
  try {
    auctions.value = await getAllAuctions();
    const ordersData = await getOrders();
    const jewels = await getAllJewels();
    for (const jewel of jewels) {
      try {
        const imageUrl = await fetchJewelImage(jewel.jewelId);
        jewel.imageUrl = imageUrl;
      } catch (error) {
        console.error(`Error fetching image for jewel ${jewel.jewelId}:`, error);
        jewel.imageUrl = '/path/to/default/image.jpg';
      }
      unpublishedJewels.value.push(jewel);
    }

    for (const order of ordersData) {
      const jewel = await fetchJewelById(order.jewelId);
      activeOrders.value.push({
        orderId: order.orderId,
        jewelName: jewel.name,
        status: order.status
      });
    }
  } catch (error) {
    console.error('Error fetching data:', error);
  }
});
</script>
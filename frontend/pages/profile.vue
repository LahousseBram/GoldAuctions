<template>
  <div class="bg-gray-900 text-white min-h-screen flex flex-col">
    <header class="bg-gray-800 shadow-xl w-full py-8">
      <div class="container mx-auto text-center">
        <h1 class="text-4xl font-extrabold text-gray-100">
          Account Details
        </h1>
        <p class="text-gray-400 text-lg mt-2">Manage your personal information</p>
      </div>
    </header>

    <main class="container mx-auto py-12 px-4 flex flex-col items-center w-full max-w-2xl">
      <section class="bg-gray-800 rounded-2xl shadow-2xl p-8 w-full space-y-6 border border-gray-700">
        <h2 class="text-2xl font-bold text-gray-100 border-b border-gray-700 pb-4">
          Personal Information
        </h2>

        <div class="space-y-5">
          <div
            v-for="(value, key) in user"
            :key="key"
            class="flex items-center justify-between p-4 bg-gray-700 rounded-xl transition-all duration-300 hover:bg-gray-600"
          >
            <div class="flex items-center space-x-4 w-full">
              <svg
                v-if="['firstName', 'lastName'].includes(key)"
                xmlns="http://www.w3.org/2000/svg"
                class="h-6 w-6 text-blue-400"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z"
                />
              </svg>

              <svg
                v-else-if="['street', 'city', 'postalCode', 'country'].includes(key)"
                xmlns="http://www.w3.org/2000/svg"
                class="h-6 w-6 text-blue-400"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"
                />
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"
                />
              </svg>

              <div class="flex-grow">
                <p class="font-semibold text-gray-300">{{ labels[key] }}</p>
                <template v-if="!editing[key]">
                  <p class="text-lg">{{ value || 'N/A' }}</p>
                </template>
                <template v-else>
                  <input
                    :value="value"
                    @input="user[key] = $event.target.value"
                    class="w-full p-2 mt-1 bg-gray-600 border border-gray-500 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none"
                  />
                </template>
              </div>
            </div>
            <button
              @click="toggleEdit(key)"
              class="ml-4 text-blue-400 hover:text-blue-300 transition-colors duration-300"
            >
              <svg
                v-if="!editing[key]"
                xmlns="http://www.w3.org/2000/svg"
                class="h-5 w-5"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"
                />
              </svg>
              <span v-else class="text-sm font-semibold">Save</span>
            </button>
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script>
import { fetchUserByEmail, updateUser } from "../data/ApiFetcher";

export default {
  data() {
    return {
      editing: {
        firstName: false,
        lastName: false,
        street: false,
        city: false,
        postalCode: false,
        country: false,
        phone: false,
      },
      user: {},
      labels: {
        firstName: "First Name",
        lastName: "Last Name",
        street: "Street + Number",
        city: "City",
        postalCode: "Postal Code",
        country: "Country",
        phone: "Phone",
      },
    };
  },
  methods: {
    async fetchUserData(email) {
      try {
        const user = await fetchUserByEmail(email);
        this.user = user;
      } catch (error) {
        console.error("Failed to fetch user data:", error.message);
      }
    },
    toggleEdit(key) {
      if (this.editing[key]) {
        this.updateUserField(key);
      }
      this.editing[key] = !this.editing[key];
    },
    async updateUserField(key) {
      try {
        const updatedData = { [key]: this.user[key] };
        await updateUser(this.user.id, updatedData);
      } catch (error) {
        console.error("Failed to update user:", error.message);
      }
    },
  },
  mounted() {
    this.fetchUserData("john.doe@example.com"); // Replace with the user's actual email address
  },
};
</script>

<script setup>
const props = defineProps({
  label: String,
  inputText: String
});

const emit = defineEmits();

let userInput = '';

function confirm() {
  emit('update:userInput', userInput);
}
</script>

<template>
  <section class="form-section w-full max-w-5xl py-8 lg:pl-20 flex flex-col items-center">
    <form @submit.prevent="confirm" class="w-full space-y-6">
      <div class="form-group space-y-2 px-8">
        <label class="text-white" for="i">{{ label }}</label>
        <input
            type="text"
            id="i"
            v-model="userInput"
            :placeholder="inputText"
            required
            class="w-full p-2 bg-gray-800 text-white rounded-md border border-gray-500 placeholder-gray-400 focus:outline-none focus:border-green-300"
        />
      </div>
      <button type="submit" class="w-full p-3 bg-green-400 text-black rounded-md hover:bg-green-700">
        Send
      </button>
    </form>
  </section>
</template>

<template>
  <div class="navbar bg-neutral">
    <div class="navbar-start">
      <div class="dropdown">
        <div tabindex="0" role="button" class="btn btn-ghost lg:hidden dropdown-toggle">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h8m-8 6h16" />
          </svg>
        </div>
        <ul class="menu menu-sm dropdown-content bg-base-100 rounded-box z-[1] mt-3 w-52 p-2 shadow">
          <li><NuxtLink to="/">Home</NuxtLink></li>
          <li><NuxtLink to="/profile">Profile</NuxtLink></li>
        </ul>
      </div>
      <a class="btn btn-ghost text-xl">Q-Jewel</a>
    </div>
    <div class="navbar-center hidden lg:flex">
      <ul class="menu menu-horizontal px-1 flex gap-3">
        <li><NuxtLink to="/">Home</NuxtLink></li>
        <li><NuxtLink to="/profile">Profile</NuxtLink></li>
        <li><NuxtLink to="/admin">Admin</NuxtLink></li>
        <li><NuxtLink to="/admin/additem">Add Jewel</NuxtLink></li>
        <li><NuxtLink to="/admin/auctions">Add Auction</NuxtLink></li>
      </ul>
    </div>
    <div class="navbar-end flex gap-4">
      <a class="btn" href="/login">Login</a>
      <a class="btn" href="/SignUp">SignUp</a>
    </div>
  </div>
</template>

<script>
</script>
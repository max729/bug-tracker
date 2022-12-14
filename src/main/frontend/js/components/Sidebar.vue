<template>


  <div class="col-12 col-md-3 col-xl-2 px-md-2 px-0 bg-dark d-flex sticky-top">
    <div
      class="d-flex flex-md-column flex-row flex-grow-1 align-items-center align-items-md-start px-3 pt-2 text-white">
      <a href="/" class="d-flex align-items-center pb-md-3 mb-md-0 me-md-auto text-white text-decoration-none">
        <span class="fs-5">B<span class="d-none d-md-inline">ug</span>-T<span class="d-none d-md-inline">racker</span></span>
      </a>
      <ul class="nav nav-pills flex-md-column flex-row flex-nowrap flex-shrink-1 flex-md-grow-0 flex-grow-1 mb-md-auto mb-0 justify-content-center align-items-center align-items-md-start" id="menu">
        <li class="nav-item">
          <router-link to="/Home" class="nav-link px-md-0 px-2">
            <i class="fs-5 bi-house"></i><span class="ms-1 d-none d-md-inline">Home</span>
          </router-link>
        </li>
        <li>
          <router-link to="/tickets" class="nav-link px-md-0 px-2">
            <i class="fs-5 bi-speedometer2"></i><span class="ms-1 d-none d-md-inline">Tickets</span> 
          </router-link>
        </li>
        <li>
          <router-link to="/projekts" class="nav-link px-md-0 px-2">
            <i class="fs-5 bi-table"></i><span class="ms-1 d-none d-md-inline">Projects</span>
          </router-link>
        </li>
      </ul>
      <div class="dropdown py-md-4 mt-md-auto ms-auto ms-md-0 flex-shrink-1">
        <a href="#" class="d-flex align-items-center text-white text-decoration-none dropdown-toggle" id="dropdownUser1"
          data-bs-toggle="dropdown" aria-expanded="false">
          <img src="../../assets/vite.svg"  width="28" height="28" class="rounded-circle">
          <span class="d-none d-md-inline mx-1">{{user.firstName}}</span>
        </a>
        <ul class="dropdown-menu dropdown-menu-dark text-small shadow" aria-labelledby="dropdownUser1">
          <li><router-link to="/profile" class="dropdown-item">Profile</router-link></li>
          <li><router-link to="/profile" class="dropdown-item">Settings</router-link></li>
          <li>
            <hr class="dropdown-divider">
          </li>
          <li><router-link to="/login" @click="logout" class="dropdown-item" >Sign out</router-link></li>
        </ul>
      </div>
    </div>
  </div>

</template>


<script setup>

import axios from 'axios';
import {  computed } from 'vue';
import { useStore } from 'vuex';

const store = useStore();    
let auth = computed(()=> store.state.auth );
let user = computed(() => store.state.user);

const logout = async () => {
      axios.post('/appUsers/logout',{},{withCredentials:true});

      axios.defaults.headers.common['Authorization'] = '';

      await store.dispatch('setAuth',false);

}
    
</script>





<style scoped>
@media (min-width: 768px) {
    .h-md-100 {
        height: 100%;
    }
}

</style>
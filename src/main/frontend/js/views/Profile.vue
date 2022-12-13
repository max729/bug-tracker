<template>

<div class="card shadow my-3" >
  <div class="card-body">
    <h5 class="card-title">Profile</h5>
    <p class="card-text">{{"Firstname: " + user.firstName   }}</p>
    <p class="card-text">{{"Lastname: " + user.lastName   }}</p>
    <p class="card-text">{{"Email: " + user.email   }}</p>
    <p class="card-text">{{"Role: " + user.userRole   }}</p>
  </div>
  <div class="card-body">
    <button type="button" class="btn btn-primary">Edit</button>
  </div>


</div>



</template>




<script setup>
import { computed, onMounted, ref } from 'vue';
import axios from "axios";
import { useStore } from 'vuex';


const store = useStore();
let auth = computed(() => store.state.auth);
let user = computed(() => store.state.user);
    

</script>

<style >
    
</style>
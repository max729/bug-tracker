<template>






  <div class="card shadow my-3">
    <div class="card-header">
      <h5 class="card-title">Profile</h5>
    </div>




    <div class="card-body">

      <div class="row">
        <div class="col-12 my-3 col-sm-6">
          Firstname : <br>
          {{ user.firstName }}
        </div>
        <div class="col-12 my-3 col-sm-6">
          Lastname : <br>
          {{  user.firstName}}
        </div>
        <div class="col-12 my-3 col-sm-6">
          Email : <br>
          {{ user.email }}
        </div>
        <div class="col-12 my-3 col-sm-6">
          Role : <br>
          {{ user.userRole }}
        </div>
      </div>
    </div>

    <div class="card-footer">
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
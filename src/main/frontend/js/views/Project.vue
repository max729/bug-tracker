<template>

  <div v-if="apiData" class="card shadow my-3">

    <div class="card-header">
      <h5 class="card-title">Project Details</h5>
    </div>

    <div class="card-body">

      <div class="row">
        <div class="col-12 my-3 col-sm-6">
          ProjektName : <br>
          {{ apiData.projectName }}
        </div>
        <div class="col-12 my-3 col-sm-6">
          Created : <br>
          {{ apiData.dateCreated.substring(0, 10) }}
        </div>

        <div class="col-12 my-3">
          Description : <br>
          {{ apiData.projectDescription }}
        </div>
        <div class="col-12 my-3">
          Users : <br>
          <p v-for="id in  apiData.allUsers"> {{ id }},</p>
        </div>

      </div>



    </div>




    <div class="card-footer">
      <button type="button" class="btn btn-primary">Edit</button>
    </div>


  </div>
  <div v-else>
    cant get data
  </div>



</template>
    
    
    
<script setup>
import { computed, onMounted, ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import axios from "axios";
import { useStore } from 'vuex';
//import DemoGrid from './Grid.vue'

const store = useStore();

let apiData = ref(null);
const route = useRoute();
const id = route.params.id;


onMounted(async () => {

  try {
    const response = await axios.get("/projects/" + id);
    apiData.value = response.data;

  } catch (e) {
    console.log(e);
    //await store.dispatch('setAuth', false);
  }
});

</script>
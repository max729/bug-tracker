<template>

    <div v-if="apiData"  class="card shadow my-3" >
      <div class="card-body">
        <h5 class="card-title">Ticket Details</h5>
        <li >
          projektName : {{ apiData.projectName }}
        </li>
        <li >
          projektDescription : {{ apiData.projectDescription }}
        </li>
        <li >
          allUsers :  <p v-for="id in  apiData.allUsers" > {{id}}</p>
        </li>

       
      </div>
      <div class="card-body">
        <button type="button" class="btn btn-primary">Edit</button>
      </div>
    
    
    </div>
    <div v-else>
    cant get data
    </div>

    
    
    </template>
    
    
    
    <script setup>
    import { computed, onMounted, ref } from 'vue';
    import { useRouter , useRoute} from 'vue-router';
    import axios from "axios";
    import { useStore } from 'vuex';
    //import DemoGrid from './Grid.vue'
    
    const store = useStore();
    
    let apiData = ref(null);
    const route = useRoute(); 
    const id = route.params.id;
    
    
    onMounted(async () => {
    
    try {
        const response = await axios.get("/projects/" + id );  
        apiData.value = response.data;
    
    } catch (e) {
        console.log(e);
        //await store.dispatch('setAuth', false);
    }
    });
    
    </script>
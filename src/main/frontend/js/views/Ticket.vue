<template>

<div v-if="apiData.value" class="card shadow my-3" >
  <div class="card-body">
    <h5 class="card-title">Ticket Details</h5>
    <li v-for="(value, key) in apiData.value">
      {{ key }}: {{ value }}
    </li>

  </div>
  <div class="card-body">
    <button type="button" class="btn btn-primary">Edit</button>
  </div>


</div>
<div v-else>
  <p class="text-align-center my-3">Cant fetch Ticket Data</p>
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
    const response = await axios.get("/tickets/" + id );
    
    apiData.value = response.data;

} catch (e) {
    console.log(e);
    //await store.dispatch('setAuth', false);
}
});

</script>
<template>

<div v-if="apiData"  class="card shadow my-3" >
  <div class="card-body">
    <h5 class="card-title">Ticket Details</h5>
    <li  >
      Name : {{ apiData.name  }}
    </li>
    <li  >
      Status : {{ apiData.status  }}
    </li>
    <li  >
      Description : {{ apiData.description  }}
    </li>
    <li  >
      Priority : {{ apiData.priority  }}
    </li>
    <li  >
      Typ : {{ apiData.typ  }}
    </li>
    <li  >
      Author : {{ apiData.author  }}
    </li>
    <li  >
      Assigned : {{ apiData.assigned  }}
    </li>
    <li  >
      LastUpdated : {{ apiData.lastUpdated }}
    </li>
  </div>
  <div class="card-body">
    <button type="button" class="btn btn-primary">Edit</button>
  </div>


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
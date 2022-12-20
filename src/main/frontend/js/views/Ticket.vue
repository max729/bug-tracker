<template>

<div v-if="apiData"  class="card shadow my-3" >
  <div class="card-header">
    <h5 class="card-title">Ticket Details</h5>
  </div>
  
  <div class="card-body">
    <div class="row">
      <div class="col-12 my-3 col-sm-6">
        Name : <br>
        {{ apiData.name  }}
      </div>
      <div class="col-12 my-3 col-sm-6">
        Typ : <br>
        {{ apiData.typ  }}
      </div>
      <div class="col-12  my-3 col-sm-6">
        Author : <br>
        {{ apiData.author  }}
      </div>
      <div class="col-12 my-3 col-sm-6">
        Assigned : <br>
        {{ apiData.assigned  }}
      </div>
      <div class="col-12 my-3 col-sm-6">
        Status : <br>
        {{ apiData.status  }}
      </div>
      <div class="col-12 my-3 col-sm-6">
        Priority : <br>
        {{ apiData.priority  }}
      </div>
      <div class="col-12 my-3">
        Description : <br>
        {{ apiData.description  }}
      </div>

    </div>

  </div>
  <div class="card-footer">
    <button type="button" class="btn btn-primary">Edit</button>
  </div>
</div>


  <div class="card my-1 shadow" v-if="apiData">
    <div class="card-header">
      <h5 class="card-title">Comments</h5>
    </div>
    <div class="card-body" >
      <div class="row border rounded my-2" v-for="com in apiData.ticketComments">
        <div class="col-2">{{ com.userLink }}</div>
        <div class="col-10">{{ com.comment }}</div>
      </div>
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
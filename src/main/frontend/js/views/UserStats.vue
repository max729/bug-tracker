<template >
    <div class="card mt-3 text-center shadow">
      <div class="card-header ">
            <h2>{{ "Hi " + user.id + ", you are logged in as: " + user.userRole  }}</h2>
      </div>
        
    

      <div class="card-body">

      <div class="row">
        <div class="col-md-6 my-2">
          <div class="card shadow">
            <div class="card-header">
              Ticket Priority
            </div>
            <div class="card-body">
              <Doughnut v-if="TicketPriorityData.loaded" :data="TicketPriorityData" :options="options" />
            </div>
            
          </div>
          
        </div>
        <div class=" my-2 col-md-6">
          <div class="card shadow">
            <div class="card-header">
              Ticket Status
            </div>
            <div class="card-body">
              <Doughnut v-if="TicketStatusData.loaded" :data="TicketStatusData" :options="options" />
            </div>
          </div>
          
        </div>
        
      </div>
      
    
    </div>

</div>

    


</template>

<script setup>
import { computed, onMounted, ref , reactive } from 'vue';
import axios from "axios";
import { useStore } from 'vuex';

import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js'
import { Doughnut } from 'vue-chartjs'

const options = {
  responsive: true,
  maintainAspectRatio: false
}

const TicketStatusData = reactive({
  loaded: false,
  labels: null,//['OPEN', 'ASSIGNED', 'MORE INFOS', 'CLOSED'],

  datasets: [
    {
      backgroundColor: ['#41B883', '#00D8FF', '#DD1B16', '#E46651'],
      data: null
    }
  ]
});


const TicketPriorityData = reactive( {
  loaded: false,
  labels: null,//['LOW', 'MID', 'HIGH', 'NONE'],

  datasets: [
    {
      backgroundColor: ['#41B883', '#00D8FF', '#DD1B16', '#E46651'],
      data: null
    }
  ]
});



ChartJS.register(ArcElement, Tooltip, Legend)

const store = useStore();
let auth = computed(() => store.state.auth);
let user = computed(() => store.state.user);

let apiData = ref(null);

onMounted(async () => {

    try {
        const response = await axios.get("/appUsers/stats");

        apiData = response.data;

      TicketPriorityData.labels = Object.keys(apiData.totalTicktetsByPriority);
      TicketPriorityData.datasets[0].data =  Object.values(apiData.totalTicktetsByPriority );
      //console.log (  Object.values(apiData.totalTicktetsByPriority ) );
      TicketPriorityData.loaded = true;


      TicketStatusData.labels = Object.keys(apiData.totalTicktetsByStatus);
      TicketStatusData.datasets[0].data =  Object.values(apiData.totalTicktetsByStatus );
      //console.log (  Object.values(apiData.totalTicktetsByPriority ) );
      TicketStatusData.loaded = true;


    } catch (e) {
        console.log(e);

    }
});


    

</script>






<style >
    
</style>
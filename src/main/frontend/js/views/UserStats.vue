<template >
    <div class="container mt-5 text-center">
        <h1>{{ "Hi" + user.id + ", you are logged in as: " + user.userRole  }}</h1>
    
      <div class="row">
        <div class="col-6">
          <Doughnut :data="data" :options="options" />
        </div>
        <div class="col-6">
          <Doughnut :data="data" :options="options" />
        </div>
        
      </div>
      
    
    </div>



    


</template>

<script setup>
import { computed, onMounted, ref } from 'vue';
import axios from "axios";
import { useStore } from 'vuex';

import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js'
import { Doughnut } from 'vue-chartjs'

const options = {
  responsive: true,
  maintainAspectRatio: false
}

const data = {
  labels: ['VueJs', 'EmberJs', 'ReactJs', 'AngularJs'],
  datasets: [
    {
      backgroundColor: ['#41B883', '#E46651', '#00D8FF', '#DD1B16'],
      data: [0, 20, 80, 10]
    }
  ]
}



ChartJS.register(ArcElement, Tooltip, Legend)

const store = useStore();
let auth = computed(() => store.state.auth);
let user = computed(() => store.state.user);

let apiData = ref(null);

onMounted(async () => {

    try {
        const response = await axios.get("/appUsers/stats");

        apiData = response.data;


    } catch (e) {
        console.log(e);

    }
});


    

</script>






<style >
    
</style>
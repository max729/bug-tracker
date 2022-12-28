<template >
  <div class="card mt-3 text-center shadow">
    <div class="card-header ">
      <h2>{{ "Hi " + user.id + ", you are logged in as: " + user.userRole }}</h2>
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

    <div class="card-body">


      <div class="card shadow my-3">
        <div class="card-body">

          <div class="row">
            <h2 class="col-auto ">Assign Tickets</h2>
          </div>
          <form id="search">
            <div class="row">

              <label class="col-md-2 col-form-label">Search: </label>
              <div class="col-md-10">
                <input class="form-control form-control-dark rounded-3 border-1 " v-model="searchQuery">
              </div>



            </div>
          </form>


          <table class="table align-items-center mb-0" v-if="filteredData.length">
            <thead>
              <tr>
                <th @click="sortBy(gridColumns[0])" :class="{ 'text-secondary': sortKey !== gridColumns[0] }"
                  class="text-uppercase   text-xxs font-weight-bolder">
                  {{ gridColumns[0] }} <span class="arrow"
                    :class="sortOrders[gridColumns[0]] > 0 ? 'asc' : 'dsc'"></span>
                </th>
                <th @click="sortBy(gridColumns[1])" :class="{ 'text-secondary': sortKey !== gridColumns[1] }"
                  class="text-center text-uppercase  text-xxs font-weight-bolder">{{ gridColumns[1] }}
                  <span class="arrow" :class="sortOrders[gridColumns[1]] > 0 ? 'asc' : 'dsc'"></span>
                </th>
                <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder ">Status</th>
                <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder ">pRIORITY</th>
                <th class="text-secondary opacity-7"></th>
              </tr>
            </thead>


            <tbody>
              <tr v-for="entry in filteredData">
                <td>
                  <div class="d-flex px-2 py-1">
                    <div class="d-flex flex-column justify-content-center">
                      <h6 class="mb-0 text-sm">{{ entry.name }}</h6>
                    </div>
                  </div>
                </td>
                <td class="align-middle text-center text-sm ">
                  <span class="text-secondary text-xs font-weight-bold">{{ entry.typ }}</span>
                </td>
                <td class="align-middle text-center text-sm">
                  <span class="text-secondary text-xs font-weight-bold">{{ entry.status }}</span>
                </td>
                <td class="align-middle text-center text-sm">
                  <span class="p-1 text-white bg-opacity-75 rounded-1"
                    :class="{ 'bg-success': entry.priority == 'LOW', 'bg-warning': entry.priority == 'MID', 'bg-danger': entry.priority == 'HIGH' }">
                    {{ entry.priority }}</span>
                </td>
                <td class="align-middle">
                  <router-link :to="{ path: '/ticket/' + entry.id }"
                    class="text-secondary font-weight-bold text-xs">Details</router-link>
                </td>
              </tr>

            </tbody>
          </table>

          <p v-else class="mt-3">- No matches found.</p>



        </div>
      </div>
    </div>

  </div>

</template>

<script setup>
import { computed, onMounted, ref, reactive } from 'vue';
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


const TicketPriorityData = reactive({
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
let apiTicketData = ref(null);

onMounted(async () => {

  try {
    const response = await axios.get("/appUsers/stats");

    apiData = response.data;

    TicketPriorityData.labels = Object.keys(apiData.totalTicktetsByPriority);
    TicketPriorityData.datasets[0].data = Object.values(apiData.totalTicktetsByPriority);
    //console.log (  Object.values(apiData.totalTicktetsByPriority ) );
    TicketPriorityData.loaded = true;


    TicketStatusData.labels = Object.keys(apiData.totalTicktetsByStatus);
    TicketStatusData.datasets[0].data = Object.values(apiData.totalTicktetsByStatus);
    //console.log (  Object.values(apiData.totalTicktetsByPriority ) );
    TicketStatusData.loaded = true;

    apiTicketData.value = await (await axios.get("/tickets/assignToMe")).data;



  } catch (e) {
    console.log(e);

  }
});








const searchQuery = ref('')
const gridColumns = ['name', 'typ', 'status', 'priority']


const sortKey = ref('')
const sortOrders = ref(
  gridColumns.reduce((o, key) => ((o[key] = 1), o), {})
)

const filteredData = computed(() => {

  let data = apiTicketData.value ? apiTicketData.value : [];
  if (searchQuery) {
    let searchQuery1 = searchQuery.value.toLowerCase()
    data = data.filter((row) => {
      return Object.keys(row).some((key) => {
        return String(row[key]).toLowerCase().indexOf(searchQuery1) > -1
      })
    })
  }

  const key = sortKey.value
  // TODO implement sorting for pRIORITY LOW < MID
  if (key) {
    const order = sortOrders.value[key]
    data = data.slice().sort((a, b) => {
      a = a[key]
      b = b[key]
      return (a === b ? 0 : a > b ? 1 : -1) * order
    })
  }
  //console.log(data)
  return data
})

function sortBy(key) {
  sortKey.value = key
  sortOrders.value[key] *= -1
}

</script>






<style >

</style>
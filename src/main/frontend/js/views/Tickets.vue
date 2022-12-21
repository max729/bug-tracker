<template>

  <div class="card shadow my-3">
    <div class="card-body">

      <div class="row">
        <h2 class="col-auto ">Tickets</h2>

        <div class="col">
          <div class="row justify-content-end mx-2">
            <button class="btn btn-primary btn-sm col-auto " data-bs-toggle="modal" data-bs-target="#exampleModal">
              Add
            </button>
          </div>

        </div>

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
              {{ gridColumns[0] }} <span class="arrow" :class="sortOrders[gridColumns[0]] > 0 ? 'asc' : 'dsc'"></span>
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
                :class="{ 'bg-success': entry.priority == 'LOW', 'bg-warning': entry.priority == 'MID', 'bg-danger': entry.priority == 'HEIGH' }">
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


  <!-- Modal -->
  <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="exampleModalLabel">New Ticket</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <form>
          <div class="modal-body">
            <div class="row">
              <div class="col-12 my-3 col-sm-6">
                Name : <br>
                <input type="name" class="form-control" >
              </div>
              <div class="col-12 my-3 col-sm-6">
                Typ : <br>
                <select class="form-select" aria-label="Default select example">
                  <option >BUG</option>
                  <option >OTHER</option>
                  <option >REQUEST</option>
                </select>
              </div>
              <div class="col-12 my-3 col-sm-6">
                Status : <br>
                <select class="form-select" aria-label="Default select example">
                  <option >OPEN</option>
                  <option >ASSIGNED</option>
                  <option >MORE_INFOS</option>
                </select>
              </div>
              <div class="col-12 my-3 col-sm-6">
                Priority : <br>
                <select class="form-select" >
                  <option >LOW</option>
                  <option >MID</option>
                  <option >HIGH</option>
                  <option >NONE</option>
                </select>
              </div>


              <div class="col-12 my-3 col-sm-6">
                Assigned : <br>
                drg
              </div>

              <div class="col-12 my-3 col-sm-6">
                Project : <br>
                drg
              </div>

              <div class="col-12 my-3">
                Description : <br>
                <textarea class="form-control"></textarea>
              </div>
            </div>

          </div>
        </form>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
          <button type="button" class="btn btn-primary">Save changes</button>
        </div>
      </div>
    </div>
  </div>

</template>

   
    
    
<script setup>
import { computed, onMounted, ref } from 'vue';
import axios from "axios";
import { useStore } from 'vuex';
//import DemoGrid from './Grid.vue'

const store = useStore();
let auth = computed(() => store.state.auth);
let user = computed(() => store.state.user);

let apiData = ref(null);

onMounted(async () => {

try {
    const response = await axios.get("/tickets");

    apiData.value = response.data;

} catch (e) {
    console.log(e);
    //await store.dispatch('setAuth', false);
}
});




const searchQuery = ref('')
const gridColumns = ['name', 'typ', 'status', 'priority']


const sortKey = ref('')
const sortOrders = ref(
  gridColumns.reduce((o, key) => ((o[key] = 1), o), {})
)

const filteredData = computed(() => {
  
  let data = apiData.value  ? apiData.value : [];
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
    
<style scoped>

.arrow {
  display: inline-block;
  vertical-align: middle;
  width: 0;
  height: 0;
  margin-left: 5px;
  opacity: 0.66;
}

.arrow.asc {
  border-left: 4px solid transparent;
  border-right: 4px solid transparent;
  border-bottom: 4px solid rgb(0, 0, 0);
}

.arrow.dsc {
  border-left: 4px solid transparent;
  border-right: 4px solid transparent;
  border-top: 4px solid rgb(0, 0, 0);
}

</style>
<template>

  <div class="card shadow my-3">
    <div class="card-body">

      <div class="row">
        <h2 class="col-auto ">App User</h2>

        <div class="col">
          <div class="row justify-content-end mx-2">
            <button class="btn btn-primary btn-sm col-auto ">Add </button>
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
            <th @click="sortBy(id)" :class="{ 'text-secondary': sortKey !== gridColumns[0] }"
              class="text-uppercase   text-xxs font-weight-bolder">
              Username <span class="arrow" :class="sortOrders[id] > 0 ? 'asc' : 'dsc'"></span>
            </th>
            <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder ">Email</th>
            <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder ">Role</th>
            <th class="text-secondary opacity-7"></th>
          </tr>
        </thead>


        <tbody>
          <tr v-for="(entry, index) in filteredData">
            <td>
              <div class="d-flex px-2 py-1">
                <div class="d-flex flex-column justify-content-center">
                  <h6 class="mb-0 text-sm">{{ entry.id }}</h6>
                </div>
              </div>
            </td>
            <td class="align-middle text-center text-sm ">
              <span class="text-secondary text-xs font-weight-bold">{{ entry.email }}</span>
            </td>
            <td class="align-middle text-center text-sm">
              <span class="text-secondary text-xs font-weight-bold">{{ entry.userRole }}</span>
            </td>
            <td class="align-middle">
              <a href="#" v-on:click.prevent="setUpdateModal(entry)"
                class="text-secondary font-weight-bold text-xs">Edit</a>
            </td>
          </tr>

        </tbody>
      </table>

      <p v-else class="mt-3">- No matches found.</p>



    </div>
  </div>


  <div v-if="apiData" class="modal fade" id="exampleModal" tabindex="-1" aria-hidden="true">
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
                Fristname : <br>
                <input v-model="formData.firstName" type="text" class="form-control">
              </div>
              <div class="col-12 my-3 col-sm-6">
                Lastname : <br>
                <input v-model="formData.lastName" type="text" class="form-control">
              </div>
              <div class="col-12 my-3 col-sm-6">
                Email : <br>
                <input v-model="formData.email" type="text" class="form-control">
              </div>


              <div class="col-12 my-3 col-sm-6">
                Role : <br>
                <select v-model="formData.userRole" class="form-select">
                  <option>GUEST</option>
                  <option>DEV</option>
                  <option>MANAGER</option>
                  <option>ADMIN</option>
                </select>
              </div>

            </div>

          </div>
        </form>
        <div class="modal-footer">
          <button type="button" id="close" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
          <button @click="submit()" type="button" class="btn btn-primary">Save</button>
        </div>
      </div>
    </div>
  </div>

</template>
    
       
        
        
<script setup>
import { computed, onMounted, ref, reactive } from 'vue';
import axios from "axios";
import { useStore } from 'vuex';
import * as bootstrap from 'bootstrap';
//import DemoGrid from './Grid.vue'

const store = useStore();
let auth = computed(() => store.state.auth);
let user = computed(() => store.state.user);

let apiData = ref(null);

const formData = reactive({
  firstName: "",
  lastName: "",
  email: "",
  userRole: "GUEST",
  id: ""
});


onMounted(async () => {

  try {
    const response = await axios.get("/appUsers");

    apiData.value = response.data;

  } catch (e) {
    console.log(e);
    //await store.dispatch('setAuth', false);
  }
});




const submit = async () => {

try {

  console.log(formData)
  if (formData.firstName === "" || formData.lastName === "" || formData.email === "" || formData.userRole === "" ) {
    return;
  }

  const response = await axios.put("/appUsers/" + formData.id , formData,
    {
      'Content-Type': 'application/json',
      withCredentials: true
    }
  )

  if (response.status == 200) {

    const modal = bootstrap.Modal.getInstance(document.getElementById('exampleModal'));

    modal.hide();


    apiData.value = await (await axios.get("/appUsers")).data;


  }
} catch (e) { }

}



const searchQuery = ref('')
const gridColumns = ['id', 'email', 'userRole']


const sortKey = ref('')
const sortOrders = ref(
  gridColumns.reduce((o, key) => ((o[key] = 1), o), {})
)

const filteredData = computed(() => {

  let data = apiData.value ? apiData.value : [];
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

const setUpdateModal = (entry) => {

Object.assign(formData,{
  firstName: entry.firstName,
  lastName: entry.lastName,
  email: entry.email,
  userRole: entry.userRole,
  id: entry.id
})

const modal = bootstrap.Modal.getOrCreateInstance(document.getElementById('exampleModal'));

modal.show();

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
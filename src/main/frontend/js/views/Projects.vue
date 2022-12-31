<template>

  <div class="card shadow my-3">
    <div class="card-body">
      <div class="row">
        <h2 class="col-auto ">Projects</h2>

        <div class="col">
          <div class="row justify-content-end mx-2">
            <button class="btn btn-primary btn-sm col-auto " data-bs-toggle="modal" data-bs-target="#exampleModal">
              Add
            </button>
          </div>

        </div>
      </div>
      <form id="search">
        Search <input class="form-control form-control-dark rounded-3 border-1" v-model="searchQuery">
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
            <th class="text-secondary opacity-7"></th>
          </tr>
        </thead>


        <tbody>
          <tr v-for="entry in filteredData">
            <td>
              <div class="d-flex px-2 py-1">
                <div class="d-flex flex-column justify-content-center">
                  <h6 class="mb-0 text-sm">{{ entry.projectName }}</h6>
                </div>
              </div>
            </td>
            <td class="align-middle text-center text-sm ">
              <span class="text-secondary text-xs font-weight-bold">{{ entry.dateCreated.substring(0, 10) }}</span>
            </td>
            <td class="align-middle">
              <router-link :to="{ path: '/project/' + entry.id }"
                class="text-secondary font-weight-bold text-xs">Details</router-link>
            </td>
          </tr>

        </tbody>
      </table>

      <p v-else>No matches found.</p>



    </div>
  </div>


  <!-- Modal -->
  <div v-if="apiAllUserNames.length > 0" class="modal fade" id="exampleModal" tabindex="-1" aria-hidden="true">
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
                <input v-model="formData.projectName" type="text" class="form-control">
              </div>


              <div class="col-12 my-3">
                Description : <br>
                <textarea v-model="formData.projectDescription" class="form-control"></textarea>
              </div>

              <div class="col-12 my-3">
                Users : <br>
                <div class="row">
                  <div v-for="u in apiAllUserNames" class="col-auto m-1">
                    <input type="checkbox" :value="u" v-model="formData.allUsers">
                    <label class="mx-1"> {{ u }}</label>
                  </div>
                </div>

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

  <div class="modal" id="successModal" tabindex="-1">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-body bg-success">
          <h5 class="modal-title">Project created</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
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
let apiAllUserNames = ref([]);


const formData = reactive({
  projectName: "",
  projectDescription: "",
  allUsers: [],
});


const submit = async () => {

  try {

    //console.log(formData)


    if (formData.projectName === "" || formData.allUsers.length < 1) {
      return;
    }

    const response = await axios.post("/projects", formData,
      {
        'Content-Type': 'application/json',
        withCredentials: true
      }
    )



    if (response.status == 201) {

      //console.log(response)

      const modal = bootstrap.Modal.getOrCreateInstance(document.getElementById('exampleModal'));

      modal.hide();

      const succsessModal = bootstrap.Modal.getOrCreateInstance(document.getElementById('successModal'));

      succsessModal.show();

      formData.projectName = "";
      formData.projectDescription = "";



      apiData.value = await (await axios.get("/projects")).data;



    }
  } catch (e) { }

}



onMounted(async () => {

  try {
    const response = await axios.get("/projects");

    apiData.value = response.data;

    const userdata = await (await axios.get("/appUsers")).data;

    userdata.forEach(ele => apiAllUserNames.value.push(ele.id));

    //allUser.value =  .reduce( (userNames ,user) => [...userNames,user.id] , [] );

  } catch (e) {
    console.log(e.message);
    //await store.dispatch('setAuth', false);
  }
});


const searchQuery = ref('')
const gridColumns = ['projectName', 'dateCreated']


const sortKey = ref('')
const sortOrders = ref(
  gridColumns.reduce((o, key) => ((o[key] = 1), o), {})
)

const filteredData = computed(() => {

  //console.log(apiData.value);
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
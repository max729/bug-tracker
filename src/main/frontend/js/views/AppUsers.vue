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
                <th @click="sortBy(id)" :class="{   'text-secondary' : sortKey !== gridColumns[0]}"
                    class="text-uppercase   text-xxs font-weight-bolder">
                  Username <span class="arrow" :class="sortOrders[id] > 0 ? 'asc' : 'dsc'"></span>
                </th>
                <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder ">Email</th>
                <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder ">Role</th>
                <th class="text-secondary opacity-7"></th>
            </tr>
        </thead>
    
    
        <tbody>
            <tr v-for="entry in filteredData">
                <td>
                    <div class="d-flex px-2 py-1">
                        <div class="d-flex flex-column justify-content-center">
                            <h6 class="mb-0 text-sm">{{entry.id}}</h6>
                        </div>
                    </div>
                </td>
                <td class="align-middle text-center text-sm ">
                    <span class="text-secondary text-xs font-weight-bold">{{entry.email}}</span>
                </td>
                <td class="align-middle text-center text-sm">
                    <span class="text-secondary text-xs font-weight-bold">{{entry.userRole}}</span>
                </td>
<!--                 <td class="align-middle">
                  <router-link :to="{path: '/ticket/' + entry.id }" class="text-secondary font-weight-bold text-xs" >Details</router-link>
                </td> -->
            </tr>
           
        </tbody>
    </table>
    
    <p v-else class="mt-3" >- No matches found.</p>
    
    
    
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
        const response = await axios.get("/appUsers");
    
        apiData.value = response.data;
    
    } catch (e) {
        console.log(e);
        //await store.dispatch('setAuth', false);
    }
    });
    
    
    
    
    const searchQuery = ref('')
    const gridColumns = ['id', 'email', 'userRole']
    
    
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
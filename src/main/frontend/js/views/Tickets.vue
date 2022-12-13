<template>

<div class="card shadow my-3">
    <div class="card-body">
    <h2 class="">Tickets</h2>

  <form id="search">
    Search <input class="form-control form-control-dark rounded-0 border-1" v-model="searchQuery">
  </form>

<table class="table align-items-center mb-0">
    <thead>
        <tr>
            <th class="text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">NAME</th>
            <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">TYPE</th>
            <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">Status</th>
            <th class="text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">pRIORITY</th>
            <th class="text-secondary opacity-7"></th>
        </tr>
    </thead>
    <tbody>
        <tr v-for="entry in gridData">
            <td>
                <div class="d-flex px-2 py-1">
                    <div class="d-flex flex-column justify-content-center">
                        <h6 class="mb-0 text-sm">{{entry.name}}</h6>
                    </div>
                </div>
            </td>
            <td class="align-middle text-center text-sm ">
                <span class="text-secondary text-xs font-weight-bold">{{entry.typ}}</span>
            </td>
            <td class="align-middle text-center text-sm">
                <span class="text-secondary text-xs font-weight-bold">{{entry.status}}</span>
            </td>
            <td class="align-middle text-center text-sm">
                <span class="p-1 bg-success text-white bg-opacity-75 rounded-1">{{entry.priority}}</span>
                
            </td>

            
            <td class="align-middle"><a href="javascript:;" class="text-secondary font-weight-bold text-xs" data-toggle="tooltip" data-original-title="Edit user">Details</a></td>
        </tr>
       
    </tbody>
</table>




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






const searchQuery = ref('')
const gridColumns = ['name', 'typ', 'status', 'priority']
const gridData = [
  { name: 'Chuck Norris', typ: "BUG" , status: "OPEN" , priority: "LOW" },
  { name: 'Chuck Norris', typ: "BUG" , status: "OPEN" , priority: "LOW" }
]


/*
const filteredData = computed(() => {
  let { data, filterKey } = props
  if (filterKey) {
    filterKey = filterKey.toLowerCase()
    data = data.filter((row) => {
      return Object.keys(row).some((key) => {
        return String(row[key]).toLowerCase().indexOf(filterKey) > -1
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
  return data
})*/




</script>
    
<style >

</style>
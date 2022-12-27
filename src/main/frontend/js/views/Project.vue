<template>

  <div v-if="apiData" class="card shadow my-3">

    <div class="card-header">
      <h5 class="card-title">Project Details</h5>
    </div>

    <div class="card-body">

      <div class="row">
        <div class="col-12 my-3 col-sm-6">
          ProjektName : <br>
          {{ apiData.projectName }}
        </div>
        <div class="col-12 my-3 col-sm-6">
          Created : <br>
          {{ apiData.dateCreated.substring(0, 10) }}
        </div>

        <div class="col-12 my-3">
          Description : <br>
          {{ apiData.projectDescription }}
        </div>
        <div class="col-12 my-3">
          Users : <br>
          <div v-for="id in  apiData.allUsers" class="d-inline" > {{ id + ", "}}</div>
        </div>

      </div>



    </div>




    <div class="card-footer">
      <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#exampleModal" >Edit</button>
      <button type="button" class="btn btn-danger ms-2" data-bs-toggle="modal" data-bs-target="#deleteModal" >Delete</button>
    </div>


  </div>
  <div v-else>
    cant get data
  </div>



    <!-- Modal -->
    <div v-if="apiData " class="modal fade" id="exampleModal" tabindex="-1" aria-hidden="true">
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


  <div class="modal" tabindex="-1" id="deleteModal">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Delete Project</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <p>Are you sure ?</p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary" data-bs-dismiss="modal" @click="deleteProject()">Delete</button>
      </div>
    </div>
  </div>
</div>




</template>
    
    
    
<script setup>
import { computed, onMounted, ref , reactive} from 'vue';
import { useRouter, useRoute } from 'vue-router';
import axios from "axios";
import { useStore } from 'vuex';
import * as bootstrap from 'bootstrap';
//import DemoGrid from './Grid.vue'

const store = useStore();

let apiData = ref(null);
const route = useRoute();
const router = useRouter();
const id = route.params.id;

let apiAllUserNames = ref([]);


const formData = reactive({
  projectName: "",
  projectDescription: "",
  allUsers: [],
});





onMounted(async () => {

  try {
    const response = await axios.get("/projects/" + id);
    apiData.value = response.data;

    const userdata = await (await axios.get("/appUsers")).data;

    userdata.forEach(ele => apiAllUserNames.value.push(ele.id));

    Object.assign(formData,{
    projectName: apiData.value.projectName,
    projectDescription: apiData.value.projectDescription,
    allUsers: apiData.value.allUsers
  })




  } catch (e) {
    console.log(e);
    //await store.dispatch('setAuth', false);
  }
});

const deleteProject = async ()=>{

  try{

    const response = await axios.delete("/projects/" + id );

    if(response.status === 204){

      router.push("/projects")

    }
 
  } catch (e) {

    console.log( e.message );

  }
  
}

const submit = async () => {

try {

  console.log(formData)


  if (formData.projectName === "" || formData.allUsers.length < 1) {
    return;
  }

  const response = await axios.put("/projects/" + id , formData,
    {
      'Content-Type': 'application/json',
      withCredentials: true
    }
  )



  if (response.status == 200) {

    //console.log(response)

    const modal = bootstrap.Modal.getOrCreateInstance(document.getElementById('exampleModal'));

    modal.hide();

    const succsessModal = bootstrap.Modal.getOrCreateInstance(document.getElementById('successModal'));

    succsessModal.show();




    apiData.value = await (await axios.get("/projects/"+id)).data;

    Object.assign(formData,{
    projectName: apiData.value.projectName,
    projectDescription: apiData.value.projectDescription,
    allUsers: apiData.value.allUsers
  })



  }
} catch (e) {
    console.log(e.message)
  }

}

</script>
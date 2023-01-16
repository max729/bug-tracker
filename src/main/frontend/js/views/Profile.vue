<template>






  <div class="card shadow my-3">
    <div class="card-header">
      <h5 class="card-title">Profile</h5>
    </div>




    <div class="card-body">

      <div class="row">
        <div class="col-12 my-3 col-sm-6">
          Firstname : <br>
          {{ user.firstName }}
        </div>
        <div class="col-12 my-3 col-sm-6">
          Lastname : <br>
          {{  user.lastName}}
        </div>
        <div class="col-12 my-3 col-sm-6">
          Email : <br>
          {{ user.email }}
        </div>
        <div class="col-12 my-3 col-sm-6">
          Role : <br>
          {{ user.userRole }}
        </div>
      </div>
    </div>

    <div class="card-footer">
      <button type="button" @click="openUpdateModal" class="btn btn-primary">Edit</button>
    </div>

  </div>


  <div  class="modal fade" id="exampleModal" tabindex="-1" aria-hidden="true">
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
            </div>

          </div>
        </form>
        <div class="modal-footer">
          <button type="button" id="close" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
          <button @click="submit()" type="button" class="btn btn-primary" :class="{ disabled: user.userRole== 'GUEST' }">Save</button>
        </div>
      </div>
    </div>
  </div>


</template>




<script setup>
import { computed, onMounted, ref,reactive } from 'vue';
import axios from "axios";
import { useStore } from 'vuex';
import * as bootstrap from 'bootstrap';


const store = useStore();
let auth = computed(() => store.state.auth);
let user = computed(() => store.state.user);

const formData = reactive({});




const openUpdateModal = ()=>{
  Object.assign(formData,store.state.user)

  const modal = bootstrap.Modal.getOrCreateInstance(document.getElementById('exampleModal'));

  modal.show();
}

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


    await store.dispatch('setUser', formData);


  }
} catch (e) { }

}
    

</script>

<style >
    
</style>
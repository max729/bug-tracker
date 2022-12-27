<template>

  <div v-if="apiData" class="card shadow my-3">
    <div class="card-header">
      <h5 class="card-title">Ticket Details</h5>
    </div>

    <div class="card-body">
      <div class="row">
        <div class="col-12 my-3 col-sm-6">
          Name : <br>
          {{ apiData.name }}
        </div>
        <div class="col-12 my-3 col-sm-6">
          Typ : <br>
          {{ apiData.typ }}
        </div>
        <div class="col-12  my-3 col-sm-6">
          Author : <br>
          {{ apiData.author }}
        </div>
        <div class="col-12 my-3 col-sm-6">
          Assigned : <br>
          {{ apiData.assigned }}
        </div>
        <div class="col-12 my-3 col-sm-6">
          Status : <br>
          {{ apiData.status }}
        </div>
        <div class="col-12 my-3 col-sm-6">
          Priority : <br>
          {{ apiData.priority }}
        </div>
        <div class="col-12 my-3">
          Description : <br>
          {{ apiData.description }}
        </div>

      </div>

    </div>
    <div class="card-footer">
      <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#exampleModal">
        Edit</button>
      <button type="button" class="btn btn-danger ms-2" data-bs-toggle="modal" data-bs-target="#deleteModal">
        Delete</button>
    </div>
  </div>

  <!-- Comments -->
  <div class="card my-1 shadow" v-if="apiData">
    <div class="card-header">

      <div class="row">
        <h4 class="col-auto mb-0">Comments</h4>

        <div class="col">
          <div class="row justify-content-end mx-2">
            <button class="btn btn-primary btn-sm col-auto " data-bs-toggle="modal" data-bs-target="#addCommentModal">
              Add
            </button>
          </div>
        </div>
      </div>

    </div>

    <div class="card-body">
      <div class="card shadow-sm rounded my-2" v-for="com in apiData.ticketComments">
        <div class="card-header">

          <div class="row">
            <div class="col-auto mb-0 fw-bold">{{ com.userLink }} </div>

            <div v-if="com.userLink === store.state.user.id" class="col">
              <div class="row justify-content-end mx-2">
                <button @click="deleteComment(com.id)" type="button" class="btn-close" aria-label="Close"></button>
              </div>
            </div>
          </div>

        </div>
        <div class="card-body"> {{ com.comment }} </div>
      </div>

    </div>


  </div>



  <div v-if="apiProjectData" class="modal fade" id="exampleModal" tabindex="-1" aria-hidden="true">
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
                <input v-model="formData.name" type="text" class="form-control">
              </div>
              <div class="col-12 my-3 col-sm-6">
                Typ : <br>
                <select v-model="formData.typ" class="form-select">
                  <option>BUG</option>
                  <option>OTHER</option>
                  <option>REQUEST</option>
                </select>
              </div>
              <div class="col-12 my-3 col-sm-6">
                Status : <br>
                <select v-model="formData.status" class="form-select" aria-label="Default select example">
                  <option>OPEN</option>
                  <option>ASSIGNED</option>
                  <option>MORE_INFOS</option>
                </select>
              </div>
              <div class="col-12 my-3 col-sm-6">
                Priority : <br>
                <select v-model="formData.priority" class="form-select">
                  <option>LOW</option>
                  <option>MID</option>
                  <option>HIGH</option>
                  <option>NONE</option>
                </select>
              </div>

              <div class="col-12 my-3 col-sm-6">
                Assigned : <br>
                <select v-model="formData.assigned" class="form-select">
                  <option :value="null"></option>
                  <option v-for="projectUser in apiProjectData.allUsers">{{ projectUser }}</option>
                </select>
              </div>

              <div class="col-12 my-3">
                Description : <br>
                <textarea v-model="formData.description" class="form-control"></textarea>
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
          <h5 class="modal-title">Ticket updated</h5>
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
          <button type="button" @click="deleteTicket" class="btn btn-primary" data-bs-dismiss="modal">Delete</button>
        </div>
      </div>
    </div>
  </div>

  <div v-if="apiData" class="modal fade" id="addCommentModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">New Comment</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <form>
          <div class="modal-body">
            <div class="row">
              <div class="col-12 my-3">
                Comment Text : <br>
                <textarea v-model="commentFormData.comment" class="form-control"></textarea>
              </div>
            </div>

          </div>
        </form>
        <div class="modal-footer">
          <button type="button" id="close" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
          <button @click="addComment()" type="button" class="btn btn-primary" data-bs-dismiss="modal">Save</button>
        </div>
      </div>
    </div>
  </div>


</template>



<script setup>
import { computed, onMounted, ref, reactive } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import axios from "axios";
import { useStore } from 'vuex';
import * as bootstrap from 'bootstrap';
//import DemoGrid from './Grid.vue'

const store = useStore();


let apiData = ref(null);
let apiProjectData = ref(null);
const route = useRoute();
const router = useRouter();
const id = route.params.id;

const commentFormData = reactive({
  comment: "",
  userLink: store.state.user.id,
  ticket: id
})

const formData = reactive({
  name: "",
  status: "",
  description: "",
  priority: "",
  typ: "",
  assigned: null,
  author: null
});


const setEditFormData = (data) => {
  return {
    name: data.name,
    status: data.status,
    description: data.description,
    priority: data.priority,
    typ: data.typ,
    assigned: data.assigned,
    author: data.author,
    projektLink: data.projektLink
  }
}

onMounted(async () => {

  try {
    const response = await axios.get("/tickets/" + id);
    apiData.value = response.data;

    apiProjectData.value = (await axios.get("/projects/" + apiData.value.projektLink)).data;


    Object.assign(formData, setEditFormData(apiData.value));


  } catch (e) {
    console.log(e.message);
    //await store.dispatch('setAuth', false);
  }
});





const deleteTicket = async () => {

  try {

    const response = await axios.delete("/tickets/" + id);

    if (response.status === 204) {

      router.push("/tickets")

    }

  } catch (e) {

    console.log(e.message);

  }

}

const submit = async () => {

  try {

    if (formData.name === "") {
      return;
    }

    const response = await axios.put("/tickets/" + id, formData,
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




      apiData.value = await (await axios.get("/tickets/" + id)).data;

      //Object.assign(formData,setEditFormData(apiData.value)) 



    }
  } catch (e) {
    console.log(e.message)
  }

}

const addComment = async () => {

  try {

    if (commentFormData.comment === "" || commentFormData.userLink === "" || commentFormData.ticket === "") {
      return;
    }

    const response = await axios.post("/comments", commentFormData,
      {
        'Content-Type': 'application/json',
        withCredentials: true
      }
    )



    if (response.status == 201) {

      apiData.value = await (await axios.get("/tickets/" + id)).data;

      commentFormData.comment = "";

    }
  } catch (e) {
    console.log(e.message)
  }

}

const deleteComment = async (comId) => {
  try {

    const response = await axios.delete("/comments/" + comId);

    if (response.status == 204) {

      apiData.value = await (await axios.get("/tickets/" + id)).data;


    }

  } catch (e) {

    console.log(e.message);

  }

}

</script>
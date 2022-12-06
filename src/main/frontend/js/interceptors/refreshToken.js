import axios from "axios";


axios.defaults.baseURL = "http://localhost:8080/api";

let refresh = false;

axios.interceptors.response.use( resp => resp, async error => {

    if( error.response.status === 401 && !refresh){
        const response = await axios.post( "/refresh", {}, {withCredentials: true});
        refresh = true;
        if( response.status === 400 ){
            axios.defaults.headers.common['Authorization'] = "Bearer " + response.data.token;

            return axios(error.config);
        }

    }
    refresh = false;

    return error;

} );
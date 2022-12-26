import axios from "axios";

axios.defaults.baseURL = "http://localhost:8080/api";

let refresh = false;

axios.interceptors.response.use( resp => resp, async error => {
    // TODO : add access token (cookie) expired logout

    if( (error.response.status === 401 || error.response.status === 407  )&& !refresh){
        

        refresh=true;
        const response = await axios.post( "/appUsers/refresh", {}, {withCredentials: true});
        
        if( response.status === 200 ){
            axios.defaults.headers.common['Authorization'] = "Bearer " + response.data.token;

            error.config.headers.set('Authorization' , "Bearer " + response.data.token ) 
            //console.log(error.config.headers)

            refresh = false;

            return axios(error.config);
        }

    }
    
    refresh = false;

    return Promise.reject(error);

} );
import { createStore  } from "vuex";

export default createStore({

    state:{
        auth: false,
        user:{
            id: "",
            firstName: "",
            lastName: "",
            email: "",
            userRole: ""
        }
    },
    mutations: {
        setAuth(state ,auth){
            localStorage.setItem("auth", auth.toString() )
            state.auth=auth; 
        },
        setUser(state ,user){
            state.user=user; 
        }
    },
    actions:  {
        setAuth( context  , auth){
            ;
            context.commit( 'setAuth', auth );
        },
        setUser( context  , user){
            context.commit( 'setUser', user );
        }




    },
    modules : {}
})
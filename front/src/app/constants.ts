import {environment} from "../environments/environment"

//backend urls
export const API_URL = environment.serverUrl + "api"

export const API_PUBLIC_URL = API_URL + "/public"
export const EVENT_URL = API_PUBLIC_URL + "/event"





import axios from 'axios';
export const api=axios.create({baseURL:import.meta.env.VITE_API_BASE_URL||'/api/v1',timeout:10000,headers:{'Content-Type':'application/json'}});
let accessToken:string|undefined;
export function setAccessToken(token?:string){accessToken=token;}
api.interceptors.request.use(config=>{if(accessToken)config.headers.Authorization=`Bearer ${accessToken}`;return config;});

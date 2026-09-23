import axios from 'axios';
export const api=axios.create({baseURL:'/api/v1',timeout:5000,headers:{'Content-Type':'application/json'}});
let accessToken:string|undefined;
export function setAccessToken(token?:string){accessToken=token;}
api.interceptors.request.use(config=>{if(accessToken)config.headers.Authorization=`Bearer ${accessToken}`;return config;});

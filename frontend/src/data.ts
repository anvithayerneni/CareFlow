export type Role='PATIENT'|'DOCTOR'|'ADMIN';
export const doctors=[
 {id:'d1',name:'Dr. Maya Chen',specialty:'Cardiology',department:'Heart & Vascular',location:'North Campus',rating:'4.9',next:'Today, 2:30 PM',color:'rose',initials:'MC'},
 {id:'d2',name:'Dr. James Wilson',specialty:'Family Medicine',department:'Primary Care',location:'Downtown Clinic',rating:'4.8',next:'Tomorrow, 9:00 AM',color:'blue',initials:'JW'},
 {id:'d3',name:'Dr. Aisha Patel',specialty:'Dermatology',department:'Skin Health',location:'North Campus',rating:'5.0',next:'Thu, 11:15 AM',color:'lavender',initials:'AP'},
 {id:'d4',name:'Dr. Daniel Brooks',specialty:'Neurology',department:'Brain & Spine',location:'Westside Center',rating:'4.9',next:'Fri, 10:00 AM',color:'amber',initials:'DB'}
];
export const appointments=[
 {id:'CF-2048',doctor:'Dr. Maya Chen',specialty:'Cardiology',date:'Today',time:'2:30 PM',type:'In person',status:'Confirmed',color:'rose'},
 {id:'CF-2031',doctor:'Dr. James Wilson',specialty:'Annual wellness',date:'Next week',time:'9:00 AM',type:'Video visit',status:'Scheduled',color:'blue'},
 {id:'CF-1984',doctor:'Dr. Aisha Patel',specialty:'Dermatology',date:'Last week',time:'11:15 AM',type:'In person',status:'Completed',color:'lavender'}
];
export const nav={PATIENT:[['Overview','/'],['Appointments','/appointments'],['Find care','/doctors'],['Documents','/documents'],['Messages','/messages'],['My profile','/profile']],DOCTOR:[['Overview','/'],['My schedule','/appointments'],['Patients','/patients'],['Availability','/availability'],['Messages','/messages'],['My profile','/profile']],ADMIN:[['Overview','/'],['Appointments','/appointments'],['Care team','/doctors'],['Patients','/patients'],['Analytics','/analytics'],['Audit log','/audit']]};

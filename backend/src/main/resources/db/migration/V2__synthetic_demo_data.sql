-- Fictional demo identities. Shared local-only password: CareFlowDemo2024!
INSERT INTO departments(id,name,description) VALUES
 ('d0000000-0000-4000-8000-000000000001','Heart & Vascular','Synthetic cardiology department'),
 ('d0000000-0000-4000-8000-000000000002','Primary Care','Synthetic family medicine department'),
 ('d0000000-0000-4000-8000-000000000003','Skin Health','Synthetic dermatology department'),
 ('d0000000-0000-4000-8000-000000000004','Brain & Spine','Synthetic neurology department');
INSERT INTO users(id,email,password_hash,first_name,last_name,role) VALUES
 ('a0000000-0000-4000-8000-000000000001','sophie@example.test',crypt('CareFlowDemo2024!',gen_salt('bf',10)),'Sophie','Carter','PATIENT'),
 ('a0000000-0000-4000-8000-000000000002','maya@example.test',crypt('CareFlowDemo2024!',gen_salt('bf',10)),'Maya','Chen','DOCTOR'),
 ('a0000000-0000-4000-8000-000000000003','james@example.test',crypt('CareFlowDemo2024!',gen_salt('bf',10)),'James','Wilson','DOCTOR'),
 ('a0000000-0000-4000-8000-000000000004','aisha@example.test',crypt('CareFlowDemo2024!',gen_salt('bf',10)),'Aisha','Patel','DOCTOR'),
 ('a0000000-0000-4000-8000-000000000005','daniel@example.test',crypt('CareFlowDemo2024!',gen_salt('bf',10)),'Daniel','Brooks','DOCTOR'),
 ('a0000000-0000-4000-8000-000000000006','admin@example.test',crypt('CareFlowDemo2024!',gen_salt('bf',10)),'Alex','Morgan','ADMIN');
INSERT INTO patient_profiles(user_id,phone,date_of_birth,preferred_location) VALUES ('a0000000-0000-4000-8000-000000000001','555-014-2098','1988-04-12','North Campus');
INSERT INTO doctor_profiles(user_id,specialty,bio,location,rating) VALUES
 ('a0000000-0000-4000-8000-000000000002','Cardiology','Fictional clinician profile for the CareFlow demo.','North Campus',4.9),
 ('a0000000-0000-4000-8000-000000000003','Family Medicine','Fictional clinician profile for the CareFlow demo.','Downtown Clinic',4.8),
 ('a0000000-0000-4000-8000-000000000004','Dermatology','Fictional clinician profile for the CareFlow demo.','North Campus',5.0),
 ('a0000000-0000-4000-8000-000000000005','Neurology','Fictional clinician profile for the CareFlow demo.','Westside Center',4.9);
INSERT INTO doctor_departments(doctor_id,department_id) VALUES
 ('a0000000-0000-4000-8000-000000000002','d0000000-0000-4000-8000-000000000001'),
 ('a0000000-0000-4000-8000-000000000003','d0000000-0000-4000-8000-000000000002'),
 ('a0000000-0000-4000-8000-000000000004','d0000000-0000-4000-8000-000000000003'),
 ('a0000000-0000-4000-8000-000000000005','d0000000-0000-4000-8000-000000000004');
INSERT INTO doctor_availability(doctor_id,day_of_week,starts_at,ends_at,slot_minutes)
SELECT doctor_id::uuid,day_of_week,CASE WHEN day_of_week=2 THEN '10:00'::time ELSE '09:00'::time END,CASE WHEN day_of_week=2 THEN '15:00'::time ELSE '17:00'::time END,30
FROM unnest(ARRAY['a0000000-0000-4000-8000-000000000002','a0000000-0000-4000-8000-000000000003','a0000000-0000-4000-8000-000000000004','a0000000-0000-4000-8000-000000000005']) AS doctors(doctor_id) CROSS JOIN generate_series(1,5) AS days(day_of_week);
INSERT INTO appointments(id,patient_id,doctor_id,starts_at,ends_at,status,appointment_type,reason) VALUES
 ('b0000000-0000-4000-8000-000000000001','a0000000-0000-4000-8000-000000000001','a0000000-0000-4000-8000-000000000002',date_trunc('day',now())+interval '14 hours 30 minutes',date_trunc('day',now())+interval '15 hours 15 minutes','CONFIRMED','IN_PERSON','Synthetic routine follow-up'),
 ('b0000000-0000-4000-8000-000000000002','a0000000-0000-4000-8000-000000000001','a0000000-0000-4000-8000-000000000003',now()+interval '7 days',now()+interval '7 days 30 minutes','BOOKED','VIDEO','Synthetic wellness visit');
INSERT INTO notifications(user_id,type,title,body) VALUES ('a0000000-0000-4000-8000-000000000001','APPOINTMENT_CONFIRMED','Visit confirmed','Your fictional demo appointment with Dr. Maya Chen is confirmed.');
INSERT INTO audit_logs(user_id,action,entity_type,entity_id,metadata) VALUES ('a0000000-0000-4000-8000-000000000001','APPOINTMENT_CREATED','APPOINTMENT','b0000000-0000-4000-8000-000000000001','{"demo":true}'::jsonb);

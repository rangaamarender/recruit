insert into REF_DEPARTMENT  (dept_id, dept_name, dept_desc) values ('DVLP','Development','Development');
insert into REF_DEPARTMENT  (dept_id, dept_name, dept_desc) values ('QA','QA','Quality Assurance');
insert into REF_DEPARTMENT  (dept_id, dept_name, dept_desc) values ('HR','HRDept','Human Resource');


insert into REF_JOB (job_id, job_name, internal_job_ind, dept_id) values('DEVARCH','Architect','F','DVLP');
insert into REF_JOB (job_id, job_name, internal_job_ind, dept_id) values('SRDEV','Senior Developer','F','DVLP');
insert into REF_JOB (job_id, job_name, internal_job_ind, dept_id) values('JRDEV','Junior Developer','F','DVLP');
insert into REF_JOB (job_id, job_name, internal_job_ind, dept_id) values('QAARCH','Architect','F','QA');
insert into REF_JOB (job_id, job_name, internal_job_ind, dept_id) values('SRQA','Senior QA Analyst','F','QA');
insert into REF_JOB (job_id, job_name, internal_job_ind, dept_id) values('JRQA', 'Junior QA Analyst', 'F', 'QA');
insert into REF_JOB (job_id, job_name, internal_job_ind, dept_id) values('HRMGR','HR Manager', 'T', 'HR');
insert into REF_JOB (job_id, job_name, internal_job_ind, dept_id) values('HRREC','HR Recruiter', 'T', 'HR');

insert into REF_WORKERTYPE (worker_type_code, worker_type_name, worker_type_desc) values('W2','W2 Employee','W2 Employee');
insert into REF_WORKERTYPE (worker_type_code, worker_type_name, worker_type_desc) values('C2C','C2C Employee','Corp to Corp');
insert into REF_WORKERTYPE (worker_type_code, worker_type_name, worker_type_desc) values('1099','1099 Employee','Contractor');
insert into worker_code_seq(tenant_id,seq_no,prefix,include_year) values ('123',1,'TNT1',true)


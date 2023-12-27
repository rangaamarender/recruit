--Reference Data for Organization Inactive Status code
insert into ref_org_inactive_status_codes (status_code, display_name,reinstate) values ('ONHOLD','OnHold',true);
insert into ref_org_inactive_status_codes (status_code, display_name,reinstate) values ('SUSPENDED','Suspended',true);
insert into ref_org_inactive_status_codes (status_code, display_name,reinstate) values ('TERMINATED','Terminated',true);
insert into ref_org_inactive_status_codes (status_code, display_name,reinstate) values ('BLOCKED','Blocked',false);
insert into ref_org_inactive_status_codes (status_code, display_name,reinstate) values ('DONOTDOBUSINESS','DonotDoBusniness',false);

----Reference Data for Countries
insert into ref_country (country_code,alpha3_code,country_name,numeric_code,taxid_dspname)
values('IN','IND','India','356','GST');
insert into ref_country (country_code,alpha3_code,country_name,numeric_code,taxid_dspname)
values('US','USA','United Stats of America','840','EIN');
insert into ref_country (country_code,alpha3_code,country_name,numeric_code,taxid_dspname)
values('GB','GBR','United Kingdom of Great Britain and Northern Ireland','826','VAT');

--Reference Data for tax calssification
insert into ref_taxclassfication(country_code,taxclass_code ,taxclass_name,
stateinc_boo,description) values('US','CCORP','CCORP',true,'C Corp');
insert into ref_taxclassfication(country_code,taxclass_code ,taxclass_name,
stateinc_boo,description) values('US','SCORP','SCORP',true,'S Corp');
insert into ref_taxclassfication(country_code,taxclass_code ,taxclass_name,
stateinc_boo,description) values('IN','HSN','HSN',false,'HSN');
insert into ref_taxclassfication(country_code,taxclass_code ,taxclass_name,
stateinc_boo,description) values('GB','CBR','CBR',false,'CBR');

--Reference Data for workerType
insert into REF_WORKERTYPE (worker_type_code, worker_type_name, worker_type_desc) values('WORKER_W2','W2 Employee','W2 Employee');
insert into REF_WORKERTYPE (worker_type_code, worker_type_name, worker_type_desc) values('WORKER_C2C','C2C Employee','Corp to Corp');
insert into REF_WORKERTYPE (worker_type_code, worker_type_name, worker_type_desc) values('WORKER_1099','1099 Employee','Contractor');

--Reference org address type
insert into ref_org_addrs_type (address_type, displayname) values ('BILLING','Billing');
insert into ref_org_addrs_type (address_type, displayname) values ('MAILING','Mailing');
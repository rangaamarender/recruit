package com.lucid.recruit.contract.service;

import com.lucid.recruit.contract.entity.Contract;
import com.lucid.recruit.contract.entity.ContractWorkOrder;
import com.lucid.recruit.worker.entity.Worker;
import org.apache.commons.lang3.StringUtils;
import org.postgresql.shaded.com.ongres.saslprep.SaslPrep;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ContractSpecification {

    private Specification<Contract> byName(String contractName) {
        return (root, query, cb) -> cb.like(root.get("contractName"),
                "%"+contractName+"%");
    }

    private Specification<Contract> byClient(String organizationID) {
        return (root, query, cb) -> cb.equal(root.join("relatedOrg").get("organizationID"),
                organizationID);
    }


    public Specification<Contract> conditionalSearchForContractMSA(String contractName,String organizationID){
        Specification<Contract> specification = null;
        if(!StringUtils.isAllBlank(contractName)){
            specification = Specification.where(byName(contractName));
        }
        if(!StringUtils.isAllBlank(organizationID)){
            if(specification == null){
                specification = Specification.where(byClient(organizationID));
            }
            else {
                specification.and(Specification.where(byClient(organizationID)));
            }
        }
        return specification;
    }

   //   specification for contractWorkOrder
    private Specification<ContractWorkOrder> byWbsCode(String wbsCode){
        return (root, query, cb) ->cb.like(root.get("wbsCode"),"%"+wbsCode+"%");
    }

    private Specification<ContractWorkOrder> byClientOrEndClient(String organizationID){
        return (root, query, cb) -> cb.equal(root.join("client").join("client").get("organizationID"),organizationID);
    }

    public Specification<ContractWorkOrder> conditionalSearchForContractWO(String wbsCode,String organizationID){
        Specification<ContractWorkOrder> specification = null;
        if(!StringUtils.isAllBlank(wbsCode)){
            specification = Specification.where(byWbsCode(wbsCode));
        }
        if(!StringUtils.isAllBlank(organizationID)){
            if(specification == null){
                specification = Specification.where(byClientOrEndClient(organizationID));
            }
            else {
                specification.and(Specification.where(byClientOrEndClient(organizationID)));
            }
        }
        return specification;
    }

}

package com.lucid.recruit.worker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkerSequenceService {
    private final String seqName="worker_seq";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private boolean isSeqExists(){
        String query = "SELECT EXISTS(SELECT 1 FROM pg_sequences WHERE schemaname = 'public' AND sequencename='"+seqName+"')";
        return jdbcTemplate.queryForObject(query, Boolean.class);
    }

    @Transactional
    public Long getSequence(){
        if(!isSeqExists()){
            String query ="CREATE SEQUENCE "+seqName+" START 1";
            jdbcTemplate.execute(query);
        }
        String query = "SELECT nextval('"+seqName+"')";
        return jdbcTemplate.queryForObject(query,Long.class);
    }

}

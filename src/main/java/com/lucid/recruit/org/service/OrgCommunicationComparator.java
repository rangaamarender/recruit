package com.lucid.recruit.org.service;

import com.lucid.recruit.org.entity.OrgCommunication;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class OrgCommunicationComparator {
    public static Comparator<OrgCommunication> createOrgCommunicationDetailsComparator() {
        return Comparator.comparing(OrgCommunication::getStartDate);
    }
}

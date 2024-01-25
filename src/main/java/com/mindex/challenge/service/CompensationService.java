package com.mindex.challenge.service;


import com.mindex.challenge.data.Compensation;

import java.util.Optional;

public interface CompensationService {

    Optional<Compensation> createCompensation(Compensation compensation);
    Optional<Compensation> readCompensation(String id);
}

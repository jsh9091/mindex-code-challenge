package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;

/**
 * Service interface for employee compensation.
 * @author jhorvath
 */
public interface CompensationService {
	Compensation create(Compensation employee);
	Compensation read(String id);
}

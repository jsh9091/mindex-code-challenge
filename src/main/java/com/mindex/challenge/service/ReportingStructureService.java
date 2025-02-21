package com.mindex.challenge.service;

import com.mindex.challenge.data.ReportingStructure;

/**
 * Service interface for employee reporting structure.
 * @author jhorvath
 */
public interface ReportingStructureService {
	ReportingStructure numberOfReports(String id);
}

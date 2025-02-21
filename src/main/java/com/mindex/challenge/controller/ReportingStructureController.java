package com.mindex.challenge.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;

/**
 * Controller for reporting structure. 
 * @author jhorvath
 */
@RestController
public class ReportingStructureController {
	private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureController.class);
	
	@Autowired
    private ReportingStructureService reportingStructureService;
	
    @GetMapping("/totalreports/{id}")
    public ReportingStructure numberOfReports(@PathVariable String id) {
        LOG.debug("Received employee total reports request for id [{}]", id);

        return reportingStructureService.numberOfReports(id);
    }

}

package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitWebConfig(locations = {"classpath:spring/mvc-test-config.xml", "classpath:spring/mvc-core-config.xml"})
class OwnerControllerTest {


    @Autowired
    OwnerController ownerController;

    @Autowired
    ClinicService clinicService;  //this is mock created in configuration xml mvc-core-config.xml
                                    // but as it is created outside it must be injected -> @Autowired

    @Test
    void tempTest() {

        //ownerController
     }
}
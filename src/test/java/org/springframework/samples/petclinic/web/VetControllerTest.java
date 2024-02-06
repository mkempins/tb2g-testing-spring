package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.service.ClinicService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VetControllerTest {

    @Mock
    ClinicService clinicService;

    @InjectMocks
    VetController controller;

    List<Vet> vetList;

    @BeforeEach
    void setUp() {
        vetList = new ArrayList<>();
        vetList.add(new Vet());
        when(clinicService.findVets()).thenReturn(vetList);

    }

    @Test
    void showVetListTest() {
        Map<String, Object> model = new HashMap<>();
        String viewName = controller.showVetList(model);

        assertEquals("vets/vetList", viewName);
        assertEquals(1, model.size());
        assertEquals(true, model.containsKey("vets"));
        verify(clinicService,times(1)).findVets();
    }

    @Test
    void showResourcesVetListTest() {
        Vets vets = controller.showResourcesVetList();

        assertNotNull(vets);
        assertEquals(vetList.size(),vets.getVetList().size());
        verify(clinicService,times(1)).findVets();
    }
}
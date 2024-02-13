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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class VetControllerTestMy {

    @Mock
    ClinicService clinicService;

    @InjectMocks
    VetController controller;

    List<Vet> vetList;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        vetList = new ArrayList<>();
        vetList.add(new Vet());
        when(clinicService.findVets()).thenReturn(vetList);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
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

    @Test
    void mockMVCControllerTEst() throws Exception {

        mockMvc.perform(get("/vets.html"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("vets"))
                .andExpect(view().name("vets/vetList"));
    }

}
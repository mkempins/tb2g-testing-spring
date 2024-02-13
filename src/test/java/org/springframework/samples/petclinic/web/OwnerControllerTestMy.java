package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringJUnitWebConfig(locations = {"classpath:spring/mvc-test-config.xml", "classpath:spring/mvc-core-config.xml"})
class OwnerControllerTestMy {


    @Autowired
    OwnerController ownerController;

    @Autowired
    ClinicService clinicService;  //this is mock created in configuration xml mvc-core-config.xml
                                    // but as it is created outside it must be injected -> @Autowired
    MockMvc mockMvc;

    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ownerController).build();
    }

    @AfterEach
    void tearDown() {
        reset(clinicService);
    }

    @Test
    void updateOwnerIsValidTest() throws Exception {
        mockMvc.perform(post("/owners/{ownerId}/edit", 22)
                .param("firstName", "Alek")
                .param("lastName", "Balek")
                .param("address", "Balonowa 2")
                .param("city","Miastko")
                .param("telephone", "1234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/{ownerId}"))
                .andExpect(redirectedUrl("/owners/22"));
    }

    @Test
    void updateOwnerIsNotValidTest() throws Exception {
        mockMvc.perform(post("/owners/{ownerId}/edit", 22)
                .param("firstName", "Alek")
                .param("lastName", "Balek")
                .param("city","Miastko"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("owner"))
                .andExpect(model().attributeHasFieldErrors("owner", "address"))
                .andExpect(model().attributeHasFieldErrors("owner", "telephone"))
                .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }

    @Test
    void newOwnerIsValidTest() throws Exception {
        mockMvc.perform(post("/owners/new")
                    .param("firstName", "Alek")
                    .param("lastName", "Balek")
                    .param("address", "Balonowa 2")
                    .param("city","Miastko")
                    .param("telephone", "1234"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void newOwnerIsNotValidTest() throws Exception {
        mockMvc.perform(post("/owners/new")
                    .param("firstName", "Alek")
                    .param("lastName", "Balek")
                    .param("city","Miastko"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasErrors("owner"))
                .andExpect(model().attributeHasFieldErrors("owner", "address"))
                .andExpect(model().attributeHasFieldErrors("owner", "telephone"))
                .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }

    @Test
    @DisplayName("Testing owner not found")
    void processFindFormTestNotFound() throws Exception {
        Collection<Owner> owners = new ArrayList<>();
        when(clinicService.findOwnerByLastName(anyString())).thenReturn(owners);

        mockMvc.perform(get("/owners")
                            .param("lastName","Do not find me"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/findOwners"));
    }

    @Test
    @DisplayName("Testing found many owners")
    void processFindFormTestFoundMany() throws Exception {

        Collection<Owner> owners = new ArrayList<>();
        Owner owner = new Owner();
        owner.setId(13);
        owners.add(owner);
        owners.add(new Owner());

        when(clinicService.findOwnerByLastName(anyString())).thenReturn(owners);

        mockMvc.perform(get("/owners")
                .param("lastName", "ManyOwners"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("selections"))
                .andExpect(view().name("owners/ownersList"));
    }

    @Test
    @DisplayName("Testing found one owner")
    void processFindFormTestFoundOne() throws Exception {

        Collection<Owner> owners = new ArrayList<>();
        Owner owner = new Owner();
        owner.setId(13);
        owners.add(owner);
        when(clinicService.findOwnerByLastName(anyString())).thenReturn(owners);

        mockMvc.perform(get("/owners"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/13"));

        verify(clinicService).findOwnerByLastName(stringArgumentCaptor.capture());
        assertThat(stringArgumentCaptor.getValue().equalsIgnoreCase(""));
    }


    @Test
    void initCreationFormTest() throws Exception{
        mockMvc.perform(get("/owners/new"))
                .andExpect(status().isOk())
                .andExpect((model().attributeExists("owner")))
                .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }
}
package org.springframework.samples.petclinic.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.repository.PetRepository;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClinicServiceImplTestMy {

    @Mock
    PetRepository petRepository;

    @InjectMocks
    ClinicServiceImpl service;

    @Test
    void findPetTypesTest() {
        List<PetType> petTypes = new ArrayList<>();

        when(petRepository.findPetTypes()).thenReturn(petTypes);
        Collection<PetType> returnedPetTypes = service.findPetTypes();

        verify(petRepository,times(1)).findPetTypes();
        assertThat(returnedPetTypes ).isNotNull();

    }
}
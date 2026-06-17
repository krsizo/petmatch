package com.petmatch.petmatchapi.controller;

import com.petmatch.petmatchapi.config.JwtAuthFilter;
import com.petmatch.petmatchapi.dto.pet.PetResponse;
import com.petmatch.petmatchapi.mapper.PetMapper;
import com.petmatch.petmatchapi.model.*;
import com.petmatch.petmatchapi.service.PetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PetController.class)
@AutoConfigureMockMvc(addFilters = false)
class PetControllerApiTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private PetService petService;

	@MockitoBean
	private PetMapper petMapper;

	@MockitoBean
	private JwtAuthFilter jwtAuthFilter;

	@Test
	void shouldReturnPaginatedPets() throws Exception {
		Pet pet = Pet.builder()
			.id(1L)
			.name("Buddy")
			.age(3)
			.breed("Labrador Retriever")
			.gender(Gender.MALE)
			.size(Size.MEDIUM)
			.status(PetStatus.AVAILABLE)
			.petType(PetType.DOG)
			.location("Tallinn")
			.adoptionFee(BigDecimal.valueOf(50))
			.build();

		PetResponse response = PetResponse.builder()
			.id(1L)
			.name("Buddy")
			.age(3)
			.breed("Labrador Retriever")
			.gender(Gender.MALE)
			.size(Size.MEDIUM)
			.status(PetStatus.AVAILABLE)
			.petType(PetType.DOG)
			.location("Tallinn")
			.adoptionFee(BigDecimal.valueOf(50))
			.build();

		when(petService.getAllPets(any()))
			.thenReturn(new PageImpl<>(List.of(pet), PageRequest.of(0, 20), 1));

		when(petMapper.toResponse(pet))
			.thenReturn(response);

		mockMvc.perform(get("/api/pets")
				.param("page", "0")
				.param("size", "20"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content[0].id").value(1))
			.andExpect(jsonPath("$.content[0].name").value("Buddy"))
			.andExpect(jsonPath("$.content[0].petType").value("DOG"))
			.andExpect(jsonPath("$.page").value(0))
			.andExpect(jsonPath("$.size").value(20))
			.andExpect(jsonPath("$.totalElements").value(1))
			.andExpect(jsonPath("$.totalPages").value(1))
			.andExpect(jsonPath("$.last").value(true));
	}

	@Test
	void shouldReturnPetById() throws Exception {
		Pet pet = Pet.builder()
			.id(1L)
			.name("Buddy")
			.age(3)
			.breed("Labrador Retriever")
			.gender(Gender.MALE)
			.size(Size.MEDIUM)
			.status(PetStatus.AVAILABLE)
			.petType(PetType.DOG)
			.location("Tallinn")
			.adoptionFee(BigDecimal.valueOf(50))
			.build();

		PetResponse response = PetResponse.builder()
			.id(1L)
			.name("Buddy")
			.age(3)
			.breed("Labrador Retriever")
			.gender(Gender.MALE)
			.size(Size.MEDIUM)
			.status(PetStatus.AVAILABLE)
			.petType(PetType.DOG)
			.location("Tallinn")
			.adoptionFee(BigDecimal.valueOf(50))
			.build();

		when(petService.getPet(1L)).thenReturn(pet);
		when(petMapper.toResponse(pet)).thenReturn(response);

		mockMvc.perform(get("/api/pets/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(1))
			.andExpect(jsonPath("$.name").value("Buddy"))
			.andExpect(jsonPath("$.petType").value("DOG"));
	}
}

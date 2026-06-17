package com.petmatch.petmatchapi.service;

import com.petmatch.petmatchapi.dto.match.PetMatchResponse;
import com.petmatch.petmatchapi.model.*;
import com.petmatch.petmatchapi.repository.PetRepository;
import com.petmatch.petmatchapi.repository.UserPreferenceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

	@Mock
	private UserService userService;

	@Mock
	private UserPreferenceRepository userPreferenceRepository;

	@Mock
	private PetRepository petRepository;

	@Mock
	private PetScoringService petScoringService;

	@InjectMocks
	private MatchService matchService;

	@Test
	void shouldReturnSortedMatches() {

		User user = User.builder()
			.id(1L)
			.email("test@test.com")
			.role(Role.USER)
			.build();

		UserPreference preference = UserPreference.builder()
			.preferredPetType(PetType.DOG)
			.build();

		Pet pet1 = Pet.builder()
			.id(1L)
			.name("Buddy")
			.petType(PetType.DOG)
			.build();

		Pet pet2 = Pet.builder()
			.id(2L)
			.name("Charlie")
			.petType(PetType.DOG)
			.build();

		PetMatchResponse lowScoreMatch = PetMatchResponse.builder()
			.petId(1L)
			.petName("Buddy")
			.score(50)
			.build();

		PetMatchResponse highScoreMatch = PetMatchResponse.builder()
			.petId(2L)
			.petName("Charlie")
			.score(90)
			.build();

		when(userService.getCurrentUser())
			.thenReturn(user);

		when(userPreferenceRepository.findByUser(user))
			.thenReturn(Optional.of(preference));

		when(petRepository.searchPets(
			eq(PetStatus.AVAILABLE),
			eq(PetType.DOG),
			isNull(),
			isNull(),
			isNull(),
			isNull(),
			isNull(),
			isNull(),
			isNull()))
			.thenReturn(List.of(pet1, pet2));

		when(petScoringService.calculateMatch(pet1, preference))
			.thenReturn(lowScoreMatch);

		when(petScoringService.calculateMatch(pet2, preference))
			.thenReturn(highScoreMatch);

		List<PetMatchResponse> result =
			matchService.getMatchesForCurrentUser(10);

		assertThat(result).hasSize(2);

		assertThat(result.get(0).getPetId())
			.isEqualTo(2L);

		assertThat(result.get(1).getPetId())
			.isEqualTo(1L);

		verify(userService).getCurrentUser();

		verify(userPreferenceRepository)
			.findByUser(user);

		verify(petScoringService)
			.calculateMatch(pet1, preference);

		verify(petScoringService)
			.calculateMatch(pet2, preference);
	}
}

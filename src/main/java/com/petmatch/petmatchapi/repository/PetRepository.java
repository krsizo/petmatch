package com.petmatch.petmatchapi.repository;

import com.petmatch.petmatchapi.model.Gender;
import com.petmatch.petmatchapi.model.Pet;
import com.petmatch.petmatchapi.model.PetStatus;
import com.petmatch.petmatchapi.model.PetType;
import com.petmatch.petmatchapi.model.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long>
{
	List<Pet> findByBreedIgnoreCase(String breed);
	List<Pet> findByGender(Gender gender);
	List<Pet> findBySize(Size size);
	List<Pet> findByTemperamentContainingIgnoreCase(String temperament);
	List<Pet> findByAgeBetween(Integer min, Integer max);
	List<Pet> findByStatus(PetStatus status);
	List<Pet> findByPetType(PetType type);
	@Query("""
    SELECT p FROM Pet p
    WHERE p.status = :status
      AND (:petType IS NULL OR p.petType = :petType)
      AND (:breed IS NULL OR LOWER(p.breed) = LOWER(:breed))
      AND (:gender IS NULL OR p.gender = :gender)
      AND (:size IS NULL OR p.size = :size)
      AND (:minAge IS NULL OR p.age >= :minAge)
      AND (:maxAge IS NULL OR p.age <= :maxAge)
      AND (:location IS NULL OR LOWER(p.location) = LOWER(:location))
      AND (:maxFee IS NULL OR p.adoptionFee <= :maxFee)
    """)
	List<Pet> searchPets(
		@Param("status") PetStatus status,
		@Param("petType") PetType petType,
		@Param("breed") String breed,
		@Param("gender") Gender gender,
		@Param("size") Size size,
		@Param("minAge") Integer minAge,
		@Param("maxAge") Integer maxAge,
		@Param("location") String location,
		@Param("maxFee") BigDecimal maxFee
	);

	@Query("""
		SELECT p FROM Pet p
		WHERE (6371 * acos(
		    cos(radians(:userLat)) * cos(radians(p.latitude)) *
		    cos(radians(p.longitude) - radians(:userLng)) +
		    sin(radians(:userLat)) * sin(radians(p.latitude))
		)) <= :radius
		""")
	List<Pet> findNearbyPets(@Param("userLat") Double userLat, @Param("userLng") Double userLng,
		@Param("radius") Double radius);
}

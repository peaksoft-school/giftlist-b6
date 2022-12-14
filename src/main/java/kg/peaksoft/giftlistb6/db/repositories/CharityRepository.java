package kg.peaksoft.giftlistb6.db.repositories;

import kg.peaksoft.giftlistb6.db.models.Charity;
import kg.peaksoft.giftlistb6.dto.responses.InnerPageCharityResponse;
import kg.peaksoft.giftlistb6.dto.responses.OtherCharityResponse;
import kg.peaksoft.giftlistb6.dto.responses.YourCharityResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CharityRepository extends JpaRepository<Charity, Long> {

    @Query("select new kg.peaksoft.giftlistb6.dto.responses.YourCharityResponse(" +
            "ch.id," +
            "ch.image)"+
            "from User u join u.charities ch where u.email = ?1")
    List<YourCharityResponse> getAllMyCharity(String email);

    @Transactional
    @Modifying
    @Query("delete from Charity ch where ch.user.id = ?2 and ch.id = ?1")
    void deleteCharityById(Long chId, Long userId);

    @Query("select new kg.peaksoft.giftlistb6.dto.responses.OtherCharityResponse(ch) from Charity ch where ch.user.email <> ?1")
    List<OtherCharityResponse> getAll(String email);

    @Query("select new kg.peaksoft.giftlistb6.dto.responses.OtherCharityResponse (" +
            "ch.id," +
            "ch.image," +
            "ch.name," +
            "ch.condition," +
            "ch.createdAt," +
            "ch.user.id," +
            "ch.user.firstName," +
            "ch.user.lastName," +
            "ch.user.image," +
            "ch.isBlock) from Charity ch")
    List<OtherCharityResponse> getAllFromAdmin();

    @Query("select new kg.peaksoft.giftlistb6.dto.responses.OtherCharityResponse("+
            "ch.id," +
            "ch.image," +
            "ch.name," +
            "ch.condition," +
            "ch.createdAt," +
            "ch.charityStatus," +
            "ch.user.id," +
            "ch.user.firstName," +
            "ch.user.lastName," +
            "ch.user.image )"+
            "from User u join u.charities ch")
    List<OtherCharityResponse> getAllCharities();

    @Query("select new kg.peaksoft.giftlistb6.dto.responses.InnerPageCharityResponse(" +
            "c.id," +
            "c.image," +
            "c.name," +
            "c.description," +
            "c.category.name," +
            "c.subCategory.name," +
            "c.condition," +
            "c.createdAt," +
            "c.charityStatus," +
            "c.user.id," +
            "c.user.image," +
            "c.user.firstName," +
            "c.user.lastName)" +
            "from Charity c where c.id = ?1")
    InnerPageCharityResponse getCharityById(Long id);

    @Query("select c from Charity c " +
            "where (lower(c.name) like lower(concat(:text,'%')) or :text is null)" +
            "and (lower(c.condition) like lower(concat(:condition,'%')) or :condition is null)" +
            "and (lower(c.category.name) like lower(concat(:category,'%')) or :category is null)" +
            "and (lower(c.subCategory.name) like lower(concat(:subCategory,'%')) or :subCategory is null) " +
            "and c.user.email not like concat(:email,'%')")
    List<Charity> searchCharity(@Param("text") String text,
                                @Param("condition") String condition,
                                @Param("category") String category,
                                @Param("subCategory") String subCategory,
                                String email);
}
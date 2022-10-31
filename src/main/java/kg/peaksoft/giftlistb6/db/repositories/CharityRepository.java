package kg.peaksoft.giftlistb6.db.repositories;

import kg.peaksoft.giftlistb6.db.models.Charity;
import kg.peaksoft.giftlistb6.dto.responses.InnerPageCharityResponse;
import kg.peaksoft.giftlistb6.dto.responses.OtherCharityResponse;
import kg.peaksoft.giftlistb6.dto.responses.UserCharityResponse;
import kg.peaksoft.giftlistb6.dto.responses.YourCharityResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CharityRepository extends JpaRepository<Charity, Long> {

    @Query("select new kg.peaksoft.giftlistb6.dto.responses.YourCharityResponse(ch.id,ch.image)" +
            "from User u join u.charities ch where u.email = ?1")
    List<YourCharityResponse> getAllMyCharity(String email);

    @Transactional
    @Modifying
    @Query("delete from Charity ch where ch.user.id = ?2 and ch.id = ?1")
    void deleteCharityById(Long chId, Long userId);


    @Query("select new kg.peaksoft.giftlistb6.dto.responses.OtherCharityResponse(" +
            "ch.id,ch.image,ch.name,ch.condition,ch.createdDate,ch.charityStatus,ch.user.id,concat(ch.user.firstName,' ',ch.user.lastName),ch.user.photo )" +
            "from User u join u.charities ch where u.email <> ?1")
    List<OtherCharityResponse> getAllOther(String email);

    @Query("select new kg.peaksoft.giftlistb6.dto.responses.InnerPageCharityResponse(" +
            "c.id," +
            "c.image," +
            "c.name," +
            "c.description," +
            "c.category.name," +
            "c.subCategory.name," +
            "c.condition," +
            "c.createdDate," +
            "c.charityStatus) from Charity c where c.id= ?1")
    InnerPageCharityResponse getCharityById(Long id);


    @Query("select new kg.peaksoft.giftlistb6.dto.responses.UserCharityResponse(u.id,u.firstName,u.lastName,u.photo)from User u join u.charities c where u.id =?1")
    UserCharityResponse getUserCharity(Long id);
}

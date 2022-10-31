package kg.peaksoft.giftlistb6.db.repositories;

import kg.peaksoft.giftlistb6.db.models.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    @Query("select c from SubCategory c where c.name = :name")
    SubCategory findByName(String name);

    @Query("select s.name from SubCategory s where s.name=:name")
    String getByName(String name);
}
package kg.peaksoft.giftlistb6.db.services;

import kg.peaksoft.giftlistb6.db.models.Charity;
import kg.peaksoft.giftlistb6.db.models.User;
import kg.peaksoft.giftlistb6.db.repositories.CategoryRepository;
import kg.peaksoft.giftlistb6.db.repositories.CharityRepository;
import kg.peaksoft.giftlistb6.db.repositories.SubCategoryRepository;
import kg.peaksoft.giftlistb6.enums.Status;
import kg.peaksoft.giftlistb6.exceptions.NotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@TestPropertySource("/application-test.yml")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataJpaTest
class CharityServiceTest {
    @Autowired
    private CharityRepository charityRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Test
    @Order(2)
    void getCharityById() {
        Charity charity = charityRepository.findById(1L).orElseThrow(()->new NotFoundException("not found"));
        Assertions.assertThat(charity.getId()).isEqualTo(1L);
    }

    @Test
    @Order(1)
    void saveCharity() {
        Charity charity = new Charity(1L,"сумка",categoryRepository.findById(2L).get(),
                subCategoryRepository.findById(10L).get(),new User(),new User(), Status.RESERVED,
                "new bag","new","image", LocalDate.of(2022, 11, 18));
        Assertions.assertThat(charity.getId()).isGreaterThan(0);
    }

    @Test
    @Order(3)
    void getAll() {
        List<Charity> charities = charityRepository.findAll();
        Assertions.assertThat(charities.size()).isGreaterThan(0);
    }

    @Test
    @Order(4)
    void updateCharity() {
        Charity charity = charityRepository.findById(1L).orElseThrow(()->new NotFoundException("not found"));
        charity.setName("платье");
        Charity charityUpdate = charityRepository.save(charity);
        Assertions.assertThat(charityUpdate.getName()).isEqualTo("платье");
    }

    @Test
    @Order(5)
    void deleteCharityById() {
        Charity charity = charityRepository.findById(1L).orElseThrow(()->new NotFoundException("not found"));
        charityRepository.delete(charity);
        Charity charity1 = null;
        Optional<Charity> optionalCharity = charityRepository.findById(1L);
        if (optionalCharity.isPresent()){
            charity1= optionalCharity.get();
        }
        Assertions.assertThat(charity1).isNull();

    }
}
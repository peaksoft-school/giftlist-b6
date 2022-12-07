package kg.peaksoft.giftlistb6.db.services;

import kg.peaksoft.giftlistb6.db.models.Holiday;
import kg.peaksoft.giftlistb6.db.repositories.HolidayRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.PropertySource;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
@PropertySource("/application-test.yml")
class HolidayServiceTest {

    @Autowired
    private HolidayRepository holidayRepository;

    @Test
    void saveHoliday() {
        Holiday holiday = Holiday.builder()
                .id(1L)
                .name("Holiday")
                .image("image")
                .dateOfHoliday(LocalDate.of(2022,12,12))
                .build();
        assertThat(holiday.getId()).isGreaterThan(0);
    }

    @Test
    void getHolidayById() {
        Holiday holiday = holidayRepository.findById(1L).get();
        assertThat(holiday.getId()).isEqualTo(1L);
    }

    @Test
    void deleteHolidayById() {
        Holiday holiday = holidayRepository.findById(1L).get();
        holidayRepository.delete(holiday);
        assertThat(holiday.getId()).isLessThan(3);
    }

    @Test
    void saveUpdateHoliday() {
        Holiday holiday = holidayRepository.findById(1L).get();
        holiday.setName("Sunat");
        Holiday holiday1 = holidayRepository.save(holiday);
        assertThat(holiday1.getName()).isEqualTo("Sunat");
    }

    @Test
    void getAllHolidays() {
        List<Holiday> holidayList = holidayRepository.findAll();
        assertThat(holidayList.size()).isGreaterThan(0);
    }
}
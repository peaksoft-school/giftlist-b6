package kg.peaksoft.giftlistb6.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.peaksoft.giftlistb6.db.services.HolidayService;
import kg.peaksoft.giftlistb6.dto.requests.HolidayRequest;
import kg.peaksoft.giftlistb6.dto.responses.HolidayResponseForGet;
import kg.peaksoft.giftlistb6.dto.responses.HolidayResponses;
import kg.peaksoft.giftlistb6.dto.responses.SimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/holidays")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Holiday Api", description = "Holiday CRUD operations")
@PreAuthorize("hasAuthority('USER')")
public class HolidayApi {

    private final HolidayService holidayService;

    @Operation(summary = "Save Holiday", description = "User can save holiday")
    @PostMapping
    public HolidayResponses saveHoliday(@RequestBody HolidayRequest request) {
        return holidayService.saveHoliday(request);
    }

    @Operation(summary = "Get all holidays", description = "User can get all holidays")
    @GetMapping
    public List<HolidayResponses> getAllHolidays() {
        return holidayService.getAllHolidays();
    }

    @Operation(summary = "Get holiday", description = "Get holiday by id")
    @GetMapping("/{id}")
    public HolidayResponseForGet getHolidayById(@PathVariable Long id) {
        return holidayService.getHolidayById(id);
    }

    @Operation(summary = "Delete holiday", description = "User can delete holiday by id")
    @DeleteMapping("/{id}")
    public SimpleResponse deleteHolidayById(@PathVariable Long id) {
        return holidayService.deleteHolidayById(id);
    }

    @Operation(summary = "Update holiday", description = "User can update holiday by id")
    @PutMapping("/{id}")
    public HolidayResponses updateHolidayById(@PathVariable Long id, @RequestBody HolidayRequest request) {
        return holidayService.saveUpdateHoliday(id, request);
    }
}
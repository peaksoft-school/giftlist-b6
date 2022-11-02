package kg.peaksoft.giftlistb6.dto.responses;

import kg.peaksoft.giftlistb6.db.models.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReservoirResponse {
    private Long id;
    private String photo;

    public ReservoirResponse(User reservoir) {
        this.id = reservoir.getId();
        this.photo = reservoir.getPhoto();
    }
}

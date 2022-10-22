package kg.peaksoft.giftlistb6.db.repositories;


import kg.peaksoft.giftlistb6.db.models.User;
import kg.peaksoft.giftlistb6.dto.responses.FriendInfoResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface FriendRepository extends JpaRepository<User,Long> {

    @Query("select new kg.peaksoft.giftlistb6.dto.responses.FriendInfoResponse( " +
            "f.id," +
            "f.photo," +
            "concat(f.firstName,' ',f.lastName)," +
            "f.holidays.size" +
            ",f.wishes.size) from User u join u.friends f where u.email =?1")
    List<FriendInfoResponse> getAllFriends(String email);

    @Query("select new kg.peaksoft.giftlistb6.dto.responses.FriendInfoResponse( " +
            "f.id," +
            "f.photo," +
            "concat(f.firstName,' ',f.lastName)," +
            "f.holidays.size" +
            ",f.wishes.size) from User u join u.requests f where u.email =?1")
    List<FriendInfoResponse>getAllRequests(String email);

}

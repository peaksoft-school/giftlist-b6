package kg.peaksoft.giftlistb6.dto.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.Deque;
import java.util.List;

@Getter
@Setter
public class AllFeedResponse {

    Deque<AllMailingListResponse> mailingLists;
    Deque<FeedResponse> feeds;
}

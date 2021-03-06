package ru.javawebinar.topjava.web.vote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.topjava.View;
import ru.javawebinar.topjava.model.Vote;
import ru.javawebinar.topjava.service.VoteService;
import ru.javawebinar.topjava.to.VoteTo;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = AdminVoteRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminVoteRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    public static final String REST_URL = "/rest/admin/vote";

    @Autowired
    private VoteService voteService;

    @GetMapping("/ratingByDate")
    public Integer getRestaurantRating(@RequestParam String restaurantName, @RequestParam String date) {
        log.info("getRestaurantRating for restaurant {} by date {}", restaurantName, date);
        return voteService.getRestaurantRating(LocalDate.parse(date), restaurantName);
    }

    @GetMapping("/ratingGroupByDate")
    public List<VoteTo> getAllRestaurantRatingGroupByDate(@RequestParam String restaurantName) {
        log.info("getAllRestaurantRatingGroupByDate for restaurant {}", restaurantName);
        return voteService.getAllRestaurantRatingGroupByDate(restaurantName);
    }
}

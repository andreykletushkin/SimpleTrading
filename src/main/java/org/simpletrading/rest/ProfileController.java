package org.simpletrading.rest;

import org.simpletrading.model.ProfileDetails;
import org.simpletrading.model.User;
import org.simpletrading.repo.ProfileRepository;
import org.simpletrading.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @GetMapping("/{id}")
    public ResponseEntity<ProfileDetails> get(@PathVariable String id) {
        User user = userRepository.findByUsername(id).get();
        return new ResponseEntity<ProfileDetails>(user.getProfileDetails(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity update(@RequestBody ProfileDetails profileDetails) {
        ProfileDetails profile = profileRepository.findById(profileDetails.getId()).get();
        User user = profile.getUser();
        user.setProfileDetails(profileDetails);
        return new ResponseEntity(userRepository.save(user),HttpStatus.OK);
    }

}

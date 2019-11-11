import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.simpletrading.Application;
import org.simpletrading.rest.dto.AuthentificationResponse;
import org.simpletrading.model.ProfileDetails;
import org.simpletrading.model.User;
import org.simpletrading.repo.ProfileRepository;
import org.simpletrading.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class ProfileControllerTest {


    @Autowired
    private MockMvc mvc;

    private String token;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    UserRepository userRepository;

    @Before
    public void tokenSetup() throws Exception {
        User user = User.builder().password("password").username("usertest")
                .roles(Arrays.asList("ROLE_USER"))
                .build();
        MvcResult result = mvc.perform(post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()))
                .andReturn();
        AuthentificationResponse response = new ObjectMapper().readValue(
                result.getResponse().getContentAsString(), AuthentificationResponse.class);
        token = response.getToken();
    }

    @Test
    public void givenUser_getProfile_returnsProfile() throws Exception {
        mvc.perform(get("/profile/usertest").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue())).andReturn();


    }

    @Test
    public void givenUser_updateProfile_returnsUpdatedProfile() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/profile/usertest").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue())).andReturn();
        ProfileDetails profileDetails = ProfileDetails.builder().id(1L).firstName("Test").build();
        profileDetails.setFirstName("UpdatedUser");
        mvc.perform(put("/profile").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(profileDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andReturn();

        mvc.perform(get("/profile/usertest").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.firstName", is("UpdatedUser")));

        assertThat(profileRepository.findAll().size(), is(1));
        assertThat(userRepository.findAll().size(), is(1));
    }

}

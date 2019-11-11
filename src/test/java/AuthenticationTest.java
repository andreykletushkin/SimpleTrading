import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.simpletrading.Application;
import org.simpletrading.rest.dto.AuthentificationResponse;
import org.simpletrading.model.User;
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

import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class AuthenticationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    private User user = User.builder().password("password").username("usertest")
            .roles(Arrays.asList("ROLE_USER"))
            .build();

    private User userWrong = User.builder().password("passwordwrong").username("user")
            .roles(Arrays.asList("ROLE_USER"))
            .build();

    @Test
    public void givenUser_whenAuthentificate_returnToken() throws Exception {
        mvc.perform(post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()))
                .andReturn();
    }

    @Test
    public void givenUserWithToken_POSTisOk() throws Exception {

        MvcResult result =  mvc.perform(post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()))
                .andReturn();
        AuthentificationResponse response =  new ObjectMapper().readValue(
                result.getResponse().getContentAsString(),AuthentificationResponse.class);


        mvc.perform(get("/profile/"+response.getUsername()).header("Authorization","Bearer ".concat(response.getToken())))
                .andExpect(status().isOk());

    }

    @Test
    public void givenWrongUser_AccessForbidden() throws Exception {
        mvc.perform(post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userWrong)))
                .andExpect(status().isForbidden());
    }





}

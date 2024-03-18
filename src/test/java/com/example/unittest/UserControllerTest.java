package com.example.unittest;

import com.example.unittest.controller.UserController;
import com.example.unittest.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

	@Autowired
	UserController userController;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void contextLoads() {
		assertThat(userController).isNotNull();
	}

	@Test
	void addUser() throws Exception {
		User user = new User(1, "Mario");

		String userJson = objectMapper.writeValueAsString(user);

		this.mockMvc.perform(post("/v1/user")
						.contentType(MediaType.APPLICATION_JSON)
						.content(userJson))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
	}

	@Test
	void findById() throws Exception {
		int id = 1;
		addUser();

		MvcResult result = this.mockMvc.perform(get("/v1/user/" + id))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		User userFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
		assertThat(userFromResponse.getId()).isEqualTo(id);
	}

	@Test
	void findAllUser() throws Exception {
		addUser();

		MvcResult result = this.mockMvc.perform(get("/v1/users"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		List<User> userFromResponseList = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
		assertThat(userFromResponseList.size()).isNotZero();
	}

	@Test
	void updateUser() throws Exception {
		int id = 1;
		addUser();

		User user = new User(3, "Agostino");
		String userJson = objectMapper.writeValueAsString(user);

		MvcResult result = this.mockMvc.perform(put("/v1/user/" + id)
						.contentType(MediaType.APPLICATION_JSON)
						.content(userJson))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		User userFromResponse = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
		assertThat(userFromResponse.getNome()).isEqualTo(user.getNome());
	}

	@Test
	void deleteUser() throws Exception {
		addUser();
		int id = 1;

		mockMvc.perform(delete("/v1/user/" + id))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
	}
}

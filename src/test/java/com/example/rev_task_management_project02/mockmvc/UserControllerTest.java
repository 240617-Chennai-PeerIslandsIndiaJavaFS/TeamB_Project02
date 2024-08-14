package com.example.rev_task_management_project02.mockmvc;


import com.example.rev_task_management_project02.controllers.UserController;
import com.example.rev_task_management_project02.models.Role;
import com.example.rev_task_management_project02.models.User;
import com.example.rev_task_management_project02.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testLogin_Success() throws Exception {
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password");

        Mockito.when(userService.login(anyString(), anyString())).thenReturn(mockUser);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/login")
                        .param("email", "test@example.com")
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"email\":\"test@example.com\",\"password\":\"password\"}"));
    }

    @Test
    public void testLogin_Failure() throws Exception {
        Mockito.when(userService.login(anyString(), anyString())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/login")
                        .param("email", "wrong@example.com")
                        .param("password", "wrongpassword"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));
    }

    @Test
    public void testResetPassword_Success() throws Exception {
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("newpassword");

        Mockito.when(userService.resetPassword(anyString(), anyString())).thenReturn(mockUser);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/resetPassword")
                        .param("email", "test@example.com")
                        .param("newPassword", "newpassword")
                        .param("confirmPassword", "newpassword"))
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset successfully"));
    }


    @Test
    public void testCreateUser() throws Exception {
        User mockUser = new User();
        mockUser.setEmail("newuser@example.com");
        mockUser.setPassword("password");

        Mockito.when(userService.createUser(Mockito.any(User.class))).thenReturn(mockUser);

        String userJson = "{\"email\":\"newuser@example.com\",\"password\":\"password\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(content().json(userJson));
    }

    @Test
    public void testDeactivateUser_Success() throws Exception {
        Mockito.when(userService.deactivateUser(anyLong())).thenReturn(new User());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/admin/deactivateUser/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("User with ID 1 deactivated successfully."));
    }

    @Test
    public void testAssignRole_Success() throws Exception {
        User mockUser = new User();
        mockUser.setUserRole(Role.ADMIN);

        Mockito.when(userService.assignRole(anyLong(), any(Role.class))).thenReturn(mockUser);

        String roleJson = "{\"role\":\"ADMIN\"}";

        mockMvc.perform(MockMvcRequestBuilders.put("/api/admin/assignRole/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(roleJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Role ADMIN assigned to user with ID 1 successfully."));
    }

    @Test
    public void testGetUserById_Success() throws Exception {
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setUserId(1L);

        Mockito.when(userService.getUserById(anyLong())).thenReturn(mockUser);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"email\":\"test@example.com\",\"userId\":1}"));
    }

    @Test
    public void testGetAllUsers_Success() throws Exception {
        User mockUser = new User();
        mockUser.setEmail("test@example.com");

        List<User> userList = Arrays.asList(mockUser);

        Mockito.when(userService.getAllUsers()).thenReturn(userList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/admin/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"email\":\"test@example.com\"}]"));
    }

    @Test
    public void testDeleteUserById_Success() throws Exception {
        User mockUser = new User();
        mockUser.setUserId(1L);
        mockUser.setEmail("deleted@example.com");

        Mockito.when(userService.deleteUserById(anyLong())).thenReturn(mockUser);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/admin/deleteUser/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"userId\":1,\"email\":\"deleted@example.com\"}"));
    }
    @Test
    public void testResetPassword_NotFound() throws Exception {
        Mockito.when(userService.resetPassword(anyString(), anyString())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/resetPassword")
                        .param("email", "nonexistent@example.com")
                        .param("newPassword", "newpassword")
                        .param("confirmPassword", "newpassword"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }



}
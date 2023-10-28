package com.example.ecommerceapp.service;

import com.example.ecommerceapp.dto.UserLoginDTO;
import com.example.ecommerceapp.dto.UserPostDTO;
import com.example.ecommerceapp.exception.UserAlreadyExistsException;
import com.example.ecommerceapp.exception.UserNameOrPasswordNotFoundException;
import com.example.ecommerceapp.model.VerificationToken;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private EmailService emailService;

    @Container
    private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:latest")
            .withDatabaseName("testContainer")
            .withUsername("root")
            .withPassword("root")
            .waitingFor(Wait.forListeningPort())
            .withEnv("MYSQL_ROOT_HOST", "%");
    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
    }
    @BeforeAll
    public static void setUp() {
        mysqlContainer.start();
    }

    @Test
    @Transactional
    @Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public  void testRegisterUser() {
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUserName("UserA");
        userPostDTO.setEmail("userA@email.com");
        userPostDTO.setFirstName("FirstName");
        userPostDTO.setLastName("LastName");
        userPostDTO.setPassword("MyPass");

        Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(userPostDTO), "User with this email or username already exists!");

        userPostDTO.setUserName("TestUser");
        userPostDTO.setEmail("userA@yahoo.com");
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(userPostDTO), "Email already should be in use");

        doNothing().when(emailService).sendVerificationEmail(any(VerificationToken.class));
        userPostDTO.setEmail("userA@gmail.com");
        Assertions.assertDoesNotThrow(() -> userService.registerUser(userPostDTO), "User should register successfully!");

    }

    @Test
    @Transactional
    @Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testLoginUser() {
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUserName("UserANotExist");
        userLoginDTO.setPassword("ciuriburi25");

        Assertions.assertThrows(UserNameOrPasswordNotFoundException.class, () -> userService.loginUser(userLoginDTO));

        userLoginDTO.setUserName("UserA");
        Assertions.assertDoesNotThrow(() -> userService.loginUser(userLoginDTO), "User should login successfully!");
    }
}

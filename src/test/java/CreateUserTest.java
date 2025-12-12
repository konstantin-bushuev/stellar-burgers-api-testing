import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import model.User;
import net.datafaker.Faker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserSteps;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

@Epic("Функционал пользователя")
@Feature("Создание пользователя")
public class CreateUserTest extends BaseTest{

    private UserSteps userSteps = new UserSteps();
    private User user;
    Faker faker = new Faker();

    @Before
    public void setUp() {
        user = new User();
        user
                .setEmail(faker.internet().emailAddress())
                .setPassword(faker.regexify("[a-z0-9]{6}"))
                .setName(faker.name().firstName());
    }

    @Test
    @DisplayName("Создание уникального пользователя: переданы email, пароль, имя")
    @Description("Проверка создания пользователя. ОР: код ответа 200; в body success=true")
    public void createUserAllDataProvided() {
        userSteps
                .createUser(user)
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание уже зарегистрированного пользователя: переданы email, пароль, имя")
    @Description("Проверка создания пользователя. ОР: код ответа 403; в body message=User already exists")
    public void createExistentUserAllDataProvided() {
        userSteps.createUser(user);
        userSteps
                .createUser(user)
                .statusCode(SC_FORBIDDEN)
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание уникального пользователя: не передан email")
    @Description("Проверка создания пользователя. ОР: код ответа 403; в body message=Email, password and name are required fields")
    public void createUserNoEmailProvided() {
        user.setEmail(null);
        userSteps
                .createUser(user)
                .statusCode(SC_FORBIDDEN)
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание уникального пользователя: не передан пароль")
    @Description("Проверка создания пользователя. ОР: код ответа 403; в body message=Email, password and name are required fields")
    public void createUserNoPasswordProvided() {
        user.setPassword(null);
        userSteps
                .createUser(user)
                .statusCode(SC_FORBIDDEN)
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание уникального пользователя: не передано имя")
    @Description("Проверка создания пользователя. ОР: код ответа 403; в body message=Email, password and name are required fields")
    public void createUserNoNameProvided() {
        user.setName(null);
        userSteps
                .createUser(user)
                .statusCode(SC_FORBIDDEN)
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @After
    public void tearDown() {
        try {
            userSteps.deleteUser(user);
        } catch (Exception e) {
        }
    }
}

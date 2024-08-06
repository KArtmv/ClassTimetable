package ua.foxminded.WebProject.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ua.foxminded.WebProject.security.WebSecurityConfig;
import ua.foxminded.WebProject.service.CourseService;
import ua.foxminded.WebProject.testDataInstance.TestItems;
import ua.foxminded.WebProject.util.CustomAuthenticationSuccessHandler;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.foxminded.WebProject.testDataInstance.CustomSecurityMockMvcRequestPostProcessors.testUser;

@Import({WebSecurityConfig.class, CustomAuthenticationSuccessHandler.class})
@WebMvcTest(CourseController.class)
class CourseControllerTest {

    private final TestItems testItems = new TestItems();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void coursePage_shouldReturnCoursePage_whenLoginUserWithAbleAccess(String role) throws Exception {
        when(courseService.getAll()).thenReturn(testItems.getCourses());

        mockMvc.perform(get("/course").with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        model().attributeExists("courses"),
                        view().name("course/course"),
                        content().string(containsString("Principles of Economics")),
                        content().string(containsString("Learn about the fundamentals of economics")),
                        content().string(containsString("World History: Ancient Civilizations")),
                        content().string(containsString("Explore the history of ancient civilizations")),
                        content().string(containsString("Creative Writing Workshop")),
                        content().string(containsString("Develop your writing skills in a creative environment")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"student", "teacher"})
    void coursePage_shouldReturnForbiddenStatus_whenLoginUserWithoutAbleAccess(String role) throws Exception {
        mockMvc.perform(get("/course").with(testUser(role))).andDo(print())
                .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void addCourseForm_shouldAddCourse_whenLoginUserWithAbleAccess(String role) throws Exception {
        mockMvc.perform(get("/course/addCourse").with(testUser(role))).andDo(print())
                .andExpectAll(status().isOk(),
                        view().name("course/features/addCourse"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"student", "teacher"})
    void addCourseForm_shouldAddCourse_whenLoginUserWithoutAbleAccess(String role) throws Exception {
        mockMvc.perform(get("/course/addCourse").with(testUser(role)))
                .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void saveCourse_shouldSaveCourse_whenSuccessfullyValid(String role) throws Exception {
        mockMvc.perform(post("/course/save")
                        .queryParam("courseName", testItems.getCourseName())
                        .queryParam("courseDescription", testItems.getCourseDescription())
                        .with(testUser(role))).andDo(print())
                .andExpectAll(status().is3xxRedirection(),
                        view().name("redirect:/course"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void saveCourse_shouldSaveCourse_whenValidIsFailed(String role) throws Exception {
        mockMvc.perform(post("/course/save")
                        .queryParam("courseName", testItems.getCourseName())
                        .queryParam("courseDescription", "")
                        .with(testUser(role))).andDo(print())
                .andExpectAll(status().isOk(),
                        view().name("course/features/addCourse"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"student", "teacher"})
    void saveCourse_shouldReturnForbidden_whenUserWithoutAbleAccess(String role) throws Exception {
        mockMvc.perform(post("/course/save")
                        .with(testUser(role))).andDo(print())
                .andExpectAll(status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void editCourseForm_shouldReturnEditFormPage_whenUserWithAbleAccess(String role) throws Exception {
        when(courseService.getById(7L)).thenReturn(testItems.getCourse());

        mockMvc.perform(get("/course/edit/{id}", 7L).with(testUser(role)))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("course/features/editCourse"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"student", "teacher"})
    void editCourseForm_shouldReturnEditFormPage_whenUserWithoutAbleAccess(String role) throws Exception {
        when(courseService.getById(7L)).thenReturn(testItems.getCourse());

        mockMvc.perform(get("/course/edit/{id}", 7L).with(testUser(role)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void updateCourse_shouldReturnCoursePage_whenCourseSuccessfullyUpdate(String role) throws Exception {
        mockMvc.perform(post("/course/update")
                        .queryParam("courseName", testItems.getCourseName())
                        .queryParam("courseDescription", testItems.getCourseDescription())
                        .with(testUser(role))).andDo(print())
                .andExpectAll(status().is3xxRedirection(),
                        view().name("redirect:/course"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void updateCourse_shouldReturnCoursePage_whenDescriptionIsEmpty(String role) throws Exception {
        mockMvc.perform(post("/course/update")
                        .queryParam("id", testItems.getCourseId().toString())
                        .queryParam("courseName", testItems.getCourseName())
                        .queryParam("courseDescription", "")
                        .with(testUser(role))).andDo(print())
                .andExpectAll(status().isOk(),
                        view().name("course/features/editCourse"),
                        content().string(containsString("Course description is required.")),
                        content().string(containsString("Course description must be between 5 and 100 characters.")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void updateCourse_shouldReturnCoursePage_whenCourseNameIsEmpty(String role) throws Exception {
        mockMvc.perform(post("/course/update")
                        .queryParam("id", testItems.getCourseId().toString())
                        .queryParam("courseName", "")
                        .queryParam("courseDescription", testItems.getCourseDescription())
                        .with(testUser(role))).andDo(print())
                .andExpectAll(status().isOk(),
                        view().name("course/features/editCourse"),
                        content().string(containsString("Course name is required.")),
                        content().string(containsString("Course name must be between 5 and 50 characters.")));
    }

    @Test
    void deleteCourseForm_shouldReturnCoursePage_whenUserWithAbleAccess() throws Exception {
        when(courseService.getById(anyLong())).thenReturn(testItems.getCourse());

        mockMvc.perform(get("/course/delete/{id}", 7L).with(testUser("admin"))).andDo(print())
                .andExpectAll(view().name("course/features/deleteCourse"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"staff", "student", "teacher"})
    void deleteCourseForm_shouldReturnErrorPage_whenUserWithoutAbleAccess(String role) throws Exception {
        mockMvc.perform(get("/course/delete/{id}", 7L).with(testUser(role)))
                .andExpect(status().isForbidden());
    }

    @Test
    void removeCourse_shouldReturnCoursePage_whenSuccessfulRemoved() throws Exception {
        mockMvc.perform(post("/course/remove").with(testUser("admin"))).andDo(print())
                .andExpectAll(status().is3xxRedirection(),
                        view().name("redirect:/course"));
    }
}
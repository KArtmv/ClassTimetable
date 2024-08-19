package ua.foxminded.WebProject.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ua.foxminded.WebProject.security.WebSecurityConfig;
import ua.foxminded.WebProject.service.*;
import ua.foxminded.WebProject.testDataInstance.TestData;
import ua.foxminded.WebProject.testDataInstance.TestItems;
import ua.foxminded.WebProject.util.CustomAuthenticationSuccessHandler;
import ua.foxminded.WebProject.util.MyLocalDate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.foxminded.WebProject.testDataInstance.CustomSecurityMockMvcRequestPostProcessors.testUser;

@Import({WebSecurityConfig.class, CustomAuthenticationSuccessHandler.class})
@WebMvcTest(LessonController.class)
class LessonControllerTest {

    private final TestItems testItems = new TestItems();
    private final TestData testData = new TestData();

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private LessonController lessonController;

    @MockBean
    private LessonService lessonService;
    @MockBean
    private GroupService groupService;
    @MockBean
    private CourseService courseService;
    @MockBean
    private TeacherService teacherService;
    @MockBean
    private ClassroomService classroomService;
    @MockBean
    private MyLocalDate localDate;

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void lessonPage_shouldReturnLessonPage_whenLoginUserWithAbleAccess(String role) throws Exception {
        when(lessonService.getAll()).thenReturn(testItems.getAllLessons());

        mockMvc.perform(get("/lesson").with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        model().attributeExists("classtables"),
                        model().attributeExists("lessonPerDayPath"),
                        model().attributeExists("lessonByDatesPath"),
                        view().name("lesson/lesson"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void lessonPage_shouldReturnMessageThatLessonListIsEmpty_whenLoginUserWithAbleAccess(String role) throws Exception {
        when(lessonService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/lesson").with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        model().attributeExists("classtables"),
                        model().attributeExists("lessonPerDayPath"),
                        model().attributeExists("lessonByDatesPath"),
                        content().string(containsString("The lessons list is currently empty.")),
                        view().name("lesson/lesson"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"student", "teacher"})
    void lessonPage_shouldReturnAddLessonPage_whenLoginUserWithoutAbleAccess(String role) throws Exception {
        mockMvc.perform(get("/lesson").with(testUser(role)))
                .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void addLesson_shouldReturnLessonPage_whenUserRoleHasAccess(String role) throws Exception {
        when(groupService.getAll()).thenReturn(testItems.getGroups());
        when(courseService.getAll()).thenReturn(testItems.getCourses());

        mockMvc.perform(get("/lesson/add-lesson").with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        model().attributeExists("lessonDto"),
                        model().attributeExists("groups"),
                        model().attributeExists("courses"),
                        model().attributeExists("edit"),
                        view().name("lesson/features/newLesson"),
                        content().string(containsString("YS-27")),
                        content().string(containsString("HV-14")),
                        content().string(containsString("QM-09")),
                        content().string(containsString("Principles of Economics")),
                        content().string(containsString("World History: Ancient Civilizations")),
                        content().string(containsString("Creative Writing Workshop")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"student", "teacher"})
    void addLesson_shouldReturnForbiddenStatus_whenUserRoleLackAccess(String role) throws Exception {
        when(groupService.getAll()).thenReturn(testItems.getGroups());
        when(courseService.getAll()).thenReturn(testItems.getCourses());

        mockMvc.perform(get("/lesson/add-lesson").with(testUser(role))).andDo(print())
                .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void save_shouldReturnLessonPage_whenUserRoleHasAccess(String role) throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("date", LocalDate.now().toString());
        params.add("lessonNum", testData.getLessenNum().toString());
        params.add("groupId", testData.getGroupId().toString());
        params.add("courseId", testData.getCourseId().toString());
        params.add("classroomId", testData.getClassroomId().toString());
        params.add("teacherId", testData.getTeacherId().toString());

        mockMvc.perform(post("/lesson/save").params(params).with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/lesson"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void save_shouldReturnErrorMessages_whenLessonHasInvalidParams(String role) throws Exception {
        when(lessonService.getAvailableNumbersOfLessons(any(LocalDate.class), anyLong())).thenReturn(testItems.getNumbersOfLessons());
        when(lessonService.getAvailableTeachers(any(LocalDate.class), anyInt())).thenReturn(testItems.teacherNames());
        when(lessonService.getAvailableClassrooms(any(LocalDate.class), anyInt())).thenReturn(testItems.getClassrooms());
        when(groupService.getAll()).thenReturn(testItems.getGroups());
        when(courseService.getAll()).thenReturn(testItems.getCourses());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", "");
        params.add("date", "");
        params.add("lessonNum", "");
        params.add("groupId", "");
        params.add("courseId", "");
        params.add("classroomId", "");
        params.add("teacherId", "");

        mockMvc.perform(post("/lesson/save").params(params).with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().string(containsString("Date is required")),
                        content().string(containsString("Lesson number can be empty")),
                        content().string(containsString("Course is required")),
                        content().string(containsString("Group is required")),
                        content().string(containsString("Classroom is required")),
                        content().string(containsString("Teacher is required")),
                        view().name("lesson/features/newLesson"),
                        model().attributeExists("edit"),
                        model().attributeExists("lessonDto"),
                        model().attributeExists("lessonsNum"),
                        model().attributeExists("teachers"),
                        model().attributeExists("classrooms"),
                        model().attributeExists("courses"),
                        model().attributeExists("groups"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void save_shouldReturnErrorMessages_whenLessonDateIsPast(String role) throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", testData.getLessonId().toString());
        params.add("date", testData.getDateOfMonday().with(DayOfWeek.SATURDAY).toString());
        params.add("lessonNum", testData.getLessenNum().toString());
        params.add("groupId", testData.getGroupId().toString());
        params.add("courseId", testData.getCourseId().toString());
        params.add("classroomId", testData.getClassroomId().toString());
        params.add("teacherId", testData.getTeacherId().toString());

        when(lessonService.getAvailableNumbersOfLessons(any(LocalDate.class), anyLong())).thenReturn(testItems.getNumbersOfLessons());
        when(lessonService.getAvailableTeachers(any(LocalDate.class), anyInt())).thenReturn(testItems.teacherNames());
        when(lessonService.getAvailableClassrooms(any(LocalDate.class), anyInt())).thenReturn(testItems.getClassrooms());
        when(groupService.getAll()).thenReturn(testItems.getGroups());
        when(courseService.getAll()).thenReturn(testItems.getCourses());

        mockMvc.perform(post("/lesson/save").params(params).with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().string(containsString("Cannot be weekend day")),
                        view().name("lesson/features/newLesson"),
                        model().attributeExists("edit"),
                        model().attributeExists("lessonDto"),
                        model().attributeExists("lessonsNum"),
                        model().attributeExists("teachers"),
                        model().attributeExists("classrooms"),
                        model().attributeExists("courses"),
                        model().attributeExists("groups"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void editLesson_shouldReturnNewLessonPage_whenUserRoleHasAccess(String role) throws Exception {
        when(lessonService.getLessonDtoById(anyLong())).thenReturn(testItems.getLessonDto());
        when(lessonService.getLessonsNums()).thenReturn(testItems.getNumbersOfLessons());
        when(teacherService.getAllNames()).thenReturn(testItems.teacherNames());
        when(classroomService.getAll()).thenReturn(testItems.getClassrooms());
        when(groupService.getAll()).thenReturn(testItems.getGroups());
        when(courseService.getAll()).thenReturn(testItems.getCourses());

        mockMvc.perform(get("/lesson/edit-{lessonId}", testData.getLessonId()).with(testUser(role)))
                .andDo(print()).andExpectAll(
                        status().isOk(),
                        model().attributeExists("edit"),
                        model().attributeExists("lessonDto"),
                        model().attributeExists("lessonsNum"),
                        model().attributeExists("teachers"),
                        model().attributeExists("classrooms"),
                        model().attributeExists("courses"),
                        model().attributeExists("groups"),
                        view().name("lesson/features/newLesson"),
                        content().string(containsString("Principles of Economics")),
                        content().string(containsString("World History: Ancient Civilizations")),
                        content().string(containsString("Creative Writing Workshop")),
                        content().string(containsString("YS-27")),
                        content().string(containsString("HV-14")),
                        content().string(containsString("QM-09")),
                        content().string(containsString("John")),
                        content().string(containsString("Smith")),
                        content().string(containsString("Emily")),
                        content().string(containsString("Johnson")),
                        content().string(containsString("Michael")),
                        content().string(containsString("Davis")),
                        content().string(containsString("Sarah")),
                        content().string(containsString("Wilson")),
                        content().string(containsString("Robert")),
                        content().string(containsString("Taylor")),
                        content().string(containsString("Jessica")),
                        content().string(containsString("Brown")),
                        content().string(containsString("William")),
                        content().string(containsString("Moore")),
                        content().string(containsString("Olivia")),
                        content().string(containsString("Anderson")),
                        content().string(containsString("Room 102")),
                        content().string(containsString("Room 103")),
                        content().string(containsString("Room 104")));
    }


    @ParameterizedTest
    @ValueSource(strings = {"student", "teacher"})
    void editLesson_shouldReturnForbidden_whenUserRoleLackAccess(String role) throws Exception {
        mockMvc.perform(get("/lesson/edit-{lessonId}", testData.getLessonId()).with(testUser(role)))
                .andDo(print()).andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void update_shouldRedirectToLessonPage_whenLessonSavedSuccessfully(String role) throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", testData.getLessonId().toString());
        params.add("date", LocalDate.now().toString());
        params.add("lessonNum", testData.getLessenNum().toString());
        params.add("groupId", testData.getGroupId().toString());
        params.add("courseId", testData.getCourseId().toString());
        params.add("classroomId", testData.getClassroomId().toString());
        params.add("teacherId", testData.getTeacherId().toString());

        mockMvc.perform(post("/lesson/update").params(params).with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/lesson"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void update_shouldReturnErrorMessage_whenLessonHasInvalidValues(String role) throws Exception {
        when(lessonService.getLessonDtoById(anyLong())).thenReturn(testItems.getLessonDto());
        when(lessonService.getLessonsNums()).thenReturn(testItems.getNumbersOfLessons());
        when(teacherService.getAllNames()).thenReturn(testItems.teacherNames());
        when(classroomService.getAll()).thenReturn(testItems.getClassrooms());
        when(groupService.getAll()).thenReturn(testItems.getGroups());
        when(courseService.getAll()).thenReturn(testItems.getCourses());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", "");
        params.add("date", "");
        params.add("lessonNum", "");
        params.add("groupId", "");
        params.add("courseId", "");
        params.add("classroomId", "");
        params.add("teacherId", "");

        mockMvc.perform(post("/lesson/update").params(params).with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().string(containsString("Date is required")),
                        content().string(containsString("Lesson number can be empty")),
                        content().string(containsString("Course is required")),
                        content().string(containsString("Group is required")),
                        content().string(containsString("Classroom is required")),
                        content().string(containsString("Teacher is required")),
                        view().name("lesson/features/newLesson"),
                        model().attributeExists("edit"),
                        model().attributeExists("lessonDto"),
                        model().attributeExists("lessonsNum"),
                        model().attributeExists("teachers"),
                        model().attributeExists("classrooms"),
                        model().attributeExists("courses"),
                        model().attributeExists("groups"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void update_shouldReturnErrorMessage_whenLessonDateIsWeekendDay(String role) throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", testData.getLessonId().toString());
        params.add("date", testData.getDateOfMonday().with(DayOfWeek.SATURDAY).toString());
        params.add("lessonNum", testData.getLessenNum().toString());
        params.add("groupId", testData.getGroupId().toString());
        params.add("courseId", testData.getCourseId().toString());
        params.add("classroomId", testData.getClassroomId().toString());
        params.add("teacherId", testData.getTeacherId().toString());

        when(lessonService.getAvailableNumbersOfLessons(any(LocalDate.class), anyLong())).thenReturn(testItems.getNumbersOfLessons());
        when(lessonService.getAvailableTeachers(any(LocalDate.class), anyInt())).thenReturn(testItems.teacherNames());
        when(lessonService.getAvailableClassrooms(any(LocalDate.class), anyInt())).thenReturn(testItems.getClassrooms());
        when(groupService.getAll()).thenReturn(testItems.getGroups());
        when(courseService.getAll()).thenReturn(testItems.getCourses());

        mockMvc.perform(post("/lesson/update").params(params).with(testUser(role))).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().string(containsString("Cannot be weekend day")),
                        view().name("lesson/features/newLesson"),
                        model().attributeExists("edit"),
                        model().attributeExists("lessonDto"),
                        model().attributeExists("lessonsNum"),
                        model().attributeExists("teachers"),
                        model().attributeExists("classrooms"),
                        model().attributeExists("courses"),
                        model().attributeExists("groups"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff", "student", "teacher"})
    void details_shouldReturnLessonDetailsPage_whenUserRoleHasAccess(String role) throws Exception {
        when(lessonService.getById(anyLong())).thenReturn(testItems.getLesson());

        mockMvc.perform(get("/lesson/details-{lessonId}", testData.getLessonId()).with(testUser(role))).andDo(print())
                .andExpectAll(status().isOk(),
                        model().attributeExists("lesson"),
                        content().string(containsString(testData.getDateOfMonday().toString())),
                        content().string(containsString(testData.getLessenNum().toString())),
                        content().string(containsString(testData.getCourseName())),
                        content().string(containsString(testData.getCourseDescription())),
                        content().string(containsString(testData.getGroupName())),
                        content().string(containsString(testItems.getClassroomName())),
                        content().string(containsString(testData.getTeacherFirstName() + " " + testData.getTeacherLastName())),
                        view().name("lesson/features/details")
                );
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void lessonPerDay_shouldReturnLessonsListPerDay_whenUserRoleHasAccess(String role) throws Exception {
        when(localDate.getCurrentDate()).thenReturn(testData.getDateOfMonday());
        when(lessonService.getLessonsPerDay(any(LocalDate.class))).thenReturn(testItems.lessonsByDate(testData.getDateOfThursday()));

        mockMvc.perform(get("/lesson/lessons-per-day").param("date", testData.getDateOfThursday().toString()).with(testUser(role))).andDo(print())
                .andExpectAll(status().isOk(),
                        model().attributeExists("lessonPerDayPath"),
                        model().attributeExists("lessonByDatesPath"),
                        model().attributeExists("classtables"),
                        model().attributeExists("selectedDate"),
                        view().name("lesson/lesson"),
                        content().string(containsString(testData.getDateOfThursday().toString())),
                        content().string(containsString("/lessons-per-day")),
                        content().string(containsString("/lessons-between-dates")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void lessonPerDay_shouldReturnLessonsListPerToday_whenDateIsPast(String role) throws Exception {
        when(localDate.getCurrentDate()).thenReturn(testData.getDateOfFriday());
        when(lessonService.getLessonsPerDay(any(LocalDate.class))).thenReturn(testItems.lessonsByDate(testData.getDateOfFriday()));

        mockMvc.perform(get("/lesson/lessons-per-day")
                        .param("date", testData.getDateOfMonday().toString())
                        .with(testUser(role)))
                .andDo(print())
                .andExpectAll(status().isOk(),
                        model().attributeExists("lessonPerDayPath"),
                        model().attributeExists("lessonByDatesPath"),
                        model().attributeExists("classtables"),
                        model().attributeExists("selectedDate"),
                        view().name("lesson/lesson"),
                        content().string(containsString(testData.getDateOfFriday().toString())),
                        content().string(containsString("/lessons-per-day")),
                        content().string(containsString("/lessons-between-dates")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void lessonPerDay_shouldReturnLessonsListPerToday_whenDateIsEmpty(String role) throws Exception {
        when(localDate.getCurrentDate()).thenReturn(testData.getDateOfMonday());
        when(lessonService.getLessonsPerDay(any(LocalDate.class))).thenReturn(testItems.lessonsByDate(testData.getDateOfMonday()));
        mockMvc.perform(get("/lesson/lessons-per-day")
                        .param("date", "")
                        .with(testUser(role))).andDo(print())
                .andExpectAll(status().isOk(),
                        model().attributeExists("lessonPerDayPath"),
                        model().attributeExists("lessonByDatesPath"),
                        model().attributeExists("classtables"),
                        model().attributeExists("selectedDate"),
                        view().name("lesson/lesson"),
                        content().string(containsString(testData.getDateOfMonday().toString())),
                        content().string(containsString("/lessons-per-day")),
                        content().string(containsString("/lessons-between-dates")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"student", "teacher"})
    void lessonPerDay_shouldReturnForbiddenStatus_whenUserRoleLackAccess(String role) throws Exception {
        when(localDate.getCurrentDate()).thenReturn(testData.getDateOfMonday());
        mockMvc.perform(get("/lesson/lessons-per-day").param("date", testData.getDateOfMonday().toString()).with(testUser(role))).andDo(print())
                .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {"student", "admin", "staff"})
    void lessonPerDayByGroupId_shouldReturnLessonsListOfGroup_whenUserRoleHasAccess(String role) throws Exception {
        when(localDate.getCurrentDate()).thenReturn(testData.getDateOfMonday());
        when(lessonService.getLessonsByGroupIdPerDay(anyLong(), any(LocalDate.class))).thenReturn(testItems.groupLessonsPerDate(testData.getDateOfThursday()));

        mockMvc.perform(get("/lesson/lessons-by-group-{id}", testData.getGroupId())
                        .param("date", testData.getDateOfThursday().toString())
                        .with(testUser(role))).andDo(print())
                .andExpectAll(status().isOk(),
                        model().attributeExists("lessonPerDayPath"),
                        model().attributeExists("lessonByDatesPath"),
                        model().attributeExists("classtables"),
                        model().attributeExists("selectedDate"),
                        view().name("lesson/lesson"),
                        content().string(containsString(testData.getDateOfThursday().toString())),
                        content().string(containsString("/lessons-by-group-" + testData.getGroupId())),
                        content().string(containsString("/lessons-between-dates/group-" + testData.getGroupId())));
    }

    @ParameterizedTest
    @ValueSource(strings = {"student", "admin", "staff"})
    void lessonPerDayByGroupId_shouldReturnLessonsListOfGroupToday_whenDateIsPast(String role) throws Exception {
        LocalDate date = testData.getDateOfThursday();
        when(localDate.getCurrentDate()).thenReturn(date);

        when(lessonService.getLessonsByGroupIdPerDay(anyLong(), any(LocalDate.class))).thenReturn(testItems.groupLessonsPerDate(date));

        mockMvc.perform(get("/lesson/lessons-by-group-{id}", testData.getGroupId())
                        .param("date", testData.getDateOfMonday().toString())
                        .with(testUser(role))).andDo(print())
                .andExpectAll(status().isOk(),
                        model().attributeExists("lessonPerDayPath"),
                        model().attributeExists("lessonByDatesPath"),
                        model().attributeExists("classtables"),
                        model().attributeExists("selectedDate"),
                        view().name("lesson/lesson"),
                        content().string(containsString(date.toString())),
                        content().string(containsString("/lessons-by-group-" + testData.getGroupId())),
                        content().string(containsString("/lessons-between-dates/group-" + testData.getGroupId())));
    }

    @ParameterizedTest
    @ValueSource(strings = {"student", "admin", "staff"})
    void lessonPerDayByGroupId_shouldReturnLessonsListOfGroupToday_whenDateIsEmpty(String role) throws Exception {
        LocalDate date = testData.getDateOfThursday();
        when(localDate.getCurrentDate()).thenReturn(date);

        when(lessonService.getLessonsByGroupIdPerDay(anyLong(), any(LocalDate.class))).thenReturn(testItems.groupLessonsPerDate(date));

        mockMvc.perform(get("/lesson/lessons-by-group-{id}", testData.getGroupId())
                        .param("date", "")
                        .with(testUser(role))).andDo(print())
                .andExpectAll(status().isOk(),
                        model().attributeExists("lessonPerDayPath"),
                        model().attributeExists("lessonByDatesPath"),
                        model().attributeExists("classtables"),
                        model().attributeExists("selectedDate"),
                        view().name("lesson/lesson"),
                        content().string(containsString(date.toString())),
                        content().string(containsString("/lessons-by-group-" + testData.getGroupId())),
                        content().string(containsString("/lessons-between-dates/group-" + testData.getGroupId())));
    }

    @Test
    void lessonPerDayByGroupId_shouldReturnForbiddenStatus_whenUserRoleIsTeacher() throws Exception {
        mockMvc.perform(get("/lesson/lessons-by-group-{id}", testData.getGroupId())
                        .param("date", testData.getDateOfMonday().toString())
                        .with(testUser("teacher"))).andDo(print())
                .andExpect(status().isForbidden());
    }


    @ParameterizedTest
    @ValueSource(strings = {"teacher", "admin", "staff"})
    void lessonPerDayByTeacherId_shouldReturnLessonsListOfTeacher_whenUserRoleHasAccess(String role) throws Exception {
        LocalDate date = testData.getDateOfMonday();
        when(localDate.getCurrentDate()).thenReturn(date);
        when(lessonService.getLessonsByGroupIdPerDay(anyLong(), any(LocalDate.class))).thenReturn(testItems.teacherLessonsPerDate(date));

        mockMvc.perform(get("/lesson/lessons-by-teacher-{id}", testData.getTeacherId())
                        .param("date", date.toString())
                        .with(testUser(role))).andDo(print())
                .andExpectAll(status().isOk(),
                        model().attributeExists("lessonPerDayPath"),
                        model().attributeExists("lessonByDatesPath"),
                        model().attributeExists("classtables"),
                        model().attributeExists("selectedDate"),
                        view().name("lesson/lesson"),
                        content().string(containsString(date.toString())),
                        content().string(containsString("/lessons-by-teacher-" + testData.getTeacherId())),
                        content().string(containsString("/lessons-between-dates/teacher-" + testData.getTeacherId())));
    }

    @ParameterizedTest
    @ValueSource(strings = {"teacher", "admin", "staff"})
    void lessonPerDayByTeacherId_shouldReturnLessonsListOfTeacherToday_whenDateIsPast(String role) throws Exception {
        LocalDate date = testData.getDateOfThursday();
        when(localDate.getCurrentDate()).thenReturn(date);
        when(lessonService.getLessonsByGroupIdPerDay(anyLong(), any(LocalDate.class))).thenReturn(testItems.teacherLessonsPerDate(date));

        mockMvc.perform(get("/lesson/lessons-by-teacher-{id}", testData.getTeacherId())
                        .param("date", testData.getDateOfMonday().toString())
                        .with(testUser(role))).andDo(print())
                .andExpectAll(status().isOk(),
                        model().attributeExists("lessonPerDayPath"),
                        model().attributeExists("lessonByDatesPath"),
                        model().attributeExists("classtables"),
                        model().attributeExists("selectedDate"),
                        view().name("lesson/lesson"),
                        content().string(containsString(date.toString())),
                        content().string(containsString("/lessons-by-teacher-" + testData.getTeacherId())),
                        content().string(containsString("/lessons-between-dates/teacher-" + testData.getTeacherId())));
    }

    @ParameterizedTest
    @ValueSource(strings = {"teacher", "admin", "staff"})
    void lessonPerDayByTeacherId_shouldReturnLessonsListOfTeacherToday_whenDateIsEmpty(String role) throws Exception {
        LocalDate date = testData.getDateOfThursday();
        when(localDate.getCurrentDate()).thenReturn(date);
        when(lessonService.getLessonsByGroupIdPerDay(anyLong(), any(LocalDate.class))).thenReturn(testItems.teacherLessonsPerDate(date));

        mockMvc.perform(get("/lesson/lessons-by-teacher-{id}", testData.getTeacherId())
                        .param("date", "")
                        .with(testUser(role))).andDo(print())
                .andExpectAll(status().isOk(),
                        model().attributeExists("lessonPerDayPath"),
                        model().attributeExists("lessonByDatesPath"),
                        model().attributeExists("classtables"),
                        model().attributeExists("selectedDate"),
                        view().name("lesson/lesson"),
                        content().string(containsString(date.toString())),
                        content().string(containsString("/lessons-by-teacher-" + testData.getTeacherId())),
                        content().string(containsString("/lessons-between-dates/teacher-" + testData.getTeacherId())));
    }

    @Test
    void lessonPerDayByTeacherId_shouldReturnForbiddenStatus_whenUserRoleIsStudent() throws Exception {
        mockMvc.perform(get("/lesson/lessons-by-teacher-{id}", testData.getTeacherId())
                        .param("date", testData.getDateOfMonday().toString())
                        .with(testUser("student"))).andDo(print())
                .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void lessonsBetweenDates_shouldReturnLessonList_whenUserRoleHasAccess(String role) throws Exception {
        when(localDate.getCurrentDate()).thenReturn(testData.getDateOfMonday());
        when(lessonService.getLessonsBetweenDates(any(LocalDate.class), any(LocalDate.class))).thenReturn(testItems.lessonsBetweenDates(testData.getDateOfTuesday(), testData.getDateOfFriday()));

        mockMvc.perform(get("/lesson/lessons-between-dates")
                        .param("start", testData.getDateOfTuesday().toString())
                        .param("end", testData.getDateOfFriday().toString())
                        .with(testUser(role))).andDo(print())
                .andExpectAll(status().isOk(),
                        model().attributeExists("lessonPerDayPath"),
                        model().attributeExists("lessonByDatesPath"),
                        view().name("lesson/lesson"),
                        content().string(containsString("/lessons-per-day")),
                        content().string(containsString("/lessons-between-dates")),
                        content().string(containsString(testData.getDateOfTuesday().toString())),
                        content().string(containsString(testData.getDateOfFriday().toString())));
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void lessonsBetweenDates_shouldReturnLessonList_whenStartDateIsPast(String role) throws Exception {
        when(localDate.getCurrentDate()).thenReturn(testData.getDateOfThursday());
        when(lessonService.getLessonsBetweenDates(any(LocalDate.class), any(LocalDate.class))).thenReturn(testItems.lessonsBetweenDates(testData.getDateOfMonday(), testData.getDateOfFriday()));

        mockMvc.perform(get("/lesson/lessons-between-dates")
                        .param("start", testData.getDateOfTuesday().toString())
                        .param("end", testData.getDateOfFriday().toString())
                        .with(testUser(role))).andDo(print())
                .andExpectAll(status().isOk(),
                        model().attributeExists("lessonPerDayPath"),
                        model().attributeExists("lessonByDatesPath"),
                        view().name("lesson/lesson"),
                        content().string(containsString("/lessons-per-day")),
                        content().string(containsString("/lessons-between-dates")),
                        content().string(containsString(testData.getDateOfMonday().toString())),
                        content().string(containsString(testData.getDateOfFriday().toString())));
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void lessonsBetweenDates_shouldReturnLessonList_whenDatesAreEmpty(String role) throws Exception {
        when(localDate.getCurrentDate()).thenReturn(testData.getDateOfThursday());
        when(lessonService.getLessonsBetweenDates(any(LocalDate.class), any(LocalDate.class))).thenReturn(testItems.lessonsBetweenDates(testData.getDateOfMonday(), testData.getDateOfFriday()));

        mockMvc.perform(get("/lesson/lessons-between-dates")
                        .param("start", "")
                        .param("end", "")
                        .with(testUser(role))).andDo(print())
                .andExpectAll(status().isOk(),
                        model().attributeExists("lessonPerDayPath"),
                        model().attributeExists("lessonByDatesPath"),
                        view().name("lesson/lesson"),
                        content().string(containsString("/lessons-per-day")),
                        content().string(containsString("/lessons-between-dates")),
                        content().string(containsString(testData.getDateOfMonday().toString())),
                        content().string(containsString(testData.getDateOfFriday().toString())));
    }

    @ParameterizedTest
    @ValueSource(strings = {"student", "teacher"})
    void lessonsBetweenDates_shouldReturnLessonList_whenUserRoleLackAccess(String role) throws Exception {
        mockMvc.perform(get("/lesson/lessons-between-dates")
                        .param("start", testItems.getDateOfMonday().toString())
                        .param("end", testItems.getDateOfFriday().toString())
                        .with(testUser(role))).andDo(print())
                .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {"student", "admin", "staff"})
    void lessonBetweenDatesByGroupId_shouldReturnLessonListOfGroupBetweenDates_whenUserRoleHasAccess(String role) throws Exception {
        when(localDate.getCurrentDate()).thenReturn(testData.getDateOfThursday());
        when(lessonService.getLessonsByGroupIdBetweenDates(anyLong(), any(LocalDate.class), any(LocalDate.class))).thenReturn(testItems.groupLessonsBetweenDates(testData.getDateOfTuesday(), testData.getDateOfFriday()));

        mockMvc.perform(get("/lesson/lessons-between-dates/group-{id}", testData.getGroupId())
                        .param("start", testData.getDateOfTuesday().toString())
                        .param("end", testData.getDateOfFriday().toString())
                        .with(testUser(role)))
                .andDo(print()).andExpectAll(
                        status().isOk(),
                        model().attributeExists("lessonPerDayPath"),
                        model().attributeExists("lessonByDatesPath"),
                        model().attributeExists("classtables"),
                        view().name("lesson/lesson"),
                        content().string(containsString("/lessons-by-group-" + testData.getGroupId())),
                        content().string(containsString("/lessons-between-dates/group-" + testData.getGroupId())),
                        content().string(containsString(testData.getDateOfTuesday().toString())),
                        content().string(containsString(testData.getDateOfFriday().toString())));
    }

    @ParameterizedTest
    @ValueSource(strings = {"student", "admin", "staff"})
    void lessonBetweenDatesByGroupId_shouldReturnLessonListOfGroupBetweenDates_whenStartDateIsPast(String role) throws Exception {
        when(localDate.getCurrentDate()).thenReturn(testData.getDateOfThursday());
        when(lessonService.getLessonsByGroupIdBetweenDates(anyLong(), any(LocalDate.class), any(LocalDate.class))).thenReturn(testItems.groupLessonsBetweenDates(testData.getDateOfMonday(), testData.getDateOfFriday()));

        mockMvc.perform(get("/lesson/lessons-between-dates/group-{id}", testData.getGroupId())
                        .param("start", testData.getDateOfTuesday().toString())
                        .param("end", testData.getDateOfFriday().toString())
                        .with(testUser(role)))
                .andDo(print()).andExpectAll(
                        status().isOk(),
                        model().attributeExists("lessonPerDayPath"),
                        model().attributeExists("lessonByDatesPath"),
                        model().attributeExists("classtables"),
                        view().name("lesson/lesson"),
                        content().string(containsString("/lessons-by-group-" + testData.getGroupId())),
                        content().string(containsString("/lessons-between-dates/group-" + testData.getGroupId())),
                        content().string(containsString(testData.getDateOfMonday().toString())),
                        content().string(containsString(testData.getDateOfFriday().toString())));
    }

    @ParameterizedTest
    @ValueSource(strings = {"student", "admin", "staff"})
    void lessonBetweenDatesByGroupId_shouldReturnLessonListOfGroupBetweenDates_whenDatesAreEmpty(String role) throws Exception {
        when(localDate.getCurrentDate()).thenReturn(testData.getDateOfThursday());
        when(lessonService.getLessonsByGroupIdBetweenDates(anyLong(), any(LocalDate.class), any(LocalDate.class))).thenReturn(testItems.groupLessonsBetweenDates(testData.getDateOfMonday(), testData.getDateOfFriday()));

        mockMvc.perform(get("/lesson/lessons-between-dates/group-{id}", testData.getGroupId())
                        .param("start", "")
                        .param("end", "")
                        .with(testUser(role)))
                .andDo(print()).andExpectAll(
                        status().isOk(),
                        model().attributeExists("lessonPerDayPath"),
                        model().attributeExists("lessonByDatesPath"),
                        model().attributeExists("classtables"),
                        view().name("lesson/lesson"),
                        content().string(containsString("/lessons-by-group-" + testData.getGroupId())),
                        content().string(containsString("/lessons-between-dates/group-" + testData.getGroupId())),
                        content().string(containsString(testData.getDateOfMonday().toString())),
                        content().string(containsString(testData.getDateOfFriday().toString())));
    }

    @Test
    void lessonBetweenDatesByGroupId_shouldReturnForbiddenStatus_whenUserRoleIsTeacher() throws Exception {
        mockMvc.perform(get("/lesson/lessons-between-dates/group-{id}", testData.getGroupId())
                        .param("start", testItems.getDateOfMonday().toString())
                        .param("end", testItems.getDateOfFriday().toString())
                        .with(testUser("teacher")))
                .andDo(print()).andExpect(status().isForbidden());
    }


    @ParameterizedTest
    @ValueSource(strings = {"teacher", "admin", "staff"})
    void lessonBetweenDatesByTeacherId_shouldReturnLessonListOfTeacherBetweenDates_whenUserRoleHasAccess(String role) throws Exception {
        when(localDate.getCurrentDate()).thenReturn(testData.getDateOfThursday());
        when(lessonService.getLessonsByTeacherIdBetweenDates(anyLong(), any(LocalDate.class), any(LocalDate.class))).thenReturn(testItems.teacherLessonsBetweenDates(testData.getDateOfTuesday(), testData.getDateOfFriday()));

        mockMvc.perform(get("/lesson/lessons-between-dates/teacher-{id}", testData.getTeacherId())
                        .param("start", testData.getDateOfTuesday().toString())
                        .param("end", testData.getDateOfFriday().toString())
                        .with(testUser(role)))
                .andDo(print()).andExpectAll(
                        status().isOk(),
                        model().attributeExists("lessonPerDayPath"),
                        model().attributeExists("lessonByDatesPath"),
                        model().attributeExists("classtables"),
                        view().name("lesson/lesson"),
                        content().string(containsString("/lessons-by-teacher-" + testData.getTeacherId())),
                        content().string(containsString("/lessons-between-dates/teacher-" + testData.getTeacherId())),
                        content().string(containsString(testData.getDateOfMonday().toString())),
                        content().string(containsString(testData.getDateOfFriday().toString())));
    }

    @ParameterizedTest
    @ValueSource(strings = {"teacher", "admin", "staff"})
    void lessonBetweenDatesByTeacherId_shouldReturnLessonListOfTeacherBetweenDates_whenStartDateIsPast(String role) throws Exception {
        when(localDate.getCurrentDate()).thenReturn(testData.getDateOfThursday());
        when(lessonService.getLessonsByTeacherIdBetweenDates(anyLong(), any(LocalDate.class), any(LocalDate.class))).thenReturn(testItems.teacherLessonsBetweenDates(testData.getDateOfMonday(), testData.getDateOfFriday()));

        mockMvc.perform(get("/lesson/lessons-between-dates/teacher-{id}", testData.getTeacherId())
                        .param("start", testData.getDateOfWednesday().toString())
                        .param("end", testData.getDateOfFriday().toString())
                        .with(testUser(role)))
                .andDo(print()).andExpectAll(
                        status().isOk(),
                        model().attributeExists("lessonPerDayPath"),
                        model().attributeExists("lessonByDatesPath"),
                        model().attributeExists("classtables"),
                        view().name("lesson/lesson"),
                        content().string(containsString("/lessons-by-teacher-" + testData.getTeacherId())),
                        content().string(containsString("/lessons-between-dates/teacher-" + testData.getTeacherId())),
                        content().string(containsString(testData.getDateOfMonday().toString())),
                        content().string(containsString(testData.getDateOfFriday().toString())));
    }

    @ParameterizedTest
    @ValueSource(strings = {"teacher", "admin", "staff"})
    void lessonBetweenDatesByTeacherId_shouldReturnLessonListOfTeacherBetweenDates_whenDatesAreEmpty(String role) throws Exception {
        when(localDate.getCurrentDate()).thenReturn(testData.getDateOfThursday());
        when(lessonService.getLessonsByTeacherIdBetweenDates(anyLong(), any(LocalDate.class), any(LocalDate.class))).thenReturn(testItems.teacherLessonsBetweenDates(testData.getDateOfMonday(), testData.getDateOfFriday()));

        mockMvc.perform(get("/lesson/lessons-between-dates/teacher-{id}", testData.getTeacherId())
                        .param("start", "")
                        .param("end", "")
                        .with(testUser(role)))
                .andDo(print()).andExpectAll(
                        status().isOk(),
                        model().attributeExists("lessonPerDayPath"),
                        model().attributeExists("lessonByDatesPath"),
                        model().attributeExists("classtables"),
                        view().name("lesson/lesson"),
                        content().string(containsString("/lessons-by-teacher-" + testData.getTeacherId())),
                        content().string(containsString("/lessons-between-dates/teacher-" + testData.getTeacherId())),
                        content().string(containsString(testData.getDateOfMonday().toString())),
                        content().string(containsString(testData.getDateOfFriday().toString())));
    }

    @Test
    void lessonBetweenDatesByTeacherId_shouldReturnForbiddenStatus_whenUserRoleIsStudent() throws Exception {
        mockMvc.perform(get("/lesson/lessons-between-dates/teacher-{id}", testData.getTeacherId())
                        .param("start", testItems.getDateOfMonday().toString())
                        .param("end", testItems.getDateOfFriday().toString())
                        .with(testUser("student")))
                .andDo(print()).andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void delete_shouldRedirectToLessonPage_whenUserRoleHasAccess(String role) throws Exception {
        mockMvc.perform(post("/lesson/delete-{id}", testData.getLessonId()).with(testUser(role))).andDo(print())
                .andExpectAll(status().is3xxRedirection(),
                        redirectedUrl("/lesson"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"student", "teacher"})
    void delete_shouldRedirectToLessonPage_whenUserRoleLackAccess(String role) throws Exception {
        mockMvc.perform(post("/lesson/delete-{id}", testData.getLessonId()).with(testUser(role))).andDo(print())
                .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void fetchLessonNum_shouldReturnAvailableLessonNumbers(String role) throws Exception {
        when(lessonService.getAvailableNumbersOfLessons(any(LocalDate.class), anyLong())).thenReturn(testItems.getNumbersOfLessons());

        mockMvc.perform(get("/lesson/fetch-lessenNum")
                        .param("date", testData.getDateOfMonday().toString())
                        .param("groupId", testData.getGroupId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(testUser(role)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(1))
                .andExpect(jsonPath("$[1]").value(2))
                .andExpect(jsonPath("$[2]").value(3))
                .andExpect(jsonPath("$[3]").value(4))
                .andExpect(jsonPath("$[4]").value(5))
                .andExpect(jsonPath("$[5]").value(6))
                .andExpect(jsonPath("$[6]").value(7))
                .andExpect(jsonPath("$[7]").value(8))
        ;
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void fetchTeacher_shouldReturnAvailableTeachers(String role) throws Exception {
        when(lessonService.getAvailableTeachers(any(LocalDate.class), any(Integer.class))).thenReturn(Collections.singletonList(testItems.getTeacherNameDto()));

        mockMvc.perform(get("/lesson/fetch-teachers")
                        .param("date", testData.getDateOfMonday().toString())
                        .param("lessonNum", testData.getLessenNum().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(testUser(role)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].firstName").value(testData.getTeacherFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(testData.getTeacherLastName()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "staff"})
    void fetchClassrooms_shouldReturnAvailableClassrooms(String role) throws Exception {
        when(lessonService.getAvailableClassrooms(any(LocalDate.class), any(Integer.class))).thenReturn(Collections.singletonList(testItems.getClassroom()));

        mockMvc.perform(get("/lesson/fetch-classrooms")
                        .param("date", testData.getDateOfMonday().toString())
                        .param("lessonNum", testData.getLessenNum().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(testUser(role)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testItems.getClassroomId()))
                .andExpect(jsonPath("$[0].classroomName").value(testItems.getClassroomName()));
    }
}
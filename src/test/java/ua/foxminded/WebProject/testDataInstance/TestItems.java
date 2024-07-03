package ua.foxminded.WebProject.testDataInstance;

import lombok.Getter;
import ua.foxminded.WebProject.DTO.*;
import ua.foxminded.WebProject.form.ChangePasswordForm;
import ua.foxminded.WebProject.form.UserRegistrationForm;
import ua.foxminded.WebProject.persistence.entity.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@Getter
public class TestItems {

    private final String password = "password";

    private final TestData testData = new TestData();
    private final Lessons lessons = new Lessons();

    private final Long classroomId = 3L;
    private final String classroomName = "Room 103";
    private final Classroom classroom = new Classroom(classroomId, classroomName);

    private final Long courseId = 7L;
    private final String courseName = "Principles of Marketing";
    private final String courseDescription = "Learn the basics of marketing";
    private final Course courseWithIdSeven = new Course(getCourseId(), getCourseName(), getCourseDescription());
    private final Course course = new Course(testData.getCourseId(), testData.getCourseName(), testData.getCourseDescription());
    private final Course secondCourse = new Course(testData.getCourseId(), testData.getSecondCourseName(), testData.getSecondCourseDescription());

    private final Group group = new Group(testData.getGroupId(), testData.getGroupName());
    private final Group secondGroup = new Group(testData.getSecondGroupId(), testData.getSecondGroupName());

    private final Long teacherId = 6L;
    private final String teacherFirstName = "Jessica";
    private final String teacherLastName = "Brown";

    private final Lesson lesson = new Lesson(testData.getLessonId(), testData.getDate(), testData.getLessenNum(), getCourseWithIdSeven(), getGroup(), getClassroom(), getFullyTeacher());
    private final LessonDto lessonDto = new LessonDto(new Course(getCourseId()), new Group(testData.getGroupId()), new Classroom(getClassroomId()), new Teacher(getTeacherId()));

    private final User savedStudent = new Student(testData.getStudentId(), testData.getStudentFirstName(), testData.getStudentLastName(), null);

    public User getCompliteUser() {
        User user1 = new User();
        user1.setId(testData.getUserId());
        user1.setFirstName(testData.getUserFirstName());
        user1.setLastName(testData.getUserLastName());
        user1.setEmail(testData.getUserEmail());
        user1.setPassword(getEncodedPassword());
        return user1;
    }

    public ChangePasswordForm getChangePasswordForm() {
        ChangePasswordForm form = new ChangePasswordForm();
        form.setOldPassword(getEncodedPassword2());
        form.setPassword(getPassword());
        form.setConfirmPassword(getPassword());
        return form;
    }

    private final UserDto userDetails = new UserDto(testData.getUserId(), testData.getUserFirstName(), testData.getUserLastName(), testData.getUserEmail());

    private final String encodedPassword = "thisIsEncodedPassword";
    private final String encodedPassword2 = "thisIsEncodedPassword2";

    private final StudentGroupDto studentGroupDto = new StudentGroupDto(testData.getStudentId(), testData.getGroupId(), testData.getGroupName());
    private final StudentGroupDto studentGroupDto2 = new StudentGroupDto(testData.getStudentId(), testData.getSecondGroupId(), testData.getSecondGroupName());

    private final LocalDate dateOfMonday = testData.getDate().with(DayOfWeek.MONDAY);

    private final LocalDate dateOfFriday = testData.getDate().with(DayOfWeek.FRIDAY);

    public UserRegistrationForm userRegistrationForm() {
        UserRegistrationForm userRegistrationForm = new UserRegistrationForm();
        userRegistrationForm.setFirstName(testData.getUserFirstName());
        userRegistrationForm.setLastName(testData.getUserLastName());
        userRegistrationForm.setEmail(testData.getUserEmail());
        userRegistrationForm.setPassword(getPassword());
        return userRegistrationForm;
    }

    private final List<Student> students = Arrays.asList(
            new Student(1L, "Liam", "Jones", getGroup()),
            new Student(2L, "Harper", "Robinson", getGroup()),
            new Student(3L, "Lucas", "Thompson", getGroup()),
            new Student(4L, "Carter", "Clark", getGroup()),
            new Student(5L, "Amelia", "Martinez", getGroup()));

    private final List<Classroom> classrooms = Arrays.asList(new Classroom(1L, "Room 101"),
            new Classroom(2L, "Room 102"),
            getClassroom(),
            new Classroom(4L, "Room 104"));

    private final List<Course> courses = Arrays.asList(
            getCourse(),
            new Course(2L, "World History: Ancient Civilizations", "Explore the history of ancient civilizations"),
            new Course(3L, "Creative Writing Workshop", "Develop your writing skills in a creative environment")
    );

    private final List<Group> groups = Arrays.asList(
            getGroup(),
            getSecondGroup(),
            new Group(3L, "QM-09"));

    public Lesson getParticipants() {
        Lesson lesson1 = getLesson();
        lesson1.getGroup().setStudents(new HashSet<>(getStudents()));
        return lesson1;
    }

    private final List<Teacher> teachers = Arrays.asList(
            getFullyTeacher(),
            new Teacher(2L, "Emily", "Johnson"),
            new Teacher(3L, "Michael", "Davis"),
            new Teacher(4L, "Sarah", "Wilson"),
            new Teacher(5L, "Robert", "Taylor"),
            new Teacher(getTeacherId(), getTeacherFirstName(), getTeacherLastName()),
            new Teacher(7L, "William", "Moore"),
            new Teacher(8L, "Olivia", "Anderson")
    );

    private final List<Lesson> lessonsPerDay = lessons.getLessons().stream().filter(l -> l.getDate().isEqual(testData.getDate())).toList();

    private final List<Lesson> allLessons = lessons.getLessons().stream().toList();

    private final List<Lesson> allLessonsOfGroupPerWeek = lessons.getLessons()
            .stream().filter(l -> l.getGroup().getId().equals(getGroup().getId())).toList();
    private final List<Lesson> allLessonsOfGroupPerDay = getAllLessonsOfGroupPerWeek()
            .stream().filter(l -> l.getDate().equals(dateOfMonday)).toList();

    private final List<Lesson> allLessonsOfGroupPerWeekWithoutCourse = getAllLessonsOfGroupPerWeek()
            .stream().filter(l -> !l.getCourse().getId().equals(getCourseId())).toList();
    private final List<Lesson> allLessonsOfGroupPerDayWithoutCourse = getAllLessonsOfGroupPerDay()
            .stream().filter(l -> !l.getCourse().getId().equals(getCourseId())).toList();

    private final List<Lesson> teacherLessonsPerWeek = lessons.getLessons().stream()
            .filter(l -> l.getTeacher().getId().equals(getFullyTeacher().getId())).toList();
    private final List<Lesson> teacherLessonsPerDay = getTeacherLessonsPerWeek().stream()
            .filter(l -> l.getDate().isEqual(testData.getDate())).toList();

    public Student getFullyStudent() {
        Student student1 = new Student();
        student1.setId(testData.getStudentId());
        student1.setFirstName(testData.getStudentFirstName());
        student1.setLastName(testData.getStudentLastName());
        student1.setPassword(getEncodedPassword());
        student1.setEmail(testData.getStudentEmail());
        student1.setGroup(getGroup());
        return student1;
    }

    public Admin getFullyAdmin() {
        Admin admin1 = new Admin();
        admin1.setId(testData.getAdminId());
        admin1.setFirstName(testData.getAdminFirstName());
        admin1.setLastName(testData.getAdminLastName());
        admin1.setEmail(testData.getAdminEmail());
        admin1.setPassword(getEncodedPassword());
        return admin1;
    }

    public Staff getFullyStaff() {
        Staff staff1 = new Staff();
        staff1.setId(testData.getStaffId());
        staff1.setFirstName(testData.getStaffFirstName());
        staff1.setLastName(testData.getStaffLastName());
        staff1.setEmail(testData.getStaffEmail());
        staff1.setPassword(getEncodedPassword());
        return staff1;
    }

    public Teacher getFullyTeacher() {
        Teacher teacher1 = new Teacher();
        teacher1.setId(testData.getTeacherId());
        teacher1.setFirstName(testData.getTeacherFirstName());
        teacher1.setLastName(testData.getTeacherLastName());
        teacher1.setEmail(testData.getTeacherEmail());
        teacher1.setPassword(getEncodedPassword());
        return teacher1;
    }
}

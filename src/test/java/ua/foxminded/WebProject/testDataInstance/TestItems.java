package ua.foxminded.WebProject.testDataInstance;

import lombok.Getter;
import ua.foxminded.WebProject.DTO.LessonDto;
import ua.foxminded.WebProject.DTO.NamesDto;
import ua.foxminded.WebProject.DTO.StudentGroupDto;
import ua.foxminded.WebProject.DTO.UserDto;
import ua.foxminded.WebProject.form.ChangePasswordForm;
import ua.foxminded.WebProject.form.UserRegistrationForm;
import ua.foxminded.WebProject.persistence.entity.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class TestItems {

    private final String password = "password";

    private static final TestData testData = new TestData();
    private static final Lessons lessons = new Lessons();

    private final Long classroomId = 3L;
    private final String classroomName = "Room 103";
    private final Classroom classroom = new Classroom(classroomId, classroomName);
    private final Classroom firstClassroom = new Classroom(testData.getClassroomId(), testData.getClassroomName());

    private final Long courseId = 7L;
    private final String courseName = "Principles of Marketing";
    private final String courseDescription = "Learn the basics of marketing";
    private final Course courseWithIdSeven = new Course(getCourseId(), getCourseName(), getCourseDescription());
    private final Course course = new Course(testData.getCourseId(), testData.getCourseName(), testData.getCourseDescription());
    private final Course secondCourse = new Course(testData.getCourseId(), testData.getSecondCourseName(), testData.getSecondCourseDescription());
    private final Course secondCourse2 = new Course(testData.getSecondCourseId(), testData.getSecondCourseName(), testData.getSecondCourseDescription());

    private final Group group = new Group(testData.getGroupId(), testData.getGroupName());
    private final Group secondGroup = new Group(testData.getSecondGroupId(), testData.getSecondGroupName());

    private final Long teacherId = 6L;
    private final String teacherFirstName = "Jessica";
    private final String teacherLastName = "Brown";

    private final Lesson lesson = new Lesson(testData.getLessonId(), testData.getDateOfMonday(), testData.getLessenNum(), getCourse(), getGroup(), getClassroom(), getFullyTeacher());
    private final LessonDto newLesson = new LessonDto(null, testData.getDateOfMonday(), testData.getLessenNum(), testData.getCourseId(), testData.getGroupId(), testData.getClassroomId(), testData.getTeacherId());
    private final LessonDto lessonDto = new LessonDto(testData.getLessonId(), testData.getDateOfMonday(), testData.getLessenNum(), testData.getCourseId(), testData.getGroupId(), testData.getClassroomId(), testData.getTeacherId());
    private final LessonDto lessonDtoUpdated = new LessonDto(testData.getLessonId(), testData.getDateOfMonday(), testData.getLessenNum(), testData.getSecondCourseId(), testData.getSecondGroupId(), testData.getClassroomId(), testData.getTeacherId());

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

    public UserDto userDto(User user) {
        return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }

    private final String encodedPassword = "thisIsEncodedPassword";
    private final String encodedPassword2 = "thisIsEncodedPassword2";

    private final StudentGroupDto studentGroupDto = new StudentGroupDto(testData.getStudentId(), testData.getGroupId(), testData.getGroupName());
    private final StudentGroupDto studentGroupDto2 = new StudentGroupDto(testData.getStudentId(), testData.getSecondGroupId(), testData.getSecondGroupName());

    private final LocalDate dateOfMonday = testData.getDateOfMonday().with(DayOfWeek.MONDAY);

    private final LocalDate dateOfFriday = testData.getDateOfMonday().with(DayOfWeek.FRIDAY);

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

    public List<NamesDto> teacherNames() {
        List<NamesDto> teacherDtos = new ArrayList<>();
        teachers.forEach(teacher -> teacherDtos.add(new NamesDto(teacher.getId(), teacher.getFirstName(), teacher.getLastName())));
        return teacherDtos;
    }

    public List<UserDto> getStudentNames() {
        List<UserDto> studentDtos = new ArrayList<>();
        students.forEach(student -> studentDtos.add(new UserDto(student.getId(), student.getFirstName(), student.getLastName(), student.getEmail())));
        return studentDtos;
    }

    private final List<Lesson> allLessons = lessons.getLessons().stream().toList();

    public List<Lesson> lessonsByDate(LocalDate date) {
        return lessons.getLessons().stream().filter(l -> l.getDate().isEqual(date)).toList();
    }

    public List<Lesson> lessonsBetweenDates(LocalDate start, LocalDate end) {
        return lessons.getLessons().stream()
                .filter(l -> !l.getDate().isBefore(start) && !l.getDate().isAfter(end)).toList();
    }

    private final List<Lesson> groupLessons = lessons.getLessons()
            .stream().filter(l -> l.getGroup().getId().equals(getGroup().getId())).toList();

    public List<Lesson> groupLessonsPerDate(LocalDate date) {
        return getGroupLessons().stream().filter(l -> l.getDate().equals(date)).toList();
    }

    public List<Lesson> groupLessonsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return getGroupLessons().stream()
                .filter(l -> !l.getDate().isBefore(startDate) && !l.getDate().isAfter(endDate)).toList();
    }

    private final List<Lesson> teacherLessons = lessons.getLessons().stream()
            .filter(l -> l.getTeacher().getId().equals(testData.getTeacherId())).toList();

    public List<Lesson> teacherLessonsPerDate(LocalDate date) {
        return getTeacherLessons().stream()
                .filter(l -> l.getDate().isEqual(date)).toList();
    }

    public List<Lesson> teacherLessonsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return teacherLessons.stream()
                .filter(l -> !l.getDate().isBefore(startDate) && !l.getDate().isAfter(endDate)).toList();
    }

    public List<Lesson> groupLessonWithCourse() {
        List<Lesson> list = groupLessons.stream().filter(l -> l.getDate().isEqual(testData.getDateOfTuesday())).toList();
        return Arrays.asList(list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6), list.get(7));
    }

    private final List<Lesson> allLessonsOfGroupPerWeekWithoutCourse = getGroupLessons()
            .stream().filter(l -> !l.getCourse().getId().equals(testData.getCourseId())).toList();

    private final List<Lesson> allLessonsOfGroupPerDayWithoutCourse = groupLessonsPerDate(testData.getDateOfWednesday())
            .stream().filter(l -> !l.getCourse().getId().equals(testData.getCourseId())).toList();

    private final List<Lesson> lessonsByDateAndLessonNum = lessons.getLessons().stream()
            .filter(l -> l.getDate().isEqual(testData.getDateOfMonday()))
            .filter(l -> l.getLessonNum().equals(testData.getLessenNum())).toList();

    public Student getStudent() {
        Student student1 = new Student();
        student1.setFirstName(testData.getStudentFirstName());
        student1.setLastName(testData.getStudentLastName());
        student1.setPassword(getEncodedPassword());
        student1.setEmail(testData.getStudentEmail());
        student1.setGroup(getGroup());
        return student1;
    }

    public Student getFullyStudent() {
        Student student1 = getStudent();
        student1.setId(testData.getStudentId());
        return student1;
    }

    public Admin getAdmin() {
        Admin admin1 = new Admin();
        admin1.setFirstName(testData.getAdminFirstName());
        admin1.setLastName(testData.getAdminLastName());
        admin1.setEmail(testData.getAdminEmail());
        admin1.setPassword(getEncodedPassword());
        return admin1;
    }

    public Admin getFullyAdmin() {
        Admin admin1 = getAdmin();
        admin1.setId(testData.getAdminId());
        return admin1;
    }

    public Staff getStaff() {
        Staff staff1 = new Staff();
        staff1.setFirstName(testData.getStaffFirstName());
        staff1.setLastName(testData.getStaffLastName());
        staff1.setEmail(testData.getStaffEmail());
        staff1.setPassword(getEncodedPassword());
        return staff1;
    }

    public Staff getFullyStaff() {
        Staff staff1 = getStaff();
        staff1.setId(testData.getStaffId());
        return staff1;
    }

    public Teacher getTeacher() {
        Teacher teacher = new Teacher();
        teacher.setFirstName(testData.getTeacherFirstName());
        teacher.setLastName(testData.getTeacherLastName());
        teacher.setEmail(testData.getTeacherEmail());
        teacher.setPassword(getEncodedPassword());
        return teacher;
    }

    public Teacher getFullyTeacher() {
        Teacher teacher = getTeacher();
        teacher.setId(testData.getTeacherId());
        return teacher;
    }

    private final List<Integer> numbersOfLessons = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
    private final NamesDto teacherNameDto = new NamesDto(testData.getTeacherId(), testData.getTeacherFirstName(), testData.getTeacherLastName());
}

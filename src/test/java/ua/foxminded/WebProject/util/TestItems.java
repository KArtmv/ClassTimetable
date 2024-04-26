package ua.foxminded.WebProject.util;

import lombok.Getter;
import ua.foxminded.WebProject.DTO.LessonDto;
import ua.foxminded.WebProject.DTO.StudentDto;
import ua.foxminded.WebProject.DTO.TeacherDto;
import ua.foxminded.WebProject.persistence.entity.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@Getter
public class TestItems {
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

    private final Group group = new Group(testData.getGroupId(), testData.getGroupName());

    private final Long teacherId = 6L;
    private final String teacherFirstName = "Jessica";
    private final String teacherLastName = "Brown";
    private final Teacher teacher = new Teacher(getTeacherId(), getTeacherFirstName(), getTeacherLastName());
    private final Teacher teacherIdFirst = new Teacher(testData.getTeacherId(), testData.getTeacherFirstName(), testData.getTeacherLastName());
    private final TeacherDto teacherDto = new TeacherDto(testData.getTeacherFirstName(), testData.getTeacherLastName());

    private final Lesson lesson = new Lesson(testData.getLessonId(), testData.getDate(), testData.getLessenNum(), getCourseWithIdSeven(), getGroup(), getClassroom(), getTeacher());
    private final LessonDto lessonDto = new LessonDto(new Course(getCourseId()), new Group(testData.getGroupId()), new Classroom(getClassroomId()), new Teacher(getTeacherId()));

    private final Student student = new Student(testData.getStudentId(), testData.getStudentFirstName(), testData.getStudentLastName(), getGroup());
    private final Student studentIdFive = new Student(5L, "Amelia", "Martinez", getGroup());
    private final StudentDto studentDto = new StudentDto(testData.getStudentFirstName(), testData.getStudentLastName(), new Group(testData.getGroupId()));

    private final Admin admin = new Admin(testData.getAdminFirstName(), testData.getAdminLastName());

    private final Staff staff = new Staff(testData.getStaffLastName(), testData.getStaffLastName());

    private final LocalDate dateOfMonday = testData.getDate().with(DayOfWeek.MONDAY);
    private final LocalDate dateOfFriday = testData.getDate().with(DayOfWeek.FRIDAY);

    private final List<Student> students = Arrays.asList(
            new Student(1L, "Liam", "Jones", getGroup()),
            new Student(2L, "Harper", "Robinson", getGroup()),
            new Student(3L, "Lucas", "Thompson", getGroup()),
            new Student(4L, "Carter", "Clark", getGroup()),
            studentIdFive);

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
            new Group(2L, "HV-14"),
            new Group(3L, "QM-09"));

    public List<Staff> getStaffs(){
        Staff staff1 = getStaff();
        staff1.setId(1L);
        return Collections.singletonList(staff1);
    }


    public Lesson getParticipants() {
        Lesson lesson1 = getLesson();
        lesson1.getGroup().setStudents(new HashSet<>(getStudents()));
        return lesson1;
    }

    private final List<Teacher> teachers = Arrays.asList(
            getTeacherIdFirst(),
            new Teacher(2L, "Emily", "Johnson"),
            new Teacher(3L, "Michael", "Davis"),
            new Teacher(4L, "Sarah", "Wilson"),
            new Teacher(5L, "Robert", "Taylor"),
            getTeacher(),
            new Teacher(7L, "William", "Moore"),
            new Teacher(8L, "Olivia", "Anderson")
    );

    public List<User> getUsers(){
        List<User> users = new ArrayList<>();
        users.add(getStaffWithEmail());
        users.add(getAdminWithEmail());
        users.add(getStudentWithEmail());
        users.add(getTeacherWithEmail());
        return users;
    }

    public Student getFullStudent(){
        Student student1 = getStudent();
        student1.setEmail(testData.getStudentEmail());
        student1.setPassword("password");
        student1.setGroup(getGroup());
        return student1;
    }

    public Teacher getFullTeacher(){
        Teacher teacher1 = getTeacher();
        teacher1.setEmail(testData.getTeacherEmail());
        teacher1.setPassword("password");
        return teacher1;
    }

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
            .filter(l -> l.getTeacher().getId().equals(getTeacher().getId())).toList();
    private final List<Lesson> teacherLessonsPerDay = getTeacherLessonsPerWeek().stream()
            .filter(l -> l.getDate().isEqual(testData.getDate())).toList();

    private Student getStudentWithEmail() {
        Student student1 = new Student();
        student1.setFirstName(testData.getStudentFirstName());
        student1.setLastName(testData.getStudentLastName());
        student1.setEmail(testData.getStudentEmail());
        return student1;
    }

    private Admin getAdminWithEmail() {
        Admin admin1 = new Admin();
        admin1.setFirstName(testData.getAdminFirstName());
        admin1.setLastName(testData.getAdminLastName());
        admin1.setEmail(testData.getAdminEmail());
        return admin1;
    }

    private Staff getStaffWithEmail() {
        Staff staff1 = new Staff();
        staff1.setFirstName(testData.getStudentFirstName());
        staff1.setLastName(testData.getStudentLastName());
        staff1.setEmail(testData.getStudentEmail());
        return staff1;
    }

    private Teacher getTeacherWithEmail() {
        Teacher teacher1 = new Teacher();
        teacher1.setFirstName(testData.getTeacherFirstName());
        teacher1.setLastName(testData.getTeacherLastName());
        teacher1.setEmail(testData.getTeacherEmail());
        return teacher1;
    }
}

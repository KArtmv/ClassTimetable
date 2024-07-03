package ua.foxminded.WebProject.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.foxminded.WebProject.persistence.entity.Teacher;
import ua.foxminded.WebProject.service.TeacherService;

@Controller
@RequestMapping("/teacher")
@PreAuthorize("hasAnyRole('admin', 'teacher', 'staff')")
public class TeacherController extends AbstractUserCrudController<Teacher> {

    protected TeacherController(TeacherService teacherService, BCryptPasswordEncoder passwordEncoder) {
        super(teacherService, passwordEncoder, Teacher.class);
    }
}

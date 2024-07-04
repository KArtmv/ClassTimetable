package ua.foxminded.WebProject.service.implementation;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.WebProject.persistence.entity.Staff;
import ua.foxminded.WebProject.persistence.repository.StaffRepository;
import ua.foxminded.WebProject.persistence.repository.UserRepository;
import ua.foxminded.WebProject.service.StaffService;

@Service
@Transactional(readOnly = true)
public class StaffServiceImpl extends AbstractUserService<Staff> implements StaffService {

    public StaffServiceImpl(StaffRepository staffRepository, BCryptPasswordEncoder passwordEncoder, UserRepository userRepository) {
        super(staffRepository, passwordEncoder, Staff.class, userRepository);
    }
}

package ua.foxminded.WebProject.service.implementation;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.WebProject.persistence.entity.Admin;
import ua.foxminded.WebProject.persistence.repository.AdminRepository;
import ua.foxminded.WebProject.persistence.repository.UserRepository;
import ua.foxminded.WebProject.service.AdminService;

@Service
@Transactional(readOnly = true)
public class AdminServiceImpl extends AbstractUserService<Admin> implements AdminService {

    public AdminServiceImpl(AdminRepository adminRepository, BCryptPasswordEncoder passwordEncoder, UserRepository userRepository) {
        super(adminRepository, passwordEncoder, Admin.class, userRepository);
    }
}

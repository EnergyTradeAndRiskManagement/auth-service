package com.trading.authservice.services;

import com.trading.authservice.models.Role;
import com.trading.authservice.respositories.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    public void createRole(String role){
        Role newRole = new Role();
        newRole.setName(role);

        roleRepository.save(newRole);
    }
}

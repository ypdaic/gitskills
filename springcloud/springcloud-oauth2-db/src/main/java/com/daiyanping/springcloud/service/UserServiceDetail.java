package com.daiyanping.springcloud.service;

import com.daiyanping.springcloud.entity.Role;
import com.daiyanping.springcloud.entity.User;
import com.daiyanping.springcloud.entity.UserRole;
import com.daiyanping.springcloud.vo.UserRoleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceDetail implements UserDetailsService {

    @Autowired
    private IUserService userService;

    @Autowired
    private IUserRoleService userRoleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        UserRoleDto userRoleDto = new UserRoleDto();
        userRoleDto.setUserId(user.getId());
        List<Role> roles = userRoleService.queryAllToRole(userRoleDto);
        List<GrantedAuthority> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(roles)) {
            roles.forEach(role -> {
                list.add(new SimpleGrantedAuthority(role.getName()));
            });

        }

        org.springframework.security.core.userdetails.User user1 = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), true, true, true, true, list);
        return user1;

    }
}

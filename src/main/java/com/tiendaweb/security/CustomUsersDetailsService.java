package com.tiendaweb.security;

import com.tiendaweb.models.Rol;
import com.tiendaweb.models.Usuario;
import com.tiendaweb.repositories.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUsersDetailsService implements UserDetailsService{
    private IUsuarioRepository userRepo;

    @Autowired
    public CustomUsersDetailsService(IUsuarioRepository userRepo) {
        this.userRepo = userRepo;
    }

    // este método nos traemos una lista de autoridades por medio de la lista de roles
    public Collection<GrantedAuthority> mapToAuthorities(List<Rol> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getTipoRol())).collect(Collectors.toList());
    }

    // traemos el username con sus datos por medio de sus credenciales
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuarios = userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return new User(usuarios.getUsername(), usuarios.getPassword(), mapToAuthorities(usuarios.getRol()));
    }
}

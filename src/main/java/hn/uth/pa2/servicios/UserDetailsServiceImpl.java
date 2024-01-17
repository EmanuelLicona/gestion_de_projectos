/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hn.uth.pa2.servicios;

import hn.uth.pa2.modelos.Rol;
import hn.uth.pa2.repositorios.UsuarioRepositorio;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Buddys
 */
@Service
@Transactional //La utilizamos porque nosotros traemos los roles de modo LAZY es decir solo cuando los voy a necesitar
//LAZY lo encuentro en la entity User
public class UserDetailsServiceImpl implements UserDetailsService {
    public static Long ID_USUARIO;
    
    @Autowired
    UsuarioRepositorio userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        hn.uth.pa2.modelos.Usuario appUser = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Login Username Invalido."));

        //Cargando los roles del usuario
        Set<GrantedAuthority> grantList = new HashSet<GrantedAuthority>(); //El set garantiza que no haya valores repetidos dentro de esa lista

        for (Rol role : appUser.getRoles()) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getDescripcion());
            grantList.add(grantedAuthority);
            //grantList contiene los roles
        }
        
        //Contendra el usuario que se cargara en sesion
        UserDetails user = (UserDetails) new User(appUser.getUsername(), appUser.getContrasenia(), grantList);
        ID_USUARIO = appUser.getId_usuario();
        
        
//        System.out.println("Username: "+appUser.getUsername());
//        System.out.println("Username: "+appUser.getId_usuario());
//        this.idUsuario = appUser.getId_usuario();
        return user;
    }

}

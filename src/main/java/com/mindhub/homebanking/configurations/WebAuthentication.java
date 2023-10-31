package com.mindhub.homebanking.configurations;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration // Clase hereda de clase e interfaz hereda de interfaz("mismo tipo").
// Le indicamos a Spring que esta clase va definir configuraciones de mi aplicacion.
public class WebAuthentication extends GlobalAuthenticationConfigurerAdapter { //Extension de Spring Security que nos permite configurar la autenticacion
    @Autowired
    ClientRepository clientRepository;

    @Override // Sobre escribimos el metodo init que proviene de GlobalAuthenticationConfigurerAdapter
    public void init(AuthenticationManagerBuilder auth) throws Exception { //El metodo init Se ejecuta y nos permite configurar
        // Los detalles de autenticacion del usuario

        auth.userDetailsService(inputName -> { // Se ejecuta cuando el cliente se loguea.

            Client client = clientRepository.findByEmail(inputName); // Buscamos el usuario por el email proporcionado
            // De forma interna se verifica que el correo y la pwd coincidan con los datos proporcionados.

            if (client != null) {
                if (client.getAdmin()) {
                    return new User(client.getEmail(), client.getPassword(), // Crea un objeto que representa los detalles del usuario
                            // Y gracias a los datos que contiene este objeto se crea un nuevo objeto de tipo Authentication
                            AuthorityUtils.createAuthorityList("ADMIN"));
                } else {
                    return new User(client.getEmail(), client.getPassword(),
                            AuthorityUtils.createAuthorityList("CLIENT"));
                }
            } else {
                throw new UsernameNotFoundException("Unknown user: " + inputName);
            }


        });

    }

    @Bean // Sea de las primeras cosas que se ejecuten y para que entre en el contexto de Spring para poder inyectarlo.
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}

package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Configuration
@EnableWebSecurity // Habilita la seguridad y nos permite realizar configuraciones de seguridad en la aplicacion.
class WebAuthorization {
    @Bean // Creamos una instancia de tipo SecurityFilterChain(Spring se encarga de administrar la instancia)
    //A traves del metodo creamos una instancia. "return http.build()"

    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { // Una clase de Spring Security me permite configurar seguridad en relacion
        // a las solicitudes HTTP.
        http.authorizeRequests()
                // Rutas Públicas (Acceso sin autenticación)
                .antMatchers(
                        HttpMethod.POST,
                        "/api/clients" // Registro de clientes
                ).permitAll()
                .antMatchers(
                        "/web/index.html",
                        "/web/pages/login.html",
                        "/web/register.html",
                        "/web/styles/**",
                        "/web/js/**",
                        "/web/img/**"
                ).permitAll()
                // Rutas de Administrador
                .antMatchers(
                        "/h2-console/**",
                        "/rest/**",
                        "/web/pages/manager.html",
                        "/web/pages/admin-loan.html"
                ).hasAuthority("ADMIN")
                // Rutas de Solo Lectura para Administradores(Obtener listado de clientes)
                .antMatchers(HttpMethod.GET, "/api/clients", "/api/loans").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/loans/create").hasAuthority("ADMIN")
                // Rutas Autenticadas (Requieren autenticación)
                .antMatchers(
                        "/web/pages/**",
                        "/api/clients/current/**"
                ).authenticated()
                // Restringir el acceso a /api/loans solo a CLIENT (si es necesario)
                .antMatchers(HttpMethod.GET, "/api/loans").authenticated()
                .antMatchers(HttpMethod.POST, "/api/loans", "/api/loans/payments").authenticated()

                // Ruta Denegada si no coincide con las rutas previamente definidas (Sin acceso)
                .anyRequest().denyAll();



        http.formLogin() // Configura el inicio de sesion basado en formulario en mi pagina web

                .usernameParameter("email") // Usuario, en nuestro caso hacemos uso del email.

                .passwordParameter("password")

                .loginPage("/api/login"); // Establecemos la ruta para inicio de sesion.


        http.logout().logoutUrl("/api/logout"); // Ruta donde se envia solicitud para cerrar sesion

        // turn off checking for CSRF tokens

        http.csrf().disable(); // Desactivamos el Token del formulario(No creamos formulario desde el back)


        //disabling frameOptions so h2-console can be accessed

        http.headers().frameOptions().disable(); // Deshabilitamos las restricciones para la carga de contenido Iframe  (Click Jacking)

        // if user is not authenticated, just send an authentication failure response  //PETICION A RUTA NO AUTENTICADO

        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication

        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response  //SI FALLA EL LOGUEO

        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response

        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler()); // Mandamos un codigo de estado 200, exitoso.

        return http.build(); // Retorna la configuracion establecida por nosotros y se crea la instancia.

    }

    // Este metodo se encarga de eliminar los atributos ligados al inicio de sesion, ya sea fallido o exitoso.
    private void clearAuthenticationAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {

            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        }

    }


}

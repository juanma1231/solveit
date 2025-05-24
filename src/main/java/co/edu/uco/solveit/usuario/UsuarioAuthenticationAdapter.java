package co.edu.uco.solveit.usuario;

import co.edu.uco.solveit.publicacion.domain.exception.PublicacionException;
import co.edu.uco.solveit.usuario.entity.Usuario;
import co.edu.uco.solveit.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static co.edu.uco.solveit.usuario.service.UsuarioService.USUARIO_NO_ENCONTRADO;

@Component
@RequiredArgsConstructor
class UsuarioAuthenticationAdapter implements UsuarioApi {

    private final UsuarioRepository usuarioRepository;

    @Override
    public Long getCurrentUserId() throws PublicacionException {
        Usuario usuario = getCurrentUser();
        return usuario.getId();
    }

    @Override
    public String getCurrentUsername() throws PublicacionException {
        try {
            return SecurityContextHolder.getContext().getAuthentication().getName();
        } catch (Exception e) {
            throw new PublicacionException("No se pudo obtener el usuario autenticado", e);
        }
    }

    @Override
    public String getCurrentUserFullName() throws PublicacionException {
        Usuario usuario = getCurrentUser();
        return usuario.getNombreCompleto();
    }

    public String getCurrentUserEmail() throws PublicacionException
    {
        Usuario usuario = getCurrentUser();
        return usuario.getEmail();
    }

    @Override
    public Optional<Usuario> findById(Long usuarioId) {
        return usuarioRepository.findById(usuarioId);
    }

    private Usuario getCurrentUser() throws PublicacionException {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            return usuarioRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException(USUARIO_NO_ENCONTRADO));
        } catch (UsernameNotFoundException e) {
            throw new PublicacionException(USUARIO_NO_ENCONTRADO, e);
        } catch (Exception e) {
            throw new PublicacionException("No se pudo obtener el usuario autenticado", e);
        }
    }
}
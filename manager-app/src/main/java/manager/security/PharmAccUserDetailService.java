//package manager.security;
//
//import lombok.RequiredArgsConstructor;
//import manager.endity.Authority;
//import manager.endity.PharmAccUser;
//import manager.repository.PharmAccUserRepository;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//public class PharmAccUserDetailService implements UserDetailsService {
//
//    private final PharmAccUserRepository pharmAccUserRepository;
//
//    @Override
//    @Transactional(readOnly = true)
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        // TODO: ленивая загрузка коллекции
//        return this.pharmAccUserRepository.findByUsername(username)
//                .map(user -> User.builder()
//                        .username(user.getUsername())
//                        .password(user.getPassword())
//                        .authorities(user.getAuthorities().stream()
//                                .map(Authority::getAuthority)
//                                .map(SimpleGrantedAuthority::new)
//                                .toList())
//                        .build())
//                .orElseThrow(() -> new UsernameNotFoundException("User %s not found".formatted(username)));
//    }
//}

package fr.campus.eni.encheres.bll;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import fr.campus.eni.encheres.bo.Utilisateur;
import fr.campus.eni.encheres.dal.UtilisateurRepositoryImpl;
import fr.campus.eni.encheres.exceptions.ExeptionEchere;

@Service
public class UtilisateurServiceImpl implements ICrudService<Utilisateur> {

    private final UtilisateurRepositoryImpl UtilisateurRepositoryImpl;
    private final PasswordEncoder passwordEncoder;

    public UtilisateurServiceImpl(
        UtilisateurRepositoryImpl utilisateurRepositoryImpl,
        PasswordEncoder passwordEncoder
    ) {
        this.UtilisateurRepositoryImpl = utilisateurRepositoryImpl;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void add(Utilisateur utilisateur) {
        UtilisateurRepositoryImpl.add(utilisateur);
    }

    @Override
    public List<Utilisateur> getAll() {
        return UtilisateurRepositoryImpl.getAll();
    }

    @Override
    public Optional<Utilisateur> getById(int id) {
        return UtilisateurRepositoryImpl.getById(id);
    }

    @Override
    public void update(Utilisateur client) {
        Optional<Utilisateur> clientOpt = getById(client.getNoUtilisateur());
        if (clientOpt.isPresent()) {
            UtilisateurRepositoryImpl.update(client);
        } else {
            //TODO gerer l'erreur
            throw new ExeptionEchere();
        }
    }

    @Override
    public void delete(int id) {
        UtilisateurRepositoryImpl.delete(id);
    }
    
    @Override
    public void save(Utilisateur entity) {
        // On vérifie si c'est une création
        if (entity.getNoUtilisateur() == null) {
            // Hachage
            if (entity.getMotDePasse() != null) {
                String hashed = passwordEncoder.encode(entity.getMotDePasse());
                entity.setMotDePasse(hashed);
            }
            this.add(entity);
        } else {
            this.update(entity);
        }
        this.update(entity);
    }

    public Optional<Utilisateur> getByPseudoAndMdp(String username, String mdp) throws Exception {
        return UtilisateurRepositoryImpl.getByPseudoAndMdp(username, passwordEncoder.encode(mdp));
    }
}

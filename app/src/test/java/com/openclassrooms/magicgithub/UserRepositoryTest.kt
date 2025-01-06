package com.openclassrooms.magicgithub

import com.openclassrooms.magicgithub.api.FakeApiServiceGenerator.FAKE_USERS
import com.openclassrooms.magicgithub.api.FakeApiServiceGenerator.FAKE_USERS_RANDOM
import com.openclassrooms.magicgithub.di.Injection
import com.openclassrooms.magicgithub.model.User
import com.openclassrooms.magicgithub.repository.UserRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class UserRepositoryTest {
    private lateinit var userRepository: UserRepository

    @Before
    fun setup() {
        userRepository = Injection.getRepository()
    }

    @Test
    fun getUsersWithSuccess() {
        val usersActual = userRepository.getUsers()
        val usersExpected: List<User> = FAKE_USERS
        assertEquals(usersActual, usersExpected)
    }

    @Test
    fun generateRandomUserWithSuccess() {
        val initialSize = userRepository.getUsers().size
        userRepository.addRandomUser()
        val user = userRepository.getUsers().last()
        assertEquals(userRepository.getUsers().size, initialSize + 1)
        assertTrue(
            FAKE_USERS_RANDOM.any { it == user }
        )
    }

    @Test
    fun deleteUserWithSuccess() {
        val userToDelete = userRepository.getUsers()[0]
        userRepository.deleteUser(userToDelete)
        assertFalse(userRepository.getUsers().contains(userToDelete))
    }

    @Test
    fun verifierChangementEtatUtilisateur() {
        // Récupérer le premier utilisateur et vérifier son état initial
        val user = userRepository.getUsers()[0]
        assertTrue("L'utilisateur devrait être actif initialement", user.isActive)

        // Désactiver l'utilisateur
        user.isActive = false
        userRepository.updateUserState(user)

        // Vérifier que l'état a été mis à jour
        val utilisateurMisAJour = userRepository.getUsers().find { it.id == user.id }
        assertNotNull("L'utilisateur devrait toujours exister", utilisateurMisAJour)
        assertFalse("L'utilisateur devrait être inactif", utilisateurMisAJour!!.isActive)

        // Réactiver l'utilisateur
        user.isActive = true
        userRepository.updateUserState(user)

        // Vérifier que l'état a été mis à jour
        val utilisateurReactive = userRepository.getUsers().find { it.id == user.id }
        assertNotNull("L'utilisateur devrait toujours exister", utilisateurReactive)
        assertTrue("L'utilisateur devrait être à nouveau actif", utilisateurReactive!!.isActive)
    }

    @Test
    fun verifierPersistanceEtatApresRafraichissement() {
        // Récupérer et modifier l'état d'un utilisateur
        val user = userRepository.getUsers()[0]
        user.isActive = false
        userRepository.updateUserState(user)

        // Récupérer une nouvelle liste d'utilisateurs
        val listeRafraichie = userRepository.getUsers()
        val utilisateurRafraichi = listeRafraichie.find { it.id == user.id }

        // Vérifier que l'état est conservé
        assertNotNull("L'utilisateur devrait exister dans la liste rafraîchie", utilisateurRafraichi)
        assertFalse("L'utilisateur devrait toujours être inactif après rafraîchissement", utilisateurRafraichi!!.isActive)
    }

    @Test
    fun verifierIndependanceEtatsUtilisateurs() {
        // Récupérer les deux premiers utilisateurs
        val utilisateur1 = userRepository.getUsers()[0]
        val utilisateur2 = userRepository.getUsers()[1]

        // Modifier l'état du premier utilisateur uniquement
        utilisateur1.isActive = false
        userRepository.updateUserState(utilisateur1)

        // Vérifier l'indépendance des états
        val utilisateur1MisAJour = userRepository.getUsers().find { it.id == utilisateur1.id }
        val utilisateur2MisAJour = userRepository.getUsers().find { it.id == utilisateur2.id }

        assertFalse("Le premier utilisateur devrait être inactif", utilisateur1MisAJour!!.isActive)
        assertTrue("Le second utilisateur devrait toujours être actif", utilisateur2MisAJour!!.isActive)
    }
}
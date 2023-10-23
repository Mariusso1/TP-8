package champollion;

import java.util.Date;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class ChampollionJUnitTest {
	Enseignant e;
	UE uml, java;

	@BeforeEach
	public void setUp() {
		e = new Enseignant("untel", "untel@gmail.com");
		uml = new UE("UML");
		java = new UE("Programmation en java");
	}

	@Test
	void ajoutInterventionChangeResteAPlanifier() {
		e.ajouteEnseignement(uml, 0, 10, 0);
		assertEquals(10, e.resteAPlanifier(uml, TypeIntervention.TD));
		Intervention inter = new Intervention(1, new Date(), e, uml, TypeIntervention.TD, null);
		e.ajouteIntervention(inter);
		assertEquals(9, e.resteAPlanifier(uml, TypeIntervention.TD));
	}

	@Test
	void heuresPlanifiees() {
		assertEquals(0, e.heuresPlanifiee());
		e.ajouteEnseignement(uml, 0, 10, 0);
		Intervention inter = new Intervention(1, new Date(), e, uml, TypeIntervention.TD, null);
		e.ajouteIntervention(inter);
		assertEquals(1, e.heuresPlanifiee());
	}

	@Test
	void ajoutInterventionNecessiteServicePrevuDansUE() {
		e.ajouteEnseignement(uml, 0, 10, 0);
		Intervention inter = new Intervention(1, new Date(), e, java, TypeIntervention.TD, null);

		try {
			e.ajouteIntervention(inter);
			fail("L'enseignant n'a pas de service prévu dans cette UE");
		} catch (IllegalArgumentException ex) {

		}
	}

	@Test
	public void nouvelEnseignantSansService() {
		assertEquals(0, e.heuresPrevuesPourUE(uml));
		assertEquals(0, e.heuresPrevues());
	}

	@Test
	void nouvelEnseignantSansUE() {
		assertTrue( e.UEPrevues().isEmpty() );
	}

	@Test
	void nouvelEnseignantEnSousService() {
		assertTrue( e.enSousService() );
	}

	@Test
	void ajouteHeures() {
		assertEquals(0, e.heuresPrevues());
		e.ajouteEnseignement(uml, 0, 10, 0);
		assertEquals(10, e.heuresPrevuesPourUE(uml));
		e.ajouteEnseignement(uml, 0, 20, 0);
		assertEquals(30, e.heuresPrevuesPourUE(uml));
		assertEquals(30, e.heuresPrevues());
	}

	@Test
	void ajoutHeureSansValeurNegative() {
		try {
			e.ajouteEnseignement(uml, 0, -1, 0);

			fail("La valeur négative aurait du être refusée");
		} catch (IllegalArgumentException ex) {

		}
	}

	@Test
	void serviceCalculCorrect() {
		e.ajouteEnseignement(uml, 0, 10, 0);
		e.ajouteEnseignement(uml, 20, 0, 0);
		e.ajouteEnseignement(java, 0, 0, 20);

		int expected = 10 + 30 + 15;
		assertEquals(expected, e.heuresPrevues());
	}

}

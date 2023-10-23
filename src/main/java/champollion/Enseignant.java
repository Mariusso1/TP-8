package champollion;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Enseignant extends Personne {

    private final Set<Intervention> planification = new HashSet<>();

    private final Map<UE, ServicePrevu> enseignements = new HashMap<>();

    public Enseignant(String nom, String email) {
        super(nom, email);
    }

    public void ajouteIntervention(Intervention inter) {
        if (!enseignements.containsKey(inter.getMatiere())) {
            throw new IllegalArgumentException("La matière ne fait pas partie des enseignements");
        }

        planification.add(inter);
    }

    public int heuresPlanifiee() {
        float resultat = 0f;
        for (Intervention inter : planification) {
            resultat += equivalentTD(inter.getType(), inter.getDuree());
        }

        return Math.round(resultat);
    }

    private float equivalentTD(TypeIntervention type, int volumeHoraire) {
        float resultat = 0f;
        switch (type) {
            case CM:
                resultat = volumeHoraire * 1.5f;
                break;
            case TD:
                resultat = volumeHoraire;
                break;
            case TP:
                resultat = volumeHoraire * 0.75f;
                break;
        }
        return resultat;
    }

    public boolean enSousService() {
        return heuresPrevues() < 192;
    }


    public int heuresPrevues() {
        float reponse = 0f;
        for (UE ue : enseignements.keySet()) {
            reponse += heuresPrevuesPourUE(ue);
        }
        return Math.round(reponse);
    }


    public int heuresPrevuesPourUE(UE ue) {
        float reponse = 0;

        ServicePrevu p = enseignements.get(ue);
        if (p != null) {
            reponse =  equivalentTD(TypeIntervention.CM, p.getVolumeCM())
                    + equivalentTD(TypeIntervention.TD, p.getVolumeTD())
                    + equivalentTD(TypeIntervention.TP, p.getVolumeTP())
            ;
        }
        return Math.round(reponse);
    }


    public void ajouteEnseignement(UE ue, int volumeCM, int volumeTD, int volumeTP) {
        if (volumeCM < 0 || volumeTD < 0 || volumeTP < 0) {
            throw new IllegalArgumentException("Les valeurs doivent être positives ou nulles");
        }

        ServicePrevu s = enseignements.get(ue);
        if (s == null) {
            s = new ServicePrevu(volumeCM, volumeTD, volumeTP, ue);
            enseignements.put(ue, s);
        } else {
            s.setVolumeCM(volumeCM + s.getVolumeCM());
            s.setVolumeTD(volumeTD + s.getVolumeTD());
            s.setVolumeTP(volumeTP + s.getVolumeTP());
        }
    }


    public Set<UE> UEPrevues() {
        return enseignements.keySet();
    }

    public int resteAPlanifier(UE ue, TypeIntervention type) {
        float planifiees = 0;
        ServicePrevu p = enseignements.get(ue);
        if (null == p)
            return 0;

        float aPlanifier = p.getVolumePour(type);

        for (Intervention inter : planification) {
            if ((ue.equals(inter.getMatiere())) && (type.equals(inter.getType()))) {
                planifiees += inter.getDuree();
            }
        }
        return Math.round(aPlanifier - planifiees);
    }

}

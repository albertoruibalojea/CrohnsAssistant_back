package crohnsassistantapi.service;

import crohnsassistantapi.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CrohnsCriticityService {

    private final MongoTemplate mongo;
    private final HealthService healthService;
    private final SymptomService symptomService;
    private final FoodService foodService;

    @Autowired
    public CrohnsCriticityService(MongoTemplate mongo, HealthService healthService, SymptomService symptomService, FoodService foodService) {
        this.mongo = mongo;
        this.healthService = healthService;
        this.symptomService = symptomService;
        this.foodService = foodService;
    }


    //IDEA: poner en la api de forma pública el averiguar si es positivo para x día concreto

    //method to check food alerts
    /*public void checkFoodAlert(User user){
        //first, we get today´s symptoms
        Optional<Page<Symptom>> symptomList = symptomService.get(user.getEmail(), new Date(), 0, 20, null);

        if(symptomList.isPresent()){
            //for each symptom, we search in db for the other days with this symptom
            for(Symptom symptom_i : symptomList.get()){
                Optional<Page<Symptom>> daysWithThatSymptom = symptomService.get(user.getEmail(), symptom_i.getName());

                if(daysWithThatSymptom.isPresent()){
                    //now, we get the date for each symptom
                    for(Symptom d : daysWithThatSymptom.get()){

                        //and now we just search for a common food with d
                    }
                }
            }
        }
    }*/
    
    //this is the main method for the disease analyzer that will be called from the controller and uses the other methods
    public void analyze(User user){
        int previousDays = user.getDaysToAnalyze();

        //for the last days, we analyze the symptoms
        for(int i=0; i<previousDays; i++){

            //this Date is the result of the substraction of the days to analyze from today
            Date checkDate = new Date();
            checkDate.setDate(checkDate.getDate() - previousDays);

            Optional<Health> health_i = healthService.get(user.getEmail(), checkDate);

            //health_i = D
            if(health_i.isPresent()){
                //if D.isCrohnActive==false && D.symptomatology==false && crohnActiveCheck==false
                if(!health_i.get().isCrohnActive() && health_i.get().isSymptomatology() && !this.crohnActiveCheck(user)){

                    //get symptoms from that day
                    Optional<Page<Symptom>> symptomList_i = symptomService.get(user.getEmail(), checkDate, 0, 20, null);

                    if(symptomList_i.isPresent()){

                        //we have to check the symptoms with the user´s crohn type
                        //if there is a coincidence, it will update the symptomatology to true
                        //if type is repeated for the daystoAnalyze days for health_i, it means crohn is active
                        if(this.isAnyType(user, symptomList_i.get().toList())){
                            health_i.get().setSymptomatology(true);
                            health_i.get().setType(user.getCROHN_TYPE());
                            healthService.update(health_i.get());

                            if(this.doesTheTypeRepeat(user, CrohnTypes.fromString(user.getCROHN_TYPE()).getType())){
                                this.setCrohnActive(user);
                            }

                        }
                    }
                } else if(!health_i.get().isCrohnActive() && this.crohnActiveCheck(user)){
                    //in this case, the disease is active for the previous days
                    //but it doesn´t mean it has symptomatology for today, it can be active and under the threshold

                    //get symptoms from that day
                    Optional<Page<Symptom>> symptomList_i = symptomService.get(user.getEmail(), checkDate, 0, 20, null);

                    if(symptomList_i.isPresent()){

                        //we have to check is symptomatology is present for the previous days
                        //if not, crohn active is ending
                        if(this.doesTheTypeRepeat(user, user.getCROHN_TYPE())){
                            //if the user is still having crohn active for the same type as he has
                            //else it means symptomatology is false, but the type is the same as the others days and crohn is active for today too
                            if(this.isAnyType(user, symptomList_i.get().toList())){
                                health_i.get().setSymptomatology(true);
                                health_i.get().setType(user.getCROHN_TYPE());
                                health_i.get().setCrohnActive(true);
                                healthService.update(health_i.get());
                            } else {
                                health_i.get().setSymptomatology(false);
                                health_i.get().setType(user.getCROHN_TYPE());
                                health_i.get().setCrohnActive(true);
                                healthService.update(health_i.get());
                            }
                        } else {
                            health_i.get().setSymptomatology(false);
                            health_i.get().setType(user.getCROHN_TYPE());
                            health_i.get().setCrohnActive(false);
                            healthService.update(health_i.get());
                        }
                    }
                }
            }
        }
    }


    // auxiliar methods

    //method to set true the Health.crohnActive for today and the previous User.daysToAnalyze days
    private void setCrohnActive(User user){
        //access to mongo and get the last User.daysToAnalyze days
        //set the Health.crohnActive to true
        int previousDays = user.getDaysToAnalyze();

        //first, call to the HealthService on get method, passing as parameters the user mail and the range from today to User.daysToAnalyze days ago

        //this Date is the result of the substraction of the days to analyze from today
        Date prevDate = new Date();
        prevDate.setDate(prevDate.getDate() - previousDays);

        Optional<Page<Health>> healths = healthService.get(user.getEmail(), prevDate, 0, 20, null);

        //then, iterate the list of Healths and set the Health.crohnActive to true and type to the user type
        if(healths.isPresent()){
            for(Health health : healths.get().getContent()){
                health.setCrohnActive(true);
                healthService.update(health);
            }
        }
    }


    //method to check if Health.crohnActive is true in the past User.daysToAnalyze days
    private boolean crohnActiveCheck(User user){
        //access to mongo and get the last User.daysToAnalyze days
        //check if the Health.crohnActive is true
        //if it is, return true, else return false;
        int previousDays = user.getDaysToAnalyze();

        //first, call to the HealthService on get method, passing as parameters the user mail and the range from today to User.daysToAnalyze days ago

        //this Date is the result of the substraction of the days to analyze from today
        Date prevDate = new Date();
        prevDate.setDate(prevDate.getDate() - previousDays);

        Optional<Page<Health>> healths = healthService.get(user.getEmail(), prevDate, 0, 20, null);

        //then, iterate the list of Healths and check if the Health.crohnActive is true for at least half of the days
        if(healths.isPresent()){
            for(Health h : healths.get().getContent()){
                if(h.isCrohnActive()){
                    previousDays--;
                }
            }
        }

        //if the number of trues is greater than the number of falses, return true
        if(previousDays <= user.getDaysToAnalyze()/2){
            return true;
        }

        return false;
    }

    //method to check if the User.CROHNTYPE is repeating and Health.symptomatology is true in the past User.daysToAnalyze days
    private boolean doesTheTypeRepeat(User user, String type){
        //access to mongo and get the last User.daysToAnalyze days
        //check if the type is the same as the User.CROHNTYPE
        //if it is, return true, else return false;
        int previousDays = user.getDaysToAnalyze();

        //first, call to the HealthService on get method, passing as parameters the user mail and the range from today to User.daysToAnalyze days ago

        //this Date is the result of the substraction of the days to analyze from today
        Date prevDate = new Date();
        prevDate.setDate(prevDate.getDate() - previousDays);

        Optional<Page<Health>> healths = healthService.get(user.getEmail(), prevDate, new Date(), 0, 20, null);

        //then, iterate the list of Healths and check if the type is the same as the User.CROHNTYPE and Health.symptomatology is true for at least half of the days
        for(Health health : healths.get().getContent()){
            if(health.getType().equalsIgnoreCase(type) && health.isSymptomatology()){
                previousDays--;
            }
        }

        //if the number of days is greater than half of the days to analyze, return true
        if(previousDays > user.getDaysToAnalyze()/2){
            return true;
        } else return false;
    }



    //in this case, to check which pattern is causing the criticity

    //this method calls all the above methods
    private boolean isAnyType(User user, List<Symptom> symptoms){

        return this.isTypeIleocolitis(user, symptoms) || this.isTypeColitis(user, symptoms) || this.isTypeIleitis(user, symptoms) ||
                this.isTypeUpperTract(user, symptoms) || this.isTypePerianal(user, symptoms);
    }

    //check if type is Ileocolitis
    private boolean isTypeIleocolitis(User user, List<Symptom> symptoms){

        int positivity = 0;

        //check if the User.type field is th same as CronTypes.ILEOCOLITIS
        if (Objects.equals(user.getCROHN_TYPE(), CrohnTypes.TYPE_ILEOCOLITIS.getType())){

            //iterate the list of symptoms and adding its value to the total
            //in this case, the list is DIARRHEA, ABDOMINAL_PAIN, FATIGUE, WEIGHT_LOSS, BLOOD, NOT_HUNGRY, JOINT_PAIN, HEADACHE, APHTHAS, SKIN_RASH
            for(Symptom symptom : symptoms){
                if(SymptomTypes.fromString(symptom.getName()).equals(SymptomTypes.DIARRHEA)){
                    positivity += SymptomTypes.DIARRHEA.getPunctuation();
                }
                if(SymptomTypes.fromString(symptom.getName()).equals(SymptomTypes.ABDOMINAL_PAIN)){
                    positivity += SymptomTypes.ABDOMINAL_PAIN.getPunctuation();
                }
                if(SymptomTypes.fromString(symptom.getName()).equals(SymptomTypes.FATIGUE)){
                    positivity += SymptomTypes.FATIGUE.getPunctuation();
                }
                if(SymptomTypes.fromString(symptom.getName()).equals(SymptomTypes.WEIGHT_LOSS)){
                    positivity += SymptomTypes.WEIGHT_LOSS.getPunctuation();
                }
                if(SymptomTypes.fromString(symptom.getName()).equals(SymptomTypes.BLOOD)){
                    positivity += SymptomTypes.BLOOD.getPunctuation();
                }
                if(SymptomTypes.fromString(symptom.getName()).equals(SymptomTypes.NOT_HUNGRY)){
                    positivity += SymptomTypes.NOT_HUNGRY.getPunctuation();
                }
                if(SymptomTypes.fromString(symptom.getName()).equals(SymptomTypes.JOINT_PAIN)){
                    positivity += SymptomTypes.JOINT_PAIN.getPunctuation();
                }
                if(SymptomTypes.fromString(symptom.getName()).equals(SymptomTypes.HEADACHE)){
                    positivity += SymptomTypes.HEADACHE.getPunctuation();
                }
                if(SymptomTypes.fromString(symptom.getName()).equals(SymptomTypes.APHTHAS)){
                    positivity += SymptomTypes.APHTHAS.getPunctuation();
                }
                if(SymptomTypes.fromString(symptom.getName()).equals(SymptomTypes.SKIN_RASH)){
                    positivity += SymptomTypes.SKIN_RASH.getPunctuation();
                }
            }

            //check if the total is greater or equal than the threshold
            if(positivity >= CrohnTypes.TYPE_ILEOCOLITIS.getThreshold()){
                return true;
            }
        }
        return false;
    }

    //check if type is Ileitis
    private boolean isTypeIleitis(User user, List<Symptom> symptoms){

        int positivity = 0;

        //check if the User.type field is th same as CronTypes.ILEITIS
        if (Objects.equals(user.getCROHN_TYPE(), CrohnTypes.TYPE_ILEITIS.getType())){

            //iterate the list of symptoms and adding its value to the total
            //in this case, the list is DIARRHEA, ABDOMINAL_PAIN, FATIGUE, FEVER, WEIGHT_LOSS, APHTHAS, SKIN_RASH
            for(Symptom symptom : symptoms){
                if(SymptomTypes.fromString(symptom.getName()).equals(SymptomTypes.DIARRHEA)){
                    positivity += SymptomTypes.DIARRHEA.getPunctuation();
                }
                if(SymptomTypes.fromString(symptom.getName()).equals(SymptomTypes.ABDOMINAL_PAIN)){
                    positivity += SymptomTypes.ABDOMINAL_PAIN.getPunctuation();
                }
                if(SymptomTypes.fromString(symptom.getName()).equals(SymptomTypes.FATIGUE)){
                    positivity += SymptomTypes.FATIGUE.getPunctuation();
                }
                if(SymptomTypes.fromString(symptom.getName()).equals(SymptomTypes.FEVER)){
                    positivity += SymptomTypes.FEVER.getPunctuation();
                }
                if(SymptomTypes.fromString(symptom.getName()).equals(SymptomTypes.WEIGHT_LOSS)){
                    positivity += SymptomTypes.WEIGHT_LOSS.getPunctuation();
                }
                if(SymptomTypes.fromString(symptom.getName()).equals(SymptomTypes.APHTHAS)){
                    positivity += SymptomTypes.APHTHAS.getPunctuation();
                }
                if(SymptomTypes.fromString(symptom.getName()).equals(SymptomTypes.SKIN_RASH)){
                    positivity += SymptomTypes.SKIN_RASH.getPunctuation();
                }
            }

            //check if the total is greater or equal than the threshold
            if(positivity >= CrohnTypes.TYPE_ILEITIS.getThreshold()){
                return true;
            }
        }
        return false;
    }

    //check if type is Colitis
    private boolean isTypeColitis(User user, List<Symptom> symptoms){

        int positivity = 0;

        //check if the User.type field is th same as CronTypes.COLITIS
        if (Objects.equals(user.getCROHN_TYPE(), CrohnTypes.TYPE_COLITIS.getType())){

            //iterate the list of symptoms and adding its value to the total
            //in this case, the list is DIARRHEA, ABDOMINAL_PAIN, FEVER, NAUSEA, NOT_HUNGRY, WEIGHT_LOSS, APHTHAS, SKIN_RASH
            for(Symptom symptom : symptoms){
                if(SymptomTypes.fromString(symptom.getName()).equals(SymptomTypes.DIARRHEA)){
                    positivity += SymptomTypes.DIARRHEA.getPunctuation();
                }
                if(SymptomTypes.fromString(symptom.getName()).equals(SymptomTypes.ABDOMINAL_PAIN)){
                    positivity += SymptomTypes.ABDOMINAL_PAIN.getPunctuation();
                }
                if(SymptomTypes.fromString(symptom.getName()).equals(SymptomTypes.FEVER)){
                    positivity += SymptomTypes.FEVER.getPunctuation();
                }
                if(SymptomTypes.fromString(symptom.getName()).equals(SymptomTypes.NAUSEA)){
                    positivity += SymptomTypes.NAUSEA.getPunctuation();
                }
                if(SymptomTypes.fromString(symptom.getName()).equals(SymptomTypes.NOT_HUNGRY)){
                    positivity += SymptomTypes.NOT_HUNGRY.getPunctuation();
                }
                if(SymptomTypes.fromString(symptom.getName()).equals(SymptomTypes.WEIGHT_LOSS)){
                    positivity += SymptomTypes.WEIGHT_LOSS.getPunctuation();
                }
                if(SymptomTypes.fromString(symptom.getName()).equals(SymptomTypes.APHTHAS)){
                    positivity += SymptomTypes.APHTHAS.getPunctuation();
                }
                if(SymptomTypes.fromString(symptom.getName()).equals(SymptomTypes.SKIN_RASH)){
                    positivity += SymptomTypes.SKIN_RASH.getPunctuation();
                }
            }

            //check if the total is greater or equal than the threshold
            return positivity >= CrohnTypes.TYPE_COLITIS.getThreshold();
        }

        return false;
    }

    //check if type is Upper Tract
    private boolean isTypeUpperTract(User user, List<Symptom> symptoms){

        int positivity = 0;

        //check if the User.type field is th same as CronTypes.UPPER_TRACT
        if (Objects.equals(user.getCROHN_TYPE(), CrohnTypes.TYPE_UPPER_TRACT.getType())){

            //iterate the list of symptoms and adding its value to the total
            //in this case, the list is ABDOMINAL_PAIN, FEVER, NAUSEA, NOT_HUNGRY, WEIGHT_LOSS, APHTHAS and SKIN_RASH
            for(Symptom symptom : symptoms){
                if(SymptomTypes.fromString(symptom.getName()).equals(SymptomTypes.ABDOMINAL_PAIN)){
                    positivity += SymptomTypes.ABDOMINAL_PAIN.getPunctuation();
                }
                if(SymptomTypes.fromString(symptom.getName()).equals(SymptomTypes.FEVER)){
                    positivity += SymptomTypes.FEVER.getPunctuation();
                }
                if(SymptomTypes.fromString(symptom.getName()).equals(SymptomTypes.NAUSEA)){
                    positivity += SymptomTypes.NAUSEA.getPunctuation();
                }
                if(SymptomTypes.fromString(symptom.getName()).equals(SymptomTypes.NOT_HUNGRY)){
                    positivity += SymptomTypes.NOT_HUNGRY.getPunctuation();
                }
                if(SymptomTypes.fromString(symptom.getName()).equals(SymptomTypes.WEIGHT_LOSS)){
                    positivity += SymptomTypes.WEIGHT_LOSS.getPunctuation();
                }
                if(SymptomTypes.fromString(symptom.getName()).equals(SymptomTypes.APHTHAS)){
                    positivity += SymptomTypes.APHTHAS.getPunctuation();
                }
                if(SymptomTypes.fromString(symptom.getName()).equals(SymptomTypes.SKIN_RASH)){
                    positivity += SymptomTypes.SKIN_RASH.getPunctuation();
                }
            }

            //check if the total is greater or equal to the threshold
            if(positivity >= CrohnTypes.TYPE_UPPER_TRACT.getThreshold()){
                return true;
            }
        }

        return false;
    }

    //check if type is Perianal
    private boolean isTypePerianal(User user, List<Symptom> symptoms){

        int positivity = 0;

        //check if the User.type field is th same as CronTypes.PERIANAL
        if (Objects.equals(user.getCROHN_TYPE(), CrohnTypes.TYPE_PERIANAL.getType())){

            //iterate the list of symptoms and adding its value to the total
            //in this case, the list is DIARRHEA, ANAL_PAIN, BLOOD, PERIANAL_PAIN and SKIN_RASH
            for(Symptom symptom : symptoms){
                if(SymptomTypes.fromString(symptom.getName()).equals(SymptomTypes.DIARRHEA)){
                    positivity += SymptomTypes.DIARRHEA.getPunctuation();
                }
                if(SymptomTypes.fromString(symptom.getName()).equals(SymptomTypes.ANAL_PAIN)){
                    positivity += SymptomTypes.ANAL_PAIN.getPunctuation();
                }
                if(SymptomTypes.fromString(symptom.getName()).equals(SymptomTypes.BLOOD)){
                    positivity += SymptomTypes.BLOOD.getPunctuation();
                }
                if(SymptomTypes.fromString(symptom.getName()).equals(SymptomTypes.PERIANAL_PAIN)){
                    positivity += SymptomTypes.PERIANAL_PAIN.getPunctuation();
                }
                if(SymptomTypes.fromString(symptom.getName()).equals(SymptomTypes.SKIN_RASH)){
                    positivity += SymptomTypes.SKIN_RASH.getPunctuation();
                }
            }
        }

        //if the total is greater than the half of the threshold, return true
        return positivity >= (CrohnTypes.TYPE_PERIANAL.getThreshold() / 2);
    }
}

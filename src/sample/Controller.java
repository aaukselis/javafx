package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Controller {
    public TextArea zmoniuSkaicius;
    int threadNumbers = 1000;

    List<Human> humans = Collections.synchronizedList (new ArrayList<>());// dirbant su threadais kad listai nepasimestu.
    String insertText = "";
    public TextArea text;
    StringBuffer string = new StringBuffer("A");

    public void meteoritas(ActionEvent actionEvent)
    {
        GodRules GR = new GodRules();
        humans=GR.Meteorite(humans);
        insertText+=" \r\n BAM Meteoritas!!!!!\r\n \r\n \r\n " + humans.toString();
        text.setText(insertText);
        ShowGenders(humans); // isveda vyru ir moteru lista i langa
    }

    public void valentinas(ActionEvent actionEvent)
    {
        GodRules GR = new GodRules();
        humans=GR.CreateHuman(humans);
        insertText+=" \n Valentino Diena!!!!!! \n  \r\n \r\n" + humans.toString();
        text.setText(insertText);
        ShowGenders(humans);
    }

    public void maras(ActionEvent actionEvent)
    {
        GodRules GR = new GodRules();
        humans=GR.KillHumansByPlague(humans);
        insertText+=" \r\n Karas Karas Karas Karas!!!!!\r\n " + humans.toString();
        text.setText(insertText);
        ShowGenders(humans);
    }

    public void karas(ActionEvent actionEvent)
    {
        GodRules GR = new GodRules();
        humans=GR.War(humans);
        insertText+=" \r\n Karas Karas Karas Karas!!!!!\r\n " + humans.toString();
        text.setText(insertText);
        ShowGenders(humans);
    }

    public void start(ActionEvent actionEvent)
    {
        GodRules GR = new GodRules();
        new Thread(() -> {
            for (int i = 0; i < threadNumbers; i++) {
                try {
                    Thread.sleep(500);
                    for (int m = 0; m<3;m++)
                    {
                        humans=GR.Borning(humans);
                    }
                    humans = GR.Aging(humans);
                    humans = GR.KillHumansByAge(humans);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                              string.append(i);

                Platform.runLater(() -> {
                    insertText+=" \n Zmoniu Papildymas \n" + humans.toString();
                    text.setText(insertText);
                    text.appendText(""); // sita gudrybe daro automatiskai skrola i apacia.
                    // Nes defaultu visada bus pradinej pozicijoje o norint scrolinti i apacia reikia tai padaryti ranka.

                    ShowGenders(humans);
                });
            }
        } ).start();
    }
    void ShowGenders(List<Human> humanoidai)
    {
        int maleNumber = 0;
        int femaleNumber = 0;
        for (Human human:humanoidai)
        {
            if (human.getGender().equals(GodRules.Gender.Male.toString())){ maleNumber++;}else {femaleNumber++;}
        }

        zmoniuSkaicius.setText("\r\n Vyru skaicius : "+ maleNumber+ "\r\n Moteru skaicius: "+femaleNumber);
        zmoniuSkaicius.appendText("");
    }

    public void stop(ActionEvent actionEvent)
    {
        this.threadNumbers=0; // Kad nereiktu kilinti thread , mazas tricksas nutraukti ciklui;
    }
}

class GodRules
{
    enum Gender{Male,Female}

    List<Human> Meteorite(List<Human> humans)
    {
       humans.removeAll(humans);
        return humans;
    }
    List<Human> CreateHuman(List<Human> humans)
    {
        Random randomAge = new Random();

        for (int i=0;i<20;i++)
        {
            int age = randomAge.nextInt(100);
            Gender value = Gender.values()[new Random().nextInt(Gender.values().length)];
            humans.add(new Human(age,value.toString()));
        }
        return humans;
    }

    List<Human> KillHumansByAge(List<Human> humanoidai)
    {
        humanoidai.removeIf(human -> human.getAge()>80);
        return humanoidai;
    }

    List<Human> KillHumansByPlague(List<Human> humanoidai)
    {
      Thread thread1=  new Thread(() -> {
          for (int i =0;i<humanoidai.size()/2;i++)
          {
              Random randomPlague = new Random();
              int remove=randomPlague.nextInt(humanoidai.size());
              humanoidai.remove(remove);
          }
      });
      thread1.setName("NameOfPlague");
      thread1.setDaemon(true);
      thread1.start();

        return humanoidai;
    }
    List<Human> War(List<Human> humanoidai)
    {
        for (int i=0;i<humanoidai.size();i++) {
          Random randomsoldier = new Random();
          humanoidai.removeIf(human -> human.getGender().equals(Gender.Male.toString()) & human.getAge() > 18 & human.getAge() < 60 & human.getId() == randomsoldier.nextInt(humanoidai.size()));
         //Specialiai palikau kad galetu removinti ID kurio net nera, taip didindamas kareiviu isgyvenima.
        }

        return humanoidai;
    }
    List<Human> Aging (List<Human> humanoidai)
    {
        humanoidai.forEach(human -> human.setAge(human.getAge()+1));
        try{
        }catch(Exception e){e.printStackTrace();}
        return humanoidai;
    }
    List<Human> Borning (List<Human> humanoidai)
    {
        Gender value = Gender.values()[new Random().nextInt(Gender.values().length)];
        humanoidai.add(new Human(0,value.toString()));
        return humanoidai;
    }

}

class Human
{

    private static int  ID=1;
    private int id;
    private int age;
    private String gender;

    public int getId() {
        return id;
    }

    public Human(int age, String gender) {
        this.id = ID++;
        this.age = age;
        this.gender = gender;
    }

    @Override
    public String toString() {
        return  "Lytis : "+ gender +"  Amžius : "+age;
    }


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
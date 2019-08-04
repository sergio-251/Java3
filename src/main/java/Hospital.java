public class Hospital {
    public GeneralDoctor generalDoctor;

    public Hospital(GeneralDoctor gd){
        this.generalDoctor = gd;
    }

    public void setDoctor(GeneralDoctor gd){
        this.generalDoctor = gd;
    }

    public void toDoctor(){
        generalDoctor.doDoctor();
        System.out.println("Пациент отправлен ко врачу!");
    }
}

public class Petrov {
    String str;
    int num;

    public Petrov (String s, int a){
        this.str = s;
        this.num = a;
    }

   public void printMsg(){
       System.out.println(this.str);
   }

   public int squareInt(){
       return this.num * this.num;
   }

   public double squareRootInt() throws Exception {
       if (this.num < 0) {
       throw new Exception("Negative number not support!");
       } else {
           return Math.sqrt(this.num);
       }
   }
}

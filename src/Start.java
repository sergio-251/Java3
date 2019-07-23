public class Start {

    static volatile int firstChar = 'A';

    public static void main(String[] args){
        Object simpleLock = new Object();




       class ThreadClass implements Runnable {

            char ch;
            char nextChar;

            public ThreadClass(char c) {
                this.ch = c;
                this.nextChar = (ch == 'C') ? 'A' : (char)(1 + ch);
            }

            @Override
            public void run() {
                synchronized (simpleLock) {
                    for (int i = 0; i < 5; i++) {
                        try {
                            while (firstChar != ch) {
                                simpleLock.wait();
                            }
                            System.out.print(ch);
                            firstChar = nextChar;
                            simpleLock.notifyAll();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }
        new Thread(new ThreadClass('A')).start();
        new Thread(new ThreadClass('B')).start();
        new Thread(new ThreadClass('C')).start();

    }



}


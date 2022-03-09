package com.sanri.test.locktest;

public class ABCThreadWaitOneThreadClass {
    static char currentPrintChar = 'A';

    static class PrintThread extends Thread{
        private Object lockSelf;
        private Object lockNext;
        private char printChar;

        public PrintThread(Object lockSelf,Object lockNext,char printChar){
            this.lockSelf = lockSelf;
            this.lockNext = lockNext;
            this.printChar = printChar;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lockSelf){
                    if(printChar != currentPrintChar){
                        try {
                            lockSelf.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    // 打印当前线程字符
                    System.out.print(printChar);

                    // 切换下一个线程，并切换状态
                    if(currentPrintChar == 'A'){currentPrintChar = 'B';}
                    else if(currentPrintChar == 'B'){currentPrintChar = 'C';}
                    else if(currentPrintChar == 'C'){currentPrintChar = 'A';}

                    //唤醒下一个线程
                    synchronized (lockNext) {
                        lockNext.notify();
                    }
                }
            }

        }
    }

    public static void main(String[] args) {
        Object lockA = new Object();
        Object lockB = new Object();
        Object lockC = new Object();

        new PrintThread(lockA,lockB,'A').start();
        new PrintThread(lockB,lockC,'B').start();
        new PrintThread(lockC,lockA,'C').start();

    }
}

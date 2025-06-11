package Test;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;

import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
//        ArrayList<Integer> al = new ArrayList<>();
//        System.out.println(al);
        term();


    }

    private static void term() throws IOException {
        int read = 0;
        Terminal terminal;
        terminal = TerminalBuilder.builder().jna(true).system(true).build();
        terminal.writer().print("Read character: ");
        terminal.enterRawMode();
        NonBlockingReader reader = terminal.reader();
      do {
          read = reader.read(500);

                terminal.writer().println("Read character: " +  read);
                terminal.writer().flush();
      }while (read!=10);
        reader.close();
        terminal.close();


    }


}

//Самостоятельная
//private static void term() throws IOException {
//
//    int read = 0;
//    Terminal terminal;
//    terminal = TerminalBuilder.builder().jna(true).system(true).build();
//    terminal.writer().print("Read character: ");
//    terminal.enterRawMode();
//    NonBlockingReader reader = terminal.reader();
//    do {
//        //            if (reader.available() > 0)
//        read = reader.read(100);
////if (read!=-2) System.out.println(read);
//        terminal.writer().println("Read character: " +  read);
//        terminal.writer().flush();
//    }while (read!=10);
//    reader.close();
//    terminal.close();
////        return read;
//
//}
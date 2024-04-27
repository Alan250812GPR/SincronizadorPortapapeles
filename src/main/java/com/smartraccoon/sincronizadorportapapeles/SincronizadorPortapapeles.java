package com.smartraccoon.sincronizadorportapapeles;

/**
 *
 * @author Alan
 *
 */
import java.awt.datatransfer.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SincronizadorPortapapeles {

    public static void main(String[] args) throws InterruptedException {
        Thread monitorThread = new Thread(() -> {
            while (!Thread.interrupted()) {
                try {
                    checkClipboardContent();
                    try {
                        TimeUnit.SECONDS.sleep(1); // Tiempo entre chequeos
                    } catch (InterruptedException e) {
                        break;
                    }
                } catch (IOException | UnsupportedFlavorException ex) {
                    Logger.getLogger(SincronizadorPortapapeles.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.out.println("Monitoreo finalizado");
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> monitorThread.interrupt()));

        monitorThread.start();
    }

    private static void checkClipboardContent() throws IOException, UnsupportedFlavorException {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
            
            Transferable transferable = clipboard.getContents(DataFlavor.stringFlavor);
            String content = (String) transferable.getTransferData(DataFlavor.stringFlavor);

            System.out.println("Portapapeles: "+content);
            
        } else {
            System.err.println("sin informacion");
        }
    }
}

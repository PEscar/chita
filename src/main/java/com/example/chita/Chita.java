package com.example.chita;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import lc.kra.system.mouse.GlobalMouseHook;
import lc.kra.system.mouse.event.GlobalMouseAdapter;
import lc.kra.system.mouse.event.GlobalMouseEvent;

public class Chita {

    int launchX = 11;
    int launchY = 11;
    private static boolean run = true;

    public static void main(String[] args) throws AWTException, IOException {

        addTrayIcon();

        Logger logger = Logger.getLogger("MyLog");
        FileHandler fh;

        try {

            // This block configure the logger with handler and formatter
            fh = new FileHandler("C:/Users/Pedro SCARSELLETTA/IdeaProjects/Chita/logs/MyLogFile.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            // the following statement is used to log any messages
            logger.info("My first log");

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // LOGIC
        // 1 ) Detect when centinel appears
        // 2 ) Get the code
        // 3 ) Open chat and write /centinela {code}

        Robot robot = new Robot();

        // Step 1.1 - Screen Capture - Define Dialog Rectangle - We ask the user to make 2 clicks to define the rectangle where the centinel dialog appears
        System.out.println("Click on upper left corner of dialog");

        // Might throw a UnsatisfiedLinkError if the native library fails to load or a RuntimeException if hooking fails
        GlobalMouseHook mouseHook = new GlobalMouseHook(); // Add true to the constructor, to switch to raw input mode

        System.out.println("Global mouse hook successfully started, press [middle] mouse button to shutdown. Connected mice:");

        for (Entry<Long,String> mouse:GlobalMouseHook.listMice().entrySet()) {
            System.out.format("%d: %s\n", mouse.getKey(), mouse.getValue());
        }

        mouseHook.addMouseListener(new GlobalMouseAdapter() {

            @Override
            public void mousePressed(GlobalMouseEvent event)  {
                System.out.println(event);
                if ((event.getButtons() & GlobalMouseEvent.BUTTON_LEFT) != GlobalMouseEvent.BUTTON_NO
                        && (event.getButtons() & GlobalMouseEvent.BUTTON_RIGHT) != GlobalMouseEvent.BUTTON_NO) {
                    System.out.println("Both mouse buttons are currently pressed!");
                }
                if (event.getButton()==GlobalMouseEvent.BUTTON_MIDDLE) {
                    run = false;
                }

                if (event.getButton()==GlobalMouseEvent.BUTTON_RIGHT) {
                    logger.info("REMO");
                    lanzarPoder("remo", robot, event);
                }
            }

            @Override
            public void mouseReleased(GlobalMouseEvent event)  {
                logger.info("mouseReleased");
                System.out.println(event);
            }

            @Override
            public void mouseMoved(GlobalMouseEvent event) {
                logger.info("mouseMoved");
                System.out.println(event);
            }

            @Override
            public void mouseWheel(GlobalMouseEvent event) {
                logger.info("mouseWheel");
//                System.out.println(event);
            }
        });

        try {
            while(run) {
                Thread.sleep(128);
            }
        } catch(InterruptedException e) {
            //Do nothing
        } finally {
            mouseHook.shutdownHook();
        }

//        //obtenemos nuestra pantalla como imagen
//        BufferedImage pantalla = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
//
//        //creamos un archivo de extensión .JPG en el directorio home del usuario del sistema
//        File file = new File(System.getProperty("user.home") + File.separator + "pantalla.jpg");
//
//        //guardamos el contenido de la imagen en el archivo .JPG
//        ImageIO.write(pantalla, "jpg", file);
    }

    private static void lanzarPoder(String remo, Robot robot, GlobalMouseEvent event) {

//        0 - Save current mouse position
        int oldX, oldY;
        oldX = event.getX();
        oldX = event.getY();


//        1 - Select skill



//        2 - Click launch
//        3 - Click pj
    }

    private static void addTrayIcon() {

        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        Image image = Toolkit.getDefaultToolkit().getImage("src/main/resources/logo.png");

        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon = new TrayIcon(image, "Chita 0.1", popup);
        final SystemTray tray = SystemTray.getSystemTray();

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(1);
            }
        });
        popup.add(exitItem);

        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
    }
}

package com.hosvir.decredwallet.gui;

import com.deadendgine.BaseGame;
import com.deadendgine.Engine;
import com.deadendgine.GameFrame;
import com.deadendgine.GameState;
import com.deadendgine.graphics.GameCanvas;
import com.deadendgine.input.Keyboard;
import com.deadendgine.input.Mouse;
import com.hosvir.decredwallet.Account;
import com.hosvir.decredwallet.Api;
import com.hosvir.decredwallet.Constants;
import com.hosvir.decredwallet.Processes;
import com.hosvir.decredwallet.gui.interfaces.*;
import com.hosvir.decredwallet.gui.interfaces.popups.AddContact;
import com.hosvir.decredwallet.gui.interfaces.popups.CreateAccount;
import com.hosvir.decredwallet.gui.interfaces.popups.Passphrase;
import com.hosvir.decredwallet.gui.interfaces.popups.RenameAccount;

import java.awt.*;
import java.util.Iterator;

/**
 * @author fsig
 * @version 1.00
 * @since 19/03/17
 */
public class Main extends BaseGame {
    public static int UPDATE_RATE = 60;
    public static long beginTime, timeTaken, timeLeft, lastLoopTime, delta;
    //-------------------Canvas / Frame---------------------
    public static GameFrame frame;
    public static GameCanvas canvas;
    //------------------------------------------------------
    public static boolean fullscreen = false;
    //Keyboard and Mouse
    public static Keyboard keyboard = null;
    public static Mouse mouse = null;
    //Volume
    public static float volume = 1.0f;
    public static boolean sound = true;
    //------------------------------------------------------
    public static boolean containsMouse = false;
    public static boolean haveInit = false;
    //------------------------------------------------------
    public static boolean exiting = false;
    //----------------------FPS / DELTA---------------------
    public int FPS = 0;
    //-------------------Variables--------------------------
    public GameState state = GameState.INITILISING;
    //---------------------Thread variables-----------------
    private Thread gameThread;
    private long UPDATE_PERIOD = 1000L / UPDATE_RATE;
    private int FRAMES = 0;
    private long totalTime = 0;
    private long curTime = System.currentTimeMillis();
    private long lastTime = curTime;

    /**
     * Constructor
     */
    public Main() {
        init();

        //Create a new thread to run the game in
        gameThread = new Thread() {

            /**
             * Default run method for the thread.
             *
             * Call loop then interrupt the loop then quit
             * the game.
             */
            @Override
            public void run() {
                loop();
                interrupt();
                quit();
            }

        };

        //Start the thread
        gameThread.setPriority(Thread.MAX_PRIORITY);
        gameThread.start();
    }

    @Override
    public void init() {
        Engine.setOpenGL(Constants.isEnableOpenGL());
        Engine.init();

        //Let the engine know this class is the game
        Engine.setGame(this);

        //Set the width and height to be used for window
        Engine.setPreferedWidth(1280);
        Engine.setPreferedHeight(750);
        Engine.setWidth(1280);
        Engine.setHeight(750);
        Engine.setMinimumWidth(1100);
        Engine.setMinimumHeight(600);

        //Create a new canvas
        canvas = new GameCanvas();
        //Create a frame with our canvas
        frame = new GameFrame("Decred Wallet", canvas);
        frame.setResizable(true);
        frame.setMinimumSize(new Dimension(Engine.getMinimumWidth(), Engine.getMinimumHeight()));
        canvas.setBackground(ColorConstants.loggedinBackgroundColor);

        //Initialise the canvas
        canvas.init();
        canvas.setFocusTraversalKeysEnabled(false);

        //Create a new keyboard and mouse
        keyboard = new Keyboard();
        mouse = new Mouse();

        //Add the listeners to the canvas
        canvas.addKeyListener(keyboard);
        canvas.addMouseListener(mouse);
        canvas.addMouseMotionListener(mouse);

        //Load images
        Images.init();

        Constants.navbar = new Navbar();
        Constants.guiInterfaces.add(new Login());
        Constants.guiInterfaces.add(new Wallet());
        Constants.guiInterfaces.add(new AddressBook());
        Constants.guiInterfaces.add(new Staking());
        Constants.guiInterfaces.add(new StakingPurchase());
        Constants.guiInterfaces.add(new StakingPool());
        Constants.guiInterfaces.add(new Send());
        Constants.guiInterfaces.add(new Receive());
        Constants.guiInterfaces.add(new Logs());
        Constants.guiInterfaces.add(new Settings());
        Constants.guiInterfaces.add(new SettingsSecurity());
        Constants.guiInterfaces.add(new SettingsNetwork());
        Constants.guiInterfaces.add(new Footer());
        Constants.guiInterfaces.add(new AddContact());
        Constants.guiInterfaces.add(new CreateAccount());
        Constants.guiInterfaces.add(new RenameAccount());
        Constants.guiInterfaces.add(new Passphrase());

        Constants.navbar.init();

        for (BaseGui bg : Constants.guiInterfaces) {
            bg.init();
        }

        haveInit = true;

        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    @Override
    public void loop() {
        state = GameState.MAIN_MENU;
        lastLoopTime = System.currentTimeMillis();

        while (true) {
            try {
                //Calculate FPS and time
                lastTime = curTime;
                curTime = System.currentTimeMillis();
                totalTime += curTime - lastTime;

                if (totalTime > 1000) {
                    totalTime -= 1000;
                    FPS = FRAMES;
                    FRAMES = 0;
                }

                //Get FPS
                FRAMES++;
                beginTime = System.currentTimeMillis();
                delta = beginTime - lastLoopTime;
                lastLoopTime = beginTime;

                //Update
                update(delta);

                //Draw the graphics
                canvas.render();

                //Sleep
                timeTaken = System.currentTimeMillis() - beginTime;
                timeLeft = (UPDATE_PERIOD - timeTaken);

                //Sleep for remaining time
                try {
                    if (timeLeft > -1) {
                        Thread.sleep(timeLeft);
                    }
                } catch (InterruptedException ex) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public synchronized void update(long delta) {
        containsMouse = false;

        //Adjust FPS to reduce load when not in use
        if (!canvas.hasFocus() && UPDATE_RATE != Constants.fpsMin) {
            UPDATE_RATE = Constants.fpsMin;
            UPDATE_PERIOD = 1000L / UPDATE_RATE;
        } else if (UPDATE_RATE != Constants.fpsMax) {
            UPDATE_RATE = Constants.fpsMax;
            UPDATE_PERIOD = 1000L / UPDATE_RATE;
        }

        if (!Constants.isDaemonReady() || !Constants.isWalletReady()) {
            Constants.guiInterfaces.get(0).update(delta);
            if (Constants.guiInterfaces.get(0).containsMouse) {
                containsMouse = true;
            }

            //Restore cursor
            if (!containsMouse) {
                Main.frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            } else {
                Main.frame.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        } else {
            //Load accounts
            if (Constants.accounts.size() == 0) {
                if (Api.getAccounts() != null) {
                    for (Iterator iterator = Api.getAccounts().keySet().iterator(); iterator.hasNext(); ) {
                        String key = (String) iterator.next();
                        Constants.accounts.add(new Account(key));
                        Constants.accountNames.add(key);
                    }
                }

                Constants.globalCache.forceUpdateInfo = true;
                Constants.globalCache.forceUpdatePeers = true;
            }

            //Update nav
            Constants.navbar.update(delta);
            if (Constants.navbar.containsMouse) containsMouse = true;

            //Update interfaces
            for (BaseGui bg : Constants.guiInterfaces) {
                if (bg.isActive()) {
                    bg.update(delta);

                    if (bg.containsMouse && !bg.blockInput) {
                        containsMouse = true;
                    }
                }
            }

            //Restore cursor
            if (!containsMouse) {
                Main.frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            } else {
                Main.frame.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            //Cleanup logs, one log per update
            if (Constants.getDaemonProcess() != null && Constants.getDaemonProcess().log.size() > Constants.maxLogLines) {
                for (int i = 0; i < 100; i++) {
                    Constants.getDaemonProcess().log.remove(i);
                }
            }
            if (Constants.getWalletProcess() != null && Constants.getWalletProcess().log.size() > Constants.maxLogLines) {
                for (int i = 0; i < 100; i++) {
                    Constants.getWalletProcess().log.remove(i);
                }
            }
            if (Constants.guiLog.size() > Constants.maxLogLines) {
                for (int i = 0; i < 100; i++) {
                    Constants.guiLog.remove(i);
                }
            }
            if (Constants.rpcLog.size() > Constants.maxLogLines) {
                for (int i = 0; i < 100; i++) {
                    Constants.rpcLog.remove(i);
                }
            }
        }
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        switch (state) {
            case INITILISING:
                g.drawString("Loading...", (Engine.getWidth() / 2) - 30, (Engine.getHeight() / 2) - 30);
                break;
            case MAIN_MENU:
                if (Constants.isDaemonReady() && Constants.isWalletReady()) {
                    Constants.navbar.render(g);
                }

                if (exiting) {
                    Constants.guiInterfaces.get(0).render(g);
                } else {
                    for (BaseGui bg : Constants.guiInterfaces) {
                        if (bg.isActive()) {
                            bg.render(g);
                        }
                    }
                }
                break;
            default:
                break;
        }

        if (Constants.isEnableFps()) {
            g.setColor(Color.WHITE);
            g.setFont(FontConstants.labelFont);
            g.drawString("FPS: " + FPS, 450, 35);
        }
    }

    @Override
    public void resize() {
        canvas.resize();

        if (haveInit) {
            Constants.navbar.resize();

            for (BaseGui bg : Constants.guiInterfaces) {
                bg.resize();
            }
        }
    }

    @Override
    public void quit() {
        exiting = true;
        Login.loadingMessage = "";
        Login.addLoadingMessage("Disconnecting from DCRD");
        Constants.getDcrdEndpoint().disconnect();
        Login.addLoadingMessage("Disconnecting from DCRWALLET");
        Constants.getDcrwalletEndpoint().disconnect();

        //Clean up after yourself, but only if we started it
        if (Constants.getWalletProcess() != null) {
            Login.addLoadingMessage("Stopping DCRWALLET process");
            Processes.killByName("dcrwallet");
        }

        if (Constants.getDaemonProcess() != null) {
            Login.addLoadingMessage("Stopping DCRD process");
            Processes.killByName("dcrd");
        }

        Login.addLoadingMessage("Exiting");
    }
}

package com.hosvir.decredwallet.gui.interfaces;

import com.hosvir.decredwallet.Constants;
import com.deadendgine.Engine;
import com.hosvir.decredwallet.gui.*;

import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * @author fsig
 * @version 1.00
 * @since 20/03/17
 */
public class Logs extends Interface implements MouseWheelListener {
    private int daemonStartLine = 0;
    private int walletStartLine = 0;
    private int guiStartLine = 0;
    private int rpcStartLine = 0;
    private int daemonEndLine = 27;
    private int walletEndLine = 27;
    private int guiEndLine = 27;
    private int rpcEndLine = 27;
    private int maxLines = 27;

    @Override
    public void init() {
        rectangles = new Rectangle[4];
        for(int i = 0; i < rectangles.length; i++){
            rectangles[i] = new Rectangle(i*170,60,170,70);
        }

        maxLines = (Engine.getHeight() - 220) / 19;

        Main.canvas.addMouseWheelListener(this);
    }

    @Override
    public void render(Graphics2D g) {
        //Second nav
        g.setColor(Color.WHITE);
        g.fillRect(0,
                60,
                Engine.getWidth(),
                60);

        g.drawImage(Images.getInterfaces()[19],
                0,
                120,
                Engine.getWidth(),
                60,
                null);

        for(int i = 0; i < rectangles.length; i++){
            g.setColor(ColorConstants.settingsSelectedColor);

            if(selectedId == i || hoverId == i){
                g.fillRect(rectangles[i].x,
                        rectangles[i].y,
                        rectangles[i].width,
                        rectangles[i].height);
            }

            g.fillRect(rectangles[i].x + 170,
                    rectangles[i].y,
                    2,
                    rectangles[i].height);
        }

        g.setFont(FontConstants.tabFont);
        g.setColor(ColorConstants.walletBalanceColor);
        g.drawString(Constants.getLangValue("Daemon-Button-Text"), 170 - 85 - (g.getFontMetrics().stringWidth(Constants.getLangValue("Daemon-Button-Text")) / 2), 105);
        g.drawString(Constants.getLangValue("Wallet-Button-Text"), (170 * 2) - 85 - (g.getFontMetrics().stringWidth(Constants.getLangValue("Wallet-Button-Text")) / 2), 105);
        g.drawString(Constants.getLangValue("GUI-Button-Text"), (170 * 3) - 85 - (g.getFontMetrics().stringWidth(Constants.getLangValue("GUI-Button-Text")) / 2), 105);
        g.drawString(Constants.getLangValue("RPC-Button-Text"), (170 * 4) - 85 - (g.getFontMetrics().stringWidth(Constants.getLangValue("RPC-Button-Text")) / 2), 105);


        //Content box
        g.drawImage(Images.getInterfaces()[6],
                20,
                150,
                10,
                Engine.getHeight() - 230,
                null);

        g.setColor(Color.WHITE);
        g.fillRect(30,
                150,
                Engine.getWidth() - 60,
                Engine.getHeight() - 230);

        g.drawImage(Images.getInterfaces()[7],
                Engine.getWidth() - 30,
                150,
                10,
                Engine.getHeight() - 230,
                null);

        g.drawImage(Images.getInterfaces()[19],
                24,
                Engine.getHeight() - 90,
                Engine.getWidth() - 48,
                60,
                null);


        //Draw log data
        g.setFont(FontConstants.labelFont);
        g.setColor(ColorConstants.labelColor);

        if(daemonStartLine > -1){
            switch(selectedId){
                case 0:
                    if(Constants.getDaemonProcess() != null) {
                        for(int i = daemonStartLine; i < daemonEndLine; i++){
                            if(i < Constants.getDaemonProcess().log.size())
                                g.drawString(Constants.getDaemonProcess().log.get(i), 40, 175 + (i - daemonStartLine)*18);
                        }
                    }else{
                        g.drawString("Not attached to DAEMON.", 40, 175 + (0 - daemonStartLine)*18);
                    }
                    break;
                case 1:
                    if(Constants.getWalletProcess() != null) {
                        for(int i = walletStartLine; i < walletEndLine; i++){
                            if(i < Constants.getWalletProcess().log.size())
                                g.drawString(Constants.getWalletProcess().log.get(i), 40, 175 + (i - walletStartLine)*18);
                        }
                    }else{
                        g.drawString("Not attached to WALLET.", 40, 175 + (0 - walletStartLine)*18);
                    }
                    break;
                case 2:
                    for(int i = guiStartLine; i < guiEndLine; i++){
                        if(i < Constants.guiLog.size())
                            g.drawString(Constants.guiLog.get(i), 40, 175 + (i - guiStartLine)*18);
                    }
                    break;
                case 3:
                    if(Constants.getDcrdEndpoint() != null)
                        for(int i = rpcStartLine; i < rpcEndLine; i++){
                            if(i < Constants.rpcLog.size())
                                g.drawString(Constants.rpcLog.get(i), 40, 175 + (i - rpcStartLine)*18);
                        }
                    break;
            }
        }
    }

    @Override
    public void resize() {
        if(Constants.getDcrdEndpoint() != null && Constants.getDcrwalletEndpoint() != null){
            maxLines = (Engine.getHeight() - 220) / 19;


            if(Constants.getDaemonProcess() != null) daemonStartLine = Constants.getDaemonProcess().log.size() - maxLines;
            if(Constants.getWalletProcess() != null) walletStartLine = Constants.getWalletProcess().log.size() - maxLines;
            guiStartLine = Constants.guiLog.size() - maxLines;
            rpcStartLine = Constants.rpcLog.size() - maxLines;
            daemonEndLine = daemonStartLine + maxLines;
            walletEndLine = walletStartLine + maxLines;
            guiEndLine = guiStartLine + maxLines;
            rpcEndLine = rpcStartLine + maxLines;

            if(daemonStartLine < 0) daemonStartLine = 0;
            if(walletStartLine < 0) walletStartLine = 0;
            if(guiStartLine < 0) guiStartLine = 0;
            if(Constants.getDaemonProcess() != null && (daemonEndLine > Constants.getDaemonProcess().log.size() || daemonEndLine < maxLines))
                daemonEndLine = Constants.getDaemonProcess().log.size();
            if(Constants.getWalletProcess() != null && (walletEndLine > Constants.getWalletProcess().log.size() || walletEndLine < maxLines))
                walletEndLine = Constants.getWalletProcess().log.size();
            if(guiEndLine > Constants.guiLog.size() || guiEndLine < maxLines) guiEndLine = Constants.guiLog.size();
            if(rpcEndLine > Constants.rpcLog.size() || rpcEndLine < maxLines) rpcEndLine = Constants.rpcLog.size();
        }
    }

    @Override
    public boolean isActive() {
        return Constants.navbar.selectedId == 1;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(isActive()){
            switch(selectedId){
                case 0:
                    if(Constants.getDaemonProcess() != null){
                        if(e.getUnitsToScroll() > 0){
                            daemonStartLine += e.getScrollAmount();
                        }else{
                            daemonStartLine -= e.getScrollAmount();
                        }

                        if(daemonStartLine > Constants.getDaemonProcess().log.size()) daemonStartLine = Constants.getDaemonProcess().log.size()-1;
                        if(daemonStartLine < 0) daemonStartLine = 0;
                        daemonEndLine = daemonStartLine + maxLines;
                        if(daemonEndLine > Constants.getDaemonProcess().log.size()) daemonEndLine = Constants.getDaemonProcess().log.size();
                    }
                    break;
                case 1:
                    if(Constants.getWalletProcess() != null){
                        if(e.getUnitsToScroll() > 0){
                            walletStartLine += e.getScrollAmount();
                        }else{
                            walletStartLine -= e.getScrollAmount();
                        }

                        if(walletStartLine > Constants.getWalletProcess().log.size()) walletStartLine = Constants.getWalletProcess().log.size()-1;
                        if(walletStartLine < 0) walletStartLine = 0;
                        walletEndLine = walletStartLine + maxLines;
                        if(walletEndLine > Constants.getWalletProcess().log.size()) walletEndLine = Constants.getWalletProcess().log.size();
                    }
                    break;
                case 2:
                    if(e.getUnitsToScroll() > 0){
                        guiStartLine += e.getScrollAmount();
                    }else{
                        guiStartLine -= e.getScrollAmount();
                    }

                    if(guiStartLine > Constants.guiLog.size()) guiStartLine = Constants.guiLog.size()-1;
                    if(guiStartLine < 0) guiStartLine = 0;
                    guiEndLine = guiStartLine + maxLines;
                    if(guiEndLine > Constants.guiLog.size()) guiEndLine = Constants.guiLog.size();
                    break;
                case 3:
                    if(e.getUnitsToScroll() > 0){
                        rpcStartLine += e.getScrollAmount();
                    }else{
                        rpcStartLine -= e.getScrollAmount();
                    }

                    if(rpcStartLine > Constants.rpcLog.size()) rpcStartLine = Constants.rpcLog.size()-1;
                    if(rpcStartLine < 0) rpcStartLine = 0;
                    rpcEndLine = rpcStartLine + maxLines;
                    if(rpcEndLine > Constants.rpcLog.size()) rpcEndLine = Constants.rpcLog.size();
                    break;
            }
        }
    }
}

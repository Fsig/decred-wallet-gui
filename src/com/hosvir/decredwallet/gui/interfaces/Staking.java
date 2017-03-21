package com.hosvir.decredwallet.gui.interfaces;

import com.hosvir.decredwallet.Constants;
import com.hosvir.decredwallet.gui.*;
import com.deadendgine.Engine;
import com.deadendgine.input.Mouse;
import com.hosvir.decredwallet.gui.Dialog;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * @author fsig
 * @version 1.00
 * @since 20/03/17
 */
public class Staking extends Interface implements MouseWheelListener {
    private int headerThird;
    private int scrollOffset = 0;
    private String[] dateString;
    private Rectangle[] txRectangles;
    private Rectangle[] vbRectangles;
    private int txHoverId = -1;
    private int vbHoverId = -1;
    private int scrollMinHeight = 130;
    private int scrollMaxHeight;
    private int scrollCurrentPosition = 130;

    @Override
    public void init() {
        rectangles = new Rectangle[3];
        for(int i = 0; i < rectangles.length; i++){
            rectangles[i] = new Rectangle(i*170,60,170,70);
        }

        this.components.add(new Dialog("errordiag", ""));

        headerThird = Engine.getWidth() / 9;
        scrollMaxHeight = Engine.getHeight() - (scrollMinHeight / 2) - 35;

        Main.canvas.addMouseWheelListener(this);

        getComponentByName("errordiag").width = 800;
    }

    @Override
    public void update(long delta) {
        super.update(delta);

        if(rectangles == null){
            rectangles = new Rectangle[3];
            for(int i = 0; i < rectangles.length; i++){
                rectangles[i] = new Rectangle(i*170,60,170,70);
            }
        }

        //Ticket hash rectangles
        if(txRectangles == null && Constants.globalCache.tickets.size() > 0){
            txRectangles = new Rectangle[Constants.globalCache.tickets.size()];

            for(int i = 0; i < txRectangles.length; i++){
                txRectangles[i] = new Rectangle((Engine.getWidth() / 2) - 150, 250 + i*70 - scrollOffset, 450,20);
            }
        }

        if(txRectangles != null){
            for(int i = 0; i < txRectangles.length; i++){
                if(txRectangles[i] != null && txRectangles[i].contains(Mouse.point)){
                    containsMouse = true;
                    txHoverId = i;

                    if(Mouse.isMouseDown(MouseEvent.BUTTON1)){
                        Constants.setClipboardString(Constants.globalCache.tickets.get(txHoverId).getTicketHash().trim());
                        getComponentByName("errordiag").text = Constants.getLangValue("Clipboard-Message") + ": " + Constants.getClipboardString();

                        //Show dialog
                        this.blockInput = true;
                        Constants.navbar.blockInput = true;
                        getComponentByName("errordiag").selectedId = 0;

                        Mouse.release(MouseEvent.BUTTON1);
                    }
                }
            }

            if(!containsMouse) txHoverId = -1;
        }


        //Vote bit rectangles
        if(vbRectangles == null && Constants.globalCache.tickets.size() > 0){
            vbRectangles = new Rectangle[Constants.globalCache.tickets.size()];

            for(int i = 0; i < vbRectangles.length; i++){
                vbRectangles[i] = new Rectangle(Engine.getWidth() - 245, 235 + i*70 - scrollOffset, 30,20);
            }
        }

        if(vbRectangles != null){
            for(int i = 0; i < vbRectangles.length; i++){
                if(vbRectangles[i] != null && vbRectangles[i].contains(Mouse.point)){
                    containsMouse = true;
                    vbHoverId = i;

                    if(Mouse.isMouseDown(MouseEvent.BUTTON1)){
                        //Constants.log("Changing vote bits on: ");
                        //Constants.setClipboardString(Constants.globalCache.tickets.get(txHoverId).getTicketHash().trim());
                        getComponentByName("errordiag").text = Constants.getLangValue("Error") + " " + Constants.getLangValue("Ticket-Votebit-Change-Message");

                        //Show dialog
                        this.blockInput = true;
                        Constants.navbar.blockInput = true;
                        getComponentByName("errordiag").selectedId = 0;

                        Mouse.release(MouseEvent.BUTTON1);
                    }
                }
            }

            if(!containsMouse) vbHoverId = -1;
        }
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
        g.drawString(Constants.getLangValue("Ticket-Button-Text"), 170 - 85 - (g.getFontMetrics().stringWidth(Constants.getLangValue("Ticket-Button-Text")) / 2), 105);
        g.drawString(Constants.getLangValue("Purchase-Button-Text"), (170 * 2) - 85 - (g.getFontMetrics().stringWidth(Constants.getLangValue("Purchase-Button-Text")) / 2), 105);
        g.drawString(Constants.getLangValue("Pool-Button-Text"), (170 * 3) - 85 - (g.getFontMetrics().stringWidth(Constants.getLangValue("Pool-Button-Text")) / 2), 105);

        switch(selectedId){
            case 0: //Statistics
                //Tickets
                if(Constants.globalCache.tickets != null){
                    for(int i = 0; i < Constants.globalCache.tickets.size(); i++){
                        if(220 + i*70 - scrollOffset < Engine.getHeight() && 220 + i*70 - scrollOffset > 80 && Constants.globalCache.tickets.get(i) != null){
                            g.drawImage(Images.getInterfaces()[6],
                                    16,
                                    220 + i*70 - scrollOffset,
                                    10,
                                    50,
                                    null);

                            g.setColor(Color.WHITE);
                            g.fillRect(20,
                                    220 + i*70 - scrollOffset,
                                    Engine.getWidth() - 45,
                                    45);

                            g.drawImage(Images.getInterfaces()[7],
                                    Engine.getWidth() - 25,
                                    220 + i*70 - scrollOffset,
                                    10,
                                    50,
                                    null);

                            g.drawImage(Images.getInterfaces()[19],
                                    20,
                                    220 + i*70 - scrollOffset + 41,
                                    Engine.getWidth() - 39,
                                    50,
                                    null);


                            //Live or not
                            if(Constants.globalCache.tickets.get(i).isLive()) {
                                g.setColor(ColorConstants.flatGreen);
                            }else {
                                g.setColor(ColorConstants.flatRed);
                            }

                            g.fillRect(20,
                                    220 + i*70 - scrollOffset,
                                    5,
                                    50);


                            //Draw date
                            dateString = Constants.getWalletDate(Long.parseLong(Constants.globalCache.tickets.get(i).getTime())).split(":");
                            g.setColor(ColorConstants.walletBalanceColor);
                            g.setFont(FontConstants.walletBalanceFont);
                            g.drawString(dateString[0], 35, 252 + i*70 - scrollOffset);

                            g.setColor(ColorConstants.labelColor);
                            g.setFont(FontConstants.transactionFont);
                            g.drawString(dateString[1].replaceAll("!", ":"), 27, 266 + i*70 - scrollOffset);

                            //Draw confirmations
                            g.drawString(Constants.globalCache.tickets.get(i).getConfirmations() + "", 27, 236 + i*70 - scrollOffset);

                            //Draw block
                            g.setColor(ColorConstants.labelColor);
                            g.setFont(FontConstants.transactionFont);
                            g.drawString(Constants.getLangValue("Block-Label"), 190, 235 + i*70 - scrollOffset);

                            g.setColor(ColorConstants.walletBalanceColor);
                            g.setFont(FontConstants.walletBalanceFont);
                            g.drawString(Constants.globalCache.tickets.get(i).getBlockHeight() + "", 180, 252 + i*70 - scrollOffset);

                            //Draw address
                            if (Constants.globalCache.tickets.get(i).getAddress() != null) {
                                g.setColor(ColorConstants.walletBalanceColor);
                                g.setFont(FontConstants.addressFont);
                                g.drawString(Constants.globalCache.tickets.get(i).getAddress(), (Engine.getWidth() / 2) - (g.getFontMetrics().stringWidth(Constants.globalCache.tickets.get(i).getAddress()) / 2), 244 + i * 70 - scrollOffset);
                            }

                            //Draw transaction
                            if(txHoverId == i) g.setColor(ColorConstants.flatBlue); else g.setColor(ColorConstants.labelColor);
                            g.setFont(FontConstants.transactionFont);
                            g.drawString(Constants.globalCache.tickets.get(i).getTicketHash(),
                                    (Engine.getWidth() / 2) - (g.getFontMetrics().stringWidth(Constants.globalCache.tickets.get(i).getTicketHash()) / 2),
                                    266 + i*70 - scrollOffset);


                            //Draw vout
                            g.setColor(ColorConstants.labelColor);
                            g.setFont(FontConstants.transactionFont);
                            g.drawString(Constants.getLangValue("Votebits-Label"), Engine.getWidth() - 255, 235 + i*70 - scrollOffset);

                            if(vbHoverId == i) g.setColor(ColorConstants.flatBlue); else g.setColor(ColorConstants.walletBalanceColor);
                            g.setFont(FontConstants.walletBalanceFont);
                            g.drawString(Constants.globalCache.tickets.get(i).getVoteBits() + "", Engine.getWidth() - 235, 252 + i*70 - scrollOffset);

                            //Draw amount
                            g.setColor(ColorConstants.walletBalanceColor);
                            g.setFont(FontConstants.walletBalanceFont);
                            g.drawString(Constants.globalCache.tickets.get(i).getAmount().replace("-", "- "), (Engine.getWidth() - 30 - g.getFontMetrics().stringWidth(Constants.globalCache.tickets.get(i).getAmount().replace("-", "- "))), 252 + i*70 - scrollOffset);

                            //Draw tx fee
                            g.setColor(ColorConstants.labelColor);
                            g.setFont(FontConstants.transactionFont);
                            g.drawString(Constants.globalCache.tickets.get(i).getFee().replace("-", "- "), (Engine.getWidth() - 30 - g.getFontMetrics().stringWidth(Constants.globalCache.tickets.get(i).getFee().replace("-", "- "))), 266 + i*70 - scrollOffset);
                        }
                    }
                }



                //Content box
                g.drawImage(Images.getInterfaces()[6],
                        10,
                        140,
                        10,
                        65,
                        null);

                g.setColor(Color.WHITE);
                g.fillRect(20,
                        140,
                        Engine.getWidth() - 40,
                        65);

                g.drawImage(Images.getInterfaces()[7],
                        Engine.getWidth() - 20,
                        140,
                        10,
                        65,
                        null);

                g.drawImage(Images.getInterfaces()[19],
                        14,
                        195,
                        Engine.getWidth() - 28,
                        60,
                        null);


                //Draw info
                g.setColor(ColorConstants.labelColor);
                g.setFont(FontConstants.labelFont);
                g.drawString(Constants.getLangValue("Pool-Label"), headerThird - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.getLangValue("Pool-Label")) / 2), 170);
                g.drawString(Constants.getLangValue("Price-Label"), (headerThird * 2) - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.getLangValue("Price-Label")) / 2), 170);

                g.drawString(Constants.getLangValue("Mempool-Label"), (headerThird * 3) - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.getLangValue("Mempool-Label")) / 2), 170);
                g.drawString(Constants.getLangValue("Ownmempool-Label"), (headerThird * 4) - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.getLangValue("Ownmempool-Label")) / 2), 170);
                g.drawString(Constants.getLangValue("Immature-Label"), (headerThird * 5) - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.getLangValue("Immature-Label")) / 2), 170);
                g.drawString(Constants.getLangValue("Live-Label"), (headerThird * 6) - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.getLangValue("Live-Label")) / 2), 170);

                g.drawString(Constants.getLangValue("Voted-Label"), (headerThird * 7) - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.getLangValue("Voted-Label")) / 2), 170);
                g.drawString(Constants.getLangValue("Missed-Label"), (headerThird * 8) - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.getLangValue("Missed-Label")) / 2), 170);
                g.drawString(Constants.getLangValue("Revoked-Label"), (headerThird * 9) - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.getLangValue("Revoked-Label")) / 2), 170);

                if(Constants.globalCache.stakeInfo != null && Constants.globalCache.stakeInfo.size() > 0 && Constants.globalCache.stakeInfo.get("poolsize") != null){
                    g.setColor(ColorConstants.walletBalanceColor);
                    g.setFont(FontConstants.walletBalanceFont);
                    g.drawString(String.valueOf(Constants.globalCache.stakeInfo.get("poolsize")), headerThird - (headerThird / 2) - (g.getFontMetrics().stringWidth(String.valueOf(Constants.globalCache.stakeInfo.get("poolsize"))) / 2), 190);
                    g.drawString(String.valueOf(Constants.globalCache.stakeInfo.get("difficulty")), (headerThird * 2) - (headerThird / 2) - (g.getFontMetrics().stringWidth(String.valueOf(Constants.globalCache.stakeInfo.get("difficulty"))) / 2), 190);

                    g.drawString(String.valueOf(Constants.globalCache.stakeInfo.get("allmempooltix")), (headerThird * 3) - (headerThird / 2) - (g.getFontMetrics().stringWidth(String.valueOf(Constants.globalCache.stakeInfo.get("allmempool"))) / 2), 190);
                    g.drawString(String.valueOf(Constants.globalCache.stakeInfo.get("ownmempooltix")), (headerThird * 4) - (headerThird / 2) - (g.getFontMetrics().stringWidth(String.valueOf(Constants.globalCache.stakeInfo.get("ownmempool"))) / 2), 190);
                    g.drawString(String.valueOf(Constants.globalCache.stakeInfo.get("immature")), (headerThird * 5) - (headerThird / 2) - (g.getFontMetrics().stringWidth(String.valueOf(Constants.globalCache.stakeInfo.get("immature"))) / 2), 190);

                    g.drawString(String.valueOf(Constants.globalCache.stakeInfo.get("live")), (headerThird * 6) - (headerThird / 2) - (g.getFontMetrics().stringWidth(String.valueOf(Constants.globalCache.stakeInfo.get("live"))) / 2), 190);
                    g.drawString(String.valueOf(Constants.globalCache.stakeInfo.get("voted")), (headerThird * 7) - (headerThird / 2) - (g.getFontMetrics().stringWidth(String.valueOf(Constants.globalCache.stakeInfo.get("voted"))) / 2), 190);
                    g.drawString(String.valueOf(Constants.globalCache.stakeInfo.get("missed")), (headerThird * 8) - (headerThird / 2) - (g.getFontMetrics().stringWidth(String.valueOf(Constants.globalCache.stakeInfo.get("missed"))) / 2), 190);
                    g.drawString(String.valueOf(Constants.globalCache.stakeInfo.get("revoked")), (headerThird * 9) - (headerThird / 2) - (g.getFontMetrics().stringWidth(String.valueOf(Constants.globalCache.stakeInfo.get("revoked"))) / 2), 190);
                }


                //Scroll bar
                if(Constants.globalCache.tickets.size() > 0) {
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawLine(Engine.getWidth() - 10, 130, Engine.getWidth() - 10, Engine.getHeight());
                    g.fillRect(Engine.getWidth() - 10, scrollCurrentPosition, 10, 60);
                }


                //Render
                super.render(g);

                break;
            case 1:
                break;
            case 2:
                break;

        }
    }

    @Override
    public void resize() {
        txRectangles = null;
        vbRectangles = null;
        headerThird = Engine.getWidth() / 9;
        scrollMaxHeight = Engine.getHeight() - (scrollMinHeight / 2);
    }

    @Override
    public boolean isActive() {
        return Constants.navbar.selectedId == 3;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(isActive()){
            if(e.getUnitsToScroll() > 0){
                scrollOffset += 70;
                if(Constants.globalCache.tickets.size() > 0)
                    scrollCurrentPosition += (Engine.getHeight() - scrollMinHeight - 60) / Constants.globalCache.tickets.size();
            }else{
                scrollOffset -= 70;
                if(Constants.globalCache.tickets.size() > 0)
                    scrollCurrentPosition -= (Engine.getHeight() - scrollMinHeight - 60) / Constants.globalCache.tickets.size();
            }

            if(scrollOffset < 0) scrollOffset = 0;
            if(scrollOffset > Constants.globalCache.tickets.size() *70) scrollOffset = Constants.globalCache.tickets.size() *70;

            if(Constants.globalCache.tickets.size() > 0){
                scrollMaxHeight = Engine.getHeight() - (scrollMinHeight / 2) - 35;
                if(scrollCurrentPosition < scrollMinHeight) scrollCurrentPosition = scrollMinHeight;
                if(scrollCurrentPosition > scrollMaxHeight) scrollCurrentPosition = scrollMaxHeight;
            }

            txRectangles = null;
            vbRectangles = null;
        }
    }
}

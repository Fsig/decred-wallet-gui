package com.hosvir.decredwallet.gui.interfaces;

import com.deadendgine.Engine;
import com.hosvir.decredwallet.Api;
import com.hosvir.decredwallet.Constants;
import com.hosvir.decredwallet.gui.*;
import com.hosvir.decredwallet.gui.Dialog;
import org.json.simple.JSONObject;

import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * @author fsig
 * @version 1.00
 * @since 20/03/17
 */
public class SettingsNetwork extends Interface implements MouseWheelListener {
    private int headerThird;
    private int scrollOffset = 0;
    private int scrollMinHeight = 130;
    private int scrollMaxHeight;
    private int scrollCurrentPosition = 130;

    @Override
    public void init() {
        headerThird = Engine.getWidth() / 3;

        this.components.add(new Dialog("errordiag", Constants.getLangValue("Disconnected-Peer-Message")));
        selectedId = -1;

        scrollMaxHeight = Engine.getHeight() - (scrollMinHeight / 2);

        Main.canvas.addMouseWheelListener(this);
    }

    @Override
    public synchronized void update(long delta) {
        //Allow diag closing
        if (getComponentByName("errordiag").isActive()) getComponentByName("errordiag").update(delta);

        if (!blockInput) {
            super.update(delta);

            //Add rectangles
            if (rectangles == null && Constants.globalCache.peers.size() > 0) {
                rectangles = new Rectangle[Constants.globalCache.peers.size()];

                for (int i = 0; i < rectangles.length; i++) {
                    rectangles[i] = new Rectangle(Engine.getWidth() - 69, 225 + i * 70 - scrollOffset, 50, 50);
                }
            }

            //Check for contains
            if (containsMouse) {
                Main.containsMouse = true;

                if (selectedId != -1) {
                    Api.disconnectPeer(String.valueOf(JSONObject.class.cast(Constants.globalCache.peers.get(selectedId)).get("id")));
                    Constants.globalCache.forceUpdatePeers = true;

                    //Show dialog
                    Constants.navbar.blockInput = true;
                    getComponentByName("errordiag").selectedId = 0;

                    //Reset
                    selectedId = -1;
                }
            }
        }
    }

    @Override
    public synchronized void render(Graphics2D g) {
        if (Constants.globalCache.peers != null) {
            for (int i = 0; i < Constants.globalCache.peers.size(); i++) {
                if (150 + i * 70 - scrollOffset < Engine.getHeight() && 150 + i * 70 - scrollOffset > 80) {
                    //Get peer
                    JSONObject peer = (JSONObject) Constants.globalCache.peers.get(i);

                    g.drawImage(Images.getInterfaces()[6],
                            20,
                            225 + i * 70 - scrollOffset,
                            10,
                            50,
                            null);

                    g.setColor(Color.WHITE);
                    g.fillRect(25,
                            225 + i * 70 - scrollOffset,
                            Engine.getWidth() - 45,
                            45);

                    g.drawImage(Images.getInterfaces()[7],
                            Engine.getWidth() - 25,
                            225 + i * 70 - scrollOffset,
                            10,
                            50,
                            null);

                    g.drawImage(Images.getInterfaces()[19],
                            25,
                            225 + i * 70 + 41 - scrollOffset,
                            Engine.getWidth() - 49,
                            50,
                            null);

                    //Draw remove button
                    if (hoverId == i) g.setColor(ColorConstants.flatRedHover);
                    else g.setColor(ColorConstants.flatRed);
                    g.fillRect(Engine.getWidth() - 69,
                            225 + i * 70 - scrollOffset,
                            50,
                            50);

                    g.drawImage(Images.getRemoveIcon(),
                            Engine.getWidth() - 69 + 9,
                            225 + i * 70 + 9 - scrollOffset,
                            Images.getRemoveIcon().getWidth(),
                            Images.getRemoveIcon().getHeight(),
                            null);

                    //Address
                    g.setColor(ColorConstants.walletBalanceColor);
                    g.setFont(FontConstants.addressFont);
                    g.drawString(
                            String.valueOf(peer.get("addr")),
                            35,
                            257 + i * 70 - scrollOffset
                    );

                    //Ping
                    g.setColor(ColorConstants.labelColor);
                    g.setFont(FontConstants.transactionFont);
                    g.drawString(
                            "" + Math.round((Double.parseDouble(String.valueOf(peer.get("pingtime"))) / 1000)) + " ms",
                            35,
                            271 + i * 70 - scrollOffset
                    );

                    //Block height
                    g.setColor(ColorConstants.walletBalanceColor);
                    g.setFont(FontConstants.addressFont);
                    g.drawString(
                            String.valueOf(peer.get("currentheight")),
                            (Engine.getWidth() / 2) - (g.getFontMetrics().stringWidth(String.valueOf(peer.get("currentheight"))) / 2),
                            257 + i * 70 - scrollOffset
                    );

                    //DCRD Version
                    g.setColor(ColorConstants.labelColor);
                    g.setFont(FontConstants.transactionFont);
                    g.drawString(
                            String.valueOf(peer.get("subver")).split("/")[2].split("dcrd:")[1],
                            (Engine.getWidth() / 2) - (g.getFontMetrics().stringWidth(String.valueOf(peer.get("currentheight"))) / 2),
                            271 + i * 70 - scrollOffset
                    );

                    //Receive
                    g.setColor(ColorConstants.walletBalanceColor);
                    g.setFont(FontConstants.addressFont);
                    g.setColor(ColorConstants.flatGreen);
                    g.drawString(
                            Engine.formatBytes(Long.parseLong(String.valueOf(peer.get("bytesrecv")))),
                            Engine.getWidth() - 190,
                            247 + i * 70 - scrollOffset
                    );

                    //Send
                    g.setColor(ColorConstants.flatRed);
                    g.drawString(
                            Engine.formatBytes(Long.parseLong(String.valueOf(peer.get("bytesrecv")))),
                            Engine.getWidth() - 190,
                            267 + i * 70 - scrollOffset
                    );

                    //Receive/Send last activity time
                    g.setColor(ColorConstants.labelColor);
                    g.setFont(FontConstants.labelFont);
                    g.drawString(
                            Constants.formatDate(Long.parseLong(String.valueOf(peer.get("lastrecv")))),
                            Engine.getWidth() - 400,
                            247 + i * 70 - scrollOffset
                    );
                    g.drawString(
                            Constants.formatDate(Long.parseLong(String.valueOf(peer.get("lastsend")))),
                            Engine.getWidth() - 400,
                            267 + i * 70 - scrollOffset
                    );
                }
            }
        }

        //Scroll bar
        if (Constants.globalCache.peers.size() - 2 > 0) {
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(Engine.getWidth() - 10, 130, Engine.getWidth() - 10, Engine.getHeight());
            g.fillRect(Engine.getWidth() - 10, scrollCurrentPosition, 10, 60);
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

        //Available, Pending and Locked
        g.setColor(ColorConstants.labelColor);
        g.setFont(FontConstants.labelFont);
        g.drawString(Constants.getLangValue("Blocks-Label"), headerThird - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.getLangValue("Blocks-Label")) / 2), 170);
        g.drawString(Constants.getLangValue("Difficulty-Label"), (headerThird * 2) - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.getLangValue("Difficulty-Label")) / 2), 170);
        g.drawString(Constants.getLangValue("Peers-Label"), (headerThird * 3) - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.getLangValue("Peers-Label")) / 2), 170);

        g.setColor(ColorConstants.walletBalanceColor);
        g.setFont(FontConstants.walletBalanceFont);
        g.drawString(
                String.valueOf(Constants.globalCache.info.get("blocks")),
                headerThird - (headerThird / 2) - (g.getFontMetrics().stringWidth(String.valueOf(Constants.globalCache.info.get("blocks"))) / 2),
                190
        );
        g.drawString(
                String.valueOf(Constants.globalCache.info.get("difficulty")),
                (headerThird * 2) - (headerThird / 2) - (g.getFontMetrics().stringWidth(String.valueOf(Constants.globalCache.info.get("difficulty"))) / 2),
                190
        );
        g.drawString(
                Constants.globalCache.peers.size() + "",
                (headerThird * 3) - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.globalCache.peers.size() - 3 + "") / 2),
                190
        );

        //Render
        super.render(g);
    }

    @Override
    public void resize() {
        rectangles = null;

        headerThird = Engine.getWidth() / 3;
        scrollMaxHeight = Engine.getHeight() - (scrollMinHeight / 2);

        super.resize();
    }

    @Override
    public boolean isActive() {
        return Constants.navbar.selectedId == 0 && Constants.guiInterfaces.get(9).selectedId == 2;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (isActive()) {
            if (e.getUnitsToScroll() > 0) {
                scrollOffset += 70;
                if (Constants.globalCache.peers.size() > 0) {
                    scrollCurrentPosition += (Engine.getHeight() - scrollMinHeight - 60) / Constants.globalCache.peers.size();
                }
            } else {
                scrollOffset -= 70;
                if (Constants.globalCache.peers.size() > 0) {
                    scrollCurrentPosition -= (Engine.getHeight() - scrollMinHeight - 60) / Constants.globalCache.peers.size();
                }
            }

            if (scrollOffset < 0) {
                scrollOffset = 0;
            }
            if (scrollOffset > Constants.globalCache.peers.size() * 70) {
                scrollOffset = Constants.globalCache.peers.size() * 70;
            }

            if (Constants.globalCache.peers.size() - 2 > 0) {
                scrollMaxHeight = Engine.getHeight() - (scrollMinHeight / 2);

                if (scrollCurrentPosition < scrollMinHeight) {
                    scrollCurrentPosition = scrollMinHeight;
                }

                if (scrollCurrentPosition > scrollMaxHeight) {
                    scrollCurrentPosition = scrollMaxHeight;
                }
            }

            rectangles = null;
        }
    }
}

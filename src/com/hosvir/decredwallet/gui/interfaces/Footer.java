package com.hosvir.decredwallet.gui.interfaces;

import com.deadendgine.Engine;
import com.hosvir.decredwallet.Constants;
import com.hosvir.decredwallet.gui.BaseGui;
import com.hosvir.decredwallet.gui.ColorConstants;
import com.hosvir.decredwallet.gui.FontConstants;
import com.hosvir.decredwallet.gui.Images;
import com.hosvir.decredwallet.utils.TimeUtils;
import org.json.simple.JSONObject;

import java.awt.*;
import java.util.Iterator;

/**
 * @author fsig
 * @version 1.00
 * @since 20/03/17
 */
public class Footer extends BaseGui {
    @Override
    public void init() {
        selectedId = -1;

        rectangles = new Rectangle[4];

        rectangles[0] = new Rectangle(8, Engine.getHeight() - 58, 32, 32);
        rectangles[1] = new Rectangle(Engine.getWidth() - 374, Engine.getHeight() - 58, 32, 32);
        rectangles[2] = new Rectangle(Engine.getWidth() - 267, Engine.getHeight() - 58, 32, 32);
        rectangles[3] = new Rectangle(Engine.getWidth() - 141, Engine.getHeight() - 58, 32, 32);
    }

    public void update(long delta) {
        super.update(delta);

        switch (selectedId) {
            case 0:
                Constants.navbar.selectedId = -1;
                Login.loadingMessage = "";
                Constants.log("Disconnecting from DCRD");
                Login.addLoadingMessage("Disconnecting from DCRD");
                Constants.getDcrdEndpoint().disconnect();
                Login.addLoadingMessage("Disconnecting from DCRWALLET");
                Constants.log("Disconnecting from DCRWALLET");
                Constants.getDcrwalletEndpoint().disconnect();
                Login.addLoadingMessage("Disconnected");
                Constants.log("Disconnected");

                Constants.setWalletReady(false);
                Constants.setDaemonReady(false);

                selectedId = -1;
                break;
            default:
                selectedId = -1;
                break;
        }
    }

    @Override
    public void render(Graphics2D g) {
        //Navbar
        g.drawImage(Images.getInterfaces()[0],
                0,
                Engine.getHeight() - 56,
                Engine.getWidth(),
                40,
                null);

        //Draw icons
        for (int i = 0; i < rectangles.length; i++) {
            if (selectedId == i) {
                g.drawImage(Images.getFooterIcons()[i + 4],
                        rectangles[i].x,
                        rectangles[i].y,
                        rectangles[i].width,
                        rectangles[i].height,
                        null);
            } else {
                if (hoverId != i)
                    g.drawImage(Images.getFooterIcons()[i],
                            rectangles[i].x,
                            rectangles[i].y,
                            rectangles[i].width,
                            rectangles[i].height,
                            null);
            }

            g.drawImage(Images.getInterfaces()[2],
                    rectangles[i].x - 10,
                    rectangles[i].y + 5,
                    2,
                    60,
                    null);
        }

        //Hover nav item
        if (hoverId != -1 && hoverId != selectedId) {
            g.drawImage(Images.getFooterIcons()[hoverId + 4],
                    rectangles[hoverId].x,
                    rectangles[hoverId].y,
                    rectangles[hoverId].width,
                    rectangles[hoverId].height,
                    null);

            switch (hoverId) {
                case 1:
                    g.setColor(ColorConstants.walletNameColor);

                    //Arrow
                    g.drawImage(Images.getArrowDown(),
                            rectangles[hoverId].x,
                            rectangles[hoverId].y - 32,
                            32,
                            32,
                            null);

                    //Box
                    g.fillRoundRect(rectangles[hoverId].x - 160,
                            rectangles[hoverId].y - (((Constants.globalCache.peers.size() + 1) * 20) + 10),
                            360,
                            ((Constants.globalCache.peers.size() + 1) * 20),
                            20,
                            20);


                    //Text
                    g.setFont(FontConstants.labelFont);
                    g.setColor(Color.WHITE);

                    int i = 0;
                    for (Iterator iterator = Constants.globalCache.peers.iterator(); iterator.hasNext(); ) {
                        JSONObject peer = (JSONObject) iterator.next();

                        if (peer.get("addr") != null) {
                            //Address
                            g.drawString(peer.get("addr").toString(),
                                    rectangles[hoverId].x - 150,
                                    rectangles[hoverId].y - (((Constants.globalCache.peers.size() + 1) * 20)) + 15 + (i * 20));

                            //Block
                            if (peer.get("currentheight") != null) {
                                g.drawString(peer.get("currentheight").toString(),
                                        rectangles[hoverId].x + 50,
                                        rectangles[hoverId].y - (((Constants.globalCache.peers.size() + 1) * 20)) + 15 + (i * 20));
                            }

                            //Version
                            if (peer.get("subver").toString().split("/").length > 1) {
                                g.drawString(peer.get("subver").toString().split("/")[2].split("dcrd:")[1],
                                        rectangles[hoverId].x + 150,
                                        rectangles[hoverId].y - (((Constants.globalCache.peers.size() + 1) * 20)) + 15 + (i * 20));
                            }
                        }

                        i++;
                    }
                    break;
                case 2:
                    g.setColor(ColorConstants.walletNameColor);

                    //Arrow
                    g.drawImage(Images.getArrowDown(),
                            rectangles[hoverId].x - 2,
                            rectangles[hoverId].y - 32,
                            32,
                            32,
                            null);

                    //Box
                    g.fillRoundRect(rectangles[hoverId].x - 115,
                            rectangles[hoverId].y - 270,
                            250,
                            260,
                            20,
                            20);


                    //Text
                    g.setFont(FontConstants.labelFont);
                    g.setColor(Color.WHITE);

                    g.drawString(
                            "Confirmations: ",
                            rectangles[hoverId].x - 100,
                            rectangles[hoverId].y - 245
                    );

                    g.drawString(
                            "Size: ",
                            rectangles[hoverId].x - 100,
                            rectangles[hoverId].y - 220
                    );

                    g.drawString(
                            "Height: ",
                            rectangles[hoverId].x - 100,
                            rectangles[hoverId].y - 195
                    );

                    g.drawString(
                            "Votebits: ",
                            rectangles[hoverId].x - 100,
                            rectangles[hoverId].y - 170
                    );

                    g.drawString(
                            "Voters: ",
                            rectangles[hoverId].x - 100,
                            rectangles[hoverId].y - 145
                    );

                    g.drawString(
                            "Freshstake: ",
                            rectangles[hoverId].x - 100,
                            rectangles[hoverId].y - 120
                    );

                    g.drawString(
                            "Revocations: ",
                            rectangles[hoverId].x - 100,
                            rectangles[hoverId].y - 95
                    );

                    g.drawString(
                            "Poolsize: ",
                            rectangles[hoverId].x - 100,
                            rectangles[hoverId].y - 70
                    );

                    g.drawString(
                            "SBits: ",
                            rectangles[hoverId].x - 100,
                            rectangles[hoverId].y - 45
                    );

                    g.drawString(
                            "Difficulty: ",
                            rectangles[hoverId].x - 100,
                            rectangles[hoverId].y - 20
                    );


                    g.drawString(
                            Constants.globalCache.currentBlock.get("confirmations").toString(),
                            rectangles[hoverId].x + 10,
                            rectangles[hoverId].y - 245
                    );

                    g.drawString(
                            Constants.globalCache.currentBlock.get("size").toString(),
                            rectangles[hoverId].x + 10,
                            rectangles[hoverId].y - 220
                    );

                    g.drawString(
                            Constants.globalCache.currentBlock.get("height").toString(),
                            rectangles[hoverId].x + 10,
                            rectangles[hoverId].y - 195
                    );

                    g.drawString(
                            Constants.globalCache.currentBlock.get("votebits").toString(),
                            rectangles[hoverId].x + 10,
                            rectangles[hoverId].y - 170
                    );

                    g.drawString(
                            Constants.globalCache.currentBlock.get("voters").toString(),
                            rectangles[hoverId].x + 10,
                            rectangles[hoverId].y - 145
                    );

                    g.drawString(
                            Constants.globalCache.currentBlock.get("freshstake").toString(),
                            rectangles[hoverId].x + 10,
                            rectangles[hoverId].y - 120
                    );

                    g.drawString(
                            Constants.globalCache.currentBlock.get("revocations").toString(),
                            rectangles[hoverId].x + 10,
                            rectangles[hoverId].y - 95
                    );

                    g.drawString(
                            Constants.globalCache.currentBlock.get("poolsize").toString(),
                            rectangles[hoverId].x + 10,
                            rectangles[hoverId].y - 70
                    );

                    g.drawString(
                            Constants.globalCache.currentBlock.get("sbits").toString(),
                            rectangles[hoverId].x + 10,
                            rectangles[hoverId].y - 45
                    );

                    g.drawString(
                            Constants.globalCache.currentBlock.get("difficulty").toString(),
                            rectangles[hoverId].x + 10,
                            rectangles[hoverId].y - 20
                    );
                    break;
                case 3:
                    g.setColor(ColorConstants.walletNameColor);

                    //Arrow
                    g.drawImage(Images.getArrowDown(),
                            rectangles[hoverId].x - 7,
                            rectangles[hoverId].y - 32,
                            32,
                            32,
                            null);

                    //Box
                    g.fillRoundRect(rectangles[hoverId].x - 120,
                            rectangles[hoverId].y - 40,
                            250,
                            30,
                            20,
                            20);


                    //Text
                    g.setFont(FontConstants.labelFont);
                    g.setColor(Color.WHITE);

                    g.drawString(TimeUtils.millisToLongDHMS(System.currentTimeMillis() - (Long.valueOf(Constants.globalCache.currentBlock.get("time").toString()) * 1000)) + " ago",
                            rectangles[hoverId].x - 100,
                            rectangles[hoverId].y - 20);
                    break;
            }
        }


        //Render information
        g.setFont(FontConstants.labelFont);
        g.setColor(Color.WHITE);

        if (Constants.globalCache.peers != null && Constants.globalCache.peers.size() > 0) {
            g.drawString(Constants.globalCache.peers.size() + " peers", rectangles[1].x + 35, Engine.getHeight() - 35);
        } else {
            g.drawString("0 peers", rectangles[1].x + 35, Engine.getHeight() - 35);
        }

        if (Constants.globalCache.info != null && Constants.globalCache.info.size() > 0) {
            g.drawString(Constants.globalCache.info.get("blocks").toString(), rectangles[2].x + 35, Engine.getHeight() - 35);
        } else {
            g.drawString("Loading", rectangles[2].x + 35, Engine.getHeight() - 35);
        }

        if (Constants.globalCache.currentBlock != null && Constants.globalCache.currentBlock.get("time") != null) {
            g.drawString(TimeUtils.millisToLongM(System.currentTimeMillis() - (Long.valueOf(Constants.globalCache.currentBlock.get("time").toString()) * 1000)) + " ago", rectangles[3].x + 30, Engine.getHeight() - 35);
        } else {
            g.drawString("Loading", rectangles[3].x + 30, Engine.getHeight() - 35);
        }
    }

    /**
     * Resize the bounds
     */
    public void resize() {
        rectangles[0].y = Engine.getHeight() - 58;
        rectangles[1].y = Engine.getHeight() - 58;
        rectangles[2].y = Engine.getHeight() - 58;
        rectangles[3].y = Engine.getHeight() - 58;

        rectangles[1].x = Engine.getWidth() - 374;
        rectangles[2].x = Engine.getWidth() - 267;
        rectangles[3].x = Engine.getWidth() - 141;
    }

    @Override
    public boolean isActive() {
        return Constants.isDaemonReady() && Constants.isWalletReady();
    }
}
